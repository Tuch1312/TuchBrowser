package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "tuchbrowserdb";

    public static final String TABLE_PREFERITI = "preferiti";
    public static final String TABLE_CRONOLOGIA = "cronologia";
    public static final String TABLE_PIU_VISITATI = "piuvisitati";

    //chiavi per preferiti
    public static final String KEY_PREFERITI_ID = "_id";
    public static final String KEY_PREFERITI_NOME = "name";
    public static final String KEY_PREFERITI_URL = "url";
    public static final String KEY_PREFERITI_IMG = "img";

    //chiavi per cronologia
    public static final String KEY_CRONOLOGIA_ID = "_id";
    public static final String KEY_CRONOLOGIA_DATA = "data";
    public static final String KEY_CRONOLOGIA_URL = "url";

    //Chiavi per PiuVisitatiAdapter
    public static final String KEY_PIU_VISITATI_ID = "_id";
    public static final String KEY_PIU_VISITATI_URL = "url";
    public static final String KEY_PIU_VISITATI_N_VISITE = "visite";
    public static final String KEY_PIU_VISITATI_IMG = "img";

    private static final String SQL_DB_CREATE_PREFERITI = "CREATE TABLE preferiti (_id integer primary key autoincrement, name text not null, url text not null, img blob);";
    private static final String SQL_DB_CREATE_CRONOLOGIA = "CREATE TABLE cronologia (_id integer primary key autoincrement, url text not null, data text);";
    private static final String SQL_DB_CREATE_PIU_VISITATI = "CREATE TABLE piuvisitati (_id integer primary key autoincrement, url text not null, visite integer not null, img blob);";



    //Costruttore
    DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DB_CREATE_PREFERITI);
        db.execSQL(SQL_DB_CREATE_CRONOLOGIA);
        db.execSQL(SQL_DB_CREATE_PIU_VISITATI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREFERITI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRONOLOGIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRONOLOGIA);

        onCreate(db);
    }
}
