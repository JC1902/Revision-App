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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlumnosActivity extends AppCompatActivity {


    List<String> listaString;
    ArrayAdapter<String> arrayAdapter;
    private final String [] alumnos   = { "Alumno 20" ,  "Alumno 43" ,  "Alumno 52" , "Alumno 6" , "Alumno 1" ,};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);

        setTitle( "Alumnos");
        Toolbar myToolbar = findViewById(R.id.tb_alumnos);
        setSupportActionBar(myToolbar);

        ListView listaTareas = findViewById ( R.id.lvAlumnos);


        listaString =new ArrayList<>(Arrays.asList( alumnos ));
        arrayAdapter = new ArrayAdapter<>(this, R.layout.lista_alumnos , R.id.txvNombreAlumno, alumnos );
        listaTareas.setAdapter(arrayAdapter);

        listaTareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ---- Aqui iria el dirrecionamiento a la materia ----
                Intent intent = new Intent(AlumnosActivity.this, AlumnoActivity.class);
                intent.putExtra("Nombre", alumnos[ position ] );
                startActivity ( intent );

                //Toast.makeText(MainActivity.this, "Ir a Materia : "+materias[ position ], Toast.LENGTH_SHORT).show();

            }
        });

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
                AlumnosActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    public void agregarAlumno ( View v ){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setIcon( R.drawable.itl )
                .setView( R.layout.alerta_agregar_alumno )
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // -------- Llamada al metodo "agregarMateria" ---------//
                        Toast.makeText( AlumnosActivity.this,
                                "Materia Agregada",
                                Toast.LENGTH_LONG ).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}