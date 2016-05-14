package com.android.devteam.androidwithopencv;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainMenu extends SherlockFragmentActivity {

    ActionBar actionbar;
    ViewPager viewpager;
    ActionBar.Tab tab1, tab2;
    Fragment frag=new Creator();
    Fragment frag2=new Gallery();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        actionbar=getSupportActionBar();

        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1=actionbar.newTab().setText("Creator");
        ActionBar.Tab tab2=actionbar.newTab().setText("Gallery");

        Fragment frag=new Creator();
        Fragment frag2=new Gallery();

        tab1.setTabListener(new MyTabListener(frag));
        tab2.setTabListener(new MyTabListener(frag2));
        actionbar.addTab(tab1);
        actionbar.addTab(tab2);

    }

    class MyTabListener implements ActionBar.TabListener
    {

        public Fragment frags;

        public MyTabListener(Fragment frags)
        {
            this.frags=frags;
        }


        @Override
        public void onTabSelected(com.actionbarsherlock.app.ActionBar.Tab tab,
                                  android.support.v4.app.FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, frags);


        }

        @Override
        public void onTabUnselected(com.actionbarsherlock.app.ActionBar.Tab tab,
                                    android.support.v4.app.FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTabReselected(com.actionbarsherlock.app.ActionBar.Tab tab,
                                    android.support.v4.app.FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

    }
}