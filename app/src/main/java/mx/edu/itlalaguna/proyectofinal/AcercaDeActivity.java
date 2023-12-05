package mx.edu.itlalaguna.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class AcercaDeActivity extends AppCompatActivity {

    TextView integrantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        integrantes = findViewById( R.id.txvEquipo );

        integrantes.append( "\n" + "Jorge Cisneros de la Torre 20130789\n" +
                            "Carlos Fernando Aguilera Garcia 20130811\n" +
                            "Cesar Gabriel Zamora Morales 20130849\n" +
                            "Pedro Sebastián García Favela 20130802\n" +
                            "Shalom Isai Salazar Arguijo 20130805" );

    }

    public void fabRegresarClick ( View v ) {
        finish ();
    }
}