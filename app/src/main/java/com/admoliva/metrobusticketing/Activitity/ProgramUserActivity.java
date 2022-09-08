package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

/**
 * Created by jUniX on 10/8/2015.
 */
public class ProgramUserActivity extends Activity {

    Context ctx = this;
    EditText editTextConId, editTextConName, editTextDriverId, getEditTextDriverName;
    EditText editTextBusNo, editTextBusName, editTextBusType, editTextInitialRoute, editTextPassword;
    Button btnUpdate;
    Boolean isLock = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprogramuser);
        TextViewSettings();

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnUpdate = (Button) findViewById(R.id.ButtonUpdateUser);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateValidation();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        getProgramUserData();
    }


    private void getProgramUserData(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getProgramUser(db);

        try{
            cr.moveToFirst();
            editTextConId.setText(cr.getString(0));
            editTextConName.setText(cr.getString(1));
            editTextDriverId.setText(cr.getString(2));
            getEditTextDriverName.setText(cr.getString(3));
            editTextBusNo.setText(cr.getString(4));
            editTextBusName.setText(cr.getString(5));
            editTextBusType.setText(cr.getString(6));
            editTextInitialRoute.setText(cr.getString(7));
            editTextPassword.setText(cr.getString(8));
        }catch (Exception e){
            Log.d("Get Program User", e.toString());
        }finally {
            cr.close();
            db.closeDB();
        }


    }
    void updateValidation(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        updateUserData();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProgramUserActivity.this);
        builder.setTitle("Administrator!");
        builder.setMessage("Are you sure you want update program user's data?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void updateUserData(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        String conid = editTextConId.getText().toString();
        String conname = editTextConName.getText().toString();
        String drivid = editTextDriverId.getText().toString();
        String drivname = getEditTextDriverName.getText().toString();
        String bno = editTextBusNo.getText().toString();
        String bname = editTextBusName.getText().toString();
        String btype = editTextBusType.getText().toString();
        String routec = editTextInitialRoute.getText().toString();
        String password = editTextPassword.getText().toString();
        try{

            if(conid.equals("") || conname.equals("") || drivid.equals("") || drivname.equals("") ||
                    bno.equals("") || bname.equals("") || btype.equals("") || routec.equals("") || password.equals("")){
                Toast.makeText(ProgramUserActivity.this, "Update UnSuccessfull!", Toast.LENGTH_SHORT).show();
            }else {
                db.updateProgramUser(db, conid, conname, drivid, drivname, bno, bname, btype, routec, password);
                Toast.makeText(ProgramUserActivity.this, "Update Successfull!", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.closeDB();
        }


    }

    private void TextViewSettings(){
        editTextConId = (EditText) findViewById(R.id.TextViewConductorID);
        editTextConName = (EditText) findViewById(R.id.TextViewConductorName);
        editTextDriverId = (EditText) findViewById(R.id.TextViewDriverID);
        getEditTextDriverName = (EditText) findViewById(R.id.TextViewDriverName);
        editTextBusNo = (EditText) findViewById(R.id.TextViewBusNo);
        editTextBusName = (EditText) findViewById(R.id.TextViewBusName);
        editTextBusType = (EditText) findViewById(R.id.TextViewBusType);
        editTextInitialRoute = (EditText) findViewById(R.id.TextViewInitialRoute);
        editTextPassword = (EditText) findViewById(R.id.TextViewPassword);

        editTextConId.setGravity(Gravity.CENTER);
        editTextConName.setGravity(Gravity.CENTER);
        editTextDriverId.setGravity(Gravity.CENTER);
        getEditTextDriverName.setGravity(Gravity.CENTER);
        editTextBusNo.setGravity(Gravity.CENTER);
        editTextBusName.setGravity(Gravity.CENTER);
        editTextBusType.setGravity(Gravity.CENTER);
        editTextInitialRoute.setGravity(Gravity.CENTER);
        editTextPassword.setGravity(Gravity.CENTER);

        editTextConId.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editTextConName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextDriverId.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        getEditTextDriverName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextBusNo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextBusName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextBusType.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTextInitialRoute.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
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
