package mx.edu.itlalaguna.proyectofinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TareaActivity extends AppCompatActivity {

    MenuItem filtro;
    Boolean pendientes = true;
    List < String > listaString;
    ArrayAdapter < String > arrayAdapter;
    private final String[] alumnosListos = { "Alumno 20", "Alumno 43", "Alumno 52", "Alumno 6", "Alumno 1", };

    private final String[] alumnosPendientes = { "Alumno 2", "Alumno 4", "Alumno 12", "Alumno 23", "Alumno 11", };
    private ListView listaAlumnos;
    private ArrayList < String > alumnosControlP;
    private ArrayList < String > alumnosControlL;
    private ArrayList < String > alumnos;
    private BaseDatosHelper dbHelper;
    private String idTarea;
    private String nombreTarea;
    private ArrayList < String > alumnosL;
    private ArrayList < String > alumnosP;
    private ArrayList < String > alumnosV;


    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_tarea );

        dbHelper = new BaseDatosHelper ( this );
        Intent intent = getIntent ( );
        setTitle ( intent.getStringExtra ( "Nombre" ) );
        Toolbar myToolbar = findViewById ( R.id.tb_tarea );
        setSupportActionBar ( myToolbar );

        idTarea = intent.getStringExtra ( "IdTarea" );
        nombreTarea = intent.getStringExtra ( "Nombre" );
        listaAlumnos = findViewById ( R.id.lvTareasAlumnos );
        cargarAlumnos ( );


    }

    //----------------------------------------------------------------------------------------------
    private void cargarAlumnos ( ) {
        alumnosP = new ArrayList <> ( );
        alumnosL = new ArrayList <> ( );
        alumnos = new ArrayList <> ( );
        alumnosControlP = new ArrayList <> ( );
        alumnosControlL = new ArrayList <> ( );

        Cursor cursorPendientes = dbHelper.getTareaAlumnos ( idTarea );
        if ( cursorPendientes != null && cursorPendientes.moveToFirst ( ) ) {
            do {
                String no_Control = cursorPendientes.getString ( 0 );
                String nombreAl = cursorPendientes.getString ( 1 ) + " " + cursorPendientes.getString ( 2 );
                int hecha = cursorPendientes.getInt ( 3 );
                String alumno = nombreAl + " - " + no_Control;
                alumnos.add ( alumno );
                if ( hecha == 0 ) {
                    alumnosP.add ( alumno );
                    alumnosControlP.add ( no_Control );
                } else {
                    alumnosL.add ( alumno );
                    alumnosControlL.add ( no_Control );
                }
            } while ( cursorPendientes.moveToNext ( ) );

            cursorPendientes.close ( );
        }
        alumnosV = alumnosP;
        listaString = new ArrayList <> ( alumnosV );
        arrayAdapter = new ArrayAdapter <> ( this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, alumnosV );
        listaAlumnos.setAdapter ( arrayAdapter );
    }

    //----------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu ( Menu menu ) {
        MenuInflater inflater = getMenuInflater ( );
        inflater.inflate ( R.menu.menu_searchview_filtro, menu );


        MenuItem buscador = menu.findItem ( R.id.search_view );
        SearchView searchView = ( SearchView ) MenuItemCompat.getActionView ( buscador );

        filtro = menu.findItem ( R.id.filtro );

        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit ( String query ) {
                return false;
            }

            @Override
            public boolean onQueryTextChange ( String newText ) {
                TareaActivity.this.arrayAdapter.getFilter ( ).filter ( newText );
                return false;
            }
        } );

        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected ( MenuItem item ) {
        int id = item.getItemId ( );
        if ( id == R.id.filtro ) {
            actualizarTareasEnBaseDeDatos ( );
            cargarAlumnos ( );
            pendientes = !pendientes;
            if ( pendientes ) {
                filtro.setIcon ( R.drawable.pendiente );
                alumnosV = alumnosP;
                seleccionados ( );
                actualizarLista ( );

            } else {
                filtro.setIcon ( R.drawable.revisados );
                alumnosV = alumnosL;
                seleccionados ( );
                actualizarLista ( );

                for ( int i = 0 ; i < arrayAdapter.getCount ( ) ; i++ ) {
                    listaAlumnos.setItemChecked ( i, true );
                }

            }

        } else return super.onOptionsItemSelected ( item );


        return true;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onPause ( ) {
        super.onPause ( );
        if ( pendientes ) {
            actualizarTareasEnBaseDeDatos ( );
        }
    }

    //----------------------------------------------------------------------------------------------
    private void actualizarTareasEnBaseDeDatos ( ) {
        int idTarea = Integer.parseInt ( getIntent ( ).getStringExtra ( "IdTarea" ) );

        for ( int i = 0 ; i < listaAlumnos.getCount ( ) ; i++ ) {
            String numControl;
            if ( pendientes )
                numControl = alumnosControlP.get ( i );
            else
                numControl = alumnosControlL.get ( i );
            boolean tareaMarcada = listaAlumnos.isItemChecked ( i );
            int estadoActual = tareaMarcada ? 1 : 0;
            dbHelper.updateAlumnoTarea ( estadoActual, numControl, idTarea );
        }
    }

    //----------------------------------------------------------------------------------------------
    public void actualizarLista ( ) {
        arrayAdapter = new ArrayAdapter <> ( this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, alumnosV );
        listaAlumnos.setAdapter ( arrayAdapter );
    }

    //----------------------------------------------------------------------------------------------
    public void alertBorrarTarea ( View v ) {
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );

        builder.setIcon ( R.drawable.itl ).setView ( R.layout.alerta_eliminar_tarea ).setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                dialog.dismiss ( );
            }
        } ).setPositiveButton ( "Eliminar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                borrarTarea ( v );
            }
        } ).create ( ).show ( );
    }

    //----------------------------------------------------------------------------------------------
    public void borrarTarea ( View v ) {
        boolean tareaElminada = dbHelper.deleteTarea ( idTarea, nombreTarea );

        if ( tareaElminada ) {
            Intent resultIntent = new Intent ( );
            setResult ( RESULT_OK, resultIntent );
            finish ( );
        } else {
            Toast.makeText ( this, "Error al borrar la tarea", Toast.LENGTH_LONG ).show ( );
        }

    }

    //----------------------------------------------------------------------------------------------
    public void seleccionados ( ) {
        Cursor cursorPendientes = dbHelper.getTareaAlumnos ( idTarea );

        long[] test = listaAlumnos.getCheckItemIds ( );
        String elementos = "";
        for ( long i : test ) {
            if ( cursorPendientes != null && cursorPendientes.moveToFirst ( ) ) {
                do {
                    String idAlumno = cursorPendientes.getString ( 0 );
                    int hecha = cursorPendientes.getInt ( 3 );
                } while ( cursorPendientes.moveToNext ( ) );

            }
            elementos += " , " + i;
        }
    }
    //----------------------------------------------------------------------------------------------
}