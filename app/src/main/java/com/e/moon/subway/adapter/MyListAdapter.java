package com.e.moon.subway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.moon.subway.R;
import com.e.moon.subway.info.MyItemInfo;

import java.util.ArrayList;

/**
 * Created by moon on 15. 2. 12.
 */
public class MyListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater Inflater;
    ArrayList<MyItemInfo> arSrc;
    int layout;

    public MyListAdapter(Context con, int lay, ArrayList<MyItemInfo> aarSrc) {
        context = con;
        Inflater = (LayoutInflater)con.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        arSrc = aarSrc;
        layout = lay;
    }

    @Override
    public int getCount() {
        return arSrc.size();
    }

    @Override
    public Object getItem(int position) {
        return arSrc.get(position).Name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 항목의 뷰 생성
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = Inflater.inflate(layout, parent, false);
        }
        ImageView img = (ImageView)convertView.findViewById(R.id.img);
        img.setImageResource(arSrc.get(position).Icon);
        TextView txt = (TextView)convertView.findViewById(R.id.text);
        txt.setText(arSrc.get(position).Name);

        return convertView;
    }
}
