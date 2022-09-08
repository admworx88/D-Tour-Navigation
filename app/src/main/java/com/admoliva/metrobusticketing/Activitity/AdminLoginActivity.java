package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.*;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aljon Moliva on 7/12/2015.
 */
public class AdminLoginActivity extends Activity {


    Button LoginButton, Back;
    EditText txtusername, txtpassword;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    Context ctx =this;
    Boolean isLock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);

        LoginButton = findViewById(R.id.LoginButtonAdmin);
        Back = (Button) findViewById(R.id.button6);
        txtusername = (EditText)findViewById(R.id.UsernameAdmin);
        txtpassword = (EditText)findViewById(R.id.PasswordAdmin);
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
                loginAdmin(txtusername.getText().toString(), txtpassword.getText().toString());
            }
        });
    }


    private void loginAdmin(String uname, String pword){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.selectAdmin(db);
        Cursor cr1 = db.getAdmin(db, uname, pword);
        try{
            if(cr.getCount() == 0){
                db.insertAdmin(db, "admin", "admin", "", "Administrator");
                startActivity(new Intent(AdminLoginActivity.this, AdminDashboard.class));
                finish();
            }else{
                if(cr1.getCount() == 1){
                    startActivity(new Intent(AdminLoginActivity.this, AdminDashboard.class));
                    finish();
                }else{
                    txtusername.setText("");
                    txtpassword.setText("");
                    Toast.makeText(AdminLoginActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e)
        {
            Log.d("Insert Admin", e.toString());
        }finally {
            cr.close();
            cr.close();
            db.closeDB();
        }

    }

    void login(){
        try
        {
            //String localhost = getResources().getResourceName(R.string.url);
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://172.20.10.7/MetroBusTicketing/logincheck.php");
            nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("idno",txtusername. getText().toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("userpw", txtpassword.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AdminLoginActivity.this, "Response from PHP : " + response, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            if(response.equalsIgnoreCase("user found"))
            {
                //GlobalVariable globalVariable = GlobalVariable.getInstance();
                //globalVariable.setFlag(1);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(AdminLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(AdminLoginActivity.this, AdminDashboard.class));
            } else {
                showAlert();
            }
        }
        catch(Exception e)
        {
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
          /*  Toast.makeText(LoginActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();*/
        }
    }
    void showAlert()
    {
        AdminLoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminLoginActivity.this);
                builder.setTitle("Login Error");
                builder.setMessage("User Not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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
