package com.admoliva.metrobusticketing.Activitity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Aljon Moliva on 8/3/2015.
 */
public class LoadDataActivity extends Activity {

    Context ctx = this;
    Button btnRead, btnBack;
    TextView txtCount, txtCount2, txtCount3, txtCount4, txtCount5, txtCount6, txtCount7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector);


        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnRead = (Button)findViewById(R.id.buttonRead);
        txtCount = (TextView)findViewById(R.id.textViewCount1);
        txtCount2 = (TextView)findViewById(R.id.textViewCount2);
        txtCount3 = (TextView)findViewById(R.id.textViewCount3);
        txtCount4 = (TextView)findViewById(R.id.textViewCount4);
        txtCount5 = (TextView)findViewById(R.id.textViewCount5);
        txtCount6 = (TextView)findViewById(R.id.textViewCount6);
        txtCount7 = (TextView)findViewById(R.id.textViewCount7);

        readInspector();
        readAnomaly();
        readValidationCode();
        readTickets();
        readProgramUser();
        readFare();
        readRoutes();
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName1 = "mbts/inspector.txt";
                String fileName2 = "mbts/anomalydata.txt";
                String fileName3 = "mbts/validationcode.txt";
                String fileName4 = "mbts/tickets.txt";
                String fileName5 = "mbts/programuser.txt";
                String fileName6 = "mbts/farematrix.txt";
                String fileName7 = "mbts/routes.txt";

                readTextFile(fileName1);
                readTextFile2(fileName2);
                readTextFile3(fileName3);
                readTextFile4(fileName4);
                readTextFile5(fileName5);
                readTextFile6(fileName6);
                readTextFile7(fileName7);
            }
        });
    }
    void readTextFile(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();

            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbtickets.closeDB();
        }
            readInspector();
    }
    void readTextFile2(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();

            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbtickets.closeDB();
        }
        readAnomaly();
    }
    void readTextFile3(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();

            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbtickets.closeDB();
            readValidationCode();
        }

    }
    void readTextFile4(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();

            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbtickets.closeDB();
            readTickets();
        }
    }
    void readTextFile5(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();

            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbtickets.closeDB();
            readProgramUser();
        }
    }
    void readTextFile6(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();

            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbtickets.closeDB();
            readFare();
        }
    }
    void readTextFile7(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();

            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbtickets.closeDB();
            readRoutes();
        }
    }
    void readInspector()
    {
        DatabaseHelper readIns = new DatabaseHelper(ctx);
        Cursor cr = readIns.getInspectorData(readIns);
        try
        {

            txtCount.setText(""+cr.getCount());
            /*if(cr.getCount() == 0)
            {
                btnRead.setEnabled(true);
                btnRead.setBackgroundResource(R.drawable.button_selector_2);
            }else
            { btnRead.setEnabled(false);
                btnRead.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));}*/
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            cr.close();
            readIns.closeDB();
        }
    }
    void readAnomaly()
    {
        DatabaseHelper readAnomaly = new DatabaseHelper(ctx);
        Cursor cr = readAnomaly.getAnomalyData(readAnomaly);
        try
        {

            txtCount2.setText(""+cr.getCount());
           /* if(cr.getCount() == 0)
            {
                btnRead.setEnabled(true);
                btnRead.setBackgroundResource(R.drawable.button_selector_2);

            }else
            {

                btnRead.setEnabled(false);
                btnRead.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));


            }*/
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            cr.close();
            readAnomaly.closeDB();
        }
    }
    void readValidationCode()
    {
        DatabaseHelper dbReadValidation = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = dbReadValidation.readValidationKeys(dbReadValidation);
            txtCount3.setText(""+cr.getCount());
            /*if(cr.getCount() == 0)
            {
                btnRead.setEnabled(true);
                btnRead.setBackgroundResource(R.drawable.button_selector_2);
            }else
            {

                btnRead.setEnabled(false);
                btnRead.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));


            }*/
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            dbReadValidation.closeDB();
        }
    }
    void readTickets()
    {
        DatabaseHelper readTickets = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = readTickets.getTicketInfo(readTickets);
            txtCount4.setText(""+cr.getCount());
            /*if(cr.getCount() == 0)
            {
                btnRead.setEnabled(true);
                btnRead.setBackgroundResource(R.drawable.button_selector_2);
            }else
            {

                btnRead.setEnabled(false);
                btnRead.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));


            }*/
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            readTickets.closeDB();
        }
    }
    void readProgramUser()
    {
        DatabaseHelper readUser = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = readUser.getProgramUser(readUser);
            txtCount5.setText(""+cr.getCount());
            /*if(cr.getCount() == 0)
            {
                btnRead.setEnabled(true);
                btnRead.setBackgroundResource(R.drawable.button_selector_2);
            }else
            {

                btnRead.setEnabled(false);
                btnRead.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));


            }*/
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            readUser.closeDB();
        }
    }
    void readFare()
    {
        DatabaseHelper readFare = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = readFare.getFareMatrics(readFare);
            txtCount6.setText(""+cr.getCount());

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            readFare.closeDB();
        }
    }
    void readRoutes()
    {
        DatabaseHelper readR = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = readR.getRoutes(readR);
            txtCount7.setText(""+cr.getCount());
            if(cr.getCount() == 0)
            {
                btnRead.setEnabled(true);
                btnRead.setBackgroundResource(R.drawable.button_selector_2);
            }else
            {

                btnRead.setEnabled(false);
                btnRead.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));


            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(LoadDataActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            readR.closeDB();
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
}
