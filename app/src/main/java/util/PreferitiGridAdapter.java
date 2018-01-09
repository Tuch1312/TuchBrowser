package util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuch.tuchbrowser.R;

import java.util.List;

import entity.Preferito;

/**
 * Created by Tuch on 28/12/17.
 */

public class PreferitiGridAdapter extends BaseAdapter {
    private Context context = null;
    private List<Preferito> preferitoList = null;

    public PreferitiGridAdapter(Context context, List<Preferito> preferiti) {
        this.context = context;
        preferitoList = preferiti;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_grid_preferiti, null);
        }
        Preferito pref = (Preferito) getItem(position);

        ImageView img = (ImageView) convertView.findViewById(R.id.iconaPreferitiOnMain) ;
        TextView txtNome = (TextView) convertView.findViewById(R.id.indirizzoPrefritiOnMain);
        img.setImageBitmap(pref.getImg());
        txtNome.setText(pref.getName());

        return convertView;



    }
}
