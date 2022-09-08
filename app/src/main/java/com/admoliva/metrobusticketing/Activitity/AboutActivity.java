package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;

/**
 * Created by jUniX on 11/15/2015.
 */
public class AboutActivity extends Activity {

    Boolean isLock = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && isLock) {

            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
}
