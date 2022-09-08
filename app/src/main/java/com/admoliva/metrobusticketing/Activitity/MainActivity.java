package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.TicketGlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;


public class MainActivity extends Activity {

    int tcktFrom;
    int tcktTo;
    int tcktLeft;
    Boolean isLock = true;

    TextView busno, busname, driver, driverid, conductor, conductorid;
    TextView tckt1, tckt2;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toast.makeText(MainActivity.this, "Welcome.....", Toast.LENGTH_SHORT).show();

        GlobalVariable busDetails = GlobalVariable.getInstance();

        TicketGlobalVariable kmfrom = TicketGlobalVariable.getInstance();
        TicketGlobalVariable kmto = TicketGlobalVariable.getInstance();
        TicketGlobalVariable tripcode = TicketGlobalVariable.getInstance();
        kmfrom.setKilometerFrom("XXX");
        kmto.setKilometerTo("XXX");
        tripcode.setTripCode("XX");

        Button menus = (Button)findViewById(R.id.Menus);

        busno = (TextView)findViewById(R.id.TextViewBusNo);
        busname = (TextView)findViewById(R.id.TextViewBusName);
        driver = (TextView)findViewById(R.id.TextViewDriver);
        driverid = (TextView)findViewById(R.id.TextViewDriverID);
        conductor = (TextView)findViewById(R.id.TextViewConductor);
        conductorid = (TextView)findViewById(R.id.TextViewConductorID);
        tckt1 = (TextView)findViewById(R.id.ticketLeft);
        tckt2 = (TextView)findViewById(R.id.ticketUsed);

        busno.setText(busDetails.getBusno());
        busname.setText(busDetails.getBusname() + "  " + busDetails.getBustype());
        driver.setText(busDetails.getDrivername());
        conductor.setText(busDetails.getConductorname());
        driverid.setText(busDetails.getDrivid());
        conductorid.setText(busDetails.getConid());

        getTicketDetails();

        menus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, MenusActivity.class));
                finish();

            }
        });

    }

    private void getTicketDetails()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cr = dbHelper.getTicketInfo(dbHelper);

        try
        {
            if(cr.moveToFirst())
            {
                cr.moveToFirst();
                tcktFrom = Integer.parseInt(cr.getString(0));
                tcktTo = Integer.parseInt(cr.getString(2));
                tcktLeft = (tcktTo - (tcktFrom - 1));
                tckt1.setText(""+tcktLeft);
                tckt2.setText(""+tcktFrom);
            }
        }catch(Exception e)
        {
            throw e;
        }finally {
            cr.close();
            dbHelper.closeDB();
        }




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
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Metro Bus Ticketing");
                builder.setMessage("Are you sure you want to exit")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

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
