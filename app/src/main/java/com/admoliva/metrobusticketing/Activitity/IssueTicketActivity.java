package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.Bluetooth;
import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.KmPostVariable;
import com.admoliva.metrobusticketing.GlobalVariable.PassTypeVariable;
import com.admoliva.metrobusticketing.ListActivity.KmPostActivity;
import com.admoliva.metrobusticketing.ListActivity.PassTypeListActivity;
import com.admoliva.metrobusticketing.ListActivity.RouteCodeListActivity;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Aljon Moliva on 7/20/2015.
 */
public class IssueTicketActivity extends Activity {

    Boolean isLock = true;

    int tcktFrom;
    int tcktTo;
    int tcktLeft;
    int totalPassenger;

    String PType = "";

    String ticketLet;
    Context ctx = this;
    Button btnPrint;
    TextView tripno, routecode, routecodefrom, routecodeto, kmpostfrom, kmpostfromnum, kmpostto, kmposttonum, paxtype, paxtypedesc;
    TextView tckLeft, tckUsed, tckLet, textamount;
    TextView BusNo, BusName, DriverInfo;
    TextView totalCount;
    RelativeLayout relativeLayoutPassType, relativeLayoutRouteCode, relativeLayoutKMPost;
    BluetoothConnectionService mBoundService;
    boolean mBound = false;
    String routefn, routetn;
    TextView RouteTicketNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paxticket_activity);

        ActionBar actionBar = getActionBar();
        //actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(IssueTicketActivity.this, BluetoothConnectionService.class);
        startService(intent);

        BusNo = (TextView) findViewById(R.id.bus);
        BusName = (TextView) findViewById(R.id.busdesc);
        DriverInfo = (TextView) findViewById(R.id.driver);

        tckUsed = (TextView) findViewById(R.id.ticketUsed);
        tckLeft = (TextView) findViewById(R.id.ticketLeft);
        tckLet = (TextView) findViewById(R.id.ticketLetter);
        RouteTicketNo = (TextView) findViewById(R.id.ticketStart);


        BusNo = (TextView) findViewById(R.id.bus);
        BusName = (TextView) findViewById(R.id.busdesc);
        DriverInfo = (TextView) findViewById(R.id.driver);
        DriverInfo.setSelected(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, BluetoothConnectionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        totalCount = (TextView) findViewById(R.id.totalCount);
        tripno = (TextView) findViewById(R.id.TextViewTripNo);
        routecode = (TextView) findViewById(R.id.TextViewRouteCode);
        routecodefrom = (TextView) findViewById(R.id.TextViewRCFrom);
        routecodeto = (TextView) findViewById(R.id.TextViewRCTo);
        kmpostfrom = (TextView) findViewById(R.id.TextViewRouteF);
        kmpostfromnum = (TextView) findViewById(R.id.TextViewRouteFN);
        kmpostto = (TextView) findViewById(R.id.TextViewRouteT);
        kmposttonum = (TextView) findViewById(R.id.TextViewRouteTN);
        textamount = (TextView) findViewById(R.id.TextViewAmount);
        paxtype = (TextView) findViewById(R.id.TextViewPaxType);
        paxtypedesc = (TextView) findViewById(R.id.TextViewPaxTypeDesc);


        btnPrint = (Button) findViewById(R.id.ButtonPrint);
        btnPrint.setOnClickListener(new ClickEvent());
        btnPrint.setEnabled(false);
        btnPrint.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));

        relativeLayoutKMPost = (RelativeLayout) findViewById(R.id.LinearKMPost);
        relativeLayoutKMPost.setOnClickListener(new ClickEvent());
        relativeLayoutPassType = (RelativeLayout) findViewById(R.id.LinearPassType);
        relativeLayoutPassType.setOnClickListener(new ClickEvent());


        getAccountInfo();
        getTicketDetails();
        getTripNo();
        kmPostData();
        passTypeData();
        textCountVisible();
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

    private void textCountVisible(){
        if(totalPassenger == 0){
            totalCount.setVisibility(View.INVISIBLE);
        }
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

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {
            if (v == btnPrint) {
                totalCount.setVisibility(View.VISIBLE);
                if(totalPassenger == 0){
                    buttonEnable();
                }else{
                    insertTicketTransaction(PType);
                }
            } else if (v == relativeLayoutPassType) {
                if (kmpostfromnum.getText().equals("XXX")) {
                    Toast.makeText(IssueTicketActivity.this, "Please select first Km Post!", Toast.LENGTH_SHORT).show();
                } else {
                    textamount.setText("XXX");
                    paxtype.setText("X");
                    paxtypedesc.setText("XXXX");
                    Intent intent = new Intent(IssueTicketActivity.this, PassTypeListActivity.class);
                    startActivity(intent);
                }
            } else if (v == relativeLayoutKMPost) {
                KmPostValidation();
            }

        }
    }

    private void KmPostValidation(){
        GlobalVariable rtec = GlobalVariable.getInstance();
        String routec = rtec.getRoutecode();
        String bustype = rtec.getBustype();
        DatabaseHelper dbKmPost = new DatabaseHelper(ctx);
        Cursor cr = dbKmPost.getFareMatricsRouteCode(dbKmPost, routec, bustype);
        if(cr.getCount() == 0){
            Toast.makeText(IssueTicketActivity.this, "No KM Post found!", Toast.LENGTH_SHORT).show();
        }else {
            totalPassenger = 0;
            totalCount.setText("0");
            startActivity(new Intent(IssueTicketActivity.this, KmPostActivity.class));
        }
    }
    private void getTripNo() {
        DatabaseHelper getTripNo = new DatabaseHelper(ctx);
        Cursor cr = getTripNo.getTripRoute(getTripNo);
        GlobalVariable accountInfo = GlobalVariable.getInstance();

        try {
            cr.moveToLast();
            String tripNo = cr.getString(0);
            tripno.setText(tripNo);
            accountInfo.setTripNo(tripNo);
        } catch (Exception e) {
            Toast.makeText(IssueTicketActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            getTripNo.closeDB();
            cr.close();
            getRouteValidation();
        }

    }

    private void getRouteValidation() {
        GlobalVariable rc = GlobalVariable.getInstance();
        String rtec = rc.getRoutecode();
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.RouteCode(db, rtec, rc.getBustype());
        cr.moveToFirst();
        try {
            routecode.setText(rtec);
            routecodefrom.setText(cr.getString(3) + " " + cr.getString(1));
            routecodeto.setText(cr.getString(4) + " " + cr.getString(2));
            routefn = cr.getString(3);
            routetn = cr.getString(4);

        } catch (Exception e) {
            Toast.makeText(IssueTicketActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            db.closeDB();
            cr.close();
        }
    }

    private void getTicketDetails() {
        DatabaseHelper dbTicketOR = new DatabaseHelper(ctx);
        Cursor cr = dbTicketOR.getTicketInfo(dbTicketOR);

        try {
            if (cr.moveToFirst()) {
                cr.moveToFirst();
                tcktFrom = Integer.parseInt(cr.getString(0));
                tcktTo = Integer.parseInt(cr.getString(2));
                ticketLet = cr.getString(1);
                tcktLeft = (tcktTo - (tcktFrom - 1));
                tckLeft.setText(String.valueOf(tcktLeft));
                tckUsed.setText(String.valueOf(tcktFrom));
                tckLet.setText(ticketLet);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            dbTicketOR.closeDB();
            cr.close();
        }

    }


    void getAccountInfo() {
        GlobalVariable accountInfo = GlobalVariable.getInstance();
        BusNo.setText(accountInfo.getBusno());
        BusName.setText(accountInfo.getBusname() + "  " + accountInfo.getBustype());
        DriverInfo.setText(accountInfo.getConid() + "     " + accountInfo.getConductorname() + "       " + accountInfo.getDrivid() + "     " + accountInfo.getDrivername() + " ");

    }


    void kmPostData() {
        KmPostVariable kmpost = KmPostVariable.getInstance();
        String kmfrn = kmpost.getKmfrn();
        String kmfrt = kmpost.getKmfrt();
        String kmton = kmpost.getKmton();
        String kmtot = kmpost.getKmtot();

        if (kmfrn.equals("XXX")) {
            kmpostfrom.setText("XXXXX");
            kmpostfromnum.setText("XXX");
            kmpostto.setText("XXXXX");
            kmposttonum.setText("XXX");
        } else {
            kmpostfrom.setText(kmfrt);
            kmpostfromnum.setText(kmfrn);
            kmpostto.setText(kmtot);
            kmposttonum.setText(kmton);
            if(totalPassenger == 0){
                getTotalPassenger();
            }
        }
    }
    private void getTotalPassenger(){
        final EditText totCount = new EditText(IssueTicketActivity.this);
        totCount.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        totCount.setText("1");
        totCount.setLayoutParams(lp);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IssueTicketActivity.this);
        alertDialog.setTitle("Number of Passenger")
                .setMessage("How many?")
                .setView(totCount)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String getVal = totCount.getText().toString();
                                if (getVal.isEmpty()) {
                                    totalPassenger = 1;
                                    totalCount.setText("1");
                                    totalCount.setVisibility(View.VISIBLE);
                                } else {
                                    totalPassenger = Integer.parseInt(getVal);
                                    totalCount.setText(getVal);
                                    totalCount.setVisibility(View.VISIBLE);
                                }
                                paxtype.setText("X");
                                paxtypedesc.setText("XXXX");
                                textamount.setText("XXX");
                            }
                        }).show();
    }

    void clearKmPostData() {
        KmPostVariable kmpost = KmPostVariable.getInstance();
        kmpost.setKmfrn("XXX");
        kmpost.setKmfrt("XXXXX");
        kmpost.setKmton("XXX");
        kmpost.setKmtot("XXXXX");

        kmpostfrom.setText("XXXXX");
        kmpostfromnum.setText("XXX");
        kmpostto.setText("XXXXX");
        kmposttonum.setText("XXX");

    }

    void passTypeData() {
        PassTypeVariable passType = PassTypeVariable.getInstance();
        String passcode = passType.getPasscode();
        String passdesc = passType.getPassdesc();
        if (passcode.equals("X")) {
            paxtype.setText("X");
            paxtypedesc.setText("XXXX");
            textamount.setText("XXX");
        } else {
            paxtype.setText(passcode);
            paxtypedesc.setText("(" + passdesc + ")");
            getAmount(passcode);
        }
    }

    void getAmount(String type) {
        KmPostVariable kmpost = KmPostVariable.getInstance();
        double fares = kmpost.getFares();
        double fareg = kmpost.getFareg();
        double farelet = kmpost.getFarelet();
        double faresak = kmpost.getFaresak();
        double fareton = kmpost.getFareton();
        double val;
        switch (type) {
            case "F":
                PType = "P";
                textamount.setText(String.valueOf(fareg));
                buttonEnable();
                break;
            case "SP":
                PType = "P";
                textamount.setText(String.valueOf(fares));
                buttonEnable();
                break;
            case "H":
                PType = "P";
                val = (fareg / 2);
                textamount.setText(String.valueOf(val));
                buttonEnable();
                break;
            case "OP":
                PType = "P";
                if(textamount.getText().toString().equals("XXX")){
                    OtherPaxAmount();
                }
                break;
            case "L":
                PType = "B";
                textamount.setText(String.valueOf(farelet));
                buttonEnable();
                break;
            case "S":
                PType = "B";
                textamount.setText(String.valueOf(faresak));
                buttonEnable();
                break;
            case "K":
                PType = "B";
                textamount.setText(String.valueOf(fareton));
                buttonEnable();
                break;
            case "OB":
                PType = "B";
                if(textamount.getText().toString().equals("XXX")){
                    OtherPaxAmount();
                }
                break;
        }

    }

    void OtherPaxAmount() {
        final EditText input = new EditText(IssueTicketActivity.this);
        input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IssueTicketActivity.this);
        alertDialog.setTitle("Other Amount")
                .setMessage("Enter amount")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String getVal = input.getText().toString();
                                if (getVal.compareTo("") == 0) {
                                    Toast.makeText(IssueTicketActivity.this, "No Amount!", Toast.LENGTH_SHORT).show();
                                } else {
                                    double paxAmount = Double.parseDouble(getVal);
                                    if (paxAmount == 0.0) {
                                        Toast.makeText(IssueTicketActivity.this, "Not Valid Amount", Toast.LENGTH_SHORT).show();
                                    } else {
                                        textamount.setText(String.valueOf(paxAmount));
                                        buttonEnable();
                                    }
                                }
                            }
                        })

                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                paxtype.setText("X");
                                paxtypedesc.setText("XXXX");
                                textamount.setText("XXX");
                                dialog.cancel();
                            }
                        }).show();
    }



    public static String DateToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        String formatDate = format.format(c.getTime());
        return formatDate;
    }

    public static String TimeToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String formatTime = format.format(c.getTime());
        return formatTime;
    }

    private void insertTicketTransaction(String type) {
        GlobalVariable accountInfo = GlobalVariable.getInstance();
        DatabaseHelper db = new DatabaseHelper(ctx);
        try {
            if (type == "P") {

                    double paxAmount = Double.parseDouble(textamount.getText().toString());
                    db.insertTicketTransaction(db, tripno.getText().toString(), routefn, routetn, routecode.getText().toString(),
                            DateToday(), TimeToday(), tcktFrom, tckLet.getText().toString(), BusNo.getText().toString(), accountInfo.getDrivid(), accountInfo.getConid(),
                            kmpostfromnum.getText().toString(), kmposttonum.getText().toString(), paxtype.getText().toString(), paxAmount, "", "", "", "");
                    db.insertAdjustTicket(db, tcktFrom, kmpostfrom.getText().toString(), kmpostto.getText().toString(), paxtypedesc.getText().toString() );
                    db.updateTicketInfo(db, (Integer.parseInt(tckUsed.getText().toString()) + 1));
                    printReceipt("P");
                totalPassenger -= 1;
                totalCount.setText(String.valueOf(totalPassenger));
            } else if (type == "B") {

                    double paxAmount = Double.parseDouble(textamount.getText().toString());
                    db.insertTicketTransaction(db, tripno.getText().toString(), routefn, routetn, routecode.getText().toString(),
                            DateToday(), TimeToday(), tcktFrom, tckLet.getText().toString(), BusNo.getText().toString(), accountInfo.getDrivid(), accountInfo.getConid(),
                            kmpostfromnum.getText().toString(), kmposttonum.getText().toString(), type, paxAmount, paxtype.getText().toString(), "", "", "");
                    db.insertAdjustTicket(db, tcktFrom, kmpostfrom.getText().toString(), kmpostto.getText().toString(), paxtypedesc.getText().toString() );
                    db.updateTicketInfo(db, (Integer.parseInt(tckUsed.getText().toString()) + 1));
                    printReceipt("B");
                totalPassenger -= 1;
                totalCount.setText(String.valueOf(totalPassenger));
            }
        } catch (Exception e) {
            Log.d("Issue Ticket", e.toString());
        } finally {
            db.closeDB();
            getTicketDetails();
            buttonEnable();
        }

    }
    void buttonEnable() {

        if (textamount.getText().equals("XXX")) {
            btnPrint.setEnabled(false);
            btnPrint.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
        } else {

            if(totalPassenger == 0){
                btnPrint.setEnabled(false);
                btnPrint.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
                textamount.setText("XXX");
                kmpostfrom.setText("XXXXX");
                kmpostfromnum.setText("XXX");
                kmpostto.setText("XXXXX");
                kmposttonum.setText("XXX");
                paxtype.setText("X");
                paxtypedesc.setText("XXXX");
                totalCount.setVisibility(View.INVISIBLE);
                Toast.makeText(IssueTicketActivity.this, "Done!", Toast.LENGTH_SHORT).show();

            }else{
                btnPrint.setEnabled(true);
                btnPrint.setBackgroundResource(R.drawable.button_selector_2);
            }


            //Mao ni ang tinuod
            /*if(mBound)
            {
                int flag = mBoundService.flag;
                if(flag !=1){
                    Toast.makeText(IssueTicketActivity.this, "You are not connected to Bluetooth Device.", Toast.LENGTH_SHORT).show();
                }else if(flag == 1){
                    if(totalPassenger == 0){
                        btnPrint.setEnabled(false);
                        btnPrint.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
                        textamount.setText("XXX");
                        kmpostfrom.setText("XXXXX");
                        kmpostfromnum.setText("XXX");
                        kmpostto.setText("XXXXX");
                        kmposttonum.setText("XXX");
                        paxtype.setText("X");
                        paxtypedesc.setText("XXXX");
                        totalCount.setVisibility(View.INVISIBLE);
                    }else{
                        btnPrint.setEnabled(true);
                        btnPrint.setBackgroundResource(R.drawable.button_selector_2);
                    }
                }
            }*/
        }

    }
    void printReceipt(String type) {
        if (mBound) {
            GlobalVariable accountInfo = GlobalVariable.getInstance();
            Bluetooth bt = Bluetooth.getInstance();
            String msg = "";
            //msg = "--------------------------------\n"
            msg = "Mobile Bus Ticketing System on Android\n"
                    + "          DAVAO CITY\n\n"
                    + "--------------------------------\n\n"
                    + "Bus   : " + BusNo.getText() + " " + BusName.getText().toString() + "\n"
                    + "BMac  : " + bt.getAddress() + "\n"
                    + "OR#   : " + tckUsed.getText().toString() + " " + tckLet.getText().toString() + "\n"
                    + "Date  : " + DateToday() + "\n\n"
                    + "TNo   : " + tripno.getText().toString() + " RCode :" + routecode.getText() + "\n"
                    + "KmFrm : " + kmpostfromnum.getText().toString() + " " + kmpostfrom.getText().toString() + "\n"
                    + "KmTo  : " + kmposttonum.getText().toString() + " " + kmpostto.getText().toString() + "\n"
                    + type+"Type : " + paxtype.getText().toString() + " " + paxtypedesc.getText().toString() + "\n"
                    + "Amnt  : " + textamount.getText().toString() + "\n"
                    + "--------------------------------\n"
                    + "Driv  : " + accountInfo.getDrivid() + " " + accountInfo.getDrivername() + "\n"
                    + "Cond  : " + accountInfo.getConid() + " " + accountInfo.getConductorname() + "\n"
                    + "--------------------------------\n\n\n";
            mBoundService.printing(msg);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(totalPassenger == 0){
                    finishActivity();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    finishActivity();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(IssueTicketActivity.this);
                    builder.setTitle("Issue Ticket");
                    builder.setMessage("Do you want to abort transaction?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setCancelable(false)
                            .setNegativeButton("No", dialogClickListener).show();
                }



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void finishActivity(){
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        stopService(intent);
        clearKmPostData();
        unbindService(mConnection);
        mBound = false;
        GlobalVariable gv = GlobalVariable.getInstance();
        PassTypeVariable passType = PassTypeVariable.getInstance();
        passType.setPasscode("X");
        passType.setPassdesc("XXXX");
        passType.setPassdesc("XXX");
        gv.setKmPostPosition(0);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && isLock) {

            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
}

