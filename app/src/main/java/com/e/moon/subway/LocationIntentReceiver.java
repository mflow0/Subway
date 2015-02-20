package com.e.moon.subway;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

/**
 * Created by moon on 15. 2. 17.
 */
public class LocationIntentReceiver extends BroadcastReceiver {
    protected static final int NAPNOTI = 1;
    private NotificationManager mNotiManager;
    private String mExpectedAction;
    private Intent mLastReceivedIntent;
    private String location;

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
        mNotiManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent content = PendingIntent.getActivity(
                context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        if (intent != null) {
            mLastReceivedIntent = intent;
            location = intent.getStringExtra("location");
        }
        Notification notification = new Notification(R.drawable.ic_launcher, location+"에 도착 하였습니다.", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL|Notification.FLAG_INSISTENT;
        notification.sound = Uri.parse("android.resource://com.e.moon.subway/" + R.raw.electronic);
        notification.setLatestEventInfo(context, "도착 알림", location + "에 도착 하였습니다.", content);

        mNotiManager.notify(LocationIntentReceiver.NAPNOTI, notification);

        Fragment03.sw.setChecked(false);

    }

    public Intent getLastReceivedIntent() {
        return mLastReceivedIntent;
    }

    public void clearReceivedIntents() {
        mLastReceivedIntent = null;
    }
}
