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
import android.view.Gravity;
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
import com.admoliva.metrobusticketing.ListActivity.RouteCodeListActivity;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jUniX on 11/16/2015.
 */

public class AdjustTicketActivity extends Activity {
    BluetoothConnectionService mBoundService;
    boolean mBound = false;

    Context ctx = this;
    Boolean isLock = true;

    Button btnPrint;

    RelativeLayout relativeLayoutPassType, relativeLayoutRouteCode, relativeLayoutKMPost, relativeLayoutAnomaly;
    TextView textamount, kmfromno, kmfromdesc, kmtono, kmtodesc, paxtype, paxtypedesc, ticketNumber, ticketLetter;
    TextView textAnmCode, textAnmDesc, textRemarks, textInsID, textInsName;
    String PType;
    String transno;
    int totalPassenger, totalAmountPax, totalBaggage, totalAmountBag;


    InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustticket);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(AdjustTicketActivity.this, BluetoothConnectionService.class);
        startService(intent);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, BluetoothConnectionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


        btnPrint = (Button) findViewById(R.id.ButtonPrint);
        btnPrint.setOnClickListener(new ClickEvent());

        kmfromdesc = (TextView) findViewById(R.id.TextViewRouteF);
        kmfromno = (TextView) findViewById(R.id.TextViewRouteFN);
        kmtodesc = (TextView) findViewById(R.id.TextViewRouteT);
        kmtono = (TextView) findViewById(R.id.TextViewRouteTN);
        textamount = (TextView) findViewById(R.id.TextViewAmount);
        paxtype = (TextView) findViewById(R.id.TextViewPaxType);
        paxtypedesc = (TextView) findViewById(R.id.TextViewPaxTypeDesc);
        ticketNumber = (TextView) findViewById(R.id.textTicketNo);
        ticketLetter = (TextView) findViewById(R.id.textTicketLetter);
        textAnmCode = (TextView) findViewById(R.id.AnomalyCode);
        textAnmDesc = (TextView) findViewById(R.id.AnomalyDesc);
        textRemarks = (TextView) findViewById(R.id.textRemarks);
        textInsID = (TextView) findViewById(R.id.textInsID);
        textInsName = (TextView) findViewById(R.id.textInsName);

        textRemarks.setGravity(Gravity.CENTER);

        relativeLayoutKMPost = (RelativeLayout)findViewById(R.id.LinearKMPost);
        relativeLayoutKMPost.setOnClickListener(new ClickEvent());
        relativeLayoutPassType = (RelativeLayout) findViewById(R.id.LinearPassType);
        relativeLayoutPassType.setOnClickListener(new ClickEvent());
        relativeLayoutAnomaly = (RelativeLayout) findViewById(R.id.LinearAnomaly);
        relativeLayoutAnomaly.setOnClickListener(new ClickEvent());

        kmPostData();
        passTypeData();
        ticket();
        anomalyInfo();
        inspectorInfo();
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
    class ClickEvent implements View.OnClickListener
    {
        public void onClick(View v) {

            if( v == btnPrint){
                if(textRemarks.getText().equals("")){
                    Toast.makeText(AdjustTicketActivity.this, "Please leave Remarks!", Toast.LENGTH_SHORT).show();
                }else{
                    InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
                    transno = inspector.getTransNo();
                    updateTicketTrans();
                    getTotalAmount();
                    oldTicketTransaction();
                    insertTicketTransaction();
                }
            }else if( v == relativeLayoutRouteCode){
                startActivity(new Intent(AdjustTicketActivity.this, RouteCodeListActivity.class));
            }else if(v == relativeLayoutPassType){
                if(kmfromno.getText().equals("XXX")){
                    Toast.makeText(AdjustTicketActivity.this, "Please select Km Post first!", Toast.LENGTH_SHORT).show();
                }else{
                    //textamount.setText("XXX");
                    Intent intent = new Intent(AdjustTicketActivity.this, PassTypeListActivity.class);
                    startActivity(intent);
                }
            }else if (v == relativeLayoutKMPost){
                startActivity(new Intent(AdjustTicketActivity.this, KmPostActivity.class));
            }else if (v == relativeLayoutAnomaly){
                if(paxtype.getText().equals("X")){
                    Toast.makeText(AdjustTicketActivity.this, "Please select Pax Type first!", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(AdjustTicketActivity.this, AnomalyDataActivity.class));
                }
            }

        }
    }

    private void oldTicketTransaction()
    {

        DatabaseHelper db = new DatabaseHelper(ctx);
        try
        {
            double paxAmount = Double.parseDouble(inspector.getFare());
            db.insertOldInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                    inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                    inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                    inspector.getKmfrom(), inspector.getKmto(), paxAmount, inspector.getFaretype(), inspector.getBagtype(),
                    inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", textAnmCode.getText().toString(),
                    Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                    Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, "OLD DATA", "ADJUSTED BY " + inspector.getInsName());

        }catch (Exception e)
        {
            Log.d("Inspector Old Ticket", e.toString());
        }finally {
            db.closeDB();
        }
    }

    private void updateTicketTrans(){
        DatabaseHelper db = new DatabaseHelper(ctx);

        try{
            if(PType == "P"){
                double paxAmount = Double.parseDouble(textamount.getText().toString());
                db.updateTransData(db, inspector.getTicketNo(), kmfromno.getText().toString(), kmtono.getText().toString(), paxtype.getText().toString(),
                        "", paxAmount, DateToday(), TimeToday(), inspector.getInsId(), inspector.getInsType(), "U");
                db.updateAdjustTicketInfo(db, Integer.parseInt(inspector.getTicketNo()), kmfromdesc.getText().toString(), kmtodesc.getText().toString(), paxtypedesc.getText().toString());
            }else if(PType=="B"){
                double paxAmount = Double.parseDouble(textamount.getText().toString());
                db.updateTransData(db, inspector.getTicketNo(), kmfromno.getText().toString(), kmtono.getText().toString(), PType,
                        paxtype.getText().toString(), paxAmount, DateToday(), TimeToday(), inspector.getInsId(), inspector.getInsType(), "U");
                db.updateAdjustTicketInfo(db, Integer.parseInt(inspector.getTicketNo()), kmfromdesc.getText().toString(), kmtodesc.getText().toString(), paxtypedesc.getText().toString());
            }

        }catch (Exception e){
            Log.d("Adjust ticket", e.toString());
        }finally {
            db.closeDB();
        }

    }

    private void getTotalAmount(){
        DatabaseHelper db = new DatabaseHelper(ctx);

        try{
            if(PType == "P")
            {
                if(Integer.parseInt(inspector.getRoutefn()) > Integer.parseInt(inspector.getRoutetn())){

                    Cursor crFull = db.getPaxTicketFull(db, inspector.getTripNo(), inspector.getKmon(), "<");
                    Cursor crSP = db.getPaxTicketSP(db, inspector.getTripNo(), inspector.getKmon(), "<");
                    Cursor crHalf = db.getPaxTicketHalf(db, inspector.getTripNo(), inspector.getKmon(), "<");
                    Cursor crOP = db.getPaxTicketOP(db, inspector.getTripNo(), inspector.getKmon(), "<");

                    Cursor crLetter = db.getPaxTicketLetter(db, inspector.getTripNo(), inspector.getKmon(), "<");
                    Cursor crSacks = db.getPaxTicketSacks(db, inspector.getTripNo(), inspector.getKmon(), "<");
                    Cursor crKilos = db.getPaxTicketKilos(db, inspector.getTripNo(), inspector.getKmon(), "<");
                    Cursor crOB = db.getPaxTicketOB(db, inspector.getTripNo(), inspector.getKmon(), "<");


                    crFull.moveToFirst();
                    crSP.moveToFirst();
                    crHalf.moveToFirst();
                    crOP.moveToFirst();

                    crLetter.moveToFirst();
                    crSacks.moveToFirst();
                    crKilos.moveToFirst();
                    crOB.moveToFirst();


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

                    int cash1 = Integer.parseInt(FullAmount)+Integer.parseInt(SPAmount)+Integer.parseInt(HAmount)+Integer.parseInt(OPAmount);
                    int totalPax = Integer.parseInt(F)+Integer.parseInt(SP)+Integer.parseInt(H)+Integer.parseInt(OP);
                    totalAmountPax = cash1;
                    totalPassenger = totalPax;


                    int cash2 = Integer.parseInt(LAmount)+Integer.parseInt(SAmount)+Integer.parseInt(KAmount)+Integer.parseInt(OBAmount);
                    int totalBag = Integer.parseInt(L)+Integer.parseInt(S)+Integer.parseInt(K)+Integer.parseInt(OB);
                    totalAmountBag = cash2;
                    totalBaggage = totalBag;

                    crFull.close();
                    crSP.close();
                    crHalf.close();
                    crOB.close();
                    crLetter.close();
                    crSacks.close();
                    crKilos.close();
                    crOP.close();

                }else  if(Integer.parseInt(inspector.getRoutefn()) < Integer.parseInt(inspector.getRoutetn())){
                    Cursor crFull = db.getPaxTicketFull(db, inspector.getTripNo(), inspector.getKmon(), ">");
                    Cursor crSP = db.getPaxTicketSP(db, inspector.getTripNo(), inspector.getKmon(), ">");
                    Cursor crHalf = db.getPaxTicketHalf(db, inspector.getTripNo(), inspector.getKmon(), ">");
                    Cursor crOP = db.getPaxTicketOP(db, inspector.getTripNo(), inspector.getKmon(), ">");

                    Cursor crLetter = db.getPaxTicketLetter(db, inspector.getTripNo(), inspector.getKmon(), ">");
                    Cursor crSacks = db.getPaxTicketSacks(db, inspector.getTripNo(), inspector.getKmon(), ">");
                    Cursor crKilos = db.getPaxTicketKilos(db, inspector.getTripNo(), inspector.getKmon(), ">");
                    Cursor crOB = db.getPaxTicketOB(db, inspector.getTripNo(), inspector.getKmon(), ">");


                    crFull.moveToFirst();
                    crSP.moveToFirst();
                    crHalf.moveToFirst();
                    crOP.moveToFirst();

                    crLetter.moveToFirst();
                    crSacks.moveToFirst();
                    crKilos.moveToFirst();
                    crOB.moveToFirst();


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

                    int cash1 = Integer.parseInt(FullAmount)+Integer.parseInt(SPAmount)+Integer.parseInt(HAmount)+Integer.parseInt(OPAmount);
                    int totalPax = Integer.parseInt(F)+Integer.parseInt(SP)+Integer.parseInt(H)+Integer.parseInt(OP);
                    totalAmountPax = cash1;
                    totalPassenger = totalPax;

                    int cash2 = Integer.parseInt(LAmount)+Integer.parseInt(SAmount)+Integer.parseInt(KAmount)+Integer.parseInt(OBAmount);
                    int totalBag = Integer.parseInt(L)+Integer.parseInt(S)+Integer.parseInt(K)+Integer.parseInt(OB);
                    totalAmountBag = cash2;
                    totalBaggage = totalBag;

                    crFull.close();
                    crSP.close();
                    crHalf.close();
                    crOB.close();
                    crLetter.close();
                    crSacks.close();
                    crKilos.close();
                    crOP.close();
                }

            }
        }catch (Exception e){
            Log.d("Adjust Ticket", e.toString());
        }finally {
            db.closeDB();
        }


    }
    private void insertTicketTransaction()
    {
        DatabaseHelper db = new DatabaseHelper(ctx);

        try
        {
            if(PType == "P")
            {
                int totAmntPax = totalAmountPax;
                int totalPax = totalPassenger;
                double paxAmount = Double.parseDouble(textamount.getText().toString());
                db.insertInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                        inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                        inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                        kmfromno.getText().toString(), kmtono.getText().toString(), paxAmount, paxtype.getText().toString(), "",
                        inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", textAnmCode.getText().toString(),
                        totalPax, Integer.parseInt(inspector.getBagno()), totAmntPax,
                        Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, textRemarks.getText().toString(), "ADJUSTED BY " + inspector.getInsName());
                db.insertAdjustTicket(db, Integer.parseInt(inspector.getTicketNo()), kmfromdesc.getText().toString(), kmtodesc.getText().toString(), paxtypedesc.getText().toString());
                printReceipt("Pax");
                Toast.makeText(AdjustTicketActivity.this, "Transaction Success", Toast.LENGTH_LONG).show();
            }else if(PType == "B")
            {
                int totAmntBag = totalAmountBag;
                int totalBag = totalBaggage;
                double paxAmount = Double.parseDouble(textamount.getText().toString());
                db.insertInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                        inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                        inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                        kmfromno.getText().toString(), kmtono.getText().toString(), paxAmount, PType, paxtype.getText().toString(),
                        inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", textAnmCode.getText().toString(),
                        Integer.parseInt(inspector.getPaxno()), totalBag, Integer.parseInt(inspector.getPaxamt()),
                        totAmntBag, 0, 0, 0, 0, textRemarks.getText().toString(), "ADJUSTED BY " + inspector.getInsName());
                db.insertAdjustTicket(db, Integer.parseInt(inspector.getTicketNo()), kmfromdesc.getText().toString(), kmtodesc.getText().toString(), paxtypedesc.getText().toString());
                printReceipt("Bag");
                Toast.makeText(AdjustTicketActivity.this, "Transaction Success", Toast.LENGTH_LONG).show();

            }

        }catch (Exception e)
        {
            Log.d("Inspector Adjust Ticket", e.toString());
        }finally {
            db.closeDB();
            textamount.setText("XXX");
            kmfromdesc.setText("XXXXX");
            kmfromno.setText("XXX");
            kmtodesc.setText("XXXXX");
            kmtono.setText("XXX");
            paxtype.setText("X");
            paxtypedesc.setText("XXXX");
            textAnmCode.setText("X");
            textAnmDesc.setText("XXXX");
            textRemarks.setText("");
            buttonEnable();
            finishAdjustActivity();
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
                    + "          DAVAO CITY\n"
                    + "--------------------------------\n"
                    + "Bus    : " + accountInfo.getBusno()+ " " + accountInfo.getBusname() + "\n"
                    + "BMac   : " + bt.getAddress() + "\n"
                    + "OR#    : " + ticketNumber.getText().toString() + " " + ticketLetter.getText().toString() + "\n"
                    + "Date   : " + DateToday() + "\n\n"
                    + "TNo    : " + inspector.getTripNo() + " RCode :"+ inspector.getRoutecode() + "\n"
                    + "KmFrom : " + kmfromno.getText().toString() + " " + kmfromdesc.getText().toString() + "\n"
                    + "KmTo   : " + kmtono.getText().toString() + " " + kmtodesc.getText().toString() + "\n"
                    + type+" Type : " + paxtype.getText().toString() + " " + paxtypedesc.getText().toString()  + "\n"
                    + "Amount : " + textamount.getText().toString()  + "\n"
                    + "--------------------------------\n"
                    + "Driv   : " + accountInfo.getDrivid() + " " + accountInfo.getDrivername() + "\n"
                    + "Cond   : " + accountInfo.getConid() + " " + accountInfo.getConductorname() + "\n"
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

    void ticket()
    {
        ticketLetter.setText(inspector.getTicketLetter());
        ticketNumber.setText(inspector.getTicketNo());
    }
    void inspectorInfo()
    {
        textInsID.setText(inspector.getInsId());
        textInsName.setText(inspector.getInsName());
    }
    void anomalyInfo()
    {
        //InspectorAuditVariable anomaly = InspectorAuditVariable.getInstance();
        String anmcode = inspector.getAnmcode();
        String anmdesc = inspector.getAnmdesc();
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
    void kmPostData()
    {

        KmPostVariable kmpost = KmPostVariable.getInstance();
        String kmfrn = kmpost.getKmfrn();
        String kmfrt = kmpost.getKmfrt();
        String kmton = kmpost.getKmton();
        String kmtot = kmpost.getKmtot();


        if(kmfrn.equals("XXX"))
        {
            kmfromdesc.setText(inspector.getKmonname());
            kmfromno.setText(inspector.getKmfrom());
            kmtodesc.setText(inspector.getKmtoname());
            kmtono.setText(inspector.getKmto());
        }else
        {
            kmfromdesc.setText(kmfrt);
            kmfromno.setText(kmfrn);
            kmtodesc.setText(kmtot);
            kmtono.setText(kmton);
        }
    }

    void clearKmPostData()
    {
        KmPostVariable kmpost = KmPostVariable.getInstance();
        kmpost.setKmfrn("XXX");
        kmpost.setKmfrt("XXXXX");
        kmpost.setKmton("XXX");
        kmpost.setKmtot("XXXXX");

        kmfromdesc.setText("XXXXX");
        kmfromno.setText("XXX");
        kmtodesc.setText("XXXXX");
        kmtono.setText("XXX");

    }
    void passTypeData()
    {
        PassTypeVariable passType = PassTypeVariable.getInstance();
        String passcode = passType.getPasscode();
        String passdesc = passType.getPassdesc();
        if(passcode.equals("X"))
        {
            if(!inspector.getFaretype().equals("B")){
                paxtype.setText(inspector.getFaretype());
                paxtypedesc.setText(inspector.getPassdesc());
                textamount.setText(inspector.getFare());
                buttonEnable();
            }else{
                paxtype.setText(inspector.getBagtype());
                paxtypedesc.setText(inspector.getPassdesc());
                textamount.setText(inspector.getFare());
                buttonEnable();
            }

        }else
        {
            paxtype.setText(passcode);
            paxtypedesc.setText("(" + passdesc + ")");
            getAmount(passcode);

        }
    }

    private String getType(String type)
    {
        String val = "";
        switch (type)
        {
            case "F":
                PType = "P";
                val = "Full";
                break;
            case "SP":
                PType = "P";
                val = "SP";
                break;
            case "H":
                PType = "P";
                val = "Half";
                break;
            case "OP":
                PType = "P";
                val = "Other PaxType";
                break;
            case "L":
                PType = "B";
                val = "Letter";
                break;
            case "S":
                PType = "B";
                val = "Sacks";
                break;
            case "K":
                PType = "B";
                val = "Kilos";
                break;
            case "OB":
                PType = "B";
                val = "Other BagType";
                break;
        }
        return val;

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
        final EditText input = new EditText(AdjustTicketActivity.this);
        input.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdjustTicketActivity.this);
        alertDialog.setTitle("Other Amount")
                .setMessage("Enter amount")
                .setView(input)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String getVal = input.getText().toString();
                                if (getVal.compareTo("") == 0) {
                                    Toast.makeText(AdjustTicketActivity.this, "No Amount!", Toast.LENGTH_SHORT).show();
                                } else {
                                    double paxAmount = Double.parseDouble(getVal);
                                    if (textamount.getText().equals("0.0")) {
                                        Toast.makeText(AdjustTicketActivity.this, "Not Valid Amount", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AdjustTicketActivity.this, "You are not connected to Bluetooth Device.", Toast.LENGTH_SHORT).show();
                }else if(flag == 1){
                    btnPrint.setEnabled(true);
                    btnPrint.setBackgroundResource(R.drawable.button_selector);
                }


            }
            btnPrint.setEnabled(true);
            btnPrint.setBackgroundResource(R.drawable.button_selector_2);
        }
    }

    private void finishAdjustActivity(){
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        stopService(intent);
        clearKmPostData();
        unbindService(mConnection);
        mBound = false;
        this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAdjustActivity();
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
