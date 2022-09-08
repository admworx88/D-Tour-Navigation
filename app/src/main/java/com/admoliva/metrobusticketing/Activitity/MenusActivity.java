package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.Bluetooth;
import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.GlobalVariable.KmPostVariable;
import com.admoliva.metrobusticketing.GlobalVariable.PassTypeVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.zj.btsdk.BluetoothService;

/**
 * Created by Aljon Moliva on 9/10/2015.
 */
public class MenusActivity extends Activity {

    int tcktFrom;
    int tcktTo;
    int tcktLeft;
    String ticketLet;

    Context ctx = this;

    TextView BusNo, BusName, DriverInfo, InspectorInfo;
    TextView tckUsed, tckLeft, tckLet, RouteTicketNo;
    Boolean isLock = true;

    LinearLayout paxTicket, selfAudit, inspectorAudit, newTrip, about, cashAudit;

    BluetoothService mService = null;
    public static final int REQUEST_ENABLE_BT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        mService = new BluetoothService(this, mHandler);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);


        tckUsed = (TextView) findViewById(R.id.ticketUsed);
        tckLeft = (TextView) findViewById(R.id.ticketLeft);
        tckLet = (TextView) findViewById(R.id.ticketLet);
        RouteTicketNo = (TextView) findViewById(R.id.ticketStart);

        DriverInfo = (TextView)findViewById(R.id.TextViewDriver);
        InspectorInfo = (TextView)findViewById(R.id.TextViewInspector);

        BusNo = (TextView)findViewById(R.id.bus);
        BusName = (TextView)findViewById(R.id.busdesc);

    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //ÒÑÁ¬½Ó
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:  //ÕýÔÚÁ¬½Ó
                            Log.d("À¶ÑÀµ÷ÊÔ","ÕýÔÚÁ¬½Ó.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //¼àÌýÁ¬½ÓµÄµ½À´
                        case BluetoothService.STATE_NONE:
                            Log.d("À¶ÑÀµ÷ÊÔ","µÈ´ýÁ¬½Ó.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //À¶ÑÀÒÑ¶Ï¿ªÁ¬½Ó
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };
    @Override
    protected void onStart() {
        super.onStart();

        if( mService.isBTopen() == false)
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        paxTicket = (LinearLayout) findViewById(R.id.PaxTicket);
        paxTicket.setOnClickListener(new clickEvent());
        selfAudit = (LinearLayout) findViewById(R.id.SelfAudit);
        selfAudit.setOnClickListener(new clickEvent());
        inspectorAudit = (LinearLayout) findViewById(R.id.Inspector);
        inspectorAudit.setOnClickListener(new clickEvent());
        cashAudit = (LinearLayout) findViewById(R.id.CashAudit);
        cashAudit.setOnClickListener(new clickEvent());
        newTrip = (LinearLayout) findViewById(R.id.NewTrip);
        newTrip.setOnClickListener(new clickEvent());
        //about = (LinearLayout) findViewById(R.id.About);
        //about.setOnClickListener(new clickEvent());

        getAccountInfo();
        getTicketDetails();
        initialValue();
        BluetoothDB();
        textRouteTicketNo();
    }
    private void textRouteTicketNo(){
        DatabaseHelper db =  new DatabaseHelper(ctx);
        Cursor cr =  db.getTripRoute(db);

        try{
            cr.moveToLast();
            RouteTicketNo.setText(cr.getString(4));
        }catch(Exception e){
            throw e;
        }finally{
            cr.close();
            db.closeDB();
        }
    }
    private void getAccountInfo()
    {
        GlobalVariable accountInfo = GlobalVariable.getInstance();

        BusNo.setText(accountInfo.getBusno());
        BusName.setText(accountInfo.getBusname() + "  " + accountInfo.getBustype());
        DriverInfo.setText(accountInfo.getConid() + "  " + accountInfo.getConductorname());
        InspectorInfo.setText(accountInfo.getDrivid() + "  " + accountInfo.getDrivername());
    }

    private void BluetoothDB(){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cr = dbHelper.getDevice(dbHelper);
        Bluetooth bt = Bluetooth.getInstance();
        try
        {
            if(cr.getCount() == 0){
                Toast.makeText(MenusActivity.this, "Note: Please Add Bluetooth Device!", Toast.LENGTH_LONG).show();
            }else{
                cr.moveToFirst();
                bt.setAddress(cr.getString(0));
            }
        }catch (Exception e){
            Log.d("BLuetooth", e.toString());
        }finally {
            cr.close();
            dbHelper.closeDB();
        }
    }
    class clickEvent implements View.OnClickListener
    {
        InspectorAuditVariable inspectorFlag = InspectorAuditVariable.getInstance();
        @Override
        public void onClick(View v) {
            if(v == paxTicket){
                validation();
            }else if(v == selfAudit) {
                startActivity(new Intent(MenusActivity.this, SelfAuditActivity.class));
            }else if(v == inspectorAudit) {
                 inspectorFlag.setInspectorFlag("1");
                 startActivity(new Intent(MenusActivity.this, InspectorAuditLoginActivity.class));
            }else if(v == newTrip) {
                startActivity(new Intent(MenusActivity.this, NewTripActivity.class));
            }else if(v == cashAudit) {
                inspectorFlag.setInspectorFlag("2");
                startActivity(new Intent(MenusActivity.this, InspectorAuditLoginActivity.class));
            }
        }
    }
    private void getTicketDetails()
    {

        DatabaseHelper dbTicketOR = new DatabaseHelper(ctx);
        Cursor cr = dbTicketOR.getTicketInfo(dbTicketOR);

        try
        {
            if(cr.moveToFirst())
            {
                cr.moveToFirst();
                tcktFrom = Integer.parseInt(cr.getString(0));
                tcktTo = Integer.parseInt(cr.getString(2));
                ticketLet = cr.getString(1);
                tcktLeft = (tcktTo - (tcktFrom - 1));
                tckLeft.setText(String.valueOf(tcktLeft));
                tckUsed.setText(String.valueOf(tcktFrom) + " " + ticketLet);
            }
        }catch(Exception e)
        {
            throw e;
        }finally {

            dbTicketOR.closeDB();
            cr.close();
        }
    }

    private void validation()
    {
        PassTypeVariable passType = PassTypeVariable.getInstance();
        GlobalVariable gv = GlobalVariable.getInstance();

        try
        {
               passType.setPasscode("X");
               passType.setPassdesc("XXXX");
               gv.setKmPostPosition(0);
               startActivity(new Intent(MenusActivity.this, IssueTicketActivity.class));

        }catch (Exception e)
        {
            e.printStackTrace();
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
    void initialValue()
    {
        PassTypeVariable passType = PassTypeVariable.getInstance();
        KmPostVariable kmPost = KmPostVariable.getInstance();

        passType.setPasscode("X");
        kmPost.setKmfrn("XXX");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetoothmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent intent = new Intent(MenusActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MenusActivity.this);
                builder.setTitle("Metro Bus Ticketing");
                builder.setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setCancelable(false)
                        .setNegativeButton("No", dialogClickListener).show();
                break;

        }
        return super.onOptionsItemSelected(item);

    }
}
