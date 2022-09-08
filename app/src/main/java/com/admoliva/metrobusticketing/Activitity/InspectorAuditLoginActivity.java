package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.GlobalVariable.PassTypeVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

/**
 * Created by Aljon Moliva on 7/12/2015.
 */
public class InspectorAuditLoginActivity extends Activity {


    Button LoginButton, Back;
    EditText txtusername, txtpassword;
    TextView headerText;
    Context ctx = this;
    String routefn, routetn, routecode;
    InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspectorlogin);

        LoginButton = (Button)findViewById(R.id.LoginButtonAdmin);
        Back = (Button) findViewById(R.id.button6);
        txtusername = (EditText)findViewById(R.id.UsernameInspector);
        txtpassword = (EditText)findViewById(R.id.PasswordInspector);
        headerText = (TextView)findViewById(R.id.textView8);

        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        txtusername.setTypeface(typeFace);
        txtpassword.setTypeface(typeFace);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginInspector();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        headerTextInspector();
        routeValidation();
    }

    private void routeValidation(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getTripRoute(db);
        try{
            cr.moveToLast();
            routecode = cr.getString(1);
            routefn = cr.getString(2);
            routetn = cr.getString(3);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }
    }

    void headerTextInspector()
    {
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        String flag = inspector.getInspectorFlag();
        if(flag.equals("1"))
        {
            headerText.setText("Line Inspection");
        }else if(flag.equals("2"))
        {
            headerText.setText("Cash Audit");
        }
    }

    void loginInspector()
    {
        String flag = inspector.getInspectorFlag();
        DatabaseHelper db = new DatabaseHelper(ctx);
        String uid = txtusername.getText().toString();
        String pword = txtpassword.getText().toString();
        try
        {
            if(uid.equals("") || pword.equals("")){
                Toast.makeText(InspectorAuditLoginActivity.this, "Please enter your account id", Toast.LENGTH_SHORT).show();
                txtusername.setText("");
                txtpassword.setText("");
                txtusername.requestFocus();
            }else {
                Cursor cr = db.loginInspector(db, uid, pword);
                if (cr.getCount() > 0) {
                    cr.moveToFirst();
                    if (flag.equals("1")) {
                        inspector.setInsId(cr.getString(1));
                        inspector.setInsType(cr.getString(2));
                        inspector.setInsName(cr.getString(4));
                        getTransNumber();
                        validator("Line Inspection");
                    } else if (flag.equals("2")) {
                        if (cr.getString(2).equals("TF")) {
                            inspector.setInsId(cr.getString(1));
                            inspector.setInsType(cr.getString(2));
                            inspector.setInsName(cr.getString(4));
                            getTransNumber();
                            validator("Cash Audit");
                        } else {
                            Toast.makeText(InspectorAuditLoginActivity.this, "You are not allowed to login as Cash Audit.", Toast.LENGTH_SHORT).show();
                            txtusername.setText("");
                            txtpassword.setText("");
                            txtusername.requestFocus();
                        }
                    }
                }else {
                    Toast.makeText(InspectorAuditLoginActivity.this, "Inspector not found!", Toast.LENGTH_SHORT).show();
                    txtusername.setText("");
                    txtpassword.setText("");
                    txtusername.requestFocus();
                }
                cr.close();
            }
        }catch (Exception e)
        {
            throw e;
        }
        finally {
            db.closeDB();
        }



    }

    private void getTransNumber(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getInspectorTrans(db);
        InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
        int transno = 0;
        try {
            cr.moveToLast();
            if(cr.getCount() == 0){
                inspector.setTransNo("1");
            }else{
                transno = Integer.parseInt(cr.getString(1))+1;
                inspector.setTransNo(String.valueOf(transno));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }
    }
        void validator(String title)
        {
            final EditText input = new EditText(InspectorAuditLoginActivity.this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            input.setLayoutParams(lp);


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspectorAuditLoginActivity.this);
            alertDialog.setTitle(title)
                    .setMessage("Enter Km on.")
                    .setView(input)
                            //.setIcon(R.drawable.logo)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String getVal = input.getText().toString();
                                    if (getVal.compareTo("") == 0) {
                                        Toast.makeText(InspectorAuditLoginActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                                    } else if (Integer.parseInt(routefn) < Integer.parseInt(routetn)) {
                                        if (Integer.parseInt(getVal) > Integer.parseInt(routetn)) {
                                            txtusername.setText("");
                                            txtpassword.setText("");
                                            Toast.makeText(InspectorAuditLoginActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                        } else if (Integer.parseInt(getVal) < Integer.parseInt(routefn)) {
                                            Toast.makeText(InspectorAuditLoginActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                            txtusername.setText("");
                                            txtpassword.setText("");
                                        } else {
                                            String flag = inspector.getInspectorFlag();
                                            if (flag.equals("1")) {
                                                txtusername.setText("");
                                                txtpassword.setText("");
                                                inspector.setKmon(getVal);
                                                startActivity(new Intent(InspectorAuditLoginActivity.this, LineInspectionActivity.class));
                                                InspectorAuditLoginActivity.this.finish();
                                            } else if (flag.equals("2")) {
                                                txtusername.setText("");
                                                txtpassword.setText("");
                                                inspector.setKmon(getVal);
                                                startActivity(new Intent(InspectorAuditLoginActivity.this, CashAuditActivity.class));
                                                InspectorAuditLoginActivity.this.finish();

                                            }
                                        }
                                    } else if (Integer.parseInt(routefn) > Integer.parseInt(routetn)) {
                                        if (Integer.parseInt(getVal) < Integer.parseInt(routetn)) {
                                            txtusername.setText("");
                                            txtpassword.setText("");
                                            Toast.makeText(InspectorAuditLoginActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                        } else if (Integer.parseInt(getVal) > Integer.parseInt(routefn)) {
                                            txtusername.setText("");
                                            txtpassword.setText("");
                                            Toast.makeText(InspectorAuditLoginActivity.this, "Invalid Km Zone!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String flag = inspector.getInspectorFlag();
                                            if (flag.equals("1")) {
                                                txtusername.setText("");
                                                txtpassword.setText("");
                                                inspector.setKmon(getVal);
                                                startActivity(new Intent(InspectorAuditLoginActivity.this, LineInspectionActivity.class));
                                                InspectorAuditLoginActivity.this.finish();
                                            } else if (flag.equals("2")) {
                                                txtusername.setText("");
                                                txtpassword.setText("");
                                                inspector.setKmon(getVal);
                                                startActivity(new Intent(InspectorAuditLoginActivity.this, CashAuditActivity.class));
                                                InspectorAuditLoginActivity.this.finish();


                                            }
                                        }

                                    }

                                }
                            })

                    .

                            setNegativeButton("BACK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            txtusername.setText("");
                                        txtpassword.setText("");
                                        dialog.cancel();
                                    }
                                }).show();


        }

    void showAlert()
    {
        InspectorAuditLoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(InspectorAuditLoginActivity.this);
                builder.setTitle("Login Error");
                builder.setMessage("User Not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
