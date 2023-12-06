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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TareaActivity extends AppCompatActivity {

    List<String> listaString;
    ArrayAdapter<String> arrayAdapter;
    private final String [] alumnos   = { "Alumno 20" ,  "Alumno 43" ,  "Alumno 52" , "Alumno 6" , "Alumno 1" ,};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);

        Intent intent = getIntent();
        setTitle( intent.getStringExtra("Nombre" ) );
        Toolbar myToolbar = findViewById(R.id.tb_tarea);
        setSupportActionBar(myToolbar);

        ListView listaTareas = findViewById ( R.id.lvTareasAlumnos);

        listaString =new ArrayList<>(Arrays.asList( alumnos ));
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice , android.R.id.text1, alumnos);
        listaTareas.setAdapter(arrayAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_searchview, menu);

        MenuItem buscador = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(buscador);

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
}