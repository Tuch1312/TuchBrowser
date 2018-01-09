package entity;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Tuch on 25/12/17.
 */

public class Visitato {

    private String id;
    private String url;
    private String data;
    private int visite;
    private Bitmap img;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getVisite() {
        return visite;
    }

    public void setVisite(int visite) {
        this.visite = visite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return data;
    }

    public void setDate(String data) {
        this.data = data;
    }




}
