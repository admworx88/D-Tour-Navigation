package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.Dropbox.UploadFile;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.google.android.gms.wearable.DataApi;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by jUniX on 11/30/2015.
 */
public class SyncToDBActivity extends Activity implements View.OnClickListener {

    Context ctx = this;
    Button login, sync, back;
    LinearLayout container;

    private DropboxAPI dropboxApi;
    private boolean isUserLoggedIn;

    private final static String DROPBOX_FILE_DIR = "/BusTicketingGeneratedReports/";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String ACCESS_KEY = "olkebzndq4rm05n"; //q93e9gvjuuq2g7h
    private final static String ACCESS_SECRET = "sy5ol5nrli5zoxd"; //9v29vd2owa9c77n
    private final static AccessType ACCESS_TYPE = AccessType.DROPBOX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synctodb);

        sync = (Button) findViewById(R.id.ButtonSync);
        sync.setOnClickListener(this);
        back = (Button) findViewById(R.id.ButtonBack);
        back.setOnClickListener(this);
        login = (Button) findViewById(R.id.Connect);
        login.setOnClickListener(this);

        AppKeyPair appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        AndroidAuthSession session;


        loggedIn(false);
        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME,0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if(key != null && secret != null){
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, token);
        }else{
            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }
        dropboxApi = new DropboxAPI(session);
    }


    @Override
    protected void onResume() {
        super.onResume();

        AndroidAuthSession session = (AndroidAuthSession)dropboxApi.getSession();

        if(session.authenticationSuccessful()){
            try{
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(ACCESS_KEY, tokens.key);
                editor.putString(ACCESS_SECRET, tokens.secret);
                editor.commit();
                loggedIn(true);

            }catch (IllegalStateException e){
                Toast.makeText(SyncToDBActivity.this, "Error during Dropbox Auth", Toast.LENGTH_SHORT).show();
            }
        }
    }

 /*  private final Handler handler = new Handler() {
        public void handleMessage(Message message) {
            ArrayList<String> result = message.getData().getStringArrayList("data");

            for (String fileName : result) {
                TextView textView = new TextView(SyncToDBActivity.this);
                textView.setText(fileName);
                container.addView(textView);
            }
        }
    };*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Connect:
                if(isUserLoggedIn){
                    dropboxApi.getSession().unlink();
                    loggedIn(false);
                    Toast.makeText(SyncToDBActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                }else{
                    ((AndroidAuthSession) dropboxApi.getSession())
                            .startAuthentication(SyncToDBActivity.this);
                    Toast.makeText(SyncToDBActivity.this, "Connecting....", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.ButtonSync:
                DatabaseHelper db = new DatabaseHelper(ctx);
                Cursor cr = db.getProgramUser(db);
                cr.moveToFirst();
                String busname = "";
                busname = "/"+cr.getString(4)+"/";
                UploadFile uploadFile = new UploadFile(this, dropboxApi, DROPBOX_FILE_DIR+busname + cr.getString(4)+"_"+DateToday()+"_"+ TimeToday()+"_");
                uploadFile.execute();
                cr.close();
                db.closeDB();
                exportCSV1();
                exportCSV2();
                loggedIn(false);
                break;

            case R.id.ButtonBack:
                dropboxApi.getSession().unlink();
                loggedIn(false);
                Toast.makeText(SyncToDBActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
                break;
        }
    }

    public static String DateToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
        String formatDate = format.format(c.getTime());
        return formatDate;
    }

    public static String TimeToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        String formatTime = format.format(c.getTime());
        return formatTime;
    }

    private void loggedIn(Boolean userLoggedIn){
        isUserLoggedIn = userLoggedIn;

        if(!userLoggedIn){
            sync.setEnabled(userLoggedIn);
            sync.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
        }else{
            sync.setEnabled(userLoggedIn);
            sync.setBackgroundResource(R.drawable.button_selector_2);
        }


    }

    private void exportCSV1(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getProgramUser(db);
        cr.moveToFirst();
        File exportDir = new File(Environment.getExternalStorageDirectory(), "mbts/");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, cr.getString(4)+"_"+DateToday() + "_"+ TimeToday()+"_" + "TKT.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = db.getAllTransData(db);
            csvWrite.writeNext(curCSV.getColumnNames());
            curCSV.moveToFirst();
            do
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2),
                        curCSV.getString(3),curCSV.getString(4), curCSV.getString(5),curCSV.getString(6),
                        curCSV.getString(7), curCSV.getString(8), curCSV.getString(9),curCSV.getString(10), curCSV.getString(11),
                        curCSV.getString(12),curCSV.getString(13), curCSV.getString(14),curCSV.getString(15),curCSV.getString(16),
                        curCSV.getString(17),curCSV.getString(18),curCSV.getString(19)};
                csvWrite.writeNext(arrStr);
            } while(curCSV.moveToNext());
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }finally {
            cr.close();
            db.closeDB();
        }
    }
    private void exportCSV2(){
        DatabaseHelper db = new DatabaseHelper(ctx);
        Cursor cr = db.getProgramUser(db);
        cr.moveToFirst();
        File exportDir = new File(Environment.getExternalStorageDirectory(), "mbts/");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, cr.getString(4)+"_"+DateToday() + "_"+ TimeToday()+"_" + "IDR.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = db.getInspectorTrans(db);
            csvWrite.writeNext(curCSV.getColumnNames());
            curCSV.moveToFirst();
            do
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2),
                        curCSV.getString(3),curCSV.getString(4), curCSV.getString(5),curCSV.getString(6),
                        curCSV.getString(7), curCSV.getString(8), curCSV.getString(9),curCSV.getString(10), curCSV.getString(11),
                        curCSV.getString(12),curCSV.getString(13), curCSV.getString(14),curCSV.getString(15),curCSV.getString(16),
                        curCSV.getString(17), curCSV.getString(18), curCSV.getString(19),curCSV.getString(20), curCSV.getString(21),
                        curCSV.getString(22), curCSV.getString(23), curCSV.getString(24), curCSV.getString(25),curCSV.getString(26),
                        curCSV.getString(27), curCSV.getString(28),curCSV.getString(29), curCSV.getString(30),curCSV.getString(31),
                        curCSV.getString(32), curCSV.getString(33)};
                csvWrite.writeNext(arrStr);
            } while(curCSV.moveToNext());
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }finally {
            cr.close();
            db.closeDB();
        }
    }
}


