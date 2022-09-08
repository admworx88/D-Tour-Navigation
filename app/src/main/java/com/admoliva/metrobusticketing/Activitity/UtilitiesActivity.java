package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

/**
 * Created by jUniX on 1/6/2016.
 */
public class UtilitiesActivity extends Activity {
    Button btnBluetooth, btnUpdateUser, btnBIR, btnTickets;
    Boolean isLock = true;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);

        btnBluetooth = (Button)findViewById(R.id.ButtonBluetooth);
        btnUpdateUser = (Button)findViewById(R.id.ButtonUpdateUser);
        btnBIR = (Button)findViewById(R.id.ButtonBIR);
        btnTickets = (Button)findViewById(R.id.ButtonUpdateTickets);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UtilitiesActivity.this, BluetoothActivity.class));
            }
        });
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(UtilitiesActivity.this, ProgramUserActivity.class));
            }
        });
        btnBIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(UtilitiesActivity.this, BIRActivity.class));
            }
        });
        btnTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(UtilitiesActivity.this, TicketsActivity.class));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ButtonProgramUser();
        ButtonTickets();


    }

    private void ButtonProgramUser(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getProgramUser(db);

        try{
            if(cr.getCount() == 0){
                btnUpdateUser.setEnabled(false);
                btnUpdateUser.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
            }else{
                btnUpdateUser.setEnabled(true);
                btnUpdateUser.setBackgroundResource(R.drawable.button_selector_2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }

    }
    private void ButtonTickets(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getTicketInfo(db);

        try{
            if(cr.getCount() == 0){
                btnTickets.setEnabled(false);
                btnTickets.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
            }else{
                btnTickets.setEnabled(true);
                btnTickets.setBackgroundResource(R.drawable.button_selector_2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }

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
        if( (keyCode==KeyEvent.KEYCODE_BACK) && isLock)
        {
            return true;
        }
        else
            return super.onKeyDown(keyCode, event);
    }
}
