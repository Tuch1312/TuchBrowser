package com.tuch.tuchbrowser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import entity.Preferito;
import entity.Visitato;
import util.PiuVisitatiAdapter;
import util.PreferitiDbAdapter;
import util.PreferitiGridAdapter;

public class MainActivity extends Activity {

    EditText barraricerca;
    SharedPreferences prefs;
    List<Preferito> preferitiList = new ArrayList<Preferito>();
    List<Visitato> piuVisitatiList = new ArrayList<Visitato>();
    GridView grigliaPreferiti;
    ListView listaPiuVisitati;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ottengo una istanza di sharedprerences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        barraricerca = (EditText) findViewById(R.id.barraRicercMain);
        Button btncerca = (Button) findViewById(R.id.buttonCercaMain);

        //Pulsante cerca
        btncerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserActivity.setPassedUrl(barraricerca.getText().toString());
                Intent openBrowser = new Intent(MainActivity.this, BrowserActivity.class);
                startActivity(openBrowser);
            }
        });

    }

    @Override
    protected void onResume() {
        barraricerca.setText("");
        clearView();
        getPreferitiView();
        getPiuVisitatiView();
        super.onResume();
    }


    //Istanzio il menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_menu, menu);
        return true;
    }

    //gestisco la pressoine dei pulsanti del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.main_menu_cronologia:
                Intent historyIntent = new Intent(this, HistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.main_menu_preferiti:
                Intent prefIntent = new Intent(this, PreferitiActivity.class);
                startActivity(prefIntent);
                break;
            case R.id.main_menu_impostazioni:
                Intent impIntent = new Intent(this, ImpostazioniActivity.class);
                startActivity(impIntent);
                break;


        }
        return true;

    }

    private void clearView() {
        preferitiList.clear();
        String[] array = new String[0];
        PreferitiGridAdapter pga = new PreferitiGridAdapter(this, preferitiList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        grigliaPreferiti = (GridView) findViewById(R.id.preferitiGrid);
        listaPiuVisitati = findViewById(R.id.visitatiRecenteGrid);
        grigliaPreferiti.setAdapter(pga);
        listaPiuVisitati.setAdapter(arrayAdapter);
    }

    //Sezione Preferiti
    private void getPreferitiView() {
        grigliaPreferiti = (GridView) findViewById(R.id.preferitiGrid);
        PreferitiDbAdapter pda = new PreferitiDbAdapter(this);
        preferitiList = pda.ListPreferiti();


        // Se ce salvato almeno un preferio
        if (preferitiList.size() > 0) {
            PreferitiGridAdapter pga = new PreferitiGridAdapter(this, preferitiList);
            grigliaPreferiti.setAdapter(pga);
            TextView avviso = findViewById(R.id.avvisoNoPreferiti);
            avviso.setTextSize(0);
            avviso.setPadding(0, 0, 0, 0);
            // se non ce nessun preferito
        } else {
            TextView avviso = findViewById(R.id.avvisoNoPreferiti);
            avviso.setTextSize(14);
            avviso.setPadding(15, 30, 15, 0);
            //Todo usare Layout_weight al posto di Text_size
        }
        registerForContextMenu(grigliaPreferiti);

        grigliaPreferiti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BrowserActivity.setPassedUrl(preferitiList.get(position).getUrl());
                Intent openBrowser = new Intent(MainActivity.this, BrowserActivity.class);
                startActivity(openBrowser);

            }
        });
    }

    //Sezione più visitati
    private void getPiuVisitatiView() {
        listaPiuVisitati = findViewById(R.id.visitatiRecenteGrid);
        TextView titolo = findViewById(R.id.PiuVisitatititolo);

        if (prefs.getBoolean("savePiuVisitati", true)) {
            listaPiuVisitati.setVisibility(View.VISIBLE);
            titolo.setVisibility(View.VISIBLE);
            PiuVisitatiAdapter pv = new PiuVisitatiAdapter(this);
            int qty = 6;
            String[] listUrl = new String[qty];
            piuVisitatiList = pv.getPiuVisitati(qty, MainActivity.this);

            if (piuVisitatiList.size() > 5) {
                int x = 0;
                for (Visitato vis : piuVisitatiList) {
                    listUrl[x] = vis.getUrl();
                    x++;
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listUrl);
                listaPiuVisitati.setAdapter(arrayAdapter);
            } else {
                TextView avviso = findViewById(R.id.avvisoNoVisitati);
                avviso.setTextSize(14);
                avviso.setPadding(15, 30, 15, 0);
                //Todo usare Layout_weight al posto di Text_size
            }

            listaPiuVisitati.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    BrowserActivity.setPassedUrl(piuVisitatiList.get(position).getUrl());
                    Intent openBrowser = new Intent(MainActivity.this, BrowserActivity.class);
                    startActivity(openBrowser);

                }
            });

        } else {
            listaPiuVisitati.setVisibility(View.INVISIBLE);
            titolo.setVisibility(View.INVISIBLE);
            //Todo, else {cambaire peso listview Piuvisitati a 0
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preferiti_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.context_preferiti_modifica:

                //Identifica super layout
                LinearLayout mainLayout = (LinearLayout)
                        findViewById(R.id.mainLayout);
                //Istanzio un LayoutInflater
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                //Istanzio una view e gli infilo il layout del popup
                final View popupView = inflater.inflate(R.layout.layout_pop_ip_window, null);
                //Setto width e height per il pop-up
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                //Istanzio un popUp e gli passo la view e le msiure
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                ;
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchable(true);
                //Mostro il popup in centro alla scehermata
                popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                //gestisco gli elementi del popUp
                final EditText inputNome = popupView.findViewById(R.id.inputNamePreferito);
                Button aggiungi = popupView.findViewById(R.id.buttonAggiungiPreferiti);
                aggiungi.setText("Modifica");
                popupView.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                inputNome.setText(preferitiList.get(info.position).getName());
                final Preferito preferito = new Preferito();
                aggiungi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preferito.setName(inputNome.getText().toString());
                        preferito.setImg(preferitiList.get(info.position).getImg());
                        preferito.setId(preferitiList.get(info.position).getId());
                        preferito.setUrl(preferitiList.get(info.position).getUrl());
                        popupWindow.dismiss();
                        PreferitiDbAdapter pda = new PreferitiDbAdapter(MainActivity.this);
                        pda.updatPreferite(preferito);
                        MainActivity.this.onResume();

                    }
                });

                return true;

            case R.id.context_preferiti_elimina:
                PreferitiDbAdapter pda = new PreferitiDbAdapter(MainActivity.this);
                pda.deletePreferito(preferitiList.get(info.position));
                onResume();
                return true;

            case R.id.context_preferiti_condividi:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = preferitiList.get(info.position).getUrl();
                String shareSubject = preferitiList.get(info.position).getName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Condividi con:"));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}

