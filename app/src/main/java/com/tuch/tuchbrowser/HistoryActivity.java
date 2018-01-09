package com.tuch.tuchbrowser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

import entity.Visitato;
import util.CronologiaDbAdapter;
import util.CronologiaListAdapter;
import util.PreferitiDbAdapter;


public class HistoryActivity extends Activity {
    private SharedPreferences prefs;
    List<Visitato> visitati = null;
    ListView cronList = null;
    CronologiaDbAdapter cda = null;
    CronologiaListAdapter cla = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        if (prefs.getBoolean("saveCronologia", true)) {
            cda = new CronologiaDbAdapter(this);
            visitati = cda.ListCronologia();
            cronList = (ListView) findViewById(R.id.listCronologia);
            cla = new CronologiaListAdapter(this, visitati);
            cronList.setAdapter(cla);
        } else {
            TextView avviso = findViewById(R.id.avvisoNoCronologia);
            avviso.setTextSize(14);
            avviso.setPadding(15, 30, 15, 0);
            //Todo usare Layout_weight al posto di Text_size
        }

        registerForContextMenu(cronList);

        final List<Visitato> finalVisitati = visitati;
        cronList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BrowserActivity.setPassedUrl(finalVisitati.get(position).getUrl());
                Intent openBrowser = new Intent(HistoryActivity.this, BrowserActivity.class);
                startActivity(openBrowser);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.elimina_tutti_options_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("saveCronologia", true)) {
            CronologiaDbAdapter ca = new CronologiaDbAdapter(this);
            visitati = ca.ListCronologia();
            cronList = (ListView) findViewById(R.id.listCronologia);
            cla = new CronologiaListAdapter(this, visitati);
            cronList.setAdapter(cla);
        } else {
            TextView avviso = findViewById(R.id.avvisoNoCronologia);
            avviso.setTextSize(14);
            avviso.setPadding(15, 30, 15, 0);
            //Todo usare Layout_weight al posto di Text_size
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cronologia_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_cronologia_elimina:
                cda.deleteVisitato(visitati.get(info.position));
                onResume();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.elimina_tutti_menu:
                CronologiaDbAdapter cda = new CronologiaDbAdapter(this);
                cda.deleteAll();
                HistoryActivity.this.onResume();
        }
        return true;
    }
}
