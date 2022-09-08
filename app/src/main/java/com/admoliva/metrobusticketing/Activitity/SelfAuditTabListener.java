package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.admoliva.metrobusticketing.R;

/**
 * Created by jUniX on 11/5/2015.
 */
public class SelfAuditTabListener implements ActionBar.TabListener {
    private Fragment fragment;

    // The contructor.
    public SelfAuditTabListener(Fragment fragment) {
        this.fragment = fragment;
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.activity_main, fragment);
    }

    // When a tab is unselected, we have to hide it from the user's view.
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }

    // Nothing special here. Fragments already did the job.
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


}
