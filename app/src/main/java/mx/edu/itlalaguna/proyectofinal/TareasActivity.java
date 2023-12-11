package mx.edu.itlalaguna.proyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class TareasActivity extends AppCompatActivity {

    List < String > listaString = new ArrayList < String > ( );
    ArrayAdapter < String > arrayAdapter;
    String idClase;
    List < String > idTarea = new ArrayList < String > ( );
    ListView lvTareas;
    List < String > lTareas;
    MiAdaptador adaptador;

    private static final int REQUEST_CODE_BORRAR_TAREA = 1;
    private BaseDatosHelper dbHelper;
    private final String[] tareas = { };

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_tareas );

        dbHelper = new BaseDatosHelper ( this );
        setTitle ( "Tareas" );
        Toolbar myToolbar = findViewById ( R.id.tb_tareas );
        setSupportActionBar ( myToolbar );
        idClase = getIntent ( ).getStringExtra ( "idMateria" );
        ListView listaTareas = findViewById ( R.id.lvAlumnos );

        dbHelper = new BaseDatosHelper ( this );
        Cursor cursorTareas = dbHelper.getTareas ( idClase );
        lvTareas = findViewById ( R.id.lvAlumnos );
        if ( cursorTareas != null && cursorTareas.moveToFirst ( ) ) {
            do {
                String nombTarea = cursorTareas.getString ( 1 );
                idTarea.add ( cursorTareas.getString ( 0 ) );
                if ( nombTarea != null ) {
                    listaString.add ( nombTarea );

                } else {
                    Toast.makeText ( this, "Esta nulo papito, es el : ", Toast.LENGTH_SHORT ).show ( );
                }
            } while ( cursorTareas.moveToNext ( ) );
            adaptador = new MiAdaptador ( this, listaString );
            lvTareas.setAdapter ( adaptador );
        } else {
            adaptador = new MiAdaptador ( this, listaString );
            lvTareas.setAdapter ( adaptador );
        }

        listaTareas.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView < ? > parent, View view, int position, long id ) {
                Intent intent = new Intent ( TareasActivity.this, TareaActivity.class );
                intent.putExtra ( "IdTarea", idTarea.get ( position ) );
                intent.putExtra ( "Nombre", listaString.get ( position ) );
                CharSequence a = "a";
                startActivity ( intent );

            }
        } );

    }

    //----------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu ( Menu menu ) {
        MenuInflater inflater = getMenuInflater ( );
        inflater.inflate ( R.menu.menu_searchview, menu );

        MenuItem buscador = menu.findItem ( R.id.search );
        SearchView searchView = ( SearchView ) MenuItemCompat.getActionView ( buscador );

        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit ( String query ) {
                return false;
            }

            @Override
            public boolean onQueryTextChange ( String newText ) {
                TareasActivity.this.adaptador.getFilter ( ).filter ( newText );
                return false;
            }
        } );

        return true;
    }

    //----------------------------------------------------------------------------------------------
    public void agregarTareas ( View v ) {
        View dialogView = getLayoutInflater ( ).inflate ( R.layout.alerta_agregar_tarea, null );
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        EditText edtNombreTarea = dialogView.findViewById ( R.id.edtNombreTarea );
        EditText edtDescTarea = dialogView.findViewById ( R.id.edtDescTarea );

        builder.setIcon ( R.drawable.itl ).setView ( dialogView ).setPositiveButton ( "Guardar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                String nombreTarea = edtNombreTarea.getText ( ).toString ( ).trim ( );
                String descTarea = edtDescTarea.getText ( ).toString ( ).trim ( );
                if ( !nombreTarea.isEmpty ( ) && !descTarea.isEmpty ( ) ) {
                    long tareaAgregada = dbHelper.addDatosTarea ( nombreTarea, descTarea, idClase );

                    if ( tareaAgregada != -1 ) {
                        Cursor alumnosMateria = dbHelper.getAlumnosMateria ( idClase );
                        if ( alumnosMateria != null && alumnosMateria.moveToFirst ( ) ) {
                            do {
                                String numControl = alumnosMateria.getString ( 0 );
                                dbHelper.addDatosTareasAlumno ( tareaAgregada, numControl, 0 );
                            } while ( alumnosMateria.moveToNext ( ) );


                            adaptador = new MiAdaptador ( TareasActivity.this, listaString );
                            lvTareas.setAdapter ( adaptador );
                        }
                        idTarea.add ( tareaAgregada + "" );
                        listaString.add ( nombreTarea );
                        adaptador.notifyDataSetChanged ( );

                        Toast.makeText ( TareasActivity.this, "Tarea Agregada", Toast.LENGTH_LONG ).show ( );
                    } else {
                        Toast.makeText ( TareasActivity.this, "Error al agregar la tarea", Toast.LENGTH_LONG ).show ( );
                    }
                } else {

                    Toast.makeText ( TareasActivity.this, "No se deben dejar espacios en blanco", Toast.LENGTH_LONG ).show ( );
                }
            }
        } ).setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                dialog.dismiss ( );
            }
        } ).create ( ).show ( );
    }

    //----------------------------------------------------------------------------------------------
    protected void onResume ( ) {
        super.onResume ( );
        actualizarListaTareas ( );
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult ( requestCode, resultCode, data );

        if ( requestCode == REQUEST_CODE_BORRAR_TAREA && resultCode == RESULT_OK ) {
            actualizarListaTareas ( );
        }
    }

    //----------------------------------------------------------------------------------------------
    private void actualizarListaTareas ( ) {
        dbHelper = new BaseDatosHelper ( this );
        Cursor cursorTareas = dbHelper.getTareas ( idClase );
        listaString.clear ( );

        if ( cursorTareas != null && cursorTareas.moveToFirst ( ) ) {
            do {
                idTarea.add ( cursorTareas.getString ( 0 ) );
                String nombTarea = cursorTareas.getString ( 1 );
                listaString.add ( nombTarea );

            } while ( cursorTareas.moveToNext ( ) );

            if ( adaptador != null ) {
                adaptador.notifyDataSetChanged ( );
            } else {
                adaptador = new MiAdaptador ( this, listaString );
                lvTareas.setAdapter ( adaptador );
            }
        } else {
            adaptador.clear ( );
        }
    }


    //----------------------------------------------------------------------------------------------
    static class MiAdaptador extends ArrayAdapter {
        private final Context context;
        private final List < String > nombres;

        //----------------------------------------------------------------------------------------------
        public MiAdaptador ( Context c, List < String > nombres ) {
            super ( c, R.layout.lista_materias, R.id.txvNombre, nombres );
            context = c;
            this.nombres = nombres;

        }

        //----------------------------------------------------------------------------------------------
        @NonNull
        @Override
        public View getView ( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {

            if ( convertView == null ) {
                LayoutInflater layoutInflater = ( LayoutInflater ) context.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
                convertView = layoutInflater.inflate ( R.layout.lista_tareas, parent, false );
            }
            TextView nombre = convertView.findViewById ( R.id.txvNombreTarea );

            // asignaciones
            nombre.setText ( nombres.get ( position ) );

            return convertView;
        }
    }

}