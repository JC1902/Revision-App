package mx.edu.itlalaguna.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
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

    MenuItem filtro;
    Boolean pendientes = true;
    List<String> listaString;
    ArrayAdapter<String> arrayAdapter;
    private final String [] tareasPendientes   = { "VideoView" , "ListView" , "Grabar Audio" , "Video 2"};
    private final String [] tareasListas   = { "Foto" , "GridView" , "ImageView" , "AHHHHHHHHH"};

    private String [] tareas   = tareasPendientes;
    ListView listaTareas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("Nombre");

        setTitle( nombre );
        Toolbar myToolbar = findViewById(R.id.tb_alumno);
        setSupportActionBar(myToolbar);

        myToolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               toast( nombre );
                return false;
            }
        });

        listaTareas = findViewById ( R.id.lvAlumnoTareas );

        listaString =new ArrayList<>(Arrays.asList( tareas  ));
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, tareas);
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
            if( pendientes ){
                filtro.setIcon ( R.drawable.pendiente );

                tareas = tareasPendientes;
                actualizarLista();

            } else {
                filtro.setIcon ( R.drawable.revisados );
                tareas = tareasListas;
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

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, tareas);
        listaTareas.setAdapter(arrayAdapter);
    }

    public void borrarAlumno (View v ){
        long [] test = listaTareas.getCheckItemIds();
        String elementos = "";
        for (long i: test ) {
            elementos += " , "+i;
        }
        Toast.makeText(this, elementos, Toast.LENGTH_SHORT).show();
    }

}