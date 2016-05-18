package com.android.devteam.androidwithopencv;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.android.devteam.androidwithopencv.network.ImageToServerAsynctask;

/**
 *This class handle our menu, with SherlockFragmentActivity.
 *
 * @author  Gábor Szanyi
 * @version 1.0
 * @since   2016-05-15
 */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_ip:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Set IP address of the server");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageToServerAsynctask.HOST = input.getText().toString();
                    }
                });

                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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