package com.e.moon.subway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

/**
 * Created by moon on 15. 2. 17.
 */
public class LocationIntentReceiver extends BroadcastReceiver {

    private String mExpectedAction;
    private Intent mLastReceivedIntent;

    public LocationIntentReceiver(String expectedAction){
        mExpectedAction = expectedAction;
        mLastReceivedIntent = null;
    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter(mExpectedAction);
        return filter;
    }

    /**
     * 받았을 때 호출되는 메소드
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            mLastReceivedIntent = intent;

            int id = intent.getIntExtra("id", 0);
            double latitude = intent.getDoubleExtra("latitude", 0.0D);
            double longitude = intent.getDoubleExtra("longitude", 0.0D);

            Toast.makeText(context, "근접한 커피숍 : " + id + ", " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
            // 통지 구현해야됨 위치에 접근했을때 울릴 프로세스
        }
    }

    public Intent getLastReceivedIntent() {
        return mLastReceivedIntent;
    }

    public void clearReceivedIntents() {
        mLastReceivedIntent = null;
    }
}
