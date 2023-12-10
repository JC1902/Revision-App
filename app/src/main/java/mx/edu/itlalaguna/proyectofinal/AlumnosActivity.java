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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlumnosActivity extends AppCompatActivity {


    ArrayAdapter < String > arrayAdapter;
    private static final int REQUEST_CODE_BORRAR_ALUMNO_MATERIA = 1;
    private boolean isOpen = false;
    private Animation girarAdelante;
    private Animation girarAtras;
    private Animation animCerrar;
    private BaseDatosHelper dbHelper;
    private Animation animAbrir;
    private String clase;
    FloatingActionButton fabPrincipal;
    FloatingActionButton fabAgregarAlumnoE;
    FloatingActionButton fabAgregarAlumnoN;
    ListView lvAlumnos;
    private final List < String > alumnos = new ArrayList < String > ( );

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_alumnos );
        fabPrincipal = findViewById ( R.id.fab_Principal );
        dbHelper = new BaseDatosHelper ( this );
        fabAgregarAlumnoN = findViewById ( R.id.fab_AgregarAlumnoN );
        fabAgregarAlumnoE = findViewById ( R.id.fab_AgregarAlumnoE );
        animAbrir = AnimationUtils.loadAnimation ( this, R.anim.fab_open );
        animCerrar = AnimationUtils.loadAnimation ( this, R.anim.fab_close );
        girarAdelante = AnimationUtils.loadAnimation ( this, R.anim.rotate_forward );
        girarAtras = AnimationUtils.loadAnimation ( this, R.anim.rotate_backward );
        clase = getIntent ( ).getStringExtra ( "Id" );
        setTitle ( "Alumnos" );
        Toolbar myToolbar = findViewById ( R.id.tb_alumnos );
        setSupportActionBar ( myToolbar );


        Cursor cursorAlumnos = dbHelper.getAlumnosMateria ( clase );
        // Llenado del ListView
        lvAlumnos = findViewById ( R.id.lvAlumnos );
        if ( cursorAlumnos != null && cursorAlumnos.moveToFirst ( ) ) {
            do {
                // Obtener el nombre del alumno desde el cursor y agregarlo a la lista
                String numControl = cursorAlumnos.getString ( 0 );
                String nombre = cursorAlumnos.getString ( 1 );
                String apellidos = cursorAlumnos.getString ( 2 );
                alumnos.add ( numControl + " " + nombre + " " + apellidos );
            } while ( cursorAlumnos.moveToNext ( ) );

            // Inicializar el adaptador con los nombres de lss alumnos
            arrayAdapter = new ArrayAdapter <> ( this, R.layout.lista_alumnos, R.id.txvNombreAlumno, alumnos );
            lvAlumnos.setAdapter ( arrayAdapter );
        } else {
            // No hay datos en la tabla "alumnos", inicializar el adaptador con datos predeterminados
            arrayAdapter = new ArrayAdapter <> ( this, R.layout.lista_alumnos, R.id.txvNombreAlumno, alumnos );
            lvAlumnos.setAdapter ( arrayAdapter );
        }

        lvAlumnos.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView < ? > parent, View view, int position, long id ) {
                // ---- Aqui iria el dirrecionamiento a la materia ----
                Intent intent = new Intent ( AlumnosActivity.this, AlumnoActivity.class );
                intent.putExtra ( "idMateria", clase );
                intent.putExtra ( "Nombre", alumnos.get ( position ) );
                intent.putExtra ( "NumControl", obtenerNumControl (0).get ( position ) );
                startActivity ( intent );

                //Toast.makeText(MainActivity.this, "Ir a Materia : "+materias[ position ], Toast.LENGTH_SHORT).show();

            }
        } );

    }

    protected void onResume() {
        super.onResume();
        // Actualizar la lista de alumnos cada vez que la actividad vuelve a estar en primer plano
        actualizarListaAlumnos();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_BORRAR_ALUMNO_MATERIA && resultCode == RESULT_OK) {
            // Aquí puedes actualizar la lista de alumnos
            actualizarListaAlumnos();
        }
    }

    public boolean onCreateOptionsMenu ( Menu menu ) {
        MenuInflater inflater = getMenuInflater ( );
        inflater.inflate ( R.menu.menu_searchview, menu );

        MenuItem buscador = menu.findItem ( R.id.search );
        SearchView searchView = ( SearchView ) MenuItemCompat.getActionView ( buscador );

        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener ( ) {
            @Override
            public boolean onQueryTextSubmit ( String query ) {
                return false;
            }

            @Override
            public boolean onQueryTextChange ( String newText ) {
                AlumnosActivity.this.arrayAdapter.getFilter ( ).filter ( newText );
                return false;
            }
        } );

        return true;
    }

    public void fabPrincipalClick ( View view ) {
        animarBoton ( );
    }

    private List < String > obtenerTodoAlumnos ( ) {
        List < String > nombresAlumnos = new ArrayList <> ( );
        String id = getIntent ( ).getStringExtra ( "Id" );
        Cursor cursor = dbHelper.getAlumnosMateria ( id );  // Reemplaza con tu método para obtener alumnos
        if ( cursor != null && cursor.moveToFirst ( ) ) {
            do {
                String nombreAlumno = cursor.getString ( 0 ) + " " + cursor.getString ( 1 ) + " " + cursor.getString ( 2 );
                nombresAlumnos.add ( nombreAlumno );
            } while ( cursor.moveToNext ( ) );

            cursor.close ( );
        }

        return nombresAlumnos;
    }

    private List < String > obtenerNombresAlumnos ( int op) {
        Cursor cursor;
        if(op==0){
            String id = getIntent ( ).getStringExtra ( "Id" );
            cursor = dbHelper.getAlumnosMateria ( id );  // Reemplaza con tu método para obtener alumnos
        }else{
            cursor = dbHelper.getAlumnos ();
        }
        List < String > nombresAlumnos = new ArrayList <> ( );
        if ( cursor != null && cursor.moveToFirst ( ) ) {
            do {
                String nombreAlumno = cursor.getString ( 1 ) + " " + cursor.getString ( 2 );
                nombresAlumnos.add ( nombreAlumno );
            } while ( cursor.moveToNext ( ) );

            cursor.close ( );
        }

        return nombresAlumnos;
    }

    private List < String > obtenerNumControl ( int op) {
        List < String > numControl = new ArrayList <> ( );
        Cursor cursor;
        if(op==0){
            String id = getIntent ( ).getStringExtra ( "Id" );
            cursor = dbHelper.getAlumnosMateria ( id );  // Reemplaza con tu método para obtener alumnos
        }else{
            cursor = dbHelper.getAlumnos ();
        }
        if ( cursor != null && cursor.moveToFirst ( ) ) {
            do {
                String nomCont = cursor.getString ( 0 );
                numControl.add ( nomCont );
            } while ( cursor.moveToNext ( ) );

            cursor.close ( );
        }

        return numControl;
    }

    public void agregarAlumnoE ( View v ) {
        String id = getIntent ( ).getStringExtra ( "Id" );
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );
        // Infla el diseño de la alerta
        View alertaAgregarAlumnoExistente = getLayoutInflater ( ).inflate ( R.layout.alerta_agregar_alumno_existente, null );
        // Obtiene el Spinner del diseño de la alerta
        Spinner spinnerAlumnos = alertaAgregarAlumnoExistente.findViewById ( R.id.spinNumControl );
        // Obtiene los nombres de los alumnos desde la base de datos
        List < String > nombresAlumnos = obtenerNombresAlumnos ( 1);
        List < String > numControl = obtenerNumControl ( 1);
        // Crea un ArrayAdapter para el Spinner
        ArrayAdapter < String > adapter = new ArrayAdapter <> ( this, android.R.layout.simple_spinner_item, nombresAlumnos );
        adapter.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        // Establece el ArrayAdapter en el Spinner
        spinnerAlumnos.setAdapter ( adapter );
        builder.setIcon ( R.drawable.itl )
                .setView ( alertaAgregarAlumnoExistente )
                .setPositiveButton ( "Guardar", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        // Acciones al hacer clic en Guardar

                        boolean alumnoClase = dbHelper.addDatosClaseAlumno ( clase, numControl.get ( spinnerAlumnos.getSelectedItemPosition () ) );
                        if ( alumnoClase ) {
                            alumnos.clear ( );
                            alumnos.addAll ( obtenerTodoAlumnos ( ) );
                            arrayAdapter.notifyDataSetChanged ( );
                        } else {
                            Toast.makeText ( AlumnosActivity.this,
                                    "Error al agregar el alumno a la clase",
                                    Toast.LENGTH_LONG ).show ( );
                        }                    }
                } )
                .setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } ).create ( ).show ( );
    }

    public void agregarAlumnoN ( View v ) {
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );

        // Inflar la vista del diálogo
        View dialogView = getLayoutInflater ( ).inflate ( R.layout.alerta_agregar_alumno, null );
        builder.setIcon ( R.drawable.itl )
                .setView ( dialogView )
                .setPositiveButton ( "Guardar", new DialogInterface.OnClickListener ( ) {
                            @Override
                            public void onClick ( DialogInterface dialog, int which ) {
                                // Obtener referencias de los elementos de la vista del diálogo
                                EditText edtNumControl = dialogView.findViewById ( R.id.edtNoControl );
                                EditText edtNombreAlumno = dialogView.findViewById ( R.id.edtNombreAlumno );
                                EditText edtApellidosPat = dialogView.findViewById ( R.id.edtApellidoPat );
                                EditText edtApellidosMat = dialogView.findViewById ( R.id.edtApellidoMat );

                                // Obtener los valores ingresados por el usuario
                                String numControl = edtNumControl.getText ( ).toString ( ).trim ( );
                                String nombreAlumno = edtNombreAlumno.getText ( ).toString ( ).trim ( );
                                String apellidosAlumno = ( edtApellidosPat.getText ( ).toString ( ).trim ( ) ) + " " + ( edtApellidosMat.getText ( ).toString ( ).trim ( ) );

                                // Verificar que los campos no estén vacíos antes de agregar el alumno
                                if ( !numControl.isEmpty ( ) && !nombreAlumno.isEmpty ( ) && !apellidosAlumno.isEmpty ( ) ) {
                                    // Llamar al método addDatosAlumno de tu base de datos para agregar el nuevo alumno
                                    boolean alumnoAgregado = dbHelper.addDatosAlumno ( numControl, nombreAlumno, apellidosAlumno );
                                    if ( alumnoAgregado ) {
                                        Toast.makeText ( AlumnosActivity.this,
                                                "Alumno Agregado",
                                                Toast.LENGTH_LONG ).show ( );

                                        boolean alumnoClase = dbHelper.addDatosClaseAlumno ( clase, numControl );
                                        if ( alumnoClase ) {
                                            alumnos.clear ( );
                                            alumnos.addAll ( obtenerTodoAlumnos ( ) );
                                            arrayAdapter.notifyDataSetChanged ( );
                                        } else {
                                            Toast.makeText ( AlumnosActivity.this,
                                                    "Error al agregar el alumno a la clase",
                                                    Toast.LENGTH_LONG ).show ( );
                                        }
                                    } else {
                                        Toast.makeText ( AlumnosActivity.this,
                                                "Error al agregar el alumno",
                                                Toast.LENGTH_LONG ).show ( );
                                    }
                                } else {
                                    Toast.makeText ( AlumnosActivity.this,
                                            "Todos los campos son requeridos",
                                            Toast.LENGTH_LONG ).show ( );
                                }
                            }
                        }
                )
                .setNegativeButton ( "Cancelar", new DialogInterface.OnClickListener ( ) {
                    @Override
                    public void onClick ( DialogInterface dialog, int which ) {
                        dialog.dismiss ( );
                    }
                } ).create ( ).show ( );
    }

    private void actualizarListaAlumnos ( ) {
        dbHelper = new BaseDatosHelper ( this );
        Cursor cursorAlumnos = dbHelper.getAlumnosMateria ( clase );
        alumnos.clear ( );

        if ( cursorAlumnos != null && cursorAlumnos.moveToFirst ( ) ) {
            do {
                // Obtener el nombre del alumno desde el cursor y agregarlo a la lista
                String numControl = cursorAlumnos.getString ( 0 );
                String nombre = cursorAlumnos.getString ( 1 );
                String apellidos = cursorAlumnos.getString ( 2 );
                alumnos.add ( numControl + " " + nombre + " " + apellidos );
            } while ( cursorAlumnos.moveToNext ( ) );

            if ( arrayAdapter != null ) {
                arrayAdapter.notifyDataSetChanged ( );
            } else {
                arrayAdapter = new ArrayAdapter <> ( this, R.layout.lista_alumnos, R.id.txvNombreAlumno, alumnos );
                lvAlumnos.setAdapter ( arrayAdapter );
            }
        } else {
            arrayAdapter.clear ( );
        }
    }

    private void animarBoton ( ) {
        if ( isOpen ) {
            fabPrincipal.startAnimation ( girarAtras );
            fabAgregarAlumnoE.startAnimation ( animCerrar );
            fabAgregarAlumnoN.startAnimation ( animCerrar );
            fabAgregarAlumnoE.setClickable ( false );
            fabAgregarAlumnoN.setClickable ( false );
            isOpen = false;
        } else {
            fabPrincipal.startAnimation ( girarAdelante );
            fabAgregarAlumnoE.startAnimation ( animAbrir );
            fabAgregarAlumnoN.startAnimation ( animAbrir );
            fabAgregarAlumnoE.startAnimation ( animAbrir );
            fabAgregarAlumnoN.setClickable ( true );
            fabAgregarAlumnoE.setClickable ( true );
            isOpen = true;
        }
    }
}