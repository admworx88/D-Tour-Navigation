package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.CashAuditActivity.CashAuditOnlyActivity;
import com.admoliva.metrobusticketing.CashAuditActivity.CashAuditWithDepositActivity;
import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.InspectorAuditActivity.AdjustTicketActivity;
import com.admoliva.metrobusticketing.InspectorAuditActivity.InspectorIssueTicketActivity;
import com.admoliva.metrobusticketing.InspectorAuditActivity.ReportAnomalyActivity;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jUniX on 11/15/2015.
 */
public class CashAuditActivity extends Activity {
    TextView txtInsName, txtInsType, txtInsID;
    Context ctx = this;
    Boolean isLock = true;

    TextView busNo, driver, conductor, tripno, routecode;
    TextView text1, text2, text3, text4, text5;
    TextView paxOnBoard, totalOnBoard, baggage;
    TextView totalBaggage, totalCash;
    String routec, routefn, routetn;
    TextView kmon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashaudit);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);


    }

    @Override
    protected void onStart() {
        super.onStart();
        textviewSettings();
        getAccountInfo();
        getTripNo();
        getRouteValidation();
        getInspectorInfo();
        routeValidation();
        getKMon();
    }

    void getAccountInfo()
    {
        GlobalVariable accountInfo = GlobalVariable.getInstance();

        busNo.setText(accountInfo.getBusno() + "  " + accountInfo.getBusname() + "  " + accountInfo.getBustype());
        driver.setText(accountInfo.getDrivid() + "  " + accountInfo.getDrivername());
        conductor.setText(accountInfo.getConid() + "  " + accountInfo.getConductorname());
        routecode.setText(accountInfo.getRoutecode());
    }

    private void getTripNo()
    {
        DatabaseHelper getTripNo = new DatabaseHelper(ctx);
        Cursor cr = getTripNo.getTripRoute(getTripNo);
        GlobalVariable accountInfo = GlobalVariable.getInstance();

        try
        {
            cr.moveToFirst();
            int tripNo = cr.getCount();
            tripno.setText("" +tripNo);
            accountInfo.setTripNo(String.valueOf(tripNo));
        }catch (Exception e)
        {
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            getTripNo.closeDB();
            cr.close();
        }

    }
    private void getRouteValidation()
    {
        GlobalVariable rc = GlobalVariable.getInstance();
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getTripRoute(db);
        cr.moveToFirst();
        String routecodes = "";
        try
        {
            cr.moveToFirst();
            do
            {
                routecodes += cr.getString(1) + " ";
            }while (cr.moveToNext());
            routecode.setText(routecodes);

        }catch (Exception e)
        {
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            db.closeDB();
            cr.close();
            getTicketTransaction();
        }
    }
    private void routeValidation(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getTripRoute(db);
        try{
            cr.moveToLast();
            routec = cr.getString(1);
            routefn = cr.getString(2);
            routetn = cr.getString(3);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }
    }
    private void getInspectorInfo(){
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        txtInsID.setText(inspector.getInsId());
        txtInsName.setText(inspector.getInsName());
        txtInsType.setText(inspector.getInsType());
    }
    private void getTicketTransaction()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = dbHelper.getPaxTicketTransAllTrips(dbHelper);

            if(cr.getCount() == 0){

                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            }else{
                cr.moveToFirst();
                String F, SP, H, OP, L, S, K, OB;
                String FullAmount, SPAmount, HAmount, OPAmount, LAmount, SAmount, KAmount, OBAmount;

                F = cr.getString(1);
                FullAmount = cr.getString(2);
                SP = cr.getString(3);
                SPAmount = cr.getString(4);
                H = cr.getString(5);
                HAmount = cr.getString(6);
                OP = cr.getString(7);
                OPAmount = cr.getString(8);
                L = cr.getString(9);
                LAmount = cr.getString(10);
                S = cr.getString(11);
                SAmount = cr.getString(12);
                K = cr.getString(13);
                KAmount = cr.getString(14);
                OB = cr.getString(15);
                OBAmount = cr.getString(16);


                int cash1 = Integer.parseInt(FullAmount)+Integer.parseInt(SPAmount)+Integer.parseInt(HAmount)+Integer.parseInt(OPAmount);
                totalOnBoard.setText(""+cash1);
                int cash2 = Integer.parseInt(LAmount)+Integer.parseInt(SAmount)+Integer.parseInt(KAmount)+Integer.parseInt(OBAmount);
                totalBaggage.setText(""+cash2);
                int totCash = cash1 + cash2;
                totalCash.setText(""+totCash);

                int totOnBoard = Integer.parseInt(F)+Integer.parseInt(SP)+Integer.parseInt(H)+Integer.parseInt(OP);
                paxOnBoard.setText("" + totOnBoard);
                int totBag = Integer.parseInt(L)+Integer.parseInt(S)+Integer.parseInt(K)+Integer.parseInt(OB);
                baggage.setText(""+totBag);
                cr.close();
            }


        }catch (Exception e)
       {
          Log.d("Cash Audit", e.toString());
       }finally {
            dbHelper.closeDB();
       }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cashauditmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.replaceZone) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            changeKmON();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(CashAuditActivity.this);
            builder.setTitle("Replace Zone");
            builder.setMessage("Are you sure you want to replace your KMON?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setCancelable(false)
                    .setNegativeButton("No", dialogClickListener).show();


        }else if(id == R.id.cashAuditOnly)
        {
            cashAuditOnly();
        }else if(id == R.id.cashAuditWithDeposit)
        {
            cashAuditWithDeposit();
        }else if(id == R.id.reportAnomaly)
        {
            reportAnomaly();

        }else if(id == R.id.logoutInspector)
        {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            logOut();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(CashAuditActivity.this);
            builder.setTitle("Cash Audit");
            builder.setMessage("Are you sure you want to exit")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setCancelable(false)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        return super.onOptionsItemSelected(item);
    }
    private void getKMon(){
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        kmon.setText(inspector.getKmon());
    }
    void cashAuditWithDeposit(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getInspectorTransByID(db, txtInsID.getText().toString());
        Cursor cr1 = db.getAllTransData(db);
        InspectorAuditVariable var = InspectorAuditVariable.getInstance();
        try {
            if(cr.getCount() > 0){
                cr.moveToLast();
                var.setTransNo(cr.getString(1));
                var.setTripNo(cr.getString(2));
                var.setRoutefn(cr.getString(3));
                var.setRoutetn(cr.getString(4));
                var.setRoutecode(cr.getString(5));
                var.setDate(cr.getString(6));
                var.setTime(cr.getString(7));
                var.setTicketNo(cr.getString(8));
                var.setTicketLetter(cr.getString(9));
                var.setBusno(cr.getString(10));
                var.setDriverid(cr.getString(11));
                var.setConid(cr.getString(12));
                var.setKmfrom(cr.getString(13));
                var.setKmto(cr.getString(14));
                var.setFare(cr.getString(15));
                var.setFaretype(cr.getString(16));
                var.setBagtype(cr.getString(17));
                var.setPaxno(paxOnBoard.getText().toString());
                var.setBagno(baggage.getText().toString());
                var.setPaxamt(totalOnBoard.getText().toString());
                var.setBagamt(totalBaggage.getText().toString());
                var.setAnmcode("X");
                var.setAnmdesc("XXX");
                startActivity(new Intent(CashAuditActivity.this, CashAuditWithDepositActivity.class));
            }else{
                cr1.moveToLast();
                var.setTripNo(cr1.getString(1));
                var.setRoutefn(cr1.getString(2));
                var.setRoutetn(cr1.getString(3));
                var.setRoutecode(cr1.getString(4));
                var.setDate(cr1.getString(5));
                var.setTime(cr1.getString(6));
                var.setTicketNo(cr1.getString(7));
                var.setTicketLetter(cr1.getString(8));
                var.setBusno(cr1.getString(9));
                var.setDriverid(cr1.getString(10));
                var.setConid(cr1.getString(11));
                var.setKmfrom(cr1.getString(12));
                var.setKmto(cr1.getString(13));
                var.setFare(cr1.getString(14));
                var.setFaretype(cr1.getString(15));
                var.setBagtype(cr1.getString(16));
                var.setPaxno(paxOnBoard.getText().toString());
                var.setBagno(baggage.getText().toString());
                var.setPaxamt(totalOnBoard.getText().toString());
                var.setBagamt(totalBaggage.getText().toString());
                var.setAnmcode("X");
                var.setAnmdesc("XXX");
                startActivity(new Intent(CashAuditActivity.this, CashAuditWithDepositActivity.class));

            }

        }catch (Exception e){
            Log.d("Report Anomaly", e.toString());
        }finally {
            cr.close();
            cr1.close();
            db.closeDB();
        }

    }
    void cashAuditOnly(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getInspectorTransByID(db, txtInsID.getText().toString());
        Cursor cr1 = db.getAllTransData(db);
        InspectorAuditVariable var = InspectorAuditVariable.getInstance();
        try {
            if(cr.getCount() > 0){
                cr.moveToLast();
                var.setTransNo(cr.getString(1));
                var.setTripNo(cr.getString(2));
                var.setRoutefn(cr.getString(3));
                var.setRoutetn(cr.getString(4));
                var.setRoutecode(cr.getString(5));
                var.setDate(cr.getString(6));
                var.setTime(cr.getString(7));
                var.setTicketNo(cr.getString(8));
                var.setTicketLetter(cr.getString(9));
                var.setBusno(cr.getString(10));
                var.setDriverid(cr.getString(11));
                var.setConid(cr.getString(12));
                var.setKmfrom(cr.getString(13));
                var.setKmto(cr.getString(14));
                var.setFare(cr.getString(15));
                var.setFaretype(cr.getString(16));
                var.setBagtype(cr.getString(17));
                var.setPaxno(paxOnBoard.getText().toString());
                var.setBagno(baggage.getText().toString());
                var.setPaxamt(totalOnBoard.getText().toString());
                var.setBagamt(totalBaggage.getText().toString());
                var.setAnmcode("X");
                var.setAnmdesc("XXX");
                startActivity(new Intent(CashAuditActivity.this, CashAuditOnlyActivity.class));
            }else{
                cr1.moveToLast();
                var.setTripNo(cr1.getString(1));
                var.setRoutefn(cr1.getString(2));
                var.setRoutetn(cr1.getString(3));
                var.setRoutecode(cr1.getString(4));
                var.setDate(cr1.getString(5));
                var.setTime(cr1.getString(6));
                var.setTicketNo(cr1.getString(7));
                var.setTicketLetter(cr1.getString(8));
                var.setBusno(cr1.getString(9));
                var.setDriverid(cr1.getString(10));
                var.setConid(cr1.getString(11));
                var.setKmfrom(cr1.getString(12));
                var.setKmto(cr1.getString(13));
                var.setFare(cr1.getString(14));
                var.setFaretype(cr1.getString(15));
                var.setBagtype(cr1.getString(16));
                var.setPaxno(paxOnBoard.getText().toString());
                var.setBagno(baggage.getText().toString());
                var.setPaxamt(totalOnBoard.getText().toString());
                var.setBagamt(totalBaggage.getText().toString());
                var.setAnmcode("X");
                var.setAnmdesc("XXX");
                startActivity(new Intent(CashAuditActivity.this, CashAuditOnlyActivity.class));

            }

        }catch (Exception e){
            Log.d("Report Anomaly", e.toString());
        }finally {
            cr.close();
            cr1.close();
            db.closeDB();
        }

    }

    private boolean searchKmCodeExist(String kmcode) {
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getKmCode(db, kmcode);
        boolean result = false;
        try {
            if (cr.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.d("CUT OFF KM POST", "ERROR " + e.toString());
        } finally {
            cr.close();
            db.closeDB();
        }
        return result;
    }

    void logOut(){
        final EditText input = new EditText(CashAuditActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CashAuditActivity.this);
        alertDialog.setTitle("Cash Audit")
                .setMessage("Enter Km off.")
                .setView(input)
                        //.setIcon(R.drawable.logo)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String getVal = input.getText().toString();
                                InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
                                DatabaseHelper db = new DatabaseHelper(ctx);
                                try {
                                    if (getVal.compareTo("") == 0) {
                                        Toast.makeText(CashAuditActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        if (Integer.parseInt(routefn) < Integer.parseInt(routetn)) {
                                            if (Integer.parseInt(getVal) > Integer.parseInt(routetn)) {
                                                Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else if (Integer.parseInt(getVal) < Integer.parseInt(routefn) || Integer.parseInt(getVal) < Integer.parseInt(inspector.getKmon())) {
                                                Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Cursor cr = db.getInspectorTransByID(db, txtInsID.getText().toString());
                                                if (cr.getCount() > 0) {
                                                    cr.moveToLast();
                                                    double paxAmount = Double.parseDouble(cr.getString(15));
                                                    db.insertInspectorTicketTransaction(db, cr.getString(1), cr.getString(2),
                                                            cr.getString(3), cr.getString(4), cr.getString(5), DateToday(), TimeToday(), Integer.parseInt(cr.getString(8)), cr.getString(9),
                                                            cr.getString(10), cr.getString(11), cr.getString(12), cr.getString(13), cr.getString(14), paxAmount, cr.getString(16), cr.getString(17),
                                                            cr.getString(18), cr.getString(19), cr.getString(20), cr.getString(21), getVal, cr.getString(23), Integer.parseInt(paxOnBoard.getText().toString()),
                                                            Integer.parseInt(baggage.getText().toString()), Integer.parseInt(totalOnBoard.getText().toString()), Integer.parseInt(totalBaggage.getText().toString()), Integer.parseInt(cr.getString(28)),
                                                            Integer.parseInt(cr.getString(29)), Integer.parseInt(cr.getString(30)), Integer.parseInt(cr.getString(31)), "", "LOG OUT BY " + inspector.getInsName());
                                                    cr.close();
                                                    CashAuditActivity.this.finish();

                                                } else {
                                                    Cursor cr1 = db.getAllTransData(db);
                                                    cr1.moveToLast();
                                                    double paxAmount = Double.parseDouble(cr1.getString(14));
                                                    db.insertInspectorTicketTransaction(db, "1", cr1.getString(1),
                                                            cr1.getString(2), cr1.getString(3), cr1.getString(4), DateToday(), TimeToday(), Integer.parseInt(cr1.getString(7)), cr1.getString(8),
                                                            cr1.getString(9), cr1.getString(10), cr1.getString(11), cr1.getString(12), cr1.getString(13), paxAmount, cr1.getString(15), cr1.getString(16),
                                                            inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), getVal, "", Integer.parseInt(paxOnBoard.getText().toString()),
                                                            Integer.parseInt(baggage.getText().toString()), Integer.parseInt(totalOnBoard.getText().toString()),
                                                            Integer.parseInt(totalBaggage.getText().toString()), 0,
                                                            0, 0, 0, "", "LOG OUT BY " + inspector.getInsName());
                                                    cr1.close();
                                                    CashAuditActivity.this.finish();

                                                }
                                            }
                                        } else if (Integer.parseInt(routefn) > Integer.parseInt(routetn)) {
                                            if (Integer.parseInt(getVal) < Integer.parseInt(routetn)) {
                                                Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else if (Integer.parseInt(getVal) > Integer.parseInt(routefn) || Integer.parseInt(getVal) > Integer.parseInt(inspector.getKmon())) {
                                                Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Cursor cr = db.getInspectorTransByID(db, txtInsID.getText().toString());
                                                if (cr.getCount() > 0) {
                                                    cr.moveToLast();
                                                    double paxAmount = Double.parseDouble(cr.getString(15));
                                                    db.insertInspectorTicketTransaction(db, cr.getString(1), cr.getString(2),
                                                            cr.getString(3), cr.getString(4), cr.getString(5), DateToday(), TimeToday(), Integer.parseInt(cr.getString(8)), cr.getString(9),
                                                            cr.getString(10), cr.getString(11), cr.getString(12), cr.getString(13), cr.getString(14), paxAmount, cr.getString(16), cr.getString(17),
                                                            cr.getString(18), cr.getString(19), cr.getString(20), cr.getString(21), getVal, cr.getString(23), Integer.parseInt(paxOnBoard.getText().toString()),
                                                            Integer.parseInt(baggage.getText().toString()), Integer.parseInt(totalOnBoard.getText().toString()), Integer.parseInt(totalBaggage.getText().toString()), Integer.parseInt(cr.getString(28)),
                                                            Integer.parseInt(cr.getString(29)), Integer.parseInt(cr.getString(30)), Integer.parseInt(cr.getString(31)), "", "LOG OUT BY " + inspector.getInsName());
                                                    cr.close();
                                                    CashAuditActivity.this.finish();

                                                } else {
                                                    Cursor cr1 = db.getAllTransData(db);
                                                    cr1.moveToLast();
                                                    double paxAmount = Double.parseDouble(cr1.getString(14));
                                                    db.insertInspectorTicketTransaction(db, "1", cr1.getString(1),
                                                            cr1.getString(2), cr1.getString(3), cr1.getString(4), DateToday(), TimeToday(), Integer.parseInt(cr1.getString(7)), cr1.getString(8),
                                                            cr1.getString(9), cr1.getString(10), cr1.getString(11), cr1.getString(12), cr1.getString(13), paxAmount, cr1.getString(15), cr1.getString(16),
                                                            inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), getVal, "", Integer.parseInt(paxOnBoard.getText().toString()),
                                                            Integer.parseInt(baggage.getText().toString()), Integer.parseInt(totalOnBoard.getText().toString()),
                                                            Integer.parseInt(totalBaggage.getText().toString()), 0,
                                                            0, 0, 0, "", "LOG OUT BY " + inspector.getInsName());
                                                    cr1.close();
                                                    CashAuditActivity.this.finish();

                                                }
                                            }
                                        }
                                    }
                                } catch (
                                        Exception e
                                        )

                                {
                                    Log.d("Log out", e.toString());
                                } finally

                                {
                                    db.closeDB();
                                }


                            }
                        })

                                .setNegativeButton("BACK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).show();
                            }

                            private static String DateToday() {
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                                String formatDate = format.format(c.getTime());
                                return formatDate;
                            }

                            private static String TimeToday() {
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                String formatTime = format.format(c.getTime());
                                return formatTime;
                            }

                            void reportAnomaly() {
                                DatabaseHelper db = new DatabaseHelper(ctx);
                                Cursor cr = db.getInspectorTransByID(db, txtInsID.getText().toString());
                                Cursor cr1 = db.getAllTransData(db);
                                Cursor crTripNo = db.getTripRoute(db);
                                InspectorAuditVariable var = InspectorAuditVariable.getInstance();
                                try {
                                    if (cr.getCount() > 0) {
                                        cr.moveToLast();
                                        crTripNo.moveToLast();
                                        var.setTransNo(cr.getString(1));
                                        var.setTripNo(crTripNo.getString(0));
                                        var.setRoutefn(cr.getString(3));
                                        var.setRoutetn(cr.getString(4));
                                        var.setRoutecode(cr.getString(5));
                                        var.setDate(cr.getString(6));
                                        var.setTime(cr.getString(7));
                                        var.setTicketNo(cr.getString(8));
                                        var.setTicketLetter(cr.getString(9));
                                        var.setBusno(cr.getString(10));
                                        var.setDriverid(cr.getString(11));
                                        var.setConid(cr.getString(12));
                                        var.setKmfrom(cr.getString(13));
                                        var.setKmto(cr.getString(14));
                                        var.setFare(cr.getString(15));
                                        var.setFaretype(cr.getString(16));
                                        var.setBagtype(cr.getString(17));
                                        var.setPaxno(paxOnBoard.getText().toString());
                                        var.setBagno(baggage.getText().toString());
                                        var.setPaxamt(totalOnBoard.getText().toString());
                                        var.setBagamt(totalBaggage.getText().toString());
                                        var.setAnmcode("X");
                                        var.setAnmdesc("XXX");
                                        startActivity(new Intent(CashAuditActivity.this, ReportAnomalyActivity.class));
                                    } else {
                                        cr1.moveToLast();
                                        crTripNo.moveToLast();
                                        var.setTripNo(crTripNo.getString(0));
                                        var.setRoutefn(cr1.getString(2));
                                        var.setRoutetn(cr1.getString(3));
                                        var.setRoutecode(cr1.getString(4));
                                        var.setDate(cr1.getString(5));
                                        var.setTime(cr1.getString(6));
                                        var.setTicketNo(cr1.getString(7));
                                        var.setTicketLetter(cr1.getString(8));
                                        var.setBusno(cr1.getString(9));
                                        var.setDriverid(cr1.getString(10));
                                        var.setConid(cr1.getString(11));
                                        var.setKmfrom(cr1.getString(12));
                                        var.setKmto(cr1.getString(13));
                                        var.setFare(cr1.getString(14));
                                        var.setFaretype(cr1.getString(15));
                                        var.setBagtype(cr1.getString(16));
                                        var.setPaxno(paxOnBoard.getText().toString());
                                        var.setBagno(baggage.getText().toString());
                                        var.setPaxamt(totalOnBoard.getText().toString());
                                        var.setBagamt(totalBaggage.getText().toString());
                                        var.setAnmcode("X");
                                        var.setAnmdesc("XXX");
                                        startActivity(new Intent(CashAuditActivity.this, ReportAnomalyActivity.class));

                                    }

                                } catch (Exception e) {
                                    Log.d("Report Anomaly", e.toString());
                                } finally {
                                    cr.close();
                                    cr1.close();
                                    cr1.close();
                                    crTripNo.close();
                                    db.closeDB();
                                }

                            }



                            void changeKmON() {
                                final EditText input = new EditText(CashAuditActivity.this);
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                input.setLayoutParams(lp);


                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CashAuditActivity.this);
                                alertDialog.setTitle("Cash Audit")
                                        .setMessage("Enter Km on.")
                                        .setView(input)
                                                //.setIcon(R.drawable.logo)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        String getVal = input.getText().toString();
                                                        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();

                                                        if (getVal.compareTo("") == 0) {
                                                            Toast.makeText(CashAuditActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            if (Integer.parseInt(routefn) < Integer.parseInt(routetn)) {
                                                                if (Integer.parseInt(getVal) > Integer.parseInt(routetn)) {
                                                                    Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                                                } else if (Integer.parseInt(getVal) < Integer.parseInt(routefn)) {
                                                                    Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    inspector.setKmon(getVal);
                                                                    getKMon();
                                                                    getTicketTransaction();

                                                                }
                                                            } else if (Integer.parseInt(routefn) > Integer.parseInt(routetn)) {
                                                                if (Integer.parseInt(getVal) < Integer.parseInt(routetn)) {
                                                                    Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                                                } else if (Integer.parseInt(getVal) > Integer.parseInt(routefn)) {
                                                                    Toast.makeText(CashAuditActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    inspector.setKmon(getVal);
                                                                    getKMon();
                                                                    getTicketTransaction();

                                                                }
                                                            }


                                                        }

                                                    }
                                                })

                                        .setNegativeButton("BACK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                }).show();
                            }

                            @Override
                            public boolean onKeyDown(int keyCode, KeyEvent event) {
                                if ((keyCode == KeyEvent.KEYCODE_BACK) && isLock) {

                                    return true;
                                } else
                                    return super.onKeyDown(keyCode, event);
                            }

                            void textviewSettings() {
                                kmon = (TextView) findViewById(R.id.TextViewKmPostNum);
                                txtInsID = (TextView) findViewById(R.id.textInsID);
                                txtInsType = (TextView) findViewById(R.id.textInsType);
                                txtInsName = (TextView) findViewById(R.id.textInsName);
                                text1 = (TextView) findViewById(R.id.TextView1);
                                busNo = (TextView) findViewById(R.id.TextView2);
                                text2 = (TextView) findViewById(R.id.TextView3);
                                driver = (TextView) findViewById(R.id.TextView4);
                                text3 = (TextView) findViewById(R.id.TextView5);
                                conductor = (TextView) findViewById(R.id.TextView6);
                                text4 = (TextView) findViewById(R.id.TextView7);
                                tripno = (TextView) findViewById(R.id.TextView8);
                                text5 = (TextView) findViewById(R.id.TextView9);
                                routecode = (TextView) findViewById(R.id.TextView10);

                                paxOnBoard = (TextView) findViewById(R.id.TextView20);
                                totalOnBoard = (TextView) findViewById(R.id.TextView25);
                                baggage = (TextView) findViewById(R.id.TextView36);
                                totalBaggage = (TextView) findViewById(R.id.TextView41);
                                totalCash = (TextView) findViewById(R.id.TextView42);
                                //TripTotalsNum = (TextView)rootView.findViewById(R.id.TextViewTripTotalsNum);

      /* Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");

        text1.setTypeface(typeFace);
        busNo.setTypeface(typeFace);
        text2.setTypeface(typeFace);
        driver.setTypeface(typeFace);
        text3.setTypeface(typeFace);
        conductor.setTypeface(typeFace);
        text4.setTypeface(typeFace);
        tripno.setTypeface(typeFace);
        text5.setTypeface(typeFace);
        routecode.setTypeface(typeFace);
        //TripTotalsNum.setTypeface(typeFace);

        paxOnBoard.setTypeface(typeFace);
        totalOnBoard.setTypeface(typeFace);
        baggage.setTypeface(typeFace);
        totalBaggage.setTypeface(typeFace);
        totalCash.setTypeface(typeFace);*/
                            }

                        }
