package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;

import javax.xml.validation.Validator;

/**
 * Created by jUniX on 9/28/2015.
 */
public class NewTripActivity extends Activity {

    Context ctx =this;
    Button btnSave, btnBack;
    EditText txtValidCode;

    boolean isLock = true;
    String code = "";
    String tripnumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtrip);

        ActionBar actionBar = getActionBar();
        //actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * .8),(int)(height * .39));*/



    }


    @Override
    protected void onStart() {
        super.onStart();
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new ClickEvent());

        txtValidCode = (EditText) findViewById(R.id.txtTripNo);
        txtValidCode.setGravity(Gravity.CENTER);
        txtValidCode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
    }



    class ClickEvent implements View.OnClickListener
    {
        public void onClick(View view) {
            if(view == btnSave)
            {
                try
                {
                    DatabaseHelper db = new DatabaseHelper(ctx);
                    Cursor crTripNo = db.getTripRoute(db);
                    crTripNo.moveToFirst();
                    int tripno = Integer.parseInt(crTripNo.getString(0))+1;
                    Cursor cr = db.getValidationKeys(db, String.valueOf(tripno));
                    if(txtValidCode.getText().toString().isEmpty())
                    {
                        Toast.makeText(NewTripActivity.this, "Please input the valid keys.", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        if(cr.getCount()>0)
                        {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Yes button clicked
                                            selectRouteCode();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewTripActivity.this);
                            builder.setTitle("Metro Bus Ticketing");
                            builder.setMessage("Are you sure you want activate new trip?")
                                    .setPositiveButton("Yes", dialogClickListener)
                                    .setCancelable(false)
                                    .setNegativeButton("No", dialogClickListener).show();

                        }else{
                            Toast.makeText(NewTripActivity.this, "Validation keys not Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cr.close();
                    crTripNo.close();
                    db.closeDB();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertTrips(String tripno, String routecode)
    {
        GlobalVariable rc = GlobalVariable.getInstance();
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getKMPostRouteCode(db, routecode, rc.getBustype());
        Cursor cr1 = db.getTicketInfo(db);
        Cursor cr2 = db.getTripRoute(db);
        cr.moveToFirst();
        cr1.moveToFirst();
        try
        {
            String routefn = cr.getString(0);
            String routetn = cr.getString(1);
            db.insertTripRoute(db, tripno, routecode, routefn, routetn, cr1.getString(0));
            cr2.moveToLast();
            rc.setRoutecode(routecode);
            rc.setRouteticketno(cr2.getString(4));
            txtValidCode.setText("");
            Toast.makeText(NewTripActivity.this, "New trip activated.", Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
          e.printStackTrace();
        }finally {
            cr.close();
            cr1.close();
            cr2.close();
            db.closeDB();
            db.closeDB();
            finish();
        }
    }

    private void selectRouteCode()
    {
        GlobalVariable rc = GlobalVariable.getInstance();
        String routecode = rc.getRoutecode();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cr = databaseHelper.RouteCode(databaseHelper, routecode, rc.getBustype());
        cr.moveToFirst();
        String val = cr.getString(7);

        try
        {
            final CharSequence[] items=val.split(",");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Route Code");
            builder.setCancelable(false);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {


                    String routecode = items[i].toString();
                    insertTrips(tripnumber, routecode);
                }
            });


            AlertDialog alert = builder.create();
            alert.show();

       }catch (Exception e) {
            e.printStackTrace();
       }finally {
            databaseHelper.closeDB();
            cr.close();
        }


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
