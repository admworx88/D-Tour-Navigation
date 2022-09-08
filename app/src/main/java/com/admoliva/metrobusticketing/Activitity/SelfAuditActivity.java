package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.admoliva.metrobusticketing.Fragments.FragmentAllTrips;
import com.admoliva.metrobusticketing.Fragments.FragmentCurrentTrip;
import com.admoliva.metrobusticketing.Fragments.FragmentCutOffKmPost;
import com.admoliva.metrobusticketing.R;

/**
 * Created by jUniX on 11/5/2015.
 */
public class SelfAuditActivity extends Activity {

    ActionBar.Tab first, second, third;
    Fragment firstFragment = new FragmentCutOffKmPost();
    Fragment secondFragment = new FragmentCurrentTrip();
    Fragment thirdFragment = new FragmentAllTrips();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfaudit);

        ActionBar actionBar = getActionBar();

        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(android.R.color.transparent);

        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(true);

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Setting custom tab icons.
        first = actionBar.newTab().setText("CUT OFF KM POST");
        second = actionBar.newTab().setText("CURRENT TRIP");
        third = actionBar.newTab().setText("ALL TRIPS");

        // Setting tab listeners.
        first.setTabListener(new SelfAuditTabListener(firstFragment));
        second.setTabListener(new SelfAuditTabListener(secondFragment));
        third.setTabListener(new SelfAuditTabListener(thirdFragment));


        // Adding tabs to the ActionBar.
        actionBar.addTab(first);
        actionBar.addTab(second);
        actionBar.addTab(third);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
