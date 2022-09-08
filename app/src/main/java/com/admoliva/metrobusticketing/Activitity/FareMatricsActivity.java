package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
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
 * Created by jUniX on 10/12/2015.
 */
public class FareMatricsActivity extends Activity {

    Button btnRead, btnBack;
    Context ctx = this;
    TextView txtCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farematrics);

        txtCount = (TextView)findViewById(R.id.textViewCount);

        btnRead = (Button)findViewById(R.id.buttonRead);
        btnBack = (Button) findViewById(R.id.buttonBack);
        readFare();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "mbts/farematrics.txt";
                readTextFile(filename);
            }
        });

    }

    void readTextFile(String fileName)
    {
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard, fileName);

        //Read text from file
        //StringBuilder text = new StringBuilder();
        DatabaseHelper dbRoutes = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbRoutes.insertDataFromTextFile(line);
                br.readLine();
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(FareMatricsActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {

            dbRoutes.closeDB();
            readFare();
        }

    }

    void readFare()
    {
        DatabaseHelper readFare = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = readFare.getFareMatrics(readFare);
            txtCount.setText(""+cr.getCount());
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
            Toast.makeText(FareMatricsActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            readFare.closeDB();
        }
    }
}
