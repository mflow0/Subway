package com.e.moon.subway;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 두번째 탭 항목
 * */

 public class Fragment02 extends Fragment {
    private ArrayAdapter<CharSequence> adspin;
    private boolean mInitSpinner;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		View v = inflater.inflate( R.layout.frag02, container, false );

        Spinner spin = (Spinner)v.findViewById(R.id.spin);
        spin.setPrompt("조건 선택");
        adspin = ArrayAdapter.createFromResource(getActivity(), R.array.station,
                android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //android.R.layout.simple_dropdown_item_1line
        spin.setAdapter(adspin);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				/* 초기화시의 선택 제외시
				if (mInitSpinner == false) {
					mInitSpinner = true;
					return;
				}
				//*/
               // Toast.makeText(getActivity(), adspin.getItem(position) + "는 맛있다.",
               //         Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
        return v;
	}
	
}