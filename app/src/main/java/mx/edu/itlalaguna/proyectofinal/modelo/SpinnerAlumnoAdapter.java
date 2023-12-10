package mx.edu.itlalaguna.proyectofinal.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import mx.edu.itlalaguna.proyectofinal.R;


public class SpinnerAlumnoAdapter extends ArrayAdapter< Alumno > {

    //----------------------------------------------------------------------------------------------
    // Constructor

    public SpinnerAlumnoAdapter ( @NonNull Context context, ArrayList< Alumno > alumnos ) {
        super ( context, 0, alumnos );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return inicializarView ( position, convertView, parent );
    }

    @Override
    public View getDropDownView ( int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return inicializarView ( position, convertView, parent );
    }

    //----------------------------------------------------------------------------------------------
    // Metodo personalizado para inicializar la vista del elemento dado por el argumento position

    private View inicializarView ( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {
        // Configurar el View llamado convertView para inflar  spinner_fila_imagen_texto  y
        // establecer la imagen del logo del club y el nombre del club en el layout.
        if(convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate( R.layout.spinner_fila_texto,parent,false);
        }
        TextView nombre=convertView.findViewById(R.id.txtvNombre);

        Alumno alumnos =getItem(position);
        nombre.setText(alumnos.getNombre());

        return convertView;
    }

    //----------------------------------------------------------------------------------------------
}
