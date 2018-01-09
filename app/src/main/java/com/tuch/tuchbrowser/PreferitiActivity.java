package com.tuch.tuchbrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import entity.Preferito;
import util.PreferitiDbAdapter;
import util.PreferitiGridAdapter;
import util.PreferitiListAdapter;

public class PreferitiActivity extends Activity {

    List<Preferito> preferitiList = null;
    PreferitiDbAdapter pda = null;
    ListView prefList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferiti);
        pda = new PreferitiDbAdapter(this);

        preferitiList = pda.ListPreferiti();
        prefList = findViewById(R.id.listPreferiti);
        PreferitiListAdapter pla = new PreferitiListAdapter(this, preferitiList);
        prefList.setAdapter(pla);
        registerForContextMenu(prefList);


        prefList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BrowserActivity.setPassedUrl(preferitiList.get(position).getUrl());
                Intent openBrowser = new Intent(PreferitiActivity.this, BrowserActivity.class);
                startActivity(openBrowser);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        preferitiList = pda.ListPreferiti();
        PreferitiListAdapter pla = new PreferitiListAdapter(this, preferitiList);
        prefList.setAdapter(pla);
        registerForContextMenu(prefList);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preferiti_context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = new MenuInflater(this);
        mi.inflate(R.menu.elimina_tutti_options_menu, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.context_preferiti_modifica:

                //Identifica super layout
                LinearLayout mainLayout = (LinearLayout)
                        findViewById(R.id.preferitiLayout);
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
                        PreferitiDbAdapter pda = new PreferitiDbAdapter(PreferitiActivity.this);
                        pda.updatPreferite(preferito);
                        PreferitiActivity.this.onResume();

                    }
                });

                return true;

            case R.id.context_preferiti_elimina:
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.elimina_tutti_menu:
                PreferitiDbAdapter pda = new PreferitiDbAdapter(this);
                pda.deleteAll();
                PreferitiActivity.this.onResume();
        }
    return true;
    }
}
