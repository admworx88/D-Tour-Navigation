package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jUniX on 10/8/2015.
 */
public class TicketsActivity extends Activity {
    Context ctx = this;
    Button btnUpdate;
    Boolean isLock = true;
    EditText orfrom, orto, orusedfrom, orusedto, orletter, orusedletter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketor);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnUpdate = (Button)findViewById(R.id.ButtonUpdateTickets);
        orfrom = (EditText)findViewById(R.id.TextViewOrFrom);
        orto = (EditText)findViewById(R.id.TextViewOrTo);
        orusedfrom = (EditText)findViewById(R.id.TextViewUsedFrom);
        orusedto = (EditText)findViewById(R.id.TextViewUsedTo);
        orletter = (EditText)findViewById(R.id.TextViewORLetter);
        orusedletter = (EditText)findViewById(R.id.TextViewOrUsedLetter);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                updateTickets();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(TicketsActivity.this);
                builder.setTitle("Update Ticket");
                builder.setMessage("Are you sure you want to update tickets info?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setCancelable(false)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTickets();
    }

    private void getTickets(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr= db.getAllTicket(db);
        try{
            cr.moveToFirst();
            orfrom.setText(cr.getString(1));
            orto.setText(cr.getString(2));
            orusedfrom.setText(cr.getString(3));
            orusedto.setText(cr.getString(4));
            orletter.setText(cr.getString(5));
            orusedletter.setText(cr.getString(6));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }

    }
    private void updateTickets(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        try{
            db.updateTickets(db, Integer.parseInt(orfrom.getText().toString()), Integer.parseInt(orto.getText().toString()), Integer.parseInt(orusedfrom.getText().toString()), Integer.parseInt(orusedto.getText().toString()), orletter.getText().toString(), orusedletter.getText().toString());
            Toast.makeText(TicketsActivity.this, "Ticket Info has been updated.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.closeDB();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(TicketsActivity.this);
                builder.setTitle("Update Ticket");
                builder.setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setCancelable(false)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
