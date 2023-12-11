package mx.edu.itlalaguna.proyectofinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class BaseDatosHelper extends SQLiteOpenHelper {

    private static final String TAG = "BaseDatosHelper";

    private static final String DB_NAME = "materiasDB";
    private static final int DB_VERSION = 1;
    private static final String tabla[] = {
            "clase",
            "alumno",
            "claseAlumno",
            "tareas",
            "tareasAlumno" };

    private static final String columnasClase[] = {
            "idMateria",
            "nombreClase",
            "grupo",
            "horaClase"
    };
    private static final String columnasAlumno[] = {
            "numControl",
            "nombreAlumno",
            "apellidosAlumno"
    };
    private static final String creacion[] = {
            "CREATE TABLE materias (" + columnasClase[ 0 ] + " TEXT PRIMARY KEY, " + columnasClase[ 1 ] + " TEXT, " + columnasClase[ 2 ] + " TEXT, "
                    + columnasClase[ 3 ] + " INTEGER)",
            "CREATE TABLE alumno (" + columnasAlumno[ 0 ] + " TEXT PRIMARY KEY, " + columnasAlumno[ 1 ] + " TEXT, " + columnasAlumno[ 2 ] + " TEXT)",
            "CREATE TABLE claseAlumno (idMateria TEXT, numControl INTEGER, " +
                    "PRIMARY KEY (idMateria,numControl), " +
                    "FOREIGN KEY (idMateria) REFERENCES materias(idMateria) ON DELETE CASCADE, " +
                    "FOREIGN KEY (numControl) REFERENCES alumno(numControl) ON DELETE CASCADE)",
            "CREATE TABLE tareas (idTarea INTEGER PRIMARY KEY AUTOINCREMENT, nombreTarea TEXT, descripcion TEXT, idMateria TEXT )",
            "CREATE TABLE tareasAlumno (" +
                    " idTarea INTEGER ," +
                    " numControl INTEGER," +
                    " hecha INTEGER, " +
                    " PRIMARY KEY (idTarea,numControl)," +
                    " FOREIGN KEY (idTarea) REFERENCES tareas(idTarea) ON DELETE CASCADE," +
                    " FOREIGN KEY (numControl) REFERENCES alumno(numControl) ON DELETE CASCADE" +
                    ")"
    };

    //----------------------------------------------------------------------------------------------

    public BaseDatosHelper ( Context context ) {

        super ( context, DB_NAME, null, DB_VERSION );
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate ( SQLiteDatabase db ) {
        int col = 0;
        for ( String tabla : creacion ) {
            String crearTabla = tabla;
            col++;
            db.execSQL ( crearTabla );
        }

    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onUpgrade ( SQLiteDatabase db, int i, int i1 ) {
        for ( String tabla : tabla ) {
            db.execSQL ( "DROP IF TABLE EXISTS " + tabla );
            onCreate ( db );
        }
    }

    // Inserts
    //----------------------------------------------------------------------------------------------
    public boolean addDatosAlumno ( String numControl, String nombreAlumno, String apellidosAlumno ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        ContentValues contentValues = new ContentValues ( );
        contentValues.put ( "numControl", numControl );
        contentValues.put ( "nombreAlumno", nombreAlumno );
        contentValues.put ( "apellidosAlumno", apellidosAlumno );

        Log.d ( TAG, "addDatosAlumno: Agregando alumno a la tabla alumno" );

        long resultado = db.insert ( "alumno", null, contentValues );
        return resultado != -1;
    }

    //----------------------------------------------------------------------------------------------
    public boolean addDatosClase ( String idMateria, String nombreClase, String grupo, int horaClase ) {
        SQLiteDatabase db = this.getWritableDatabase ( );

        ContentValues contentValues = new ContentValues ( );
        contentValues.put ( "idMateria", idMateria );
        contentValues.put ( "nombreClase", nombreClase );
        contentValues.put ( "grupo", grupo );
        contentValues.put ( "horaClase", horaClase );

        Log.d ( TAG, "addDatosClase: Agregando " + nombreClase + " a la tabla clase" );

        long resultado = db.insert ( "materias", null, contentValues );

        if ( resultado == -1 ) {
            return false;
        } else {
            return true;
        }
    }

    //----------------------------------------------------------------------------------------------
    public boolean addDatosClaseAlumno ( String idMateria, String numControl ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        ContentValues contentValues = new ContentValues ( );
        contentValues.put ( "idMateria", idMateria );
        contentValues.put ( "numControl", numControl );

        Log.d ( TAG, "addDatosClaseAlumno: Agregando relación clase-alumno a la tabla claseAlumno" );

        long resultado = db.insert ( "claseAlumno", null, contentValues );
        return resultado != -1;
    }

    //----------------------------------------------------------------------------------------------
    public long addDatosTarea ( String nombreTarea, String descripcionTarea, String idMateria ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        ContentValues values = new ContentValues ( );
        values.put ( "nombreTarea", nombreTarea );
        values.put ( "descripcion", descripcionTarea );
        values.put ( "idMateria", idMateria );

        long idTarea = db.insert ( "tareas", null, values );
        db.close ( );
        return idTarea;
    }

    //----------------------------------------------------------------------------------------------
    public boolean addDatosTareasAlumno ( long idTarea, String numControl, int hecha ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        ContentValues contentValues = new ContentValues ( );
        contentValues.put ( "idTarea", idTarea );
        contentValues.put ( "numControl", numControl );
        contentValues.put ( "hecha", hecha );

        Log.d ( TAG, "addDatosTareasAlumno: Agregando tarea a la tabla tareasAlumno" );

        long resultado = db.insert ( "tareasAlumno", null, contentValues );

        return resultado != -1;
    }

    //----------------------------------------------------------------------------------------------
    public Cursor getAlumno ( String buscado ) {
        SQLiteDatabase db = this.getReadableDatabase ( );
        String query = "SELECT * FROM alumnos WHERE numControl = '" + buscado + "'";
        Cursor data = db.rawQuery ( query, null );
        return data;
    }

    //----------------------------------------------------------------------------------------------
    public Cursor getAlumnos() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT alumno.* " +
                "FROM alumno " +
                "ORDER BY alumno.nombreAlumno ASC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //----------------------------------------------------------------------------------------------
    public Cursor getAlumnosMateria(String idMateria) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT alumno.* " +
                "FROM alumno " +
                "JOIN claseAlumno ON alumno.numControl = claseAlumno.numControl " +
                "WHERE claseAlumno.idMateria = ? " +
                "ORDER BY alumno.numControl ASC";
        String[] selectionArgs = {idMateria};
        Cursor data = db.rawQuery(query, selectionArgs);
        return data;
    }


    //----------------------------------------------------------------------------------------------
    public Cursor getMateria() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM materias ORDER BY nombreClase ASC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //----------------------------------------------------------------------------------------------
    public Cursor getTareas(String buscado) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT idtarea, nombreTarea, descripcion FROM tareas WHERE idMateria = '" + buscado + "' ORDER BY nombreTarea ASC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //----------------------------------------------------------------------------------------------
    public Cursor getTareaAlumnos(String idTarea) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT alumno.numControl, alumno.nombreAlumno, alumno.apellidosAlumno, tareasAlumno.hecha " +
                "FROM alumno " +
                "JOIN tareasAlumno ON alumno.numControl = tareasAlumno.numControl " +
                "WHERE tareasAlumno.idTarea = '" + idTarea + "'" +
                "ORDER BY alumno.nombreAlumno ASC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    //----------------------------------------------------------------------------------------------
    public Cursor getTareasAsignadasAlumnoEnMateria(String numControl, String idMateria) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT tareas.idTarea, tareas.nombreTarea, tareas.descripcion, tareasAlumno.hecha " +
                "FROM tareasAlumno " +
                "JOIN materias ON tareas.idMateria = materias.idMateria " +
                "JOIN tareas ON tareas.idTarea = tareasAlumno.idTarea " +
                "AND tareasAlumno.numControl = '" + numControl + "' " +
                "WHERE materias.idMateria = '" + idMateria + "'" +
                "ORDER BY tareas.nombreTarea ASC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //----------------------------------------------------------------------------------------------
    public Cursor getIdsTareasEnMateria ( String idMateria ) {
        SQLiteDatabase db = this.getReadableDatabase ( );
        String query = "SELECT idTarea " +
                "FROM tareas " +
                "WHERE idMateria = '" + idMateria + "'";
        Cursor data = db.rawQuery ( query, null );
        return data;
    }

    //----------------------------------------------------------------------------------------------
    public void updateAlumnoTarea ( int hecha, String idAlumno, int idTarea ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "UPDATE tareasAlumno SET hecha = '" + hecha + "' WHERE numControl = '" + idAlumno + "'" +
                " AND idTarea = '" + idTarea + "'";
        Log.d ( TAG, "updateName: query: " + query );
        Log.d ( TAG, "updateName: Setting name to " + hecha );
        db.execSQL ( query );
    }

    //----------------------------------------------------------------------------------------------
    public void updateDatosAlumno ( String numControl, String nuevoNombre, String nuevosApellidos ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "UPDATE alumno SET nombreAlumno = '" + nuevoNombre + "', apellidosAlumno = '" + nuevosApellidos + "' " +
                "WHERE numControl = '" + numControl + "'";
        Log.d ( TAG, "updateDatosAlumno: query: " + query );
        Log.d ( TAG, "updateDatosAlumno: Setting name to " + nuevoNombre + " " + nuevosApellidos );
        db.execSQL ( query );
    }

    //----------------------------------------------------------------------------------------------
    public void updateDatosTareas ( int idTarea, String nuevoNombreTarea, String nuevaDescripcion, String nuevaidMateria ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "UPDATE tareas SET nombreTarea = '" + nuevoNombreTarea + "', descripcion = '" + nuevaDescripcion + "', idMateria = '" + nuevaidMateria + "'" +
                " WHERE idTarea = " + idTarea;
        Log.d ( TAG, "updateDatosTareas: query: " + query );
        Log.d ( TAG, "updateDatosTareas: Setting task details for " + nuevoNombreTarea );
        db.execSQL ( query );
    }

    //----------------------------------------------------------------------------------------------
    public void updateDatosClase ( String idMateria, String nuevoNombreClase, String nuevoGrupo, int nuevaHoraClase ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "UPDATE materias SET nombreClase = '" + nuevoNombreClase + "', grupo = '" + nuevoGrupo + "', horaClase = " + nuevaHoraClase +
                " WHERE idMateria = '" + idMateria + "'";
        Log.d ( TAG, "updateDatosClase: query: " + query );
        Log.d ( TAG, "updateDatosClase: Setting class details for " + nuevoNombreClase + " " + nuevoGrupo );
        db.execSQL ( query );
    }

    //----------------------------------------------------------------------------------------------
    public void deleteAlumno ( String numControl, String nombre ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "DELETE FROM alumno WHERE numControl = '" + numControl + "'";
        Log.d ( TAG, "deleteNombre: query: " + query );
        Log.d ( TAG, "deleteNombre: Eliminando " + nombre + " de la tabla alumnos." );
        db.execSQL ( query );
    }

    //----------------------------------------------------------------------------------------------
    public boolean deleteAlumnoClase ( String numControl, String idMateria ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "DELETE FROM claseAlumno WHERE numControl = ? AND idMateria = ?";
        Log.d ( TAG, "deleteAlumnoClase: query: " + query );

        try {
            db.execSQL ( query, new String[] { numControl, idMateria } );
            Log.d ( TAG, "deleteAlumnoClase: Eliminé al alumno de la materia" );
            return true;
        } catch ( SQLException e ) {
            Log.e ( TAG, "deleteAlumnoClase: Error al eliminar al alumno de la materia: " + e.getMessage ( ) );
            return false;
        }
    }

    //----------------------------------------------------------------------------------------------
    public boolean deleteClase ( String idMateria, String nombre ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "DELETE FROM materias WHERE idMateria = '" + idMateria + "'";
        Log.d ( TAG, "deleteNombre: query: " + query );
        Log.d ( TAG, "deleteNombre: Eliminando " + nombre + " de la tabla clase." );

        try {
            db.execSQL ( query );
            return true;
        } catch ( SQLException e ) {
            Log.e ( TAG, "Error al eliminar la materia: " + e.getMessage ( ) );
            return false;
        }
    }
    //----------------------------------------------------------------------------------------------
    public boolean deleteClaseAlumno ( String idMateria, String numControl ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "DELETE FROM claseAlumno  WHERE numControl = '" + numControl + "' AND idMateria= '" + idMateria + "'";
        Log.d ( TAG, "deleteNombre: query: " + query );
        Log.d ( TAG, "deleteNombre: Eliminando " + idMateria + " de la tabla clase." );
        try {
            db.execSQL ( query );
            return true;
        } catch ( SQLException e ) {
            Log.e ( TAG, "Error al eliminar la materia: " + e.getMessage ( ) );
            return false;
        }
    }
    //----------------------------------------------------------------------------------------------
    public boolean deleteTareaAlumno ( String numControl, long idTarea ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "DELETE FROM tareasAlumno  WHERE numControl = '" + numControl + "' AND idTarea= '" + idTarea + "'";
        Log.d ( TAG, "deleteNombre: query: " + query );
        Log.d ( TAG, "deleteNombre: Eliminando  de la tabla tareasAlumno." );

        try {
            db.execSQL ( query );
            return true;
        } catch ( SQLException e ) {
            Log.e ( TAG, "Error al eliminar la tarea: " + e.getMessage ( ) );
            return false;
        }

    }

    //----------------------------------------------------------------------------------------------
    public boolean deleteTarea ( String idTarea, String nombre ) {
        SQLiteDatabase db = this.getWritableDatabase ( );
        String query = "DELETE FROM tareas WHERE idTarea = '" + idTarea + "'";
        Log.d ( TAG, "deleteNombre: query: " + query );
        Log.d ( TAG, "deleteNombre: Eliminando " + nombre + " de la tabla tareas." );

        try {
            db.execSQL ( query );
            return true;
        } catch ( SQLException e ) {
            Log.e ( TAG, "Error al eliminar la tarea: " + e.getMessage ( ) );
            return false;
        }

    }
    //----------------------------------------------------------------------------------------------
}