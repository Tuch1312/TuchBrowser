package entity;

import android.graphics.Bitmap;

/**
 * Created by Tuch on 21/12/17.
 */

public class Preferito {

    private String id;
    private String nome;
    private String url;
    private Bitmap img;


    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




}
