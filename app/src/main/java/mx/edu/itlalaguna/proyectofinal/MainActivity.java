package mx.edu.itlalaguna.proyectofinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {


    private ListView lvMaterias;
    private static final int REQUEST_CODE_BORRAR_MATERIA = 1;
    private boolean isOpen = false;
    // Arreglos por Default para pruebas
    List < String > idMaterias = new ArrayList <> ( );
    List < String > nombresMaterias = new ArrayList <> ( );
    List < String > grupo = new ArrayList <> ( );

    private BaseDatosHelper dbHelper;
    List < Integer > horaInicio = new ArrayList <> ( );

    MiAdaptador adaptador;


    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        setTitle ( "Materias" );
        androidx.appcompat.widget.Toolbar myToolbar = findViewById ( R.id.toolbarMaterias );
        setSupportActionBar ( myToolbar );

        dbHelper = new BaseDatosHelper ( this );
        Cursor cursorMaterias = dbHelper.getMateria ( );
        lvMaterias = findViewById ( R.id.materias_LV );
        if ( cursorMaterias != null && cursorMaterias.moveToFirst ( ) ) {
            do {
                String idMateria = cursorMaterias.getString ( 0 );
                idMaterias.add ( idMateria );
                String nombreMateria = cursorMaterias.getString ( 1 );
                nombresMaterias.add ( nombreMateria );
                String grupoMateria = cursorMaterias.getString ( 2 );
                grupo.add ( grupoMateria );
                int horaMateria = cursorMaterias.getInt ( 3 );
                horaInicio.add ( horaMateria );
            } while ( cursorMaterias.moveToNext ( ) );
            adaptador = new MiAdaptador ( this, idMaterias, nombresMaterias, grupo, horaInicio );
            lvMaterias.setAdapter ( adaptador );
        } else {
            adaptador = new MiAdaptador ( this, idMaterias, nombresMaterias, grupo, horaInicio );
            lvMaterias.setAdapter ( adaptador );
        }

        lvMaterias.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView < ? > parent, View view, int position, long id ) {

                Intent intent = new Intent ( MainActivity.this, MateriaActivity.class );
                intent.putExtra ( "Id", idMaterias.get ( position ) );
                intent.putExtra ( "Nombre", nombresMaterias.get ( position ) );
                intent.putExtra ( "Grupo", "Grupo " + grupo.get ( position ) );
                int hr = horaInicio.get ( position );
                String horaFormato = ( ( hr < 10 ) ? "0" + hr : hr ) + ":00 - " + ( ( ( hr + 1 ) < 10 ) ? "0" + ( hr + 1 ) : ( hr + 1 ) ) + ":00";
                intent.putExtra ( "Horario", horaFormato );
                startActivityForResult ( intent, REQUEST_CODE_BORRAR_MATERIA );


            }
        } );

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult ( requestCode, resultCode, data );

        if ( requestCode == REQUEST_CODE_BORRAR_MATERIA && resultCode == RESULT_OK ) {
            actualizarListaMaterias ( );
        }
    }

    private void actualizarListaMaterias ( ) {
        dbHelper = new BaseDatosHelper ( this );
        Cursor cursorMaterias = dbHelper.getMateria ( );
        idMaterias.clear ( );
        nombresMaterias.clear ( );
        grupo.clear ( );
        horaInicio.clear ( );

        if ( cursorMaterias != null && cursorMaterias.moveToFirst ( ) ) {
            do {
                String idMateria = cursorMaterias.getString ( 0 );
                idMaterias.add ( idMateria );
                String nombreMateria = cursorMaterias.getString ( 1 );
                nombresMaterias.add ( nombreMateria );
                String grupoMateria = cursorMaterias.getString ( 2 );
                grupo.add ( grupoMateria );
                int horaMateria = cursorMaterias.getInt ( 3 );
                horaInicio.add ( horaMateria );
            } while ( cursorMaterias.moveToNext ( ) );

            if ( adaptador != null ) {
                adaptador.notifyDataSetChanged ( );
            } else {
                adaptador = new MiAdaptador ( this, idMaterias, nombresMaterias, grupo, horaInicio );
                lvMaterias.setAdapter ( adaptador );
            }
        } else {
            adaptador.clear ( );
        }
    }

    public void agregarMateria ( View v ) {
        View alertaAgregarMateria = getLayoutInflater ( ).inflate ( R.layout.alerta_agregar_materia, null );

        EditText edtId = alertaAgregarMateria.findViewById ( R.id.edtIdMateria );
        EditText edtNombre = alertaAgregarMateria.findViewById ( R.id.edtNombreMateria );
        EditText edtGrupo = alertaAgregarMateria.findViewById ( R.id.edtGrupoMateria );
        EditText edtHora = alertaAgregarMateria.findViewById ( R.id.edtHoraMateria );

        AlertDialog.Builder builder = new AlertDialog.Builder ( this );

        builder.setIcon ( R.drawable.itl ).setView ( alertaAgregarMateria ).setPositiveButton ( "Guardar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                String idClase = edtId.getText ( ).toString ( ).trim ( );
                String nombreMateria = edtNombre.getText ( ).toString ( ).trim ( );
                String grupoMateria = edtGrupo.getText ( ).toString ( ).trim ( );
                String horaMateria = edtHora.getText ( ).toString ( ).trim ( );
                if ( !nombreMateria.isEmpty ( ) && !grupoMateria.isEmpty ( ) && ( !horaMateria.isEmpty ( ) && TextUtils.isDigitsOnly ( horaMateria ) ) ) {
                    boolean materiaAgregada = dbHelper.addDatosClase ( idClase, nombreMateria, grupoMateria, Integer.parseInt ( horaMateria ) );

                    if ( materiaAgregada ) {
                        idMaterias.add ( idClase );
                        nombresMaterias.add ( nombreMateria );
                        grupo.add ( grupoMateria );
                        horaInicio.add ( Integer.parseInt ( horaMateria ) );
                        adaptador.notifyDataSetChanged ( );
                        Toast.makeText ( MainActivity.this, "Materia Agregada", Toast.LENGTH_LONG ).show ( );
                    } else {
                        Toast.makeText ( MainActivity.this, "Error al agregar la materia", Toast.LENGTH_LONG ).show ( );
                    }
                } else {
                    Toast.makeText ( MainActivity.this, ( TextUtils.isDigitsOnly ( horaMateria ) ) ? "Todos los campos son requeridos" : "En el campo hora solo agregar números", Toast.LENGTH_LONG ).show ( );
                }
            }
        } ).setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                dialog.dismiss ( );
            }
        } ).create ( ).show ( );
    }

    @Override
    //----------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu ( Menu menu ) {
        MenuInflater inflater = getMenuInflater ( );
        inflater.inflate ( R.menu.main_menu, menu );

        return true;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        int id = item.getItemId ( );


        if ( id == R.id.mniAcercaDe ) {
            Intent intent = new Intent ( MainActivity.this, AcercaDeActivity.class );
            startActivity ( intent );

        } else return super.onOptionsItemSelected ( item );


        return true;
    }

    //----------------------------------------------------------------------------------------------
    public void fabAgregarMaterias ( View v ) {
        agregarMateria ( v );
    }

    //----------------------------------------------------------------------------------------------
    static class MiAdaptador extends ArrayAdapter {
        private final Context context;
        private final List < String > id;
        private final List < String > nombres;
        private final List < String > grupo;
        private final List < Integer > horasIni;


        public MiAdaptador ( Context c, List < String > id, List < String > nombres, List < String > grupo, List < Integer > horaIni ) {
            super ( c, R.layout.lista_materias, R.id.txvNombre, nombres );
            context = c;
            this.nombres = nombres;
            this.id = id;
            this.grupo = grupo;
            this.horasIni = horaIni;
        }

        @NonNull
        @Override
        public View getView ( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {

            if ( convertView == null ) {
                LayoutInflater layoutInflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
                convertView = layoutInflater.inflate ( R.layout.lista_materias, parent, false );
            }
            TextView nombre = convertView.findViewById ( R.id.txvNombre );
            TextView grupo = convertView.findViewById ( R.id.txvGrupo );
            TextView hora = convertView.findViewById ( R.id.txvHora );
            GifImageView gif = convertView.findViewById ( R.id.gifMateria );
            int hr = horasIni.get ( position );
            String horaFormato = ( ( hr < 10 ) ? "0" + hr : hr ) + ":00 - " + ( ( ( hr + 1 ) < 10 ) ? "0" + ( hr + 1 ) : ( hr + 1 ) ) + ":00";
            nombre.setText ( nombres.get ( position ) );
            grupo.setText ( "Grupo " + this.grupo.get ( position ) );
            hora.setText ( horaFormato );
            if ( hr >= 6 && hr < 12 ) {
                gif.setImageResource ( R.drawable.materia_manana );
            } else if ( hr >= 12 && hr < 18 ) gif.setImageResource ( R.drawable.materia_tarde );
            else gif.setImageResource ( R.drawable.materia_noche );
            return convertView;
        }
    }
}
