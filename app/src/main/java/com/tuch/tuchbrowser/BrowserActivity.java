package com.tuch.tuchbrowser;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import util.PreferitiDbAdapter;
import util.WebViewclientcustom;
import util.Websearch;


public class BrowserActivity extends Activity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    static WebView body = null;
    public static EditText barraRicerca = null;
    private Websearch ws = null;
    private static String passedUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);


        //Istanzio tutti gli oggetti della GUi
        Button btnCerca = (Button) findViewById(R.id.buttonCerca);
        Button btnHome = (Button) findViewById(R.id.buttonHome);
        Button btnAvanti = (Button) findViewById(R.id.buttonAvanti);
        Button btnIndietro = (Button) findViewById(R.id.buttonIndietro);
        barraRicerca = (EditText) findViewById(R.id.barraRicerca);
        body = (WebView) findViewById(R.id.body);

        //Istanzio la classe per la ricerca sul web diretta
        ws = new Websearch(this);


        //attivo Javascript
        body.getSettings().setJavaScriptEnabled(true);

        //Evito l'apertura del browser di default
        body.setWebViewClient(new WebViewclientcustom(BrowserActivity.this));


        /*Gestico la pressione dei pulsanti*/

        //Pulsante home, quando schiaccio carica la pagina salvato come home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barraRicerca.setText(prefs.getString("home", "http://www.ecosia.com"));
                body.loadUrl(barraRicerca.getText().toString());
            }
        });

        //Pulsante cerca, invia l'URL dalla barra di ricerca alla Webview
        btnCerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.loadUrl(ws.search(barraRicerca.getText().toString()));
            }
        });

        //Pulsante indietro, posso adare indidtro? Se si vai, se no visualizza il toast
        btnIndietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (body.canGoBack()) {
                    body.goBack();

                } else {
                    Toast.makeText(BrowserActivity.this, "Non si può più tornare indietro...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Pulsante avanti, posso adare avanti? Se si vai, se no visualizza il toast
        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (body.canGoForward()) {
                    body.goForward();
                } else {
                    Toast.makeText(BrowserActivity.this, "Non si può andare più avanti...", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onResume() {

        super.onResume();
        body = (WebView) findViewById(R.id.body);
        //Apro la url passata se presente
        if (passedUrl != null) {
            body.loadUrl(ws.search(passedUrl));
            barraRicerca.setText(passedUrl);
            passedUrl = null;
            barraRicerca.setText(body.getUrl());
        }
    }

    @Override
    protected void onStop() {
        passedUrl = body.getUrl();
        super.onStop();
        body.stopLoading();
        body.loadUrl("about:blank");
    }

    //Istanzio il menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.browsermenu, menu);
        return true;
    }

    //gestisco la pressoine dei pulsanti del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.browser_menu_setHome:
               editor.putString("home", body.getUrl());
               editor.apply();
                Toast.makeText(this, "Pagina corrente salvata come Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.browser_menu_addPreferito:
                addPreferito();
                break;
            case R.id.browser_menu_history:
                Intent historyIntent = new Intent(this, HistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.browser_menu_preferiti:
                Intent prefIntent = new Intent(this, PreferitiActivity.class);
                startActivity(prefIntent);
                break;
            case R.id.browser_menu_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = body.getUrl();
                String shareSubject = body.getTitle();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Condividi con:"));
                break;

        }
        return true;

    }

    public static String getPassedUrl() {
        return passedUrl;
    }

    public static void setPassedUrl(String passedUrl) {
        BrowserActivity.passedUrl = passedUrl;
    }

    private void addPreferito() {
        final String url = body.getUrl();
        final Bitmap img = body.getFavicon();

        //Identifica super layout
        LinearLayout mainLayout = (LinearLayout)
                findViewById(R.id.browserLayout);
        //Istanzio un LayoutInflater
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        //Istanzio una view e gli infilo il layout del popup
        final View popupView = inflater.inflate(R.layout.layout_pop_ip_window, null);
        //Setto width e height per il pop-up
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //Istanzio un popUp e  gli passo la view e le msiure
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());;
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        //Mostro il popup in centro alla scehermata
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        //gestisco gli elementi del popUp
        final EditText inputNome = popupView.findViewById(R.id.inputNamePreferito);
        Button aggiungi = popupView.findViewById(R.id.buttonAggiungiPreferiti);

        inputNome.setText(body.getTitle());
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = inputNome.getText().toString();
                popupWindow.dismiss();
                PreferitiDbAdapter adpter = new PreferitiDbAdapter(BrowserActivity.this);
                adpter.addPreferito(nome, url, img);

            }
        });

    }

}






