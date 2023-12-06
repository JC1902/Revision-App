package mx.edu.itlalaguna.proyectofinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;



public class MateriaActivity extends AppCompatActivity {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);

        Intent intent = getIntent();

        setTitle( intent.getStringExtra("Nombre" ) );
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
}