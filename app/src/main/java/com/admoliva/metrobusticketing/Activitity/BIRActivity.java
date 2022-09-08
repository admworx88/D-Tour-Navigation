package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

/**
 * Created by jUniX on 1/17/2016.
 */
public class BIRActivity extends Activity {

    Button btnSave;
    EditText EditTextTin;
    Boolean isLock = true;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bir);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);


        btnSave = (Button) findViewById(R.id.ButtonInsertTin);
        EditTextTin = (EditText)findViewById(R.id.TextViewTinNumber);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditTextTin.getText().toString().isEmpty()){
                    Toast.makeText(BIRActivity.this, "Please input TIN.", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseHelper db = new DatabaseHelper(ctx);
                    Cursor cr = db.getBIR(db);
                    try{
                        if(cr.getCount() == 0){
                            popUpMessage1();
                        }else{
                            popUpMessage2();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        cr.close();
                        db.closeDB();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBIR();
    }

    private void getBIR(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getBIR(db);
        try{
            if(cr.getCount() != 0){
                cr.moveToFirst();
                EditTextTin.setText(cr.getString(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }
    }
    private void popUpMessage1(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        insertBIR();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(BIRActivity.this);
        builder.setTitle("Metro Bus Ticketing");
        builder.setMessage("Are you sure you want to add new TIN?")
                .setPositiveButton("Yes", dialogClickListener)
                .setCancelable(false)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void popUpMessage2(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                       updateBIR();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(BIRActivity.this);
        builder.setTitle("Metro Bus Ticketing");
        builder.setMessage("Are you sure you want to update existing TIN?")
                .setPositiveButton("Yes", dialogClickListener)
                .setCancelable(false)
                .setNegativeButton("No", dialogClickListener).show();
    }
    private void insertBIR(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        try{
            db.insertBIR(db, EditTextTin.getText().toString());
            Toast.makeText(BIRActivity.this, "TIN has been added.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.closeDB();
        }
    }
    private void updateBIR(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        try{
            db.updateBIR(db, EditTextTin.getText().toString());
            Toast.makeText(BIRActivity.this, "TIN has been updated.", Toast.LENGTH_SHORT).show();
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
