package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuch.tuchbrowser.R;

import java.util.List;

import entity.Preferito;

/**
 * Created by Tuch on 27/12/17.
 */

public class PreferitiListAdapter extends BaseAdapter {
    private Context context = null;
    List<Preferito> preferitoList = null;

    public PreferitiListAdapter(Context context, List<Preferito> preferitoList) {
        this.context = context;
        this.preferitoList = preferitoList;
    }

    @Override
    public int getCount() {
        return preferitoList.size();
    }

    @Override
    public Object getItem(int position) {
        return preferitoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return preferitoList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.layoutlisr_element_preferiti, null);
        }
        Preferito pref = (Preferito) getItem(position);

        ImageView img = (ImageView) convertView.findViewById(R.id.faviconPreferito) ;
        TextView txtNome = (TextView) convertView.findViewById(R.id.ListNomePreferito);
         img.setImageBitmap(pref.getImg());
        txtNome.setText(pref.getName());

        return convertView;




    }
}