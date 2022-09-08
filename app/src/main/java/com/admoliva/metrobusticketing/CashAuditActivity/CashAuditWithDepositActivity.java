package com.admoliva.metrobusticketing.CashAuditActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.Bluetooth;
import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.ListActivity.AnomalyDataActivity;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jUniX on 12/5/2015.
 */
public class CashAuditWithDepositActivity extends Activity {
    TextView txtInsName, txtInsID;
    Context ctx = this;
    Boolean isLock = true;
    TextView paxOnBoard, totalOnBoard, baggage;
    TextView totalBaggage, totalCash;
    RelativeLayout LinearAnomaly;
    TextView textAnmCode, textAnmDesc;
    TextView textExpenses, textCashShortage, textComputedCash, textCashOnHand, textRemarks, textDeposits;
    Button btnSubmit;

    BluetoothConnectionService mBoundService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashauditwithdeposit);

        LinearAnomaly = (RelativeLayout)findViewById(R.id.LinearAnomaly);
        btnSubmit = (Button) findViewById(R.id.ButtonReport);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        startService(intent);

        LinearAnomaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashAuditWithDepositActivity.this, AnomalyDataActivity.class));
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSubmit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


        textviewSettings();
        getInspectorInfo();
        getTicketTransaction();
        anomalyInfo();
        textChange();
        textValidationCashOnHand();
        textValidationExpenses();
        textValidationCashDeposit();
    }

    private void textValidationExpenses(){
        if(textExpenses.getText().toString().isEmpty()){
            textComputedCash.setText("0");
        }
    }
    private void textValidationCashOnHand(){
        if(textCashOnHand.getText().toString().isEmpty()){
            textCashShortage.setText("0");
        }
    }

    private void textValidationCashDeposit(){
        if(textExpenses.getText().toString().isEmpty() && textCashOnHand.getText().toString().isEmpty()){
            textDeposits.setText("0");
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

    private void clickSubmit(){
        if(textExpenses.getText().toString().isEmpty() || textCashOnHand.getText().toString().isEmpty() || textRemarks.getText().toString().isEmpty() ){
            Toast.makeText(CashAuditWithDepositActivity.this, "Please complete the following fields", Toast.LENGTH_SHORT).show();
        }else{
            submitReport();
        }
    }
    private void submitReport(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        String transno = "";
        transno = inspector.getTransNo();
        try
        {
            double paxAmount = Double.parseDouble(inspector.getFare());
            int expenses = Integer.parseInt(textExpenses.getText().toString());
            int cashOnHand = Integer.parseInt(textCashOnHand.getText().toString());
            int shortage = Integer.parseInt(textCashShortage.getText().toString());
            int deposits = Integer.parseInt(textDeposits.getText().toString());

            db.insertInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                    inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                    inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                    inspector.getKmfrom(), inspector.getKmto(), paxAmount, inspector.getFaretype(), inspector.getBagtype(),
                    inspector.getInsId(), inspector.getInsType(), "A", inspector.getKmon(), "", textAnmCode.getText().toString(),
                    Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                    Integer.parseInt(inspector.getBagamt()), expenses, cashOnHand, deposits, shortage, textRemarks.getText().toString(), "AUDIT BY " + inspector.getInsName());

            Toast.makeText(CashAuditWithDepositActivity.this, "Successfully Audit!", Toast.LENGTH_LONG).show();

            printReceipt();
        }catch (Exception e)
       {
           Log.d("Cash Audit", e.toString());
        }finally {
            inspector.setAnmcode("X");
            textAnmCode.setText("X");
            textAnmDesc.setText("XXXX");
            textRemarks.setText("");
            textCashOnHand.setText("");
            textExpenses.setText("");
            textComputedCash.setText("0");
            textCashShortage.setText("0");
            textDeposits.setText("");
            db.closeDB();
            finish();

        }
    }
    void printReceipt() {
        if (mBound) {
            GlobalVariable g = GlobalVariable.getInstance();
            InspectorAuditVariable i = InspectorAuditVariable.getInstance();
            Bluetooth bt = Bluetooth.getInstance();
            String msg = "";
            //msg = "--------------------------------\n"
            msg = "Mobile Bus Ticketing System on Android\n"
                    + "          DAVAO CITY\n\n"

                    + "-----CASH AUDIT WITH DEP.----\n\n"

                    + "Bus      : " + g.getBusno() + " " +g.getBusname() + "\n"
                    + "TNo      : " + i.getTripNo() + "\n"
                    + "RCode    : " + i.getRoutecode() + "\n"
                    + "BMac     : " + bt.getAddress() + "\n\n"

                    + "--------------------------------\n\n"

                    + "Ins      : " + i.getInsId() + " " + i.getInsType() + " " + i.getInsName() + "\n"
                    + "KmZone   : " + i.getKmon() + "\n"
                    + "TotOnBrd : " + i.getPaxno() + " " + i.getPaxamt() + "\n"
                    + "TotBag   : " + i.getBagno() + " " + i.getBagamt() + "\n\n"
                    + "TotCol   : " + totalCash.getText().toString() + "\n"
                    + "Less(Exp): " + textExpenses.getText().toString() + "\n"
                    + "TComCash : " + textComputedCash.getText().toString() + "\n"
                    + "COnHnd   : " + textCashOnHand.getText().toString() + "\n"
                    + "CashShort: " + textCashShortage.getText().toString() + "\n"
                    + "CashDep  : " + textDeposits.getText().toString() + "\n\n"
                    + "RepCode  : " + textAnmCode.getText().toString() + " " + textAnmDesc.getText().toString() + "\n"
                    + "Remarks  : " + textRemarks.getText().toString() + "\n\n"

                    + "--------------------------------\n\n"
                    + "This serves as a Deposit Slip for:\n\n\n"

                    + "Cond :" + g.getConductorname() + "\n"
                    + "Driv : " + g.getDrivername() + "\n\n\n"
                    + i.getInsType() + "  "  + i.getInsName() + "\n\n"
                    + "--------------------------------\n\n";


            mBoundService.printing(msg);

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
    private void getInspectorInfo(){
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        txtInsID.setText(inspector.getInsId());
        txtInsName.setText(inspector.getInsName());
    }
    private void textChange(){
        textExpenses.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(textExpenses.getText().toString().isEmpty()){
                        textComputedCash.setText("0");
                    }else{
                        int result = Integer.parseInt(totalCash.getText().toString()) - Integer.parseInt(textExpenses.getText().toString());
                        textComputedCash.setText(String.valueOf(result));
                    }
                }

            }
        });
        textCashOnHand.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    if(textCashOnHand.getText().toString().isEmpty()) {
                        textCashShortage.setText("0");
                    }else{
                        int result = Integer.parseInt(textComputedCash.getText().toString()) - Integer.parseInt(textCashOnHand.getText().toString());
                        textCashShortage.setText(String.valueOf(result));
                        AnomalyShortage();
                    }

                }
            }
        });
    }

    private void AnomalyShortage(){
        if(Integer.parseInt(textCashShortage.getText().toString()) < 300){
            //SH1
            String anmcode = "SH1";
            String anmdesc = getAnomalyDesc(anmcode);
            textAnmCode.setText(anmcode);
            textAnmDesc.setText(anmdesc);
        }else if(Integer.parseInt(textCashShortage.getText().toString()) < 1000){
            //SH2
            String anmcode = "SH2";
            String anmdesc = getAnomalyDesc(anmcode);
            textAnmCode.setText(anmcode);
            textAnmDesc.setText(anmdesc);
        }else if(Integer.parseInt(textCashShortage.getText().toString()) > 1000){
            //SH3
            String anmcode = "SH3";
            String anmdesc = getAnomalyDesc(anmcode);
            textAnmCode.setText(anmcode);
            textAnmDesc.setText(anmdesc);
        }else if(Integer.parseInt(textCashShortage.getText().toString()) == 0){
            //SH3
            String anmcode = "SH0";
            String anmdesc = getAnomalyDesc(anmcode);
            textAnmCode.setText(anmcode);
            textAnmDesc.setText(anmdesc);
        }
    }

    private String getAnomalyDesc(String anmcode){
        String anmdesc = "";
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.searchAnomalyData(db, anmcode);
        try{
            cr.moveToFirst();
            anmdesc = cr.getString(2);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }
        return anmdesc;
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
        }else{
            textAnmCode.setText(anmcode);
            textAnmDesc.setText("(" + anmdesc + ")");
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
                InspectorAuditVariable anomaly = InspectorAuditVariable.getInstance();
                anomaly.setAnmcode("X");
                anomaly.setAnmcode("XXXX");
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
    void textviewSettings() {

        textRemarks = (TextView) findViewById(R.id.textRemarks);

        textCashOnHand = (TextView)findViewById(R.id.textCashOnHand);
        textCashShortage = (TextView) findViewById(R.id.textCashStorage);
        textComputedCash = (TextView) findViewById(R.id.textComputedCash);
        textExpenses = (TextView)findViewById(R.id.textExpenses);
        textDeposits = (TextView) findViewById(R.id.textDeposits);

        txtInsID = (TextView) findViewById(R.id.textInsID);
        txtInsName = (TextView) findViewById(R.id.textInsName);
        textAnmCode = (TextView) findViewById(R.id.AnomalyCode);
        textAnmDesc = (TextView) findViewById(R.id.AnomalyDesc);

        paxOnBoard = (TextView) findViewById(R.id.TextView20);
        totalOnBoard = (TextView) findViewById(R.id.TextView25);
        baggage = (TextView) findViewById(R.id.TextView36);
        totalBaggage = (TextView) findViewById(R.id.TextView41);
        totalCash = (TextView) findViewById(R.id.TextView42);


        textCashOnHand.setGravity(Gravity.CENTER);
        textCashShortage.setGravity(Gravity.CENTER);
        textComputedCash.setGravity(Gravity.CENTER);
        textExpenses.setGravity(Gravity.CENTER);
        textDeposits.setGravity(Gravity.CENTER);
    }
}
