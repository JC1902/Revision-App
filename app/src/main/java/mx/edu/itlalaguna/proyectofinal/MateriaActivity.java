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

    public static final int CODIGO_SELECCIONAR_ARCHIVO  = 1944;
    TextView horario;
    TextView grupo;
    private BaseDatosHelper dbHelper;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);
        dbHelper = new BaseDatosHelper ( this );
        grupo=findViewById ( R.id.textGrupo );
        grupo.setText ( getIntent ().getStringExtra ( "Grupo" ) );
        horario=findViewById ( R.id.textHorario );
        horario.setText ( getIntent ().getStringExtra ( "Horario" ) );
        Intent intent = getIntent();

        setTitle(intent.getStringExtra ("Id")+" "+ intent.getStringExtra("Nombre" ) );
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.tb_materia);
        setSupportActionBar(myToolbar);


    }


    public void tareas ( View v ) {
        String materia = getIntent().getStringExtra( "Id" ) ;
        Intent intent = new Intent(this, TareasActivity.class);
        //Toast.makeText( this, materia.getStringExtra(), Toast.LENGTH_LONG ).show();
        intent.putExtra( "idMateria", materia );
        startActivity( intent );
    }

    public void alumnos ( View v ) {

        String id=getIntent ().getStringExtra ( "Id" );
        Intent intent = new Intent(this, AlumnosActivity.class);
        intent.putExtra ( "Id", id );
        startActivity ( intent );
    }


    public void alertBorrarMateria ( View v){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setIcon(R.drawable.itl )
                .setView( R.layout.alerta_eliminar_materia)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrarMateria( v );
                    }
                })
                .create()
                .show();
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


    // ================  Abrir Archivo ===============================
    public void btnExploradorArchs    ( View v ) {

        Intent intent = new Intent ( Intent.ACTION_GET_CONTENT );
        intent.setType             ( "*/*" );
        intent.addCategory         (Intent.CATEGORY_OPENABLE);

        try {

            startActivityForResult ( Intent.createChooser ( intent , "Seleccione una opcion ") ,
                    CODIGO_SELECCIONAR_ARCHIVO );

        } catch ( ActivityNotFoundException e){
            Toast.makeText(this, "Explorador de archivos no encontrado.\n" +
                    "Por favor instale un exploador de archivos.", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_SELECCIONAR_ARCHIVO) {
            // Se invocó la app para explorar archivos

            if (resultCode == RESULT_OK) {
                // metodo de importacion
                importar( data );

            }
        }
    }

    //logica de importarcion
    // De momento le deje un alert de placeholder
    public  void importar (Intent data ){
        String archivo = data.toString();

        new AlertDialog.Builder(this)
                .setTitle("Importar archivo: ")
                .setMessage(archivo)
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }


}