package com.admoliva.metrobusticketing.InspectorAuditActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jUniX on 11/16/2015.
 */
public class CancelTicketActivity extends Activity {

    Context ctx =this;
    Boolean isLock = true;
    TextView textRemarks, textInsID, textInsName, ticketNumber, ticketLetter;
    InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelticket);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onStart() {
        super.onStart();
        ticketNumber = (TextView) findViewById(R.id.textTicketNo);
        ticketLetter = (TextView) findViewById(R.id.textTicketLetter);
        textRemarks = (TextView) findViewById(R.id.textRemarks);
        textInsID = (TextView) findViewById(R.id.textInsID);
        textInsName = (TextView) findViewById(R.id.textInsName);

        btnCancel = (Button) findViewById(R.id.ButtonCancel);
        btnCancel.setOnClickListener(new ClickEvent());

        ticket();
        inspectorInfo();
    }
    class ClickEvent implements View.OnClickListener
    {
        public void onClick(View v) {
             if(v == btnCancel){
                 if(textRemarks.getText().toString().equals("")){
                     Toast.makeText(CancelTicketActivity.this, "Please insert a Remarks!", Toast.LENGTH_SHORT).show();
                 }else {
                     cancelTicket();
                 }

            }

        }
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

    void cancelTicket(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        String transno = "";
        transno = inspector.getTransNo();
            try
            {
                if(inspector.getFaretype().equals("B")){
                    db.insertInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                            inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                            inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                            inspector.getKmfrom(), inspector.getKmto(), 0.0, "C", "C",
                            inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", "",
                            Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                            Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, textRemarks.getText().toString(), "CANCELLED BY " + inspector.getInsName());
                    db.deleteAdjustTicketInfo(db, Integer.parseInt(inspector.getTicketNo()));
                    db.cancelTransData(db, inspector.getTicketNo(), "C", "C", 0.0, DateToday(), TimeToday(),inspector.getInsId(), inspector.getInsType(), "U");
                    Toast.makeText(CancelTicketActivity.this, "Ticket Number Cancelled!", Toast.LENGTH_LONG).show();
                }else{
                    db.insertInspectorTicketTransaction(db, transno, inspector.getTripNo(), inspector.getRoutefn(), inspector.getRoutetn(),
                            inspector.getRoutecode(), DateToday(), TimeToday(), Integer.parseInt(inspector.getTicketNo()),
                            inspector.getTicketLetter(), inspector.getBusno(), inspector.getDriverid(), inspector.getConid(),
                            inspector.getKmfrom(), inspector.getKmto(), 0.0, "C", "",
                            inspector.getInsId(), inspector.getInsType(), "I", inspector.getKmon(), "", "",
                            Integer.parseInt(inspector.getPaxno()), Integer.parseInt(inspector.getBagno()), Integer.parseInt(inspector.getPaxamt()),
                            Integer.parseInt(inspector.getBagamt()), 0, 0, 0, 0, textRemarks.getText().toString(), "CANCELLED BY " + inspector.getInsName());
                    db.deleteAdjustTicketInfo(db, Integer.parseInt(inspector.getTicketNo()));
                    db.cancelTransData(db, inspector.getTicketNo(), "C", "", 0.0, DateToday(), TimeToday(),inspector.getInsId(), inspector.getInsType(),"U");
                    Toast.makeText(CancelTicketActivity.this, "Ticket Number Cancelled!", Toast.LENGTH_LONG).show();
                }


            }catch (Exception e)
            {
                Toast.makeText(CancelTicketActivity.this, "Insert Transaction Error :  "+e.toString(), Toast.LENGTH_SHORT).show();
            }finally {
                db.closeDB();
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
