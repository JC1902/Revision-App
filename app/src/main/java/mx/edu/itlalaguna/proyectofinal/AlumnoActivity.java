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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlumnoActivity extends AppCompatActivity {

    String nombre;
    MenuItem filtro;
    Boolean pendientes = true;
    List < String > listaString;
    BaseDatosHelper dbHelper;
    ArrayAdapter < String > arrayAdapter;
    private ArrayList < Integer > tareasPen;
    private ArrayList < Integer > tareasLis;
    private ArrayList < String > tareasV;
    private ArrayList < String > tareasP;
    private ArrayList < String > tareasL;
    ListView listaTareas;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_alumno );
        dbHelper = new BaseDatosHelper ( this );
        Intent intent = getIntent ( );
        nombre = intent.getStringExtra ( "Nombre" );

        setTitle ( nombre );
        Toolbar myToolbar = findViewById ( R.id.tb_alumno );
        setSupportActionBar ( myToolbar );

        myToolbar.setOnLongClickListener ( new View.OnLongClickListener ( ) {
            @Override
            public boolean onLongClick ( View v ) {
                toast ( nombre );
                return false;
            }
        } );
        listaTareas = findViewById ( R.id.lvAlumnoTareas );
        String numControl = getIntent ( ).getStringExtra ( "NumControl" );
        String idMateria = getIntent ( ).getStringExtra ( "idMateria" );
        Cursor cursorPendientes = dbHelper.getTareasAsignadasAlumnoEnMateria ( numControl, idMateria );
        cargarTareas ( cursorPendientes );
    }

    //----------------------------------------------------------------------------------------------
    private void cargarTareas ( Cursor cursorPendientes ) {
        tareasPen = new ArrayList <> ( );
        tareasLis = new ArrayList <> ( );
        tareasP = new ArrayList <> ( );
        tareasL = new ArrayList <> ( );
        if ( cursorPendientes != null && cursorPendientes.moveToFirst ( ) ) {
            do {
                int id = cursorPendientes.getInt ( 0 );
                String nombreTarea = cursorPendientes.getString ( 1 );
                String descripcionTarea = cursorPendientes.getString ( 2 );
                int hecha = cursorPendientes.getInt ( 3 );

                String tarea = nombreTarea + " - " + descripcionTarea;

                if ( hecha == 0 ) {
                    tareasPen.add ( id );
                    tareasP.add ( tarea );
                } else {
                    tareasLis.add ( id );
                    tareasL.add ( tarea );
                }
            } while ( cursorPendientes.moveToNext ( ) );

            cursorPendientes.close ( );
        }
        tareasV = tareasP;
        listaString = new ArrayList <> ( tareasV );
        arrayAdapter = new ArrayAdapter <> ( this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, tareasV );
        listaTareas.setAdapter ( arrayAdapter );
    }

    //----------------------------------------------------------------------------------------------
    public void toast ( String x ) {
        Toast.makeText ( this, x, Toast.LENGTH_SHORT ).show ( );
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
                AlumnoActivity.this.arrayAdapter.getFilter ( ).filter ( newText );
                return false;
            }
        } );

        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected ( MenuItem item ) {
        int id = item.getItemId ( );
        if ( id == R.id.filtro ) {
            actualizarTareasEnBaseDeDatos();
            pendientes = !pendientes;
            String numControl = getIntent ( ).getStringExtra ( "NumControl" );
            String idMateria = getIntent ( ).getStringExtra ( "idMateria" );
            Cursor cursorPendientes = dbHelper.getTareasAsignadasAlumnoEnMateria ( numControl, idMateria );
            cargarTareas ( cursorPendientes );
            if ( pendientes ) {
                filtro.setIcon ( R.drawable.pendiente );
                tareasV = tareasP;
                actualizarLista ( );
            } else {
                filtro.setIcon ( R.drawable.revisados );
                tareasV = tareasL;
                actualizarLista ( );
                for ( int i = 0 ; i < listaTareas.getCount () ; i++ ) {
                    listaTareas.setItemChecked ( i, true );
                }
            }
        } else
            return super.onOptionsItemSelected ( item );


        return true;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onPause ( ) {
        super.onPause ( );

        actualizarTareasEnBaseDeDatos ( );
    }

    //----------------------------------------------------------------------------------------------
    private void actualizarTareasEnBaseDeDatos ( ) {
        String numControl = getIntent ( ).getStringExtra ( "NumControl" );

        for ( int i = 0 ; i < listaTareas.getCount ( ) ; i++ ) {
            int idT;
            if(pendientes){
                idT= tareasPen.get ( i );
            }else{
                idT=tareasLis.get ( i );
            }

            boolean tareaMarcada = listaTareas.isItemChecked ( i );
            int estadoActual = tareaMarcada ? 1 : 0;
            dbHelper.updateAlumnoTarea ( estadoActual, numControl, idT );
        }
    }

    public void actualizarLista ( ) {
        arrayAdapter = new ArrayAdapter <> ( this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, tareasV );
        listaTareas.setAdapter ( arrayAdapter );
    }

    //----------------------------------------------------------------------------------------------
    public void alertaBorrarAlumno ( View v ) {

        AlertDialog.Builder builder = new AlertDialog.Builder ( this );

        builder.setIcon ( R.drawable.itl )
                .setView ( R.layout.alerta_eliminar )
                .setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } )
                .setPositiveButton ( "Eliminar", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        borrarAlumno ( v );
                    }
                } )
                .create ( )
                .show ( );


    }

    //----------------------------------------------------------------------------------------------
    public void borrarAlumno ( View v ) {
        String numControl = getIntent ( ).getStringExtra ( "NumControl" );
        String idMateria = getIntent ( ).getStringExtra ( "idMateria" );
        Cursor cursor = dbHelper.getIdsTareasEnMateria ( idMateria );
        if ( cursor != null && cursor.moveToFirst ( ) ) {
            do {
                long tarea=cursor.getInt ( 0 );
                boolean alumnoTarea = dbHelper.deleteTareaAlumno (numControl,tarea);
            } while ( cursor.moveToNext ( ) );

            cursor.close ( );
        }
        boolean alumnoBorrado = dbHelper.deleteAlumnoClase ( numControl, idMateria );

        if ( alumnoBorrado ) {
            Intent resultIntent = new Intent ( );
            setResult ( RESULT_OK, resultIntent );
            finish ( );
        } else {
            Toast.makeText ( this, "Error al borrar la materia", Toast.LENGTH_LONG ).show ( );
        }
    }

}