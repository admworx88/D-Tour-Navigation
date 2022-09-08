package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.Bluetooth;
import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.TicketGlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.zj.btsdk.BluetoothService;


/**
 * Created by Aljon Moliva on 6/30/2015.
 */
public class LoginActivity extends Activity {

    String Uname, Pword;
    Context ctx = this;
    EditText uname, pword;
    boolean loginstatus = false;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    public static final int REQUEST_ENABLE_BT = 2;
    Boolean isLock = true;


    GlobalVariable user = GlobalVariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mService = new BluetoothService(this, mHandler);


        ActionBar actionBar = get();
        actionBar.setIcon(android.R.color.transparent);

        uname = (EditText)findViewById(R.id.Username);
        pword = (EditText)findViewById(R.id.Password);

        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        uname.setTypeface(typeFace);
        pword.setTypeface(typeFace);
        Button login = (Button) findViewById(R.id.LoginButton);
        Button back = (Button) findViewById(R.id.ExitButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginValidation();



            }
        });

                back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Metro Bus Ticketing");
                builder.setMessage("Are you sure you want to exit")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


   }

    private void loginValidation(){
        Uname = uname.getText().toString();
        Pword = pword.getText().toString();
        DatabaseHelper dbConductor = new DatabaseHelper(ctx);
        Cursor cr = dbConductor.getProgramUser(dbConductor);
        DatabaseHelper dbTicketOR = new DatabaseHelper(ctx);
        Cursor cur = dbTicketOR.getTicketInfo(dbTicketOR);
        Cursor crBustype = dbConductor.getBustype(dbConductor);
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor crBT = db.getDevice(db);

        String bno = "";
        String bname = "";
        String conname = "";
        String drivname = "";
        String conid = "";
        String drivid = "";
        String bustpe = "";
        String routecode = "";

        try {
            if (cr.getCount() == 0) {
                Toast.makeText(LoginActivity.this, "Please registered an account!", Toast.LENGTH_LONG).show();
                uname.setText("");
                pword.setText("");
                uname.requestFocus();
            } else {
                if (cur.getCount() == 0) {
                    Toast.makeText(LoginActivity.this, "Please insert the tickets first before logging in...", Toast.LENGTH_LONG).show();
                    uname.setText("");
                    pword.setText("");
                    uname.requestFocus();
                } else {
                    crBustype.moveToFirst();
                    String btype = "";
                    btype = crBustype.getString(0);
                    if(crBT.getCount() == 0 ){
                        Toast.makeText(LoginActivity.this, "Please add Bluetooth device first... ", Toast.LENGTH_LONG).show();
                    }else{
                        if(btype.equals("C")){
                            if (cr.moveToFirst()) {
                                cr.moveToFirst();
                                if (Uname.equals(cr.getString(2)) && Pword.equals(cr.getString(8))) {
                                    loginstatus = true;
                                    bno = cr.getString(4);
                                    bname = cr.getString(5);
                                    conname = cr.getString(1);
                                    drivname = cr.getString(3);
                                    drivid = cr.getString(2);
                                    conid = cr.getString(0);
                                    bustpe = cr.getString(6);
                                    routecode = cr.getString(7);

                                    user.setBusno(bno);
                                    user.setBusname(bname);
                                    user.setConductorname(conname);
                                    user.setDrivername(drivname);
                                    user.setDrivid(drivid);
                                    user.setConid(conid);
                                    user.setBustype(bustpe);
                                    TripRoute(routecode);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                                    uname.setText("");
                                    pword.setText("");
                                    uname.requestFocus();
                                }
                            }
                        }else{
                            if (cr.moveToFirst()) {
                                cr.moveToFirst();
                                if (Uname.equals(cr.getString(0)) && Pword.equals(cr.getString(8))) {
                                    loginstatus = true;
                                    bno = cr.getString(4);
                                    bname = cr.getString(5);
                                    conname = cr.getString(1);
                                    drivname = cr.getString(3);
                                    drivid = cr.getString(2);
                                    conid = cr.getString(0);
                                    bustpe = cr.getString(6);
                                    routecode = cr.getString(7);

                                    user.setBusno(bno);
                                    user.setBusname(bname);
                                    user.setConductorname(conname);
                                    user.setDrivername(drivname);
                                    user.setDrivid(drivid);
                                    user.setConid(conid);
                                    user.setBustype(bustpe);
                                    TripRoute(routecode);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                                    uname.setText("");
                                    pword.setText("");
                                    uname.requestFocus();
                                }
                            }

                        }
                    }

                }
            }


        } catch (Exception e) {
            throw e;
        }finally {
            dbConductor.closeDB();
            dbTicketOR.closeDB();
            cr.close();
            cur.close();
            crBT.close();
            crBustype.close();
        }
    }




    void TripRoute(String routecode)
    {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cr = dbHelper.getTripRoute(dbHelper);
        Cursor cr1 = dbHelper.getKMPostRouteCode(dbHelper, routecode, user.getBustype());
        Cursor cr2 = dbHelper.getTicketInfo(dbHelper);
        try
        {
            if(cr.getCount() == 0)
            {
                cr1.moveToFirst();
                cr2.moveToFirst();
                String routefn = cr1.getString(0);
                String routetn = cr1.getString(1);
                dbHelper.insertTripRoute(dbHelper, "1", routecode, routefn, routetn, cr2.getString(0));
                user.setRoutecode(routecode);
                user.setTripNo("1");
                cr.moveToLast();
                user.setRouteticketno(cr2.getString(0));
                startActivity(new Intent(LoginActivity.this, MenusActivity.class));

            }else{
                cr.moveToLast();
                String routec =  cr.getString(1);
                String tripno =  cr.getString(0);
                user.setRoutecode(routec);
                user.setTripNo(tripno);
                user.setRouteticketno(cr.getString(4));
                startActivity(new Intent(LoginActivity.this, MenusActivity.class));

            }
        }catch (Exception e)
        {
            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            cr.close();
            cr1.close();
            cr2.close();
            dbHelper.closeDB();
            this.finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mService.isBTopen())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        initial();

    }

    private void initial()
    {
        TicketGlobalVariable ticket = TicketGlobalVariable.getInstance();
        ticket.setTripNo("XX");
        ticket.setTripNo("XX");
        ticket.setTripNo("XX");
    }

    private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };
     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.adminlogin) {
            startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
        } else if(id == R.id.aboutdev){
            startActivity(new Intent(LoginActivity.this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
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


