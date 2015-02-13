package com.e.moon.subway;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.e.moon.subway.adapter.MyListAdapter;
import com.e.moon.subway.info.MyItemInfo;

import java.util.ArrayList;

/**
 * 네번째 탭 항목
 * */

 public class Fragment04 extends Fragment {
    private ArrayList<MyItemInfo> arItem;
    private MyListAdapter MyAdapter;
    private MyItemInfo mi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag04, container, false);

        arItem = new ArrayList<MyItemInfo>();
        mi = new MyItemInfo(R.drawable.facebook, "페이스북 연동");
        arItem.add(mi);
        mi = new MyItemInfo(R.drawable.no_mail, "문의 / 오류보고");
        arItem.add(mi);
        mi = new MyItemInfo(R.drawable.info, "앱 정보");
        arItem.add(mi);

        MyAdapter = new MyListAdapter(getActivity(),
                R.layout.listitem, arItem);

        ListView list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(MyAdapter);
        list.setDivider(new ColorDrawable(Color.LTGRAY));
        list.setDividerHeight(3);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setOnItemClickListener(mItemClickListener);
        return v;
    }

    AdapterView.OnItemClickListener mItemClickListener =
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Select Item = ", String.valueOf(position));
                    switch (position) {
                        case 0:
                            Intent intent1 = new Intent(getActivity(), Facebook.class);
                            startActivityForResult(intent1, 1001);
                            break;
                        case 1:
                            Uri uri = Uri.parse("mailto:moonbs11@naver.com");
                            Intent intent2 = new Intent(Intent.ACTION_SENDTO, uri);
                            intent2.putExtra(Intent.EXTRA_SUBJECT, "[문의/오류보고]");
                            intent2.putExtra(Intent.EXTRA_TEXT, "DEVICE : "+ Build.MODEL+"\nAndroid SDK Version : "+Build.VERSION.SDK_INT+"\n\n");
                            startActivityForResult(intent2, 1002);
                            break;
                        case 2:
                            Intent intent3 = new Intent(getActivity(), AboutDialog.class);
                            startActivityForResult(intent3, 1003);
                            break;
                    }
                }
            };
}
