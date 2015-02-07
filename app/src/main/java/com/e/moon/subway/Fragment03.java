package com.e.moon.subway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 세번째 탭 항목 : 역주변검색
 * */

public class Fragment03 extends Fragment {

    private static GoogleMap map;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        view = inflater.inflate(R.layout.frag03, container, false);

        final EditText editText = (EditText)view.findViewById(R.id.edit01);
        Button btn = (Button)view.findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Log.d("yy",text);
            }
        });

        if(checkNetworkState()) {
            // 지도 객체 참조
            map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            // 위치 확인하여 위치 표시 시작
            showCurrentLocation(37.618862, 126.921347);   //연신내 : 37.618862, 126.921347 (위도, 경도)
        } else {
            showSettingsAlert();
        }
        return view;
	}

    /**
     * 네트워크 연결 체크
     */
    protected boolean checkNetworkState() {
        ConnectivityManager manager =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mobile.isConnected() || wifi.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(checkNetworkState()){
                    map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
                    showCurrentLocation(37.618862, 126.921347);
                }
            }
        }, 5500);// 5.5초 정도 딜레이를 준 후 시작

    }

    /**
     * 현재 위치의 지도를 보여주기 위해 정의한 메소드
     *
     * @param latitude
     * @param longitude
     */
    private void showCurrentLocation(Double latitude, Double longitude) {
        // 현재 위치를 이용해 LatLon 객체 생성
        LatLng curPoint = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        MarkerOptions options = new MarkerOptions().position(curPoint);
        map.addMarker(options).showInfoWindow();   //마커 화면에 띄움
        // 지도 유형 설정. 지형도인 경우에는 GoogleMap.MAP_TYPE_TERRAIN, 위성 지도인 경우에는 GoogleMap.MAP_TYPE_SATELLITE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    /**
     * 네트워크 정보를 가져오지 못했을때 설정값으로 갈지 물어보는 alert 창
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());

        alertDialog.setTitle("네트워크 설정");
        alertDialog
                .setMessage("네트워크가 활성화 되어있지 않습니다.\n\n 설정창으로 가시겠습니까?");

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


}