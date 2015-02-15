package com.e.moon.subway;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by moon on 15. 2. 12.
 * dialog 창을 띄워주며 비트맵설정과 클릭이벤트를 실행한다.
 *
 */
public class AboutDialog extends Activity {
    ImageView twitter;
    ImageView facebook;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window win = getWindow();
        win.requestFeature(Window.FEATURE_NO_TITLE);
        win.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setContentView(R.layout.about);

        LinearLayout btn = (LinearLayout) findViewById(R.id.layout01);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        twitter = (ImageView) findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=mflow0")));
                }catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mflow0")));
                }
            }
        });
        facebook = (ImageView) findViewById(R.id.facebook_se);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/885433791507073")));
                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/mflow0")));
                }
            }
        });
    }

    protected void onApplyThemeResource(Resources.Theme theme, int resId, boolean first) {
        super.onApplyThemeResource(theme, resId, first);
        theme.applyStyle(android.R.style.Theme_Panel, true);
    }
}
