package mx.edu.itlalaguna.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MateriaActivity extends AppCompatActivity {

    ImageButton imgBtnAlumnos , imgBtnTareas ;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);

        Intent intent = getIntent();

        setTitle( intent.getStringExtra("Nombre" ) );
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_materia);
        setSupportActionBar(myToolbar);

        imgBtnAlumnos = (ImageButton) findViewById (  R.id.imgBtnAlumnos );
        imgBtnTareas = (ImageButton) findViewById ( R.id.imgBtnTareas );
        imgBtnAlumnos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Código a ejecutar cuando el botón es presionado
                        imgBtnAlumnos.setAlpha( 0.5F );
                        break;
                    case MotionEvent.ACTION_UP:
                        // Código a ejecutar cuando se deja de presionar el botón
                        imgBtnAlumnos.setAlpha( 1F );
                        break;
                }
                return true;
            }
        });

        imgBtnTareas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Código a ejecutar cuando el botón es presionado
                        imgBtnTareas.setAlpha( 0.5F );
                        break;
                    case MotionEvent.ACTION_UP:
                        // Código a ejecutar cuando se deja de presionar el botón
                        imgBtnTareas.setAlpha( 1F );
                        break;
                }
                return true;
            }
        });
    }
}