package com.tuch.tuchbrowser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ImpostazioniActivity extends Activity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        //Ottengo una istanza di sharedprerences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        //Istanzio tutti gli oggetti della gui
        RadioGroup motoreRicercaRadiogroup = findViewById(R.id.motore_ricerca_radiogroup);
        RadioButton ecosiaRadioButton = findViewById(R.id.radioButton_ecosia);
        RadioButton googleRadioButton = findViewById(R.id.radioButton_google);
        final CheckBox cronologiaCheckbox = findViewById(R.id.checkBoxCronologia);
        final CheckBox piuVisitatiCheckbox = findViewById(R.id.piu_visitati_checkBox);

        //Inizializzazione radiobutton
        if (prefs.getInt("motoreRicerca", 1) == 1) {
            ecosiaRadioButton.setChecked(true);
            googleRadioButton.setChecked(false);
        } else if (prefs.getInt("motoreRicerca", 1) == 2) {
            ecosiaRadioButton.setChecked(false);
            googleRadioButton.setChecked(true);
        }

        //inizializzazione checkbox cronologia
        if (prefs.getBoolean("saveCronologia", true)) {
            cronologiaCheckbox.setChecked(true);
        } else {
            cronologiaCheckbox.setChecked(false);
        }

        //inizializzazione checkbox Piu Visitati
        if (prefs.getBoolean("savePiuVisitati", true)) {
            piuVisitatiCheckbox.setChecked(true);
        } else {
            piuVisitatiCheckbox.setChecked(false);
        }


        //Gestione pressione radiobutton per scelta motore di ricerca
        motoreRicercaRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case  R.id.radioButton_ecosia:
                    editor.putInt("motoreRicerca", 1);
                    editor.apply();
                    break;

                case  R.id.radioButton_google:
                    editor.putInt("motoreRicerca", 2);
                    editor.apply();
                    break;

            }
            }
        });

        //Gestione pressione checkbox cronologia
        cronologiaCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    editor.putBoolean("saveCronologia", true);
                    editor.apply();
                }

                if (!isChecked) {
                    editor.putBoolean("saveCronologia", false);
                    editor.putBoolean("savePiuVisitati", false);
                    piuVisitatiCheckbox.setChecked(false);
                    editor.apply();

                }

            }
        });


        //Gestione pressione checkbox Piu Visitati
        piuVisitatiCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    editor.putBoolean("savePiuVisitati", true);
                    editor.putBoolean("saveCronologia", true);
                    cronologiaCheckbox.setChecked(true);
                    editor.apply();
                }

                if (!isChecked) {
                    editor.putBoolean("savePiuVisitati", false);
                    editor.apply();

                }

            }
        });


    }
}
