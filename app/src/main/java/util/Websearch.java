package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tuch on 27/12/17.
 */

public class Websearch {

    private SharedPreferences prefs;
    Context context;

    public Websearch(Context context) {
        this.context = context;

    }


    public String search(String url) {

        //Ottengo una istanza di sharedprerences
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //Divido la stringa in ingresso in singole parole
        String[] splittedUrl = url.trim().split(" ");
        String returnedUrl = null;

        //Se  il motore di ricerca imopstato è Ecosia    | ecosia = 1 | google = 2 |  
        if (prefs.getInt("motoreRicerca", 1) == 1) {
            switch (splittedUrl.length) {
                case 1:
                    if (splittedUrl[0].equalsIgnoreCase("")) {
                        returnedUrl = "https://www.ecosia.org/";
                    } else if (splittedUrl[0].contains("/") && (splittedUrl[0].contains("http") || splittedUrl[0].contains("https"))) {
                        returnedUrl = url;
                    } else if (splittedUrl[0].indexOf("www") == 0 && !(splittedUrl[0].contains(" "))) {
                        returnedUrl = "https://" + splittedUrl[0];
                    } else {
                        returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0];
                    }
                    break;
                case 2:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1];
                    break;
                case 3:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2];
                    break;
                case 4:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3];
                    break;
                case 5:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4];
                    break;
                case 6:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5];
                    break;
                case 7:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6];
                    break;
                case 8:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6] + splittedUrl[7];
                    break;
                case 9:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6] + splittedUrl[7] + splittedUrl[8];
                    break;
                case 10:
                    returnedUrl = "https://www.ecosia.org/search?q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6] + splittedUrl[7] + splittedUrl[8] + splittedUrl[9];
                    break;
                default:
                    returnedUrl = "https://www.ecosia.org/";
                    break;
            }

        //altrimenti se il motote di riecra impostato è google   | ecosia = 1 | google = 2 |
        } else if (prefs.getInt("motoreRicerca", 1) == 2){

            switch (splittedUrl.length) {
                case 1:
                    if (splittedUrl[0].equalsIgnoreCase("")) {
                        returnedUrl = "https://www.google.com/";
                    } else if (splittedUrl[0].contains("/") && (splittedUrl[0].contains("http") || splittedUrl[0].contains("https"))) {
                        returnedUrl = url;
                    } else if (splittedUrl[0].indexOf("www") == 0 && !(splittedUrl[0].contains(" "))) {
                        returnedUrl = "https://" + splittedUrl[0];
                    } else {
                        returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0];
                    }
                    break;
                case 2:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1];
                    break;
                case 3:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2];
                    break;
                case 4:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3];
                    break;
                case 5:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4];
                    break;
                case 6:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5];
                    break;
                case 7:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6];
                    break;
                case 8:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6] + splittedUrl[7];
                    break;
                case 9:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6] + splittedUrl[7] + splittedUrl[8];
                    break;
                case 10:
                    returnedUrl = "https://www.google.com/search?client=tuchbrowser&q=" + splittedUrl[0] + "+" + splittedUrl[1] + splittedUrl[2] + splittedUrl[3] + splittedUrl[4] + splittedUrl[5] + splittedUrl[6] + splittedUrl[7] + splittedUrl[8] + splittedUrl[9];
                    break;
                default:
                    returnedUrl = "https://www.google.com";
                    break;
            }

        }


        return returnedUrl;
    }
}

    
