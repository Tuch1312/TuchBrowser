package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import entity.Visitato;


public class PiuVisitatiAdapter {

    private Context context;
    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private boolean firstacces;

    //Costruttuore, metto il contesto chiamante nella variabile contesto
    public PiuVisitatiAdapter(Context context) {
        this.context = context;
    }

    //Apre un datbase scrivibile
    private PiuVisitatiAdapter open() throws SQLException {
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();

        return this;
    }

    //Chiude il databse
    private void close() {
        dbHelper.close();
        database.close();
    }

    /*
    Questo metodo permette di prendere dalla tabella cronologia tutte le voci uguali e fare il conto di
     quante volte compaiono in modo da stabilire quante volte si stato visitato iun link, le voci gruppate
     vengono poi inserite nel databse dei piu visitati (senza data)
     */
    public void countVisite() {
        List<Visitato> visitati = new ArrayList<Visitato>();
        String emptyPiuVisitati = "DELETE FROM " + DbHelper.TABLE_PIU_VISITATI + " WHERE " +  DbHelper.KEY_PIU_VISITATI_URL + " IS NOT NULL" ;


        String selectQuery = "SELECT " + DbHelper.KEY_CRONOLOGIA_URL + ", count(" + DbHelper.KEY_CRONOLOGIA_URL + ") FROM " + DbHelper.TABLE_CRONOLOGIA + " group by " + DbHelper.KEY_CRONOLOGIA_URL + ";";

        open();
        database.execSQL(emptyPiuVisitati);

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Visitato visitato = new Visitato();
                visitato.setUrl(cursor.getString(0));
                visitato.setVisite(cursor.getInt(1));

                visitati.add(visitato);
            } while (cursor.moveToNext());
        }

        for (Visitato vis : visitati) {
            ContentValues valori = new ContentValues();
            valori.put(DbHelper.KEY_PIU_VISITATI_URL, vis.getUrl());
            valori.put(DbHelper.KEY_PIU_VISITATI_N_VISITE, vis.getVisite());
            database.insert(DbHelper.TABLE_PIU_VISITATI, null, valori);
        }

        close();
        cursor.close();
    }

    public List<Visitato> getPiuVisitati(int num,Context context) {

        countVisite();

        List<Visitato> visitati = new ArrayList<Visitato>();
        int previusMax = 0;

        String firstSelection = "select " + DbHelper.KEY_PIU_VISITATI_URL + ", " + DbHelper.KEY_PIU_VISITATI_N_VISITE + " from " + DbHelper.TABLE_PIU_VISITATI +
                " where " + DbHelper.KEY_PIU_VISITATI_N_VISITE + " = (select max(" + DbHelper.KEY_PIU_VISITATI_N_VISITE + ") from " + DbHelper.TABLE_PIU_VISITATI + ");";


        open();
        Cursor cursor = database.rawQuery(firstSelection, null);
        int count = 0;
        if(cursor.moveToFirst()) {
            do {
                Visitato visitato = new Visitato();
                visitato.setUrl(cursor.getString(0));
                visitato.setVisite(cursor.getInt(1));
                visitati.add(visitato);
                previusMax = visitato.getVisite();
                count++;
            } while (cursor.moveToNext() && count < num);
        }

        for (int i = 0; i <  num; i++) {
            String otherSelection = "select " + DbHelper.KEY_PIU_VISITATI_URL + ", " + DbHelper.KEY_PIU_VISITATI_N_VISITE + " from " + DbHelper.TABLE_PIU_VISITATI +
                    " where " + DbHelper.KEY_PIU_VISITATI_N_VISITE + " = (select max(" + DbHelper.KEY_PIU_VISITATI_N_VISITE + ") from " + DbHelper.TABLE_PIU_VISITATI +
                    " where " + DbHelper.KEY_PIU_VISITATI_N_VISITE + " < " + previusMax + ");";
            cursor = database.rawQuery(otherSelection, null);
            if (cursor.moveToFirst()) {
                do {
                    if(count < num) {
                        Visitato visitato = new Visitato();
                        visitato.setUrl(cursor.getString(0));
                        visitato.setVisite(cursor.getInt(1));
                        visitati.add(visitato);
                        previusMax = visitato.getVisite();
                        count++;
                    }
                } while (cursor.moveToNext());
            }
        }
        close();
        cursor.close();

        return visitati;
    }

    protected void addPiuVisitati(Visitato visitato){
        open();
        ContentValues valori = new ContentValues();
        valori.put(DbHelper.KEY_PIU_VISITATI_N_VISITE, visitato.getVisite());
        valori.put(DbHelper.KEY_PIU_VISITATI_URL, visitato.getUrl());
        database.insert(DbHelper.TABLE_PIU_VISITATI, null, valori);
        close();

    }


    }









