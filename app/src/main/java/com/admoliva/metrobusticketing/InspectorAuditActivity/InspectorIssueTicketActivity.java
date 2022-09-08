package com.admoliva.metrobusticketing.InspectorAuditActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
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
import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.GlobalVariable.KmPostVariable;
import com.admoliva.metrobusticketing.GlobalVariable.PassTypeVariable;
import com.admoliva.metrobusticketing.ListActivity.AnomalyDataActivity;
import com.admoliva.metrobusticketing.ListActivity.KmPostActivity;
import com.admoliva.metrobusticketing.ListActivity.PassTypeListActivity;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Aljon Moliva on 7/20/2015.
 */
public class InspectorIssueTicketActivity extends Activity {

    Boolean isLock = true;

    int tcktFrom;
    int tcktTo;
    int tcktLeft;

    String PType = "";

    String ticketLet;
    Context ctx = this;
    Button btnPrint;
    TextView tripno, routecode, routecodefrom, routecodeto, kmpostfrom, kmpostfromnum, kmpostto, kmposttonum, paxtype, paxtypedesc;
    TextView tckLeft, tckUsed, tckLet, textamount;
    TextView txtInsID, txtInsName;
    TextView textRemarks, textAnmCode, textAnmDesc;
    RelativeLayout relativeLayoutPassType, relativeLayoutKMPost, relativeLayoutAnomaly;
    BluetoothConnectionService mBoundService;
    boolean mBound = false;
    String routefn, routetn;
    TextView RouteTicketNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspector_issueticket_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        startService(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        textAnmCode = (TextView) findViewById(R.id.AnomalyCode);
        textAnmDesc = (TextView) findViewById(R.id.AnomalyDesc);
        textRemarks = (TextView) findViewById(R.id.textRemarks);
        tckUsed = (TextView) findViewById(R.id.ticketUsed);
        tckLeft = (TextView) findViewById(R.id.ticketLeft);
        tckLet = (TextView) findViewById(R.id.ticketLetter);
        txtInsID = (TextView) findViewById(R.id.textInsID);
        txtInsName = (TextView) findViewById(R.id.textInsName);
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
        RouteTicketNo = (TextView) findViewById(R.id.ticketStart);


        btnPrint = (Button) findViewById(R.id.ButtonPrint);
        btnPrint.setOnClickListener(new ClickEvent());
        btnPrint.setEnabled(false);
        btnPrint.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));

        relativeLayoutAnomaly = (RelativeLayout)findViewById(R.id.LinearAnomaly);
        relativeLayoutAnomaly.setOnClickListener(new ClickEvent());
        relativeLayoutKMPost = (RelativeLayout)findViewById(R.id.LinearKMPost);
        relativeLayoutKMPost.setOnClickListener(new ClickEvent());
        relativeLayoutPassType = (RelativeLayout) findViewById(R.id.LinearPassType);
        relativeLayoutPassType.setOnClickListener(new ClickEvent());

        getTicketDetails();
        getTripNo();
        kmPostData();
        passTypeData();
        getInspectorInfo();
        anomalyInfo();
        textRouteTicketNo();
    }
    /** Defines callbacks for service binding, passed to bindService() */
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
    class ClickEvent implements View.OnClickListener
    {
        public void onClick(View v) {
            if(v == btnPrint) {
                if(textRemarks.getText().toString().equals("")){
                    Toast.makeText(InspectorIssueTicketActivity.this, "Please insert a remarks!", Toast.LENGTH_SHORT).show();
                }else{
                    insertTicketTransaction(PType);
                }

            } else if(v == relativeLayoutPassType){
                if(kmpostfromnum.getText().equals("XXX")) {
                    Toast.makeText(InspectorIssueTicketActivity.this, "Please select first Km Post!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(InspectorIssueTicketActivity.this, PassTypeListActivity.class);
                    startActivity(intent);
                }
            }else if (v == relativeLayoutKMPost) {
                startActivity(new Intent(InspectorIssueTicketActivity.this, KmPostActivity.class));
            }else if( v == relativeLayoutAnomaly){
                if(paxtype.getText().equals("X")){
                    Toast.makeText(InspectorIssueTicketActivity.this, "Please select Pax Type first!", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(InspectorIssueTicketActivity.this, AnomalyDataActivity.class));
                }
            }

        }
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

    void anomalyInfo()
    {
        InspectorAuditVariable anomaly = InspectorAuditVariable.getInstance();
        String anmcode = anomaly.getAnmcode();
        String anmdesc = anomaly.getAnmdesc();
        if(anmcode.equals("X"))
        {
            textAnmCode.setText("X");
            textAnmDesc.setText("XXXX");
        }else
        {
            textAnmCode.setText(anmcode);
            textAnmDesc.setText("(" + anmdesc + ")");
            buttonEnable();
        }

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
            Toast.makeText(InspectorIssueTicketActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            getTripNo.closeDB();
            cr.close();
            getRouteValidation();
        }

    }
    private void getRouteValidation()
    {
        GlobalVariable rc = GlobalVariable.getInstance();
        String rtec = rc.getRoutecode();
        String trip = tripno.getText().toString();
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.RouteCode(db, rtec, rc.getBustype());
        cr.moveToFirst();
        try
        {
            routecode.setText(rtec);
            routecodefrom.setText(cr.getString(3) + " " + cr.getString(1));
            routecodeto.setText(cr.getString(4) + " " + cr.getString(2));
            routefn = cr.getString(3);
            routetn = cr.getString(4);
        }catch (Exception e)
        {
            Toast.makeText(InspectorIssueTicketActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            db.closeDB();
            cr.close();
        }
    }
    private void getTicketDetails()
    {
        DatabaseHelper dbTicketOR = new DatabaseHelper(ctx);
        Cursor cr = dbTicketOR.getTicketInfo(dbTicketOR);

        try
        {

                cr.moveToFirst();
                tcktFrom = Integer.parseInt(cr.getString(0));
                tcktTo = Integer.parseInt(cr.getString(2));
                ticketLet = cr.getString(1);
                tcktLeft = (tcktTo - (tcktFrom - 1));
                tckLeft.setText(String.valueOf(tcktLeft));
                tckUsed.setText(String.valueOf(tcktFrom));
                tckLet.setText(ticketLet);

        }catch(Exception e)
        {
            throw e;
        }finally {
            dbTicketOR.closeDB();
            cr.close();
        }

    }



    private void getInspectorInfo(){
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        txtInsID.setText(inspector.getInsId());
        txtInsName.setText(inspector.getInsName());
    }

    void kmPostData()
    {
        KmPostVariable kmpost = KmPostVariable.getInstance();
        String kmfrn = kmpost.getKmfrn();
        String kmfrt = kmpost.getKmfrt();
        String kmton = kmpost.getKmton();
        String kmtot = kmpost.getKmtot();

        if(kmfrn.equals("XXX"))
        {
            kmpostfrom.setText("XXXXX");
            kmpostfromnum.setText("XXX");
            kmpostto.setText("XXXXX");
            kmposttonum.setText("XXX");
        }else
        {
            kmpostfrom.setText(kmfrt);
            kmpostfromnum.setText(kmfrn);
            kmpostto.setText(kmtot);
            kmposttonum.setText(kmton);
        }

    }

    void clearKmPostData()
    {
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
    void passTypeData()
    {
        PassTypeVariable passType = PassTypeVariable.getInstance();
        String passcode = passType.getPasscode();
        String passdesc = passType.getPassdesc();
        if(passcode.equals("X"))
        {
            paxtype.setText("X");
            paxtypedesc.setText("XXXX");
            textamount.setText("XXX");
        }else
        {
            paxtype.setText(passcode);
            paxtypedesc.setText("(" + passdesc + ")");
            getAmount(passcode);
        }
    }

    void getAmount(String type)
    {
        KmPostVariable kmpost = KmPostVariable.getInstance();
        double fares = kmpost.getFares();
        double fareg = kmpost.getFareg();
        double farelet = kmpost.getFarelet();
        double faresak = kmpost.getFaresak();
        double fareton = kmpost.getFareton();
        double val;
        switch (type)
        {
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
    void OtherPaxAmount()
    {
        final EditText input = new EditText(InspectorIssueTicketActivity.this);
        input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspectorIssueTicketActivity.this);
        alertDialog.setTitle("Other Amount")
                .setMessage("Enter amount")
                .setView(input)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String getVal = input.getText().toString();
                                if (getVal.compareTo("") == 0) {
                                    Toast.makeText(InspectorIssueTicketActivity.this, "No Amount!", Toast.LENGTH_SHORT).show();
                                } else {
                                    double paxAmount = Double.parseDouble(getVal);
                                    if (paxAmount == 0.0) {
                                        Toast.makeText(InspectorIssueTicketActivity.this, "Not Valid Amount", Toast.LENGTH_SHORT).show();
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
    void buttonEnable()
    {

        if(textAnmCode.getText().equals("X"))
        {
            btnPrint.setEnabled(false);
            btnPrint.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
        }else
        {
            if(mBound)
            {
                int flag = mBoundService.flag;
                if(flag !=1){
                    Toast.makeText(InspectorIssueTicketActivity.this, "You are not connected to Bluetooth Device.", Toast.LENGTH_SHORT).show();
                }else if(flag == 1){
                    btnPrint.setEnabled(true);
                    btnPrint.setBackgroundResource(R.drawable.button_selector);
                }

            }
            btnPrint.setEnabled(true);
            btnPrint.setBackgroundResource(R.drawable.button_selector_2);
        }

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
    private void insertTicketTransaction(String type)
    {
        GlobalVariable accountInfo = GlobalVariable.getInstance();
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        DatabaseHelper db = new DatabaseHelper(ctx);
        String transno = "";
        transno = inspector.getTransNo();
       try
        {

            if(type == "P")
            {
                double paxAmount = Double.parseDouble(textamount.getText().toString());
                db.insertTicketTransaction(db, tripno.getText().toString(), routefn, routetn, routecode.getText().toString(),
                        DateToday(), TimeToday(), tcktFrom, tckLet.getText().toString(), accountInfo.getBusno(), accountInfo.getDrivid(), accountInfo.getConid(),
                        kmpostfromnum.getText().toString(), kmposttonum.getText().toString(), paxtype.getText().toString(), paxAmount, "", inspector.getInsId(),inspector.getInsType(),"A");

                db.insertInspectorTicketTransaction(db, transno, tripno.getText().toString(), routefn, routetn,
                        routecode.getText().toString(), DateToday(), TimeToday(), tcktFrom,
                        tckLet.getText().toString(), accountInfo.getBusno(), accountInfo.getDrivid(), accountInfo.getConid(),
                        kmpostfromnum.getText().toString(), kmposttonum.getText().toString(), paxAmount, paxtype.getText().toString(), "",
                        inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", textAnmCode.getText().toString(),
                        Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                        Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, textRemarks.getText().toString(), "ISSUED BY " + inspector.getInsName());


                db.insertAdjustTicket(db, tcktFrom, kmpostfrom.getText().toString(), kmpostto.getText().toString(), paxtypedesc.getText().toString());
                db.updateTicketInfo(db, (Integer.parseInt(tckUsed.getText().toString()) + 1));
                printReceipt("P");
                Toast.makeText(InspectorIssueTicketActivity.this, "Transaction Success!", Toast.LENGTH_SHORT).show();
            }else if(type == "B")
            {
                double paxAmount = Double.parseDouble(textamount.getText().toString());
                db.insertTicketTransaction(db, tripno.getText().toString(), routefn, routetn, routecode.getText().toString(),
                        DateToday(), TimeToday(), tcktFrom, tckLet.getText().toString(), accountInfo.getBusno(), accountInfo.getDrivid(), accountInfo.getConid(),
                        kmpostfromnum.getText().toString(), kmposttonum.getText().toString(), type, paxAmount, paxtype.getText().toString(),inspector.getInsId(),inspector.getInsType(),"A");
                db.insertInspectorTicketTransaction(db, transno, tripno.getText().toString(), routefn, routetn,
                        routecode.getText().toString(), DateToday(), TimeToday(), tcktFrom,
                        tckLet.getText().toString(), accountInfo.getBusno(), accountInfo.getDrivid(), accountInfo.getConid(),
                        kmpostfromnum.getText().toString(), kmposttonum.getText().toString(), paxAmount, type, paxtype.getText().toString(),
                        inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", textAnmCode.getText().toString(),
                        Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                        Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, textRemarks.getText().toString(), "ISSUED BY " + inspector.getInsName());
                db.insertAdjustTicket(db, tcktFrom, kmpostfrom.getText().toString(), kmpostto.getText().toString(), paxtypedesc.getText().toString() );
                db.updateTicketInfo(db, (Integer.parseInt(tckUsed.getText().toString()) + 1));
                printReceipt("B");
                Toast.makeText(InspectorIssueTicketActivity.this, "Transaction Success!", Toast.LENGTH_SHORT).show();
            }

       }catch (Exception e)
       {
           //Log.d("Inspector Issue Ticket", e.toString());
           Toast.makeText(InspectorIssueTicketActivity.this, "Inspector Issue Ticket :  "+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
           db.closeDB();
            textamount.setText("XXX");
            kmpostfrom.setText("XXXXX");
            kmpostfromnum.setText("XXX");
            kmpostto.setText("XXXXX");
            kmposttonum.setText("XXX");
            paxtype.setText("X");
            paxtypedesc.setText("XXXX");
            textRemarks.setText("");
            textAnmCode.setText("X");
            textAnmDesc.setText("XXX");
            getTicketDetails();
            buttonEnable();
        }

    }

    void printReceipt(String type)
    {
        if(mBound)
        {
            GlobalVariable accountInfo = GlobalVariable.getInstance();
            Bluetooth bt = Bluetooth.getInstance();
            String msg = "";
            //msg = "--------------------------------\n"
            msg =   "Mobile Bus Ticketing System on Android\n"
                    + "          DAVAO CITY\n\n"
                    + "--------------------------------\n\n"
                    + "Bus   : " + accountInfo.getBusno()+ " " + accountInfo.getBusname() + "\n"
                    + "BMac  : " + bt.getAddress() + "\n"
                    + "OR#   : " + tckUsed.getText().toString() + " " + tckLet.getText().toString()
                    + "Date  : " + DateToday() + "\n\n"
                    + "TNo   : " + tripno.getText().toString() + " RCode :"+ routecode.getText() + "\n"
                    + "KmFrm : " + kmpostfromnum.getText().toString() + " " + kmpostfrom.getText().toString() + "\n"
                    + "KmTo  : " + kmposttonum.getText().toString() + " " + kmpostto.getText().toString() + "\n"
                    + type+"Type : " + paxtype.getText().toString() + " " + paxtypedesc.getText().toString()  + "\n"
                    + "Amount : " + textamount.getText().toString()  + "\n"
                    + "--------------------------------\n"
                    + "Driv  : " + accountInfo.getDrivid() + " " + accountInfo.getDrivername() + "\n"
                    + "Cond  : " + accountInfo.getConid() + " " + accountInfo.getConductorname() + "\n"
                    + "--------------------------------\n\n\n";
            mBoundService.printing(msg);
            if(textAnmCode.getText().toString().equals("SH0") || textAnmCode.getText().toString().equals("NOR")){
                GlobalVariable g = GlobalVariable.getInstance();
                InspectorAuditVariable i = InspectorAuditVariable.getInstance();
                String msg2 = "";
                //msg = "--------------------------------\n"
                msg2 = "Mobile Bus Ticketing System on Android\n"
                        + "          DAVAO CITY\n\n"

                        + "--------ANOMALY REPORT--------\n\n"

                        + "Bus     : " + g.getBusno() + " " +g.getBusname() + "\n"
                        + "Cond    : " + g.getConid() + " " + g.getConductorname() + "\n"
                        + "TNo     : " + i.getTripNo() + "\n"
                        + "RCode   : " + i.getRoutecode() + "\n"
                        + "BMac    : " + bt.getAddress() + "\n\n"

                        + "--------------------------------\n\n"

                        + "KmZone  : " + i.getKmon() + "\n"
                        + "PaxOnBrd: " + i.getPaxno() + "PaxTotAmnt" + i.getPaxamt() + "\n"
                        + "BagTotal: " + i.getBagno() + "BagTotAmnt" + i.getBagamt() + "\n"
                        + "RepCode : " + textAnmCode.getText().toString() + " " + textAnmDesc.getText().toString() + "\n\n\n"

                        + "Reported by:\n\n\n"
                        + i.getInsId() + " " + i.getInsType() + " " + i.getInsName() + "\n\n"

                        + "Acknowledged by:\n\n\n"
                        + g.getConid() + " " + g.getConductorname() + "/\n"
                        + g.getDrivid() + " " + g.getDrivername() + "\n\n"

                        + "  (  ) admitted\n"
                        + "  (  ) under protest\n"

                        + "--------------------------------\n\n\n";
                mBoundService.printing(msg2);
            }


        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, BluetoothConnectionService.class);
                stopService(intent);
                clearKmPostData();
                unbindService(mConnection);
                mBound = false;
                PassTypeVariable passType = PassTypeVariable.getInstance();
                passType.setPasscode("X");
                passType.setPassdesc("XXXX");
                passType.setPassdesc("XXX");
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

