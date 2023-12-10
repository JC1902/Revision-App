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
    List<String> listaString;
    BaseDatosHelper dbHelper;
    ArrayAdapter<String> arrayAdapter;
    private ArrayList<String>tareasV;
    private ArrayList<String>tareasP;
    private ArrayList<String>tareasL;
    ListView listaTareas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        dbHelper = new BaseDatosHelper(this);
        Intent intent = getIntent();
        nombre = intent.getStringExtra("Nombre");

        setTitle(nombre);
        Toolbar myToolbar = findViewById(R.id.tb_alumno);
        setSupportActionBar(myToolbar);

        myToolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toast(nombre);
                return false;
            }
        });

        // Asegúrate de que listaTareas se inicialice correctamente
        listaTareas = findViewById(R.id.lvAlumnoTareas);
        tareasP = new ArrayList<>();
        tareasL = new ArrayList<>();
        String numControl = getIntent().getStringExtra("NumControl");
        String idMateria = getIntent().getStringExtra("idMateria");
        Cursor cursorPendientes = dbHelper.getTareasAsignadasAlumnoEnMateria(numControl, idMateria);

        // Recorrer el cursor y clasificar las tareas
        if (cursorPendientes != null && cursorPendientes.moveToFirst()) {
            do {
                String nombreTarea = cursorPendientes.getString(0);
                String descripcionTarea = cursorPendientes.getString(1);
                int hecha = cursorPendientes.getInt(2);

                String tarea = nombreTarea + " - " + descripcionTarea;

                if (hecha == 0) {
                    tareasP.add(tarea);
                } else {
                    tareasL.add(tarea);
                }
            } while (cursorPendientes.moveToNext());

            cursorPendientes.close();
        }

        // Inicializar lista con tareas pendientes
        tareasV = tareasP;
        listaString = new ArrayList<>(tareasV);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, tareasV);
        listaTareas.setAdapter(arrayAdapter);
    }

    public void toast (String x){
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
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
                AlumnoActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filtro) {
            pendientes = !pendientes;
            if ( pendientes ) {
                filtro.setIcon ( R.drawable.pendiente );
                tareasV = tareasP;
                actualizarLista ( );
            } else {
                filtro.setIcon ( R.drawable.revisados );
                tareasV = tareasL;
                actualizarLista ( );
                for ( int i = 0 ; i < arrayAdapter.getCount ( ) ; i++ ) {
                    listaTareas.setItemChecked ( i, true );
                }
            }
        }else
            return super.onOptionsItemSelected(item);



        return true;
    }

    public void actualizarLista (  ) {

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, tareasV);
        listaTareas.setAdapter(arrayAdapter);
    }

    public void alertaBorrarAlumno (View v ){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setIcon(R.drawable.itl )
                .setView( R.layout.alerta_eliminar )
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrarAlumno( v );
                    }
                })
                .create()
                .show();


    }

    public void borrarAlumno (View v ) {
        String numControl=getIntent ().getStringExtra ( "NumControl" );
        String idMateria=getIntent ().getStringExtra ( "idMateria" );
        boolean alumnoBorrado=dbHelper.deleteAlumnoClase (numControl,idMateria);

        if (alumnoBorrado) {
            // Si la materia se borra correctamente, puedes cerrar la actividad actual

            // Notifica al adaptador que los datos han cambiado
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
            // También podrías mostrar un mensaje o realizar otras acciones después de borrar la materia
        } else {
            // Muestra un mensaje si hay un error al borrar la materia
            Toast.makeText(this, "Error al borrar la materia", Toast.LENGTH_LONG).show();
        }
    }

}