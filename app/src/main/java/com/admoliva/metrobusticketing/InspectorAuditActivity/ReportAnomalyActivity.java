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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.Activitity.LineInspectionActivity;
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
 * Created by jUniX on 11/16/2015.
 */
public class ReportAnomalyActivity extends Activity {
    Context ctx =this;
    Boolean isLock = true;
    TextView textRemarks, textInsID, textInsName, ticketNumber, ticketLetter;
    TextView textAnmCode, textAnmDesc;
    RelativeLayout relativeLayoutAnomaly;
    InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
    Button btnReport;
    BluetoothConnectionService mBoundService;
    boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportanomaly);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(ReportAnomalyActivity.this, BluetoothConnectionService.class);
        startService(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        ticketNumber = (TextView) findViewById(R.id.textTicketNo);
        ticketLetter = (TextView) findViewById(R.id.textTicketLetter);
        textRemarks = (TextView) findViewById(R.id.textRemarks);
        textInsID = (TextView) findViewById(R.id.textInsID);
        textInsName = (TextView) findViewById(R.id.textInsName);
        textAnmCode = (TextView) findViewById(R.id.AnomalyCode);
        textAnmDesc = (TextView) findViewById(R.id.AnomalyDesc);

        relativeLayoutAnomaly = (RelativeLayout) findViewById(R.id.LinearAnomaly);
        relativeLayoutAnomaly.setOnClickListener(new ClickEvent());
        btnReport = (Button) findViewById(R.id.ButtonReport);
        btnReport.setOnClickListener(new ClickEvent());

        textRemarks.setGravity(Gravity.CENTER);

        inspectorInfo();
        ticket();
        anomalyInfo();
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


    private void finishActivity(){
        Intent intent = new Intent(this, BluetoothConnectionService.class);
        stopService(intent);
        unbindService(mConnection);
        mBound = false;
        GlobalVariable gv = GlobalVariable.getInstance();
        gv.setKmPostPosition(0);
        InspectorAuditVariable anomaly = InspectorAuditVariable.getInstance();
        anomaly.setAnmcode("X");
        anomaly.setAnmcode("XXXX");
        this.finish();
    }

    private void validatePrint(){
        if(textAnmCode.getText().toString().equals("NOR") || textAnmCode.getText().toString().equals("SH0")){
            finishActivity();
        }else {
            printReceipt();

        }

    }

    void printReceipt() {
        if (mBound) {
            int count = 2;
            for(int x = 0; x < count; x++){
                GlobalVariable g = GlobalVariable.getInstance();
                InspectorAuditVariable i = InspectorAuditVariable.getInstance();
                Bluetooth bt = Bluetooth.getInstance();
                String msg = "";
                //msg = "--------------------------------\n"
                msg = "Mobile Bus Ticketing System on Android\n"
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
                mBoundService.printing(msg);
            }
            finishActivity();
        }
    }
    void inspectorInfo()
    {
        textInsID.setText(inspector.getInsId());
        textInsName.setText(inspector.getInsName());
    }
    void ticket()
    {
        ticketLetter.setText(inspector.getTicketLetter());
        ticketNumber.setText(inspector.getTicketNo());
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
        }

    }
    class ClickEvent implements View.OnClickListener{

        public void onClick(View v) {
            if(v == btnReport){
                if(textRemarks.getText().toString().equals("")){
                    Toast.makeText(ReportAnomalyActivity.this, "Please input a Remarks!", Toast.LENGTH_SHORT).show();
                }else{
                    reportAnomaly();
                }
            }else if ( v == relativeLayoutAnomaly){
                startActivity(new Intent(ReportAnomalyActivity.this, AnomalyDataActivity.class));
            }
        }
    }
    void reportAnomaly(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        String transno = "";
        transno = inspector.getTransNo();
        try
        {
            if(inspector.getFaretype().equals("B")){
                double paxAmount = Double.parseDouble(inspector.getFare());
                db.insertInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                        inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                        inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                        inspector.getKmfrom(), inspector.getKmto(), paxAmount, inspector.getFaretype(), inspector.getBagtype(),
                        inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", textAnmCode.getText().toString(),
                        Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                        Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, textRemarks.getText().toString(), "REPORT BY " + inspector.getInsName());
                validatePrint();
                Toast.makeText(ReportAnomalyActivity.this, "Successfully Reported!", Toast.LENGTH_LONG).show();
            }else{
                double paxAmount = Double.parseDouble(inspector.getFare());
                db.insertInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                        inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                        inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                        inspector.getKmfrom(), inspector.getKmto(), paxAmount, inspector.getFaretype(), "",
                        inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", textAnmCode.getText().toString(),
                        Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                        Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, textRemarks.getText().toString(), "REPORT BY " + inspector.getInsName());
                Toast.makeText(ReportAnomalyActivity.this, "Successfully Reported!", Toast.LENGTH_LONG).show();
                validatePrint();
            }
        }catch (Exception e)
        {
            Toast.makeText(ReportAnomalyActivity.this, "Insert Transaction Error :  "+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            inspector.setAnmcode("X");
            textAnmCode.setText("X");
            textAnmDesc.setText("XXXX");
            textRemarks.setText("");
            db.closeDB();
            //this.finish();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
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
