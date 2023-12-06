package mx.edu.itlalaguna.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDatosHelper extends SQLiteOpenHelper {

    private static final String TAG = "BaseDatosHelper";

    private static final String DB_NAME    = "materiasDB";
    private static final int    DB_VERSION = 1;
    private static final String tabla []= {
            "clase",
            "alumno",
            "claseAlumno",
            "tareas",
            "tareasAlumno"};
    private static final String COL1 = "ID";
    private static final String COL2 = "nombre";
    private static final String creacion[]={
            "CREATE TABLE clase (idClase TEXT PRIMARY KEY, nombreClase TEXT, grupo TEXT, horaClase INTEGER)",
            "CREATE TABLE alumno (numControl TEXT PRIMARY KEY, nombreAlumno TEXT, apellidosAlumno TEXT)",
            "CREATE TABLE claseAlumno (idClase TEXT, numControl INTEGER, " +
                    "PRIMARY KEY (idClase,numControl), " +
                    "FOREIGN KEY (idClase) REFERENCES clase(idClase), " +
                    "FOREIGN KEY (numControl) REFERENCES alumno(numControl))",
            "CREATE TABLE tareas (idTarea INTEGER PRIMARY KEY AUTOINCREMENT, nombreTarea TEXT, descripcion TEXT, idClase TEXT )",
            "CREATE TABLE tareasAlumno (idTarea INTEGER , numControl INTEGER, hecha INTEGER " +
                    "PRIMARY KEY (idTarea,numControl), " +
                    "FOREIGN KEY (idTarea) REFERENCES tareas(idTarea), " +
                    "FOREIGN KEY (numControl) REFERENCES alumno(numControl))"
    };

    //----------------------------------------------------------------------------------------------

    public BaseDatosHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION );
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        int col=0;
        for(String tabla:creacion){
            String crearTabla = tabla;
            col++;
            db.execSQL ( crearTabla );
        }

    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onUpgrade ( SQLiteDatabase db, int i, int i1 ) {
        for(String tabla:tabla) {
            db.execSQL("DROP IF TABLE EXISTS " + tabla);
            onCreate(db);
        }
    }
    // Inserts
    //----------------------------------------------------------------------------------------------
    public boolean addDatosAlumno(String numControl, String nombreAlumno, String apellidosAlumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("numControl", numControl);
        contentValues.put("nombreAlumno", nombreAlumno);
        contentValues.put("apellidosAlumno", apellidosAlumno);

        Log.d(TAG, "addDatosAlumno: Agregando alumno a la tabla alumno");

        long resultado = db.insert("alumno", null, contentValues);

        // si se insertó correctamente resultado valdrá -1
        return resultado != -1;
    }

    //----------------------------------------------------------------------------------------------
    public boolean addDatosClase(String idClase, String nombreClase, String grupo, int horaClase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idClase", idClase);
        contentValues.put("nombreClase", nombreClase);
        contentValues.put("grupo", grupo);
        contentValues.put("horaClase", horaClase);

        Log.d(TAG, "addDatosClase: Agregando " + nombreClase + " a la tabla clase");

        long resultado = db.insert("clase", null, contentValues);

        if ( resultado == -1 ) {
            return false;
        } else {
            return true;
        }
    }
    //----------------------------------------------------------------------------------------------
    public boolean addDatosClaseAlumno(String idClase, int numControl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idClase", idClase);
        contentValues.put("numControl", numControl);

        Log.d(TAG, "addDatosClaseAlumno: Agregando relación clase-alumno a la tabla claseAlumno");

        long resultado = db.insert("claseAlumno", null, contentValues);

        // Si se insertó correctamente, resultado valdrá -1
        return resultado != -1;
    }

    //----------------------------------------------------------------------------------------------
    public boolean addDatosTarea(String nombreTarea, String descripcion, String idClase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombreTarea", nombreTarea);
        contentValues.put("descripcion", descripcion);
        contentValues.put("idClase", idClase);

        Log.d(TAG, "addDatosTarea: Agregando tarea a la tabla tareas");

        long resultado = db.insert("tareas", null, contentValues);

        // si se insertó correctamente resultado valdrá -1
        return resultado != -1;
    }
    //----------------------------------------------------------------------------------------------
    public boolean addDatosTareasAlumno(int idTarea, String numControl, int hecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idTarea", idTarea);
        contentValues.put("numControl", numControl);
        contentValues.put("hecha", hecha);

        Log.d(TAG, "addDatosTareasAlumno: Agregando tarea a la tabla tareasAlumno");

        long resultado = db.insert("tareasAlumno", null, contentValues);

        // si se insertó correctamente resultado valdrá -1
        return resultado != -1;
    }
    //----------------------------------------------------------------------------------------------

    public Cursor getDatos () {
        // getReadableDatabase () y getWritableDatabase () devuelven el mismo objeto
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tabla;
        Cursor data = db.rawQuery ( query, null );
        return data;
    }

    //----------------------------------------------------------------------------------------------
    public Cursor getAlumnos (String buscado ) {
        SQLiteDatabase db = this.getReadableDatabase ();
        String query = "SELECT * FROM alumnos WHERE numControl = '" + buscado + "'";
        Cursor data = db.rawQuery ( query, null );
        return data;
    }
    //----------------------------------------------------------------------------------------------
    public Cursor getClase (String Tabla,String columna, String buscado ) {
        SQLiteDatabase db = this.getReadableDatabase ();
        String query = "SELECT * FROM clase ";
        Cursor data = db.rawQuery ( query, null );
        return data;
    }

    public Cursor getTareas (String buscado ) {
        SQLiteDatabase db = this.getReadableDatabase ();
        String query = "SELECT idtarea,nombreTarea,descripcion FROM tareas where idClase = '"+buscado+ "'";
        Cursor data = db.rawQuery ( query, null );
        return data;
    }

    public Cursor getTareaAlumnos(String idTarea) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT alumno.*, tareasAlumno.hecha " +
                "FROM alumno " +
                "JOIN tareasAlumno ON alumno.numControl = tareasAlumno.numControl " +
                "WHERE tareasAlumno.idTarea = '" + idTarea + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getTareasa (String buscado ) {
        SQLiteDatabase db = this.getReadableDatabase ();
        String query = "SELECT idtarea,nombreTarea,descripcion FROM tareas where idTarea = '"+buscado+ "'";
        Cursor data = db.rawQuery ( query, null );
        return data;
    }
    //----------------------------------------------------------------------------------------------

    public void updateNombre ( String newNombre, int id, String oldNombre ) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + tabla + " SET " + COL2 +
                " = '" + newNombre + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldNombre + "'";
        Log.d ( TAG, "updateName: query: " + query );
        Log.d ( TAG, "updateName: Setting name to " + newNombre );
        db.execSQL ( query );
    }

    //----------------------------------------------------------------------------------------------

    public void deleteNombre ( int id, String nombre ){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + tabla + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + nombre + "'";
        Log.d ( TAG, "deleteNombre: query: " + query );
        Log.d ( TAG, "deleteNombre: Eliminando " + nombre + " de la base de datos.");
        db.execSQL(query);
    }

    //----------------------------------------------------------------------------------------------
}