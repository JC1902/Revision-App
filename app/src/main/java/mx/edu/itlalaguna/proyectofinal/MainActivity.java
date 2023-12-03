package mx.edu.itlalaguna.proyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {


    private ListView lvMaterias;

    // Arreglos por Default para pruebas
    private final String [] materias = { "Andorid" , "Topicos" , "Ejemplo " };
    private final String [] grupo = { "Grupo A" , "Grupo B" , "Grupo B" };

    private final int [] horaInicio = { 8 , 13  , 20};
    private final int [] horaFin = { 9 , 18  , 21};

    MiAdaptador adaptador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //asingnacion de titulo y toolbar al activity para mostrar el menu
        setTitle( "Materias");
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarMaterias);
        setSupportActionBar(myToolbar);

        // Llenado del ListView
        lvMaterias = findViewById ( R.id.materias_LV );
        adaptador = new MiAdaptador ( this , materias , grupo , horaInicio , horaFin);
        lvMaterias.setAdapter( adaptador );

        lvMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ---- Aqui iria el dirrecionamiento a la materia ----
                Toast.makeText(MainActivity.this, "Ir a Materia : "+materias[ position ], Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    // inflado del menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    // Manejo de acciones de los items del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.mniAcercaDe) {
            Intent intent = new Intent(MainActivity.this,  AcercaDeActivity.class);
            startActivity(intent);

        }else
            return super.onOptionsItemSelected(item);



        return true;
    }

    public void fabAgregarMaterias ( View v ) {
        //  ---- Aqui va el direccionamienta a nueva materia ----
        Toast.makeText(this, "Crear materia", Toast.LENGTH_SHORT).show();
    }

    // clase adaptador ... Le falta agregar un filter para que funcione el SearchView
    class MiAdaptador extends ArrayAdapter  {
        private Context context;
        private  String [] materias;
        private  String [] grupo;
        private  int [] horasIni;
        private  int [] horasFin;
        private List<String> materiasFiltradas;


        public MiAdaptador(Context c , String [] materias ,String[] grupo , int[] horaIni , int[] horasFin){
            super ( c, R.layout.lista_materias ,R.id.txvNombre , materias);
            context = c;
            this.materias = materias;
            this.grupo = grupo;
            this.horasIni = horaIni;
            this.horasFin = horasFin;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if( convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater)  context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.lista_materias, parent , false);
            }

            // campos a editar
            TextView nombre = convertView.findViewById( R.id.txvNombre );
            TextView grupo = convertView.findViewById( R.id.txvGrupo );
            TextView hora = convertView.findViewById( R.id.txvHora );
            GifImageView  gif = convertView.findViewById ( R.id.gifMateria );

            // variables
            int hr = horasIni [ position ];
            String horaFormato = hr + ":00 - "+ horasFin [ position ] + ":00";

            // asignaciones
            nombre.setText ( materias[ position ]);
            grupo.setText( this.grupo [ position ]);
            hora.setText ( horaFormato );

            // validacion de hora para el gif
            if ( hr >= 6 && hr < 12){
                gif.setImageResource( R.drawable.materia_manana );
            }else if ( hr >= 12 && hr < 18 )
                gif.setImageResource ( R.drawable.materia_tarde );
            else
                gif.setImageResource ( R.drawable.materia_noche );


            return convertView;
        }


    }



}
