package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.Bluetooth;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;
import com.zj.btsdk.BluetoothService;


/**
 * Created by Aljon Moliva on 8/26/2015.
 */
public class BluetoothActivity extends Activity {
    Context ctx = this;
    Button btnAdd, btnDel, btnPrint;
    BluetoothConnectionService mBoundService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Intent intent = new Intent(BluetoothActivity.this, BluetoothConnectionService.class);
        startService(intent);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        btnAdd = (Button)findViewById(R.id.btnConnectBT);
        btnAdd.setOnClickListener(new ClickEvent());
        btnDel = (Button)findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new ClickEvent());
        btnPrint = (Button)findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new ClickEvent());
        buttonEnabled();
    }
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            //We've bound to LocalService, cast the IBinder and get LocalService instance
            BluetoothConnectionService.LocalBinder binder = (BluetoothConnectionService.LocalBinder) service;
            mBoundService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    private void buttonEnabled(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getDevice(db);
        if (cr.getCount() == 1) {
            btnAdd.setEnabled(false);
            btnAdd.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
            btnDel.setEnabled(true);
            btnDel.setBackgroundResource(R.drawable.button_selector_2);
        } else {

            btnAdd.setEnabled(true);
            btnAdd.setBackgroundResource(R.drawable.button_selector_2);
            btnDel.setEnabled(false);
            btnDel.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
            PrintButton();

        }
    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnAdd) {
                Intent serverIntent = new Intent(BluetoothActivity.this, DeviceListActivity.class);
                startActivity(serverIntent);
            } else if(v == btnDel)
            {
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

                try{
                    dbHelper.deleteDevice();
                    Toast.makeText(BluetoothActivity.this, "Device Name deleted", Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    Log.d("Database Operation", e.toString());
                }finally {
                    dbHelper.closeDB();
                }

            }else if( v == btnPrint){
                mBoundService.samplePrint();
            }
        }
    }

    private void PrintButton(){
        if(mBound){
            int flag = mBoundService.flag;
            if(flag !=1){
                Toast.makeText(BluetoothActivity.this, "You are not connected to Bluetooth Device.", Toast.LENGTH_SHORT).show();
                btnPrint.setEnabled(false);
                btnPrint.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
            }else if(flag == 1) {
                btnPrint.setEnabled(true);
                btnPrint.setBackgroundResource(R.drawable.button_selector_2);

            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, BluetoothConnectionService.class);
                stopService(intent);
                unbindService(mConnection);
                mBound = false;
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createPrintLayout(){

    }
}
