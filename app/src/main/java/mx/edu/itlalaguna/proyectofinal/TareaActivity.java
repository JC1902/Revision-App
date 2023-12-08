package mx.edu.itlalaguna.proyectofinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.DialogInterface;
import android.content.Intent;
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
    private ListView listaTareas;
    private  String [] alumnos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);

        Intent intent = getIntent();
        setTitle( intent.getStringExtra("Nombre" ) );
        Toolbar myToolbar = findViewById(R.id.tb_tarea);
        setSupportActionBar(myToolbar);



        listaTareas = findViewById ( R.id.lvTareasAlumnos);
        alumnos = alumnosPendientes;

        listaString =new ArrayList<>(Arrays.asList( alumnos ));
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, alumnos);
        listaTareas.setAdapter(arrayAdapter);

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

                alumnos = alumnosPendientes;
                actualizarLista();

            } else {
                filtro.setIcon ( R.drawable.revisados );
                alumnos = alumnosListos;
                actualizarLista();

                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    listaTareas.setItemChecked(i, true);
                }


            }




        }else
            return super.onOptionsItemSelected(item);



        return true;
    }

    public void actualizarLista (  ) {

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, alumnos);
        listaTareas.setAdapter(arrayAdapter);
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
        Toast.makeText(this, "Tarea Eliminada", Toast.LENGTH_SHORT).show();
    }
    public String seleccionados () {
        long [] test = listaTareas.getCheckItemIds();
        String elementos = "";
        for (long i: test ) {
            elementos += " , "+i;
        }

        return  elementos;
    }
}