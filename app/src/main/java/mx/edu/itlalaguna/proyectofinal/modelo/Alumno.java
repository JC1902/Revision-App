package mx.edu.itlalaguna.proyectofinal.modelo;

public class Alumno {
    private String nombre;
    private String numControl;

    //----------------------------------------------------------------------------------------------
    public Alumno ( String nombre, String numControl ) {
        this.nombre = nombre;
        this.numControl = numControl;
    }

    //----------------------------------------------------------------------------------------------
    public String getNombre ( ) {
        return nombre;
    }

    //----------------------------------------------------------------------------------------------
    public void setNombre ( String nombre ) {
        this.nombre = nombre;
    }

    //----------------------------------------------------------------------------------------------
    public String getNumControl ( ) {
        return numControl;
    }

    //----------------------------------------------------------------------------------------------
    public void setNumControl ( String numControl ) {
        this.numControl = numControl;
    }
}
