package util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entity.Preferito;
import entity.Visitato;

public class CronologiaDbAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    private SharedPreferences prefs;


    //Costruttuore, metto il contesto chiamante nella variabile contesto
    public CronologiaDbAdapter(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //Apre un datbase scrivibile
    private CronologiaDbAdapter open() throws SQLException {
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();

        return this;
    }

    //Chiude il databse
    private void close() {
        dbHelper.close();
        database.close();
    }


    /* CRUD */

    public void addVisitato(String url){
        if (prefs.getBoolean("saveCronologia", true)) {
            open();
            String currentTime = (Calendar.getInstance().getTime()).toString();
            ContentValues valori = new ContentValues();
            valori.put(DbHelper.KEY_CRONOLOGIA_DATA, currentTime);
            valori.put(DbHelper.KEY_CRONOLOGIA_URL, url);
            database.insert(DbHelper.TABLE_CRONOLOGIA, null, valori);
        }
        close();

    }


    public List<Visitato> ListCronologia() {
        List<Visitato> visitati = new ArrayList<Visitato>();

        String selectQuery = "SELECT * FROM " + DbHelper.TABLE_CRONOLOGIA + " ORDER BY " + DbHelper.KEY_CRONOLOGIA_ID + " DESC;";

        open();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Visitato visitato = new Visitato();
                visitato.setId(cursor.getString(0));
                visitato.setUrl(cursor.getString(1));
                visitato.setDate(cursor.getString(2));

                if (visitato.getDate() != null) {
                    visitati.add(visitato);
                }
            } while (cursor.moveToNext());
        }
        close();
        cursor.close();
        return visitati;
    }

    public void deleteVisitato(Visitato visitato) {
        open();
        database.delete(DbHelper.TABLE_CRONOLOGIA, DbHelper.KEY_CRONOLOGIA_ID + " = ?", new String[] { String.valueOf(visitato.getId()) });
        close();
    }

    public void deleteAll() {
        open();
        database.delete(DbHelper.TABLE_CRONOLOGIA, null, null);
        close();
    }
}