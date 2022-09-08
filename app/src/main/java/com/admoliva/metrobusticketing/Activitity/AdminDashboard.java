package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

/**
 * Created by Aljon Moliva on 7/12/2015.
 */
public class AdminDashboard extends Activity {

    Context ctx = this;

    Button btnLoadAll;
    Button btnSync, btnDelete, btnUtilities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnSync = (Button) findViewById(R.id.ButtonSync);
        btnSync.setOnClickListener(new ClickEvent());

        btnLoadAll = (Button)findViewById(R.id.ButtonLoadAll);
        btnLoadAll.setOnClickListener(new ClickEvent());

        btnDelete = (Button) findViewById(R.id.ButtonDelete);
        btnDelete.setOnClickListener(new ClickEvent());

        btnUtilities = (Button) findViewById(R.id.ButtonUtilities);
        btnUtilities.setOnClickListener(new ClickEvent());


        deleteEnabled();
        buttonEnabled();

    }

    class ClickEvent implements View.OnClickListener
    {

        public void onClick(View v) {
            if( v == btnLoadAll){
                startActivity(new Intent(AdminDashboard.this, LoadDataActivity.class));
            }else if( v== btnSync){
                startActivity(new Intent(AdminDashboard.this, SyncToDBActivity.class));
            }else if( v== btnDelete){
               deleteTransaction();
            }else if( v== btnUtilities){
                startActivity(new Intent(AdminDashboard.this, UtilitiesActivity.class));
            }

        }
    }
    private void deleteEnabled(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getAllTransData(db);
        try{
            if(cr.getCount() == 0){
                btnDelete.setEnabled(false);
                btnDelete.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
            }else{
                btnDelete.setEnabled(true);
                btnDelete.setBackgroundResource(R.drawable.button_selector_2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }

    }

    void deleteTransaction(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        approval();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
        builder.setTitle("Administrator!");
        builder.setMessage("Are you sure you want delete all transactions?")
                .setPositiveButton("Yes", dialogClickListener)
                .setCancelable(false)
                .setNegativeButton("No", dialogClickListener).show();

    }

    void buttonEnabled(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getAllTransData(db);

        try{
            if(cr.getCount() == 0){
                btnSync.setEnabled(false);
                btnSync.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
            }else{
                btnSync.setEnabled(true);
                btnSync.setBackgroundResource(R.drawable.button_selector_2);

            }
        }catch (Exception e){
            Log.d("Admin Dashboard", e.toString());
        }finally {
            cr.close();
            db.closeDB();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admindashboard, menu);
        return true;
    }

    private void approval(){
        final EditText input = new EditText(AdminDashboard.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDashboard.this);
        alertDialog.setTitle("Admin Approver")
                .setMessage("Enter you password")
                .setView(input)
                        //.setIcon(R.drawable.logo)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String getVal = input.getText().toString();
                                if (getVal.isEmpty()) {
                                    Toast.makeText(AdminDashboard.this, "Please input approvers password", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseHelper db = new DatabaseHelper(ctx);
                                    Cursor cr = db.getAdminApproval(db, getVal);
                                    if (cr.getCount() == 0) {
                                        Toast.makeText(AdminDashboard.this, "Approver not found!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Cursor cr1 = db.getInspectorTrans(db);
                                        try {
                                            if (cr1.getCount() == 0) {
                                                db.deleteTicketTransaction(db);
                                                db.delAdjustTicketInfo(db);
                                            } else {
                                                db.deleteTicketTransaction(db);
                                                db.deleteInspectorTransaction(db);
                                                db.delAdjustTicketInfo(db);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            cr.close();
                                            cr1.close();
                                            db.closeDB();
                                            deleteEnabled();
                                        }

                                        Toast.makeText(AdminDashboard.this, "Delete Transaction Data Successfully!", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                        })

                .

                        setNegativeButton("BACK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboard.this);
            builder.setTitle("Administrator!");
            builder.setMessage("Are you sure you want to exit")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();


        }

        return super.onOptionsItemSelected(item);
    }


}
