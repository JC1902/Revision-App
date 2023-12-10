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
    List<String> listaString;
    ArrayAdapter<String> arrayAdapter;
    private final String [] alumnosListos   = { "Alumno 20" ,  "Alumno 43" ,  "Alumno 52" , "Alumno 6" , "Alumno 1" ,};

    private final String [] alumnosPendientes   = { "Alumno 2" ,  "Alumno 4" ,  "Alumno 12" , "Alumno 23" , "Alumno 11" ,};
    private ListView listaAlumnos;
    private  String [] alumnos;
    private BaseDatosHelper dbHelper;
    private String idTarea;
    private String nombreTarea;
    private ArrayList < String > alumnosL ;
    private ArrayList < String > alumnosP ;
    private ArrayList < String > alumnosV ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);

        dbHelper = new BaseDatosHelper( this );
        Intent intent = getIntent();
        setTitle( intent.getStringExtra("Nombre" ) );
        Toolbar myToolbar = findViewById(R.id.tb_tarea);
        setSupportActionBar(myToolbar);

        idTarea = intent.getStringExtra( "IdTarea" );
        nombreTarea = intent.getStringExtra( "Nombre" );

//        listaTareas = findViewById ( R.id.lvTareasAlumnos);
//        alumnos = alumnosPendientes;
//
//        listaString =new ArrayList<>(Arrays.asList( alumnos ));
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, alumnos);
//        listaTareas.setAdapter(arrayAdapter);

        // Asegúrate de que listaTareas se inicialice correctamente
        listaAlumnos = findViewById( R.id.lvTareasAlumnos );
        alumnosP = new ArrayList<>();
        alumnosL = new ArrayList<>();

        Cursor cursorPendientes = dbHelper.getTareaAlumnos( idTarea );

        Toast.makeText(this, "idTarea: " + idTarea, Toast.LENGTH_SHORT).show();
        // Recorrer el cursor y clasificar las tareas
        if (cursorPendientes != null && cursorPendientes.moveToFirst()) {
            do {
                String no_Control = cursorPendientes.getString( 0 );
                Toast.makeText(this, "num_Control: " + no_Control, Toast.LENGTH_SHORT).show();
                String nombreAl = cursorPendientes.getString( 1 ) + cursorPendientes.getString( 2 );
                Toast.makeText(this, "nombre: " + nombreAl, Toast.LENGTH_SHORT).show();
                int hecha = cursorPendientes.getInt(3);
                Toast.makeText(this, "hecha: " + hecha, Toast.LENGTH_SHORT).show();
                String alumno = nombreAl + " - " + no_Control;

                if (hecha == 0) {
                    alumnosP.add( alumno );
                } else {
                    alumnosL.add( alumno );
                }
            } while (cursorPendientes.moveToNext());

            cursorPendientes.close();
        }

        // Inicializar lista
        alumnosV = alumnosP;
        listaString = new ArrayList<>( alumnosV );
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1,alumnosV );
        listaAlumnos.setAdapter(arrayAdapter);

    }
    //----------------------------------------------------------------------------------------------
    public boolean onCreateOptionsMenu ( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_searchview_filtro, menu);


        MenuItem buscador = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(buscador);

        filtro = menu.findItem(R.id.filtro);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TareaActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filtro) {
            pendientes = !pendientes;
            if( pendientes ){
                filtro.setIcon ( R.drawable.pendiente );

                alumnosV = alumnosP;
                actualizarLista();

            } else {
                filtro.setIcon ( R.drawable.revisados );
                alumnosV = alumnosL;
                actualizarLista();

                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    listaAlumnos.setItemChecked(i, true);
                }

            }

        }else
            return super.onOptionsItemSelected(item);



        return true;
    }

    public void actualizarLista (  ) {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, alumnosV);
        listaAlumnos.setAdapter(arrayAdapter);
    }

    public void alertBorrarTarea ( View v){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setIcon(R.drawable.itl )
                .setView( R.layout.alerta_eliminar_tarea)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrarTarea( v );
                    }
                })
                .create()
                .show();
    }

    public void borrarTarea (View v ){
        Toast.makeText( this, "idTarea: " + idTarea + " Nombre: " + nombreTarea, Toast.LENGTH_LONG ).show();
        boolean tareaElminada = dbHelper.deleteTarea( idTarea, nombreTarea );

        if ( tareaElminada ) {
            // Si la materia se borra correctamente, puedes cerrar la actividad actual
            // Notifica al adaptador que los datos han cambiado

            Intent resultIntent = new Intent(  );
            setResult( RESULT_OK, resultIntent );
            finish();
            // También podrías mostrar un mensaje o realizar otras acciones después de borrar la tarea
        } else {
            // Muestra un mensaje si hay un error al borrar la tarea
            Toast.makeText(this, "Error al borrar la tarea", Toast.LENGTH_LONG).show();
        }

    }
    public String seleccionados () {
        long [] test = listaAlumnos.getCheckItemIds();
        String elementos = "";
        for (long i: test ) {
            elementos += " , "+i;
        }

        return  elementos;
    }
    //----------------------------------------------------------------------------------------------
}