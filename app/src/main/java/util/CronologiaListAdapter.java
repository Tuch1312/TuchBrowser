package util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tuch.tuchbrowser.R;

import java.util.List;

import entity.Visitato;



public class CronologiaListAdapter extends BaseAdapter {
    private Context context = null;
    private List<Visitato> visitatoList = null;

    public CronologiaListAdapter(Context context, List<Visitato> visitatoList) {
        this.context = context;
        this.visitatoList = visitatoList;
    }

    @Override
    public int getCount() {
        return visitatoList.size();
    }

    @Override
    public Object getItem(int position) {
        return visitatoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return visitatoList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_liss_element_cronologia, null);
        }
        Visitato vis = (Visitato) getItem(position);

        TextView txtUrl = (TextView) convertView.findViewById(R.id.ListUrlCronologia);
        TextView txtDate = (TextView) convertView.findViewById(R.id.Listdatacronologia);
        txtUrl.setText(vis.getUrl());
        txtDate.setText(vis.getDate());

        return convertView;


    }
}
