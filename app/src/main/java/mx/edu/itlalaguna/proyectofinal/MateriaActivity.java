package mx.edu.itlalaguna.proyectofinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class MateriaActivity extends AppCompatActivity {


    private BaseDatosHelper dbHelper;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);
        dbHelper = new BaseDatosHelper ( this );

        Intent intent = getIntent();

        setTitle(intent.getStringExtra ("Id")+" "+ intent.getStringExtra("Nombre" ) );
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.tb_materia);
        setSupportActionBar(myToolbar);


    }



    public void tareas ( View v ) {

        Intent intent = new Intent(this, TareasActivity.class);
        startActivity ( intent );
    }

    public void alumnos ( View v ) {

        Intent intent = new Intent(this, AlumnosActivity.class);
        startActivity ( intent );
    }
    public void fabBorrarMateria ( View v ) {
        //  alert de agregar materia
        borrarMateria ( v );
    }
    public void borrarMateria(View v) {
        // Obtén el Id y Nombre de la materia del Intent
        Intent intent = getIntent();
        String idMateria = intent.getStringExtra("Id");
        String nombreMateria = intent.getStringExtra("Nombre");

        // Llama al método deleteClase de tu base de datos
        boolean materiaBorrada = dbHelper.deleteClase(idMateria, nombreMateria);

        if (materiaBorrada) {
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