package mx.edu.itlalaguna.proyectofinal;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MateriaActivity extends AppCompatActivity {

    public static final int CODIGO_SELECCIONAR_ARCHIVO = 1944;
    TextView horario;
    TextView grupo;
    private BaseDatosHelper dbHelper;

    @SuppressLint ( "ClickableViewAccessibility" )
    @Override
    //----------------------------------------------------------------------------------------------
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_materia );
        dbHelper = new BaseDatosHelper ( this );
        grupo = findViewById ( R.id.textGrupo );
        grupo.setText ( getIntent ( ).getStringExtra ( "Grupo" ) );
        horario = findViewById ( R.id.textHorario );
        horario.setText ( getIntent ( ).getStringExtra ( "Horario" ) );
        Intent intent = getIntent ( );

        setTitle ( intent.getStringExtra ( "Id" ) + " " + intent.getStringExtra ( "Nombre" ) );
        androidx.appcompat.widget.Toolbar myToolbar = findViewById ( R.id.tb_materia );
        setSupportActionBar ( myToolbar );


    }


    //----------------------------------------------------------------------------------------------
    public void tareas ( View v ) {
        String materia = getIntent ( ).getStringExtra ( "Id" );
        Intent intent = new Intent ( this, TareasActivity.class );
        intent.putExtra ( "idMateria", materia );
        startActivity ( intent );
    }

    //----------------------------------------------------------------------------------------------
    public void alumnos ( View v ) {

        String id = getIntent ( ).getStringExtra ( "Id" );
        Intent intent = new Intent ( this, AlumnosActivity.class );
        intent.putExtra ( "Id", id );
        startActivity ( intent );
    }


    //----------------------------------------------------------------------------------------------
    public void alertBorrarMateria ( View v ) {
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );

        builder.setIcon ( R.drawable.itl ).setView ( R.layout.alerta_eliminar_materia ).setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                dialog.dismiss ( );
            }
        } ).setPositiveButton ( "Eliminar", new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick ( DialogInterface dialog, int which ) {
                borrarMateria ( v );
            }
        } ).create ( ).show ( );
    }

    //----------------------------------------------------------------------------------------------
    public void borrarMateria ( View v ) {
        Intent intent = getIntent ( );
        String idMateria = intent.getStringExtra ( "Id" );
        String nombreMateria = intent.getStringExtra ( "Nombre" );
        boolean materiaBorrada = dbHelper.deleteClase ( idMateria, nombreMateria );

        if ( materiaBorrada ) {
            Intent resultIntent = new Intent ( );
            setResult ( RESULT_OK, resultIntent );
            finish ( );
        } else {
            Toast.makeText ( this, "Error al borrar la materia", Toast.LENGTH_LONG ).show ( );
        }
    }
}