package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jUniX on 10/8/2015.
 */
public class ValidationActivity extends Activity {

    Context ctx = this;
    Button btnRead, btnBack;
    TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        txtCount = (TextView)findViewById(R.id.textViewCount);

        btnRead = (Button) findViewById(R.id.buttonRead);
        btnBack = (Button) findViewById(R.id.buttonBack);
        readValidationCode();

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "mbts/validationcode.txt";
                readTextFile(fileName);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        DatabaseHelper dbtickets = new DatabaseHelper(ctx);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                dbtickets.insertDataFromTextFile(line);
                br.readLine();
                //text.append(line);
                //text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
           readValidationCode();
        }
    }

    void readValidationCode()
    {
        DatabaseHelper dbReadValidation = new DatabaseHelper(ctx);
        try
        {
            Cursor cr = dbReadValidation.readValidationKeys(dbReadValidation);
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
            Toast.makeText(ValidationActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            dbReadValidation.closeDB();
        }
    }
}
