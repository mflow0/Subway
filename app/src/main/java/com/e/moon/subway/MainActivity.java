package com.e.moon.subway;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private PagerAdapter mPagerAdapter;

    private class TabInfo {
        private String tag;
        private Class<?> cls;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.cls = clazz;
            this.args = args;
        }

    }
    /**
     * 탭호스트에 returns dummy views
     */
    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.initTabHost(savedInstanceState);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }

        this.initViewPager();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Init ViewPager
     */
    private void initViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, Fragment01.class.getName()));
        fragments.add(Fragment.instantiate(this, Fragment02.class.getName()));
        fragments.add(Fragment.instantiate(this, Fragment03.class.getName()));
        fragments.add(Fragment.instantiate(this, Fragment04.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Init the Tab Host
     */
    private void initTabHost(Bundle args) {
        Resources res = getResources();

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator(createTabView(this, res.getString(R.string.tab1), R.drawable.tag)), ( tabInfo = new TabInfo("Tab1", Fragment01.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator(createTabView(this, res.getString(R.string.tab2), R.drawable.search)), ( tabInfo = new TabInfo("Tab2", Fragment02.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator(createTabView(this, res.getString(R.string.tab3), R.drawable.location_pin)), ( tabInfo = new TabInfo("Tab3", Fragment03.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4").setIndicator(createTabView(this, res.getString(R.string.tab4), R.drawable.settings)), ( tabInfo = new TabInfo("Tab4", Fragment04.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        // Default to first tab
        //this.onTabChanged("Tab1");

        mTabHost.setOnTabChangedListener(this);
    }

    /**
     * 탭호스트를 탭에 추가한다.
     */
    private static void AddTab(MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /**
     * OnTabChangeListener method
     * */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    /**
     * OnPageChangeListener method
     * */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        this.mTabHost.setCurrentTab(position);   //page select -> tab change
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }


    /**
     * tab custom view
     */
    private View createTabView(final Context context, final String textStringId, final int imageResId) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_menu, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.tabli);
        layout.setBackgroundResource(R.drawable.tab_bg_color);
        ImageView iv = (ImageView) view.findViewById(R.id.tabsIcon);
        iv.setImageResource(imageResId);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(textStringId);
        return view;
    }

}
