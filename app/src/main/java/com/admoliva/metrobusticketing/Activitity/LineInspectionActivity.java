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

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.GlobalVariable.PassTypeVariable;
import com.admoliva.metrobusticketing.InspectorAuditActivity.AdjustTicketActivity;
import com.admoliva.metrobusticketing.InspectorAuditActivity.CancelTicketActivity;
import com.admoliva.metrobusticketing.InspectorAuditActivity.InspectorIssueTicketActivity;
import com.admoliva.metrobusticketing.InspectorAuditActivity.ReportAnomalyActivity;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jUniX on 11/15/2015.
 */
public class LineInspectionActivity extends Activity {
    TextView txtInsName, txtInsType, txtInsID;
    Context ctx = this;
    Boolean isLock = true;

    TextView busNo, driver, conductor, tripno, routecode, kmfrom, kmto;
    TextView text1, text2, text3, text4, text5, text6, text7;
    TextView text8, text9, text10, text11, text12, text13, text14, text15, text16, text17, text18, text19, text20, text21;
    TextView paxOnBoard, full, sp, half, other, totalOnBoard, totalFull, totalSP, totalHalf, totalOther;
    TextView baggage, letters, sacks, kilos, others;
    TextView totalBaggage, totalLetters, totalSacks, totalKilos, totalOthers, totalCash;
    String routefn, routetn;
    TextView kmon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineinspection);
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
        getKMon();
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        getTicketTransaction(routefn, routetn, tripno.getText().toString(), inspector.getKmon());
    }
    private void getKMon(){
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        kmon.setText(inspector.getKmon());
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
            cr.moveToLast();
            String tripNo = cr.getString(0);
            tripno.setText(tripNo);
            accountInfo.setTripNo(tripNo);
        }catch (Exception e)
        {
            Toast.makeText(LineInspectionActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            getTripNo.closeDB();
            cr.close();
        }

    }
    private void getRouteValidation()
    {
        GlobalVariable rc = GlobalVariable.getInstance();
        String rtec = rc.getRoutecode();
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.RouteCode(db, rtec, rc.getBustype());
        cr.moveToFirst();
        try
        {
                kmfrom.setText(cr.getString(3) + " " + cr.getString(1));
                kmto.setText(cr.getString(4) + " " + cr.getString(2));
                routefn = cr.getString(3);
                routetn = cr.getString(4);
        }catch (Exception e)
        {
            Toast.makeText(LineInspectionActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            db.closeDB();
            cr.close();
            InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();

        }
    }
    private void getInspectorInfo(){
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        txtInsID.setText(inspector.getInsId());
        txtInsName.setText(inspector.getInsName());
        txtInsType.setText(inspector.getInsType());
    }
    private void getTicketTransaction(String routefn, String routetn, String tripno, String kmpost)
    {
        DatabaseHelper dbHelper = new DatabaseHelper(ctx);
        try
        {
            if(Integer.parseInt(routefn) > Integer.parseInt(routetn))
            {

                Cursor crPax = dbHelper.getPaxTicketTransCountPax(dbHelper, tripno, kmpost);
                Cursor crBag = dbHelper.getPaxTicketTransCountBag(dbHelper, tripno, kmpost);
                //Cursor cursorCountPaxType = dbHelper.getPaxTicketTransPaxType2(dbHelper, tripno, kmpost);

                Cursor crFull = dbHelper.getPaxTicketFull(dbHelper, tripno, kmpost, "<");
                Cursor crSP = dbHelper.getPaxTicketSP(dbHelper, tripno, kmpost, "<");
                Cursor crHalf = dbHelper.getPaxTicketHalf(dbHelper, tripno, kmpost, "<");
                Cursor crOP = dbHelper.getPaxTicketOP(dbHelper, tripno, kmpost, "<");

                Cursor crLetter = dbHelper.getPaxTicketLetter(dbHelper, tripno, kmpost, "<");
                Cursor crSacks = dbHelper.getPaxTicketSacks(dbHelper, tripno, kmpost, "<");
                Cursor crKilos = dbHelper.getPaxTicketKilos(dbHelper, tripno, kmpost, "<");
                Cursor crOB = dbHelper.getPaxTicketOB(dbHelper, tripno, kmpost, "<");


                crFull.moveToFirst();
                crSP.moveToFirst();
                crHalf.moveToFirst();
                crOP.moveToFirst();

                crLetter.moveToFirst();
                crSacks.moveToFirst();
                crKilos.moveToFirst();
                crOB.moveToFirst();


                crPax.moveToFirst();
                crBag.moveToFirst();
                String F, SP, H, OP, L, S, K, OB;
                String FullAmount, SPAmount, HAmount, OPAmount, LAmount, SAmount, KAmount, OBAmount;


                F = crFull.getString(0);
                FullAmount = crFull.getString(1);
                SP = crSP.getString(0);
                SPAmount = crSP.getString(1);
                H = crHalf.getString(0);
                HAmount = crHalf.getString(1);
                OP = crOP.getString(0);
                OPAmount = crOP.getString(1);
                L = crLetter.getString(0);
                LAmount = crLetter.getString(1);
                S = crSacks.getString(0);
                SAmount = crSacks.getString(1);
                K = crKilos.getString(0);
                KAmount = crKilos.getString(1);
                OB = crOB.getString(0);
                OBAmount = crOB.getString(1);

                String paxCount = crPax.getString(0);
                String bagCount = crBag.getString(0);

                full.setText(F);
                sp.setText(SP);
                half.setText(H);
                other.setText(OP);
                letters.setText(L);
                sacks.setText(S);
                kilos.setText(K);
                others.setText(OB);




                totalFull.setText(FullAmount);
                totalSP.setText(SPAmount);
                totalHalf.setText(HAmount);
                totalOther.setText(OPAmount);
                totalLetters.setText(LAmount);
                totalSacks.setText(SAmount);
                totalKilos.setText(KAmount);
                totalOthers.setText(OBAmount);

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
                crPax.close();
                crBag.close();
                crFull.close();
                crSP.close();
                crHalf.close();
                crOB.close();
                crLetter.close();
                crSacks.close();
                crKilos.close();
                crOP.close();

            }else if(Integer.parseInt(routefn) < Integer.parseInt(routetn)){
                Cursor crPax = dbHelper.getPaxTicketTransCountPax(dbHelper, tripno, kmpost);
                Cursor crBag = dbHelper.getPaxTicketTransCountBag(dbHelper, tripno, kmpost);
                //Cursor cursorCountPaxType = dbHelper.getPaxTicketTransPaxType1(dbHelper, tripno, kmpost);
                Cursor crFull = dbHelper.getPaxTicketFull(dbHelper, tripno, kmpost, ">");
                Cursor crSP = dbHelper.getPaxTicketSP(dbHelper, tripno, kmpost, ">");
                Cursor crHalf = dbHelper.getPaxTicketHalf(dbHelper, tripno, kmpost, ">");
                Cursor crOP = dbHelper.getPaxTicketOP(dbHelper, tripno, kmpost, ">");

                Cursor crLetter = dbHelper.getPaxTicketLetter(dbHelper, tripno, kmpost, ">");
                Cursor crSacks = dbHelper.getPaxTicketSacks(dbHelper, tripno, kmpost, ">");
                Cursor crKilos = dbHelper.getPaxTicketKilos(dbHelper, tripno, kmpost, ">");
                Cursor crOB = dbHelper.getPaxTicketOB(dbHelper, tripno, kmpost, ">");


                crFull.moveToFirst();
                crSP.moveToFirst();
                crHalf.moveToFirst();
                crOP.moveToFirst();

                crLetter.moveToFirst();
                crSacks.moveToFirst();
                crKilos.moveToFirst();
                crOB.moveToFirst();


                crPax.moveToFirst();
                crBag.moveToFirst();

                String F, SP, H, OP, L, S, K, OB;
                String FullAmount, SPAmount, HAmount, OPAmount, LAmount, SAmount, KAmount, OBAmount;

                F = crFull.getString(0);
                FullAmount = crFull.getString(1);
                SP = crSP.getString(0);
                SPAmount = crSP.getString(1);
                H = crHalf.getString(0);
                HAmount = crHalf.getString(1);
                OP = crOP.getString(0);
                OPAmount = crOP.getString(1);
                L = crLetter.getString(0);
                LAmount = crLetter.getString(1);
                S = crSacks.getString(0);
                SAmount = crSacks.getString(1);
                K = crKilos.getString(0);
                KAmount = crKilos.getString(1);
                OB = crOB.getString(0);
                OBAmount = crOB.getString(1);

                full.setText(F);
                sp.setText(SP);
                half.setText(H);
                other.setText(OP);
                letters.setText(L);
                sacks.setText(S);
                kilos.setText(K);
                others.setText(OB);

                totalFull.setText(FullAmount);
                totalSP.setText(SPAmount);
                totalHalf.setText(HAmount);
                totalOther.setText(OPAmount);
                totalLetters.setText(LAmount);
                totalSacks.setText(SAmount);
                totalKilos.setText(KAmount);
                totalOthers.setText(OBAmount);

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


                //Toast.makeText(getActivity().getApplicationContext(), F + S, Toast.LENGTH_SHORT).show();
                crPax.close();
                crBag.close();
                crFull.close();
                crSP.close();
                crHalf.close();
                crOB.close();
                crLetter.close();
                crSacks.close();
                crKilos.close();
                crOP.close();


            }
        }catch (Exception e) {
            Toast.makeText(LineInspectionActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            dbHelper.closeDB();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inspectorauditmenu, menu);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(LineInspectionActivity.this);
            builder.setTitle("Replace Zone");
            builder.setMessage("Are you sure you want to replace your KMON?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setCancelable(false)
                    .setNegativeButton("No", dialogClickListener).show();
        }else if(id == R.id.adjustTicket)
        {
            addjustTicket();
        }else if(id == R.id.addPax)
        {
            issueTicket();
        }else if(id == R.id.cancelTicket)
        {
            promptcancelTicket();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(LineInspectionActivity.this);
            builder.setTitle("Line Inspection");
            builder.setMessage("Are you sure you want to exit")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setCancelable(false)
                    .setNegativeButton("No", dialogClickListener).show();

        }

        return super.onOptionsItemSelected(item);
    }

    void issueTicket(){
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        inspector.setPaxno(paxOnBoard.getText().toString());
        inspector.setBagno(baggage.getText().toString());
        inspector.setPaxamt(totalOnBoard.getText().toString());
        inspector.setBagamt(totalBaggage.getText().toString());
        inspector.setAnmcode("X");
        inspector.setAnmdesc("XXX");
        startActivity(new Intent(LineInspectionActivity.this, InspectorIssueTicketActivity.class));

    }
    void promptcancelTicket(){
        final EditText input = new EditText(LineInspectionActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LineInspectionActivity.this);
        alertDialog.setTitle("Cancel Ticket")
                .setMessage("Enter Ticket No.")
                .setView(input)
                        //.setIcon(R.drawable.logo)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String getVal = input.getText().toString();
                                InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
                                DatabaseHelper db = new DatabaseHelper(ctx);
                                Cursor cr = db.getTransData(db, getVal);
                                try {
                                    if (getVal.compareTo("") == 0) {
                                        Toast.makeText(LineInspectionActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    } else {

                                        if (cr.getCount() > 0) {
                                            cr.moveToFirst();
                                            String ticket = cr.getString(15);
                                            if (ticket.equals("C")) {
                                                Toast.makeText(LineInspectionActivity.this, "This ticket is already been cancelled!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                inspector.setTripNo(cr.getString(1));
                                                inspector.setRoutefn(cr.getString(2));
                                                inspector.setRoutetn(cr.getString(3));
                                                inspector.setRoutecode(cr.getString(4));
                                                inspector.setDate(cr.getString(5));
                                                inspector.setTime(cr.getString(6));
                                                inspector.setTicketNo(getVal);
                                                inspector.setTicketLetter(cr.getString(8));
                                                inspector.setBusno(cr.getString(9));
                                                inspector.setDriverid(cr.getString(10));
                                                inspector.setConid(cr.getString(11));
                                                inspector.setKmfrom(cr.getString(12));
                                                inspector.setKmto(cr.getString(13));
                                                inspector.setFare(cr.getString(14));
                                                inspector.setFaretype(cr.getString(15));
                                                inspector.setBagtype(cr.getString(16));
                                                inspector.setPaxno(paxOnBoard.getText().toString());
                                                inspector.setBagno(baggage.getText().toString());
                                                inspector.setPaxamt(totalOnBoard.getText().toString());
                                                inspector.setBagamt(totalBaggage.getText().toString());
                                                popupCancelTicket();
                                            }
                                        } else {
                                            Toast.makeText(LineInspectionActivity.this, "Ticket not found!", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                } catch (Exception e) {
                                    Log.d("Cancel Ticket", e.toString());
                                } finally {
                                    cr.close();
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
        final EditText input = new EditText(LineInspectionActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LineInspectionActivity.this);
        alertDialog.setTitle("Line Inspection")
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
                                        Toast.makeText(LineInspectionActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (Integer.parseInt(routefn) < Integer.parseInt(routetn)) {
                                            if (Integer.parseInt(getVal) > Integer.parseInt(routetn)) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else if (Integer.parseInt(getVal) < Integer.parseInt(routefn) || Integer.parseInt(getVal) < Integer.parseInt(inspector.getKmon())) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
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
                                                    LineInspectionActivity.this.finish();
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
                                                    LineInspectionActivity.this.finish();

                                                }
                                            }
                                        } else if (Integer.parseInt(routefn) > Integer.parseInt(routetn)) {
                                            if (Integer.parseInt(getVal) < Integer.parseInt(routetn)) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else if (Integer.parseInt(getVal) > Integer.parseInt(routefn) || Integer.parseInt(getVal) > Integer.parseInt(inspector.getKmon())) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
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
                                                    LineInspectionActivity.this.finish();
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
                                                    LineInspectionActivity.this.finish();
                                                }
                                            }
                                        }



                                    }
                                } catch (Exception e) {
                                    Log.d("Log out", e.toString());
                                } finally {
                                    db.closeDB();

                                }


                            }
                        }).setNegativeButton("BACK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }

                            ).

                            show();
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
    void reportAnomaly(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getInspectorTransByID(db, txtInsID.getText().toString());
        Cursor cr1 = db.getAllTransData(db);
        Cursor crTripNo = db.getTripRoute(db);
        InspectorAuditVariable var = InspectorAuditVariable.getInstance();
        try {
            if(cr.getCount() > 0){
                cr.moveToLast();
                crTripNo.moveToLast();
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
                startActivity(new Intent(LineInspectionActivity.this, ReportAnomalyActivity.class));
            }else{
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
                startActivity(new Intent(LineInspectionActivity.this, ReportAnomalyActivity.class));

            }

        }catch (Exception e){
            Log.d("Report Anomaly", e.toString());
        }finally {
            cr.close();
            cr1.close();
            crTripNo.close();
            db.closeDB();
        }

    }
    void popupCancelTicket(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                    cancelTicket();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(LineInspectionActivity.this);
        builder.setTitle("Line Inspection");
        builder.setMessage("Are you sure you want to Cancel this Ticket?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();




    }

   private void cancelTicket(){
       InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
       DatabaseHelper db = new DatabaseHelper(ctx);
       try
       {
           if(inspector.getFaretype().equals("B")){
               db.insertInspectorTicketTransaction(db, "1", inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                       inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                       inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                       inspector.getKmfrom(), inspector.getKmto(), 0.0, "C", "C",
                       inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", "",
                       Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                       Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, "", "CANCELLED BY " + inspector.getInsName());
                db.deleteAdjustTicketInfo(db, Integer.parseInt(inspector.getTicketNo()));
               db.cancelTransData(db, inspector.getTicketNo(), "C", "C", 0.0, DateToday(), TimeToday(),inspector.getInsId(), inspector.getInsType() ,"1");
               Toast.makeText(LineInspectionActivity.this, "Ticket Number Cancelled!", Toast.LENGTH_LONG).show();
           }else{
               db.insertInspectorTicketTransaction(db, "1", inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                       inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                       inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                       inspector.getKmfrom(), inspector.getKmto(), 0.0, "C", "",
                       inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", "",
                       Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                       Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, "", "CANCELLED BY " + inspector.getInsName());
               db.deleteAdjustTicketInfo(db, Integer.parseInt(inspector.getTicketNo()));
               db.cancelTransData(db, inspector.getTicketNo(), "C", "", 0.0, DateToday(), TimeToday(),inspector.getInsId(), inspector.getInsType(),"1");
               Toast.makeText(LineInspectionActivity.this, "Ticket Number Cancelled!", Toast.LENGTH_LONG).show();
           }


       }catch (Exception e)
       {
           Toast.makeText(LineInspectionActivity.this, "Insert Transaction Error :  "+e.toString(), Toast.LENGTH_SHORT).show();
       }finally {
           db.closeDB();
       }
   }
    void addjustTicket()
    {
        final EditText input = new EditText(LineInspectionActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LineInspectionActivity.this);
        alertDialog.setTitle("Adjust Ticket")
                .setMessage("Enter Ticket No.")
                .setView(input)
                        //.setIcon(R.drawable.logo)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String getVal = input.getText().toString();
                                InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
                                DatabaseHelper db = new DatabaseHelper(ctx);
                                Cursor cr = db.getTransData(db, getVal);
                                Cursor cr1 = db.getAdjustTicketInfo(db, Integer.parseInt(getVal));
                                try {
                                    if (getVal.compareTo("") == 0) {
                                        Toast.makeText(LineInspectionActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (cr.getCount() > 0) {
                                            cr.moveToFirst();
                                            cr1.moveToFirst();
                                            String ticket = cr.getString(15);
                                            if (ticket.equals("C")) {
                                                Toast.makeText(LineInspectionActivity.this, "This ticket is already been cancelled!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                PassTypeVariable passType = PassTypeVariable.getInstance();

                                                inspector.setKmonname(cr1.getString(2));
                                                inspector.setKmtoname(cr1.getString(3));
                                                inspector.setPassdesc(cr1.getString(4));
                                                inspector.setTripNo(cr.getString(1));
                                                inspector.setRoutefn(cr.getString(2));
                                                inspector.setRoutetn(cr.getString(3));
                                                inspector.setRoutecode(cr.getString(4));
                                                inspector.setDate(cr.getString(5));
                                                inspector.setTime(cr.getString(6));
                                                inspector.setTicketNo(cr.getString(7));
                                                inspector.setTicketLetter(cr.getString(8));
                                                inspector.setBusno(cr.getString(9));
                                                inspector.setDriverid(cr.getString(10));
                                                inspector.setConid(cr.getString(11));
                                                inspector.setKmfrom(cr.getString(12));
                                                inspector.setKmto(cr.getString(13));
                                                inspector.setFare(cr.getString(14));
                                                inspector.setFaretype(cr.getString(15));
                                                inspector.setBagtype(cr.getString(16));
                                                inspector.setPaxno(paxOnBoard.getText().toString());
                                                inspector.setBagno(baggage.getText().toString());
                                                inspector.setPaxamt(totalOnBoard.getText().toString());
                                                inspector.setBagamt(totalBaggage.getText().toString());
                                                inspector.setAnmcode("X");
                                                inspector.setAnmdesc("XXX");
                                                passType.setPasscode("X");
                                                passType.setPassdesc("XXXX");
                                                startActivity(new Intent(LineInspectionActivity.this, AdjustTicketActivity.class));
                                            }
                                            }else{
                                                Toast.makeText(LineInspectionActivity.this, "Ticket not found!", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }finally{
                                        cr.close();
                                    cr1.close();
                                    db.closeDB();
                                    }


                                }
                            }

                            )

                                    .

                            setNegativeButton("BACK",
                                                      new DialogInterface.OnClickListener() {
                                public void onClick (DialogInterface dialog,int which){
                                    dialog.cancel();
                                }
                            }

                            ).

                            show();


                        }

    void changeKmON()
    {
        final EditText input = new EditText(LineInspectionActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LineInspectionActivity.this);
        alertDialog.setTitle("Replace KM Zone")
                .setMessage("Enter Km on.")
                .setView(input)
                        //.setIcon(R.drawable.logo)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String getVal = input.getText().toString();
                                InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();

                                if (getVal.compareTo("") == 0) {
                                    Toast.makeText(LineInspectionActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                } else {
                                        if (Integer.parseInt(routefn) < Integer.parseInt(routetn)) {
                                            if (Integer.parseInt(getVal) > Integer.parseInt(routetn)) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else if (Integer.parseInt(getVal) < Integer.parseInt(routefn)) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                inspector.setKmon(getVal);
                                                getTicketTransaction(routefn, routetn, tripno.getText().toString(), getVal);
                                                getKMon();
                                            }
                                        } else if (Integer.parseInt(routefn) > Integer.parseInt(routetn)) {
                                            if (Integer.parseInt(getVal) < Integer.parseInt(routetn)) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else if (Integer.parseInt(getVal) > Integer.parseInt(routefn)) {
                                                Toast.makeText(LineInspectionActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                inspector.setKmon(getVal);
                                                getTicketTransaction(routefn, routetn, tripno.getText().toString(), getVal);
                                                getKMon();
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

                            void clear() {
                                paxOnBoard.setText("00");
                                full.setText("00");
                                sp.setText("00");
                                half.setText("00");
                                other.setText("00");
                                totalOnBoard.setText("000.00");
                                totalFull.setText("000.00");
                                totalSP.setText("000.00");
                                totalHalf.setText("000.00");
                                totalOther.setText("000.00");

                                baggage.setText("00");
                                letters.setText("00");
                                sacks.setText("00");
                                kilos.setText("00");
                                others.setText("00");
                                totalCash.setText("000.00");
                                totalBaggage.setText("000.00");
                                totalLetters.setText("000.00");
                                totalSacks.setText("000.00");
                                totalKilos.setText("000.00");
                                totalOthers.setText("000.00");


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
                                text6 = (TextView) findViewById(R.id.TextView11);
                                kmfrom = (TextView) findViewById(R.id.TextView12);
                                text7 = (TextView) findViewById(R.id.TextView13);
                                kmto = (TextView) findViewById(R.id.TextView14);

                                text8 = (TextView) findViewById(R.id.TextView15);
                                text9 = (TextView) findViewById(R.id.TextView16);
                                text10 = (TextView) findViewById(R.id.TextView17);
                                text11 = (TextView) findViewById(R.id.TextView18);
                                text12 = (TextView) findViewById(R.id.TextView19);
                                text13 = (TextView) findViewById(R.id.TextView30);
                                text14 = (TextView) findViewById(R.id.TextView31);
                                text15 = (TextView) findViewById(R.id.TextView32);
                                text16 = (TextView) findViewById(R.id.TextView33);
                                text17 = (TextView) findViewById(R.id.TextView34);
                                text18 = (TextView) findViewById(R.id.TextView35);

                                paxOnBoard = (TextView) findViewById(R.id.TextView20);
                                full = (TextView) findViewById(R.id.TextView21);
                                sp = (TextView) findViewById(R.id.TextView22);
                                half = (TextView) findViewById(R.id.TextView23);
                                other = (TextView) findViewById(R.id.TextView24);
                                totalOnBoard = (TextView) findViewById(R.id.TextView25);
                                totalFull = (TextView) findViewById(R.id.TextView26);
                                totalSP = (TextView) findViewById(R.id.TextView27);
                                totalHalf = (TextView) findViewById(R.id.TextView28);
                                totalOther = (TextView) findViewById(R.id.TextView29);
                                baggage = (TextView) findViewById(R.id.TextView36);
                                letters = (TextView) findViewById(R.id.TextView37);
                                sacks = (TextView) findViewById(R.id.TextView38);
                                kilos = (TextView) findViewById(R.id.TextView39);
                                others = (TextView) findViewById(R.id.TextView40);
                                totalBaggage = (TextView) findViewById(R.id.TextView41);
                                totalLetters = (TextView) findViewById(R.id.TextView42);
                                totalSacks = (TextView) findViewById(R.id.TextView43);
                                totalKilos = (TextView) findViewById(R.id.TextView44);
                                totalOthers = (TextView) findViewById(R.id.TextView45);
                                totalCash = (TextView) findViewById(R.id.TextView46);
                                //TripTotalsNum = (TextView)rootView.findViewById(R.id.TextViewTripTotalsNum);

                                Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");

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
                                text6.setTypeface(typeFace);
                                kmfrom.setTypeface(typeFace);
                                text7.setTypeface(typeFace);
                                kmto.setTypeface(typeFace);

                                text8.setTypeface(typeFace);
                                text9.setTypeface(typeFace);
                                text10.setTypeface(typeFace);
                                text11.setTypeface(typeFace);
                                text12.setTypeface(typeFace);
                                text13.setTypeface(typeFace);
                                text14.setTypeface(typeFace);
                                text15.setTypeface(typeFace);
                                text16.setTypeface(typeFace);
                                text17.setTypeface(typeFace);
                                text18.setTypeface(typeFace);

                                //TripTotalsNum.setTypeface(typeFace);

                                paxOnBoard.setTypeface(typeFace);
                                full.setTypeface(typeFace);
                                sp.setTypeface(typeFace);
                                half.setTypeface(typeFace);
                                other.setTypeface(typeFace);
                                totalOnBoard.setTypeface(typeFace);
                                totalFull.setTypeface(typeFace);
                                totalSP.setTypeface(typeFace);
                                totalHalf.setTypeface(typeFace);
                                totalOther.setTypeface(typeFace);
                                baggage.setTypeface(typeFace);
                                letters.setTypeface(typeFace);
                                sacks.setTypeface(typeFace);
                                kilos.setTypeface(typeFace);
                                others.setTypeface(typeFace);
                                totalBaggage.setTypeface(typeFace);
                                totalLetters.setTypeface(typeFace);
                                totalSacks.setTypeface(typeFace);
                                totalKilos.setTypeface(typeFace);
                                totalOthers.setTypeface(typeFace);
                                totalCash.setTypeface(typeFace);
                            }

                        }
