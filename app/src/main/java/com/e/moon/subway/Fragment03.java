package com.e.moon.subway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 세번째 탭 항목 : 역주변검색
 * */

public class Fragment03 extends Fragment {

    private static GoogleMap map;
    private View view;
    private Geocoder mCoder;
    private int mSelect= -1;        //초기에 선택할 값 -1은 아무것도 선택안함
    private List<String> Locality;        // 알람창에 띄울 제목(지역) 리스트
    private List<HashMap> mapList;          // 위도,경도를 담은 hashMap
    private HashMap<String,Double> req;  //위도 ,경도 담을 HashMap
    private String[] strings;
    private InputMethodManager input_Manager;

    TextView result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        view = inflater.inflate(R.layout.frag03, container, false);

        input_Manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mCoder = new Geocoder(getActivity(), Locale.KOREAN);
        result = (TextView)view.findViewById(R.id.text22);
        final EditText editText = (EditText)view.findViewById(R.id.edit01);
        Button btn = (Button)view.findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_Manager.hideSoftInputFromWindow(editText.getWindowToken(),0);  //키보드 닫기
                String text = editText.getText().toString();
                List<Address> addresses;
                Locality = new ArrayList<String>();  //클릭 시 마다 객체 생성하여  데이터가 쌓여서 중복되지 않게 4+2+1
                mapList = new ArrayList<HashMap>();

                if(checkNetworkState()) {
                    if ( text.length() != 0 ) {
                        try {
                            addresses = mCoder.getFromLocationName(text, 10);    //지역 이름으로 주소 찾기. 최대 10개까지 세팅
                        } catch (IOException e) {
                            result.setText("IO Error (실행도중 에러) : " + e.getMessage());
                            return;
                        }

                        if (addresses == null) {  // null 값일 경우
                            result.setText("주소를 찾을 수 없습니다.");
                        }
                        try{
                            for (Address add : addresses) {
                                if (add.getCountryCode().trim().equalsIgnoreCase("KR")) {
                                    Locality.add(add.getAdminArea()+" "+add.getLocality()+" "+add.getFeatureName());
                                    req = new HashMap<String, Double>();
                                    req.put("Latitude",add.getLatitude());
                                    req.put("Longitude", add.getLongitude());
                                    mapList.add(req);

                                }
                            }
                            strings = new String[Locality.size()];
                            for (int i = 0; i < strings.length; i++) {
                                strings[i] = Locality.get(i);
                            }

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("길 찾기")
                                    .setIcon(R.drawable.menu_search)
                                    .setCancelable(false)
                                    .setSingleChoiceItems(strings, mSelect, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            result.setText(strings[which]);
                                            HashMap<String, Double> reqMap = mapList.get(which);
                                            showLocation(reqMap.get("Latitude"), reqMap.get("Longitude"));
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("닫기", null)
                                    .show();
                            //(스레드로 감싸기)
                        }catch(NullPointerException e) {
                            result.setText("주소를 찾을 수 없습니다.");
                        }
                    }
                } else {
                    showSettingsAlert();
                }

            }
        });

        if(checkNetworkState()) {
            // 지도 객체 참조
            map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            // 위치 확인하여 위치 표시 시작
            showCurrentLocation(37.563739, 126.978907);   //서울시청 : 37.563739, 126.978907 (위도, 경도)
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

   /* @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(checkNetworkState()){
                    map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
                    showCurrentLocation(37.563739, 126.978907);
                }
            }
        }, 5500);// 5.5초 정도 딜레이를 준 후 시작

    }*/

    /**
     * 현재 위치의 지도를 보여주기 위해 정의한 메소드
     *
     * @param latitude
     * @param longitude
     */
    private void showCurrentLocation(Double latitude, Double longitude) {
        // 현재 위치를 이용해 LatLon 객체 생성
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 11));
        // 지도 유형 설정. 지형도인 경우에는 GoogleMap.MAP_TYPE_TERRAIN, 위성 지도인 경우에는 GoogleMap.MAP_TYPE_SATELLITE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    /**
     * 지정한 위치를 보여주는 메소드
     */
    private void showLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        MarkerOptions options = new MarkerOptions().position(curPoint);
        map.addMarker(options).showInfoWindow();   //마커 화면에 띄움
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