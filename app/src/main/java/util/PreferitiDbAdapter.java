package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import entity.Preferito;
import util.DbBitmapUtility;
public class PreferitiDbAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    //Costruttuore, metto il contesto chiamante nella variabile contesto
    public PreferitiDbAdapter(Context context) {
        this.context = context;
    }

    //Apre un datbase scrivibile
    private PreferitiDbAdapter open() throws SQLException {
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

    public void addPreferito(String nome, String url, Bitmap img) {
        open();
        ContentValues valori = new ContentValues();
        valori.put(DbHelper.KEY_PREFERITI_NOME, nome);
        valori.put(DbHelper.KEY_PREFERITI_URL, url);
        valori.put(DbHelper.KEY_PREFERITI_IMG, DbBitmapUtility.getBytes(img));
        database.insert(DbHelper.TABLE_PREFERITI, null, valori);
        close();
    }


    public List<Preferito> ListPreferiti() {
        List<Preferito> preferitoList = new ArrayList<Preferito>();

        String selectQuery = "SELECT  * FROM " + DbHelper.TABLE_PREFERITI;

        open();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Preferito preferito = new Preferito();
                preferito.setId(cursor.getString(0));
                preferito.setName(cursor.getString(1));
                preferito.setUrl(cursor.getString(2));
                preferito.setImg(DbBitmapUtility.getImage(cursor.getBlob(3)));

                preferitoList.add(preferito);
            } while (cursor.moveToNext());
        }

        return preferitoList;
    }

    public int updatPreferite(Preferito preferito) {
        open();

        ContentValues values = new ContentValues();
        values.put(DbHelper.KEY_PREFERITI_NOME, preferito.getName());

        return database.update(DbHelper.TABLE_PREFERITI, values, DbHelper.KEY_PREFERITI_ID + " = ?",
                new String[] { String.valueOf(preferito.getId()) });
    }

    public void deletePreferito(Preferito preferito) {
        open();
        database.delete(DbHelper.TABLE_PREFERITI, DbHelper.KEY_PREFERITI_ID + " = ?", new String[] { String.valueOf(preferito.getId()) });
        close();
    }

    public void deleteAll() {
        open();
        database.delete(DbHelper.TABLE_PREFERITI, null, null);
        close();
    }
    //

}
