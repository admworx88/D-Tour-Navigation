package com.admoliva.metrobusticketing.Dropbox;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by jUniX on 12/3/2015.
 */
public class UploadFile extends AsyncTask<Void, Void, Boolean> {

    Context context;
    DropboxAPI dropboxAPI;
    String path, name, date;

    public UploadFile(Context context, DropboxAPI dropboxAPI, String path){
        this.context = context;
        this.dropboxAPI = dropboxAPI;
        this.path = path;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        DatabaseHelper db = new DatabaseHelper(this.context);
        String csvHeader = "";
        String csvValues = "";

        String csvHeader1 = "";
        String csvValues1 = "";

        final File tempDropboxDirectory = context.getCacheDir();
        File tempFileToUploadToDropbox;
        FileWriter fileWriter = null;

        final File tempDropboxDirectory1 = context.getCacheDir();
        File tempFileToUploadToDropbox1;
        FileWriter fileWriter1 = null;

        try {
            // Creating a temporal file.
            tempFileToUploadToDropbox = File.createTempFile("file", ".csv", tempDropboxDirectory);
            fileWriter = new FileWriter(tempFileToUploadToDropbox);

            tempFileToUploadToDropbox1 = File.createTempFile("file1", ".csv", tempDropboxDirectory1);
            fileWriter1 = new FileWriter(tempFileToUploadToDropbox1);

            Cursor curCSV = db.getAllTransData(db);
            csvHeader = curCSV.getColumnName(0)+ ",";
            csvHeader += curCSV.getColumnName(1)+ ",";
            csvHeader += curCSV.getColumnName(2)+ ",";
            csvHeader += curCSV.getColumnName(3)+ ",";
            csvHeader += curCSV.getColumnName(4)+ ",";
            csvHeader += curCSV.getColumnName(5)+ ",";
            csvHeader += curCSV.getColumnName(6)+ ",";
            csvHeader += curCSV.getColumnName(7)+ ",";
            csvHeader += curCSV.getColumnName(8)+ ",";
            csvHeader += curCSV.getColumnName(9)+ ",";
            csvHeader += curCSV.getColumnName(10)+ ",";
            csvHeader += curCSV.getColumnName(11)+ ",";
            csvHeader += curCSV.getColumnName(12)+ ",";
            csvHeader += curCSV.getColumnName(13)+ ",";
            csvHeader += curCSV.getColumnName(14)+ ",";
            csvHeader += curCSV.getColumnName(15)+ ",";
            csvHeader += curCSV.getColumnName(16)+ ",";
            csvHeader += curCSV.getColumnName(17)+ ",";
            csvHeader += curCSV.getColumnName(18)+ ",";
            csvHeader += curCSV.getColumnName(19)+ ",";
            csvHeader += "\n";
            fileWriter.write(csvHeader);
            curCSV.moveToFirst();
            do
            {
                //Which column you want to exprort
                csvValues = curCSV.getString(0) + ",";
                csvValues += curCSV.getString(1) + ",";
                csvValues += curCSV.getString(2) + ",";
                csvValues += curCSV.getString(3) + ",";
                csvValues += curCSV.getString(4) + ",";
                csvValues += curCSV.getString(5) + ",";
                csvValues += curCSV.getString(6) + ",";
                csvValues += curCSV.getString(7) + ",";
                csvValues += curCSV.getString(8) + ",";
                csvValues += curCSV.getString(9) + ",";
                csvValues += curCSV.getString(10) + ",";
                csvValues += curCSV.getString(11) + ",";
                csvValues += curCSV.getString(12) + ",";
                csvValues += curCSV.getString(13) + ",";
                csvValues += curCSV.getString(14) + ",";
                csvValues += curCSV.getString(15) + ",";
                csvValues += curCSV.getString(16) + ",";
                csvValues += curCSV.getString(17) + ",";
                csvValues += curCSV.getString(18) + ",";
                csvValues += curCSV.getString(19) + ",";
                csvValues += "\n";
                fileWriter.write(csvValues);
            } while(curCSV.moveToNext());
            fileWriter.close();

            Cursor curCSV1 = db.getInspectorTrans(db);
            if(curCSV1.getCount() != 0){
                csvHeader1 = curCSV1.getColumnName(0)+ ",";
                csvHeader1 += curCSV1.getColumnName(1)+ ",";
                csvHeader1 += curCSV1.getColumnName(2)+ ",";
                csvHeader1 += curCSV1.getColumnName(3)+ ",";
                csvHeader1 += curCSV1.getColumnName(4)+ ",";
                csvHeader1 += curCSV1.getColumnName(5)+ ",";
                csvHeader1 += curCSV1.getColumnName(6)+ ",";
                csvHeader1 += curCSV1.getColumnName(7)+ ",";
                csvHeader1 += curCSV1.getColumnName(8)+ ",";
                csvHeader1 += curCSV1.getColumnName(9)+ ",";
                csvHeader1 += curCSV1.getColumnName(10)+ ",";
                csvHeader1 += curCSV1.getColumnName(11)+ ",";
                csvHeader1 += curCSV1.getColumnName(12)+ ",";
                csvHeader1 += curCSV1.getColumnName(13)+ ",";
                csvHeader1 += curCSV1.getColumnName(14)+ ",";
                csvHeader1 += curCSV1.getColumnName(15)+ ",";
                csvHeader1 += curCSV1.getColumnName(16)+ ",";
                csvHeader1 += curCSV1.getColumnName(17)+ ",";
                csvHeader1 += curCSV1.getColumnName(18)+ ",";
                csvHeader1 += curCSV1.getColumnName(19)+ ",";
                csvHeader1 += curCSV1.getColumnName(20) + ",";
                csvHeader1 += curCSV1.getColumnName(21) + ",";
                csvHeader1 += curCSV1.getColumnName(22) + ",";
                csvHeader1 += curCSV1.getColumnName(23) + ",";
                csvHeader1 += curCSV1.getColumnName(24) + ",";
                csvHeader1 += curCSV1.getColumnName(25) + ",";
                csvHeader1 += curCSV1.getColumnName(26) + ",";
                csvHeader1 += curCSV1.getColumnName(27) + ",";
                csvHeader1 += curCSV1.getColumnName(28) + ",";
                csvHeader1 += curCSV1.getColumnName(29) + ",";
                csvHeader1 += curCSV1.getColumnName(30) + ",";
                csvHeader1 += curCSV1.getColumnName(31) + ",";
                csvHeader1 += curCSV1.getColumnName(32) + ",";
                csvHeader1 += curCSV1.getColumnName(33) + ",";
                csvHeader1 += "\n";
                fileWriter1.write(csvHeader1);

                curCSV1.moveToFirst();
                do
                {
                    //Which column you want to exprort
                    csvValues1 = curCSV1.getString(0) + ",";
                    csvValues1 += curCSV1.getString(1) + ",";
                    csvValues1 += curCSV1.getString(2) + ",";
                    csvValues1 += curCSV1.getString(3) + ",";
                    csvValues1 += curCSV1.getString(4) + ",";
                    csvValues1 += curCSV1.getString(5) + ",";
                    csvValues1 += curCSV1.getString(6) + ",";
                    csvValues1 += curCSV1.getString(7) + ",";
                    csvValues1 += curCSV1.getString(8) + ",";
                    csvValues1 += curCSV1.getString(9) + ",";
                    csvValues1 += curCSV1.getString(10) + ",";
                    csvValues1 += curCSV1.getString(11) + ",";
                    csvValues1 += curCSV1.getString(12) + ",";
                    csvValues1 += curCSV1.getString(13) + ",";
                    csvValues1 += curCSV1.getString(14) + ",";
                    csvValues1 += curCSV1.getString(15) + ",";
                    csvValues1 += curCSV1.getString(16) + ",";
                    csvValues1 += curCSV1.getString(17) + ",";
                    csvValues1 += curCSV1.getString(18) + ",";
                    csvValues1 += curCSV1.getString(19) + ",";
                    csvValues1 += curCSV1.getString(20) + ",";
                    csvValues1 += curCSV1.getString(21) + ",";
                    csvValues1 += curCSV1.getString(22) + ",";
                    csvValues1 += curCSV1.getString(23) + ",";
                    csvValues1 += curCSV1.getString(24) + ",";
                    csvValues1 += curCSV1.getString(25) + ",";
                    csvValues1 += curCSV1.getString(26) + ",";
                    csvValues1 += curCSV1.getString(27) + ",";
                    csvValues1 += curCSV1.getString(28) + ",";
                    csvValues1 += curCSV1.getString(29) + ",";
                    csvValues1 += curCSV1.getString(30) + ",";
                    csvValues1 += curCSV1.getString(31) + ",";
                    csvValues1 += curCSV1.getString(32) + ",";
                    csvValues1 += curCSV1.getString(33) + ",";
                    csvValues1 += "\n";
                    fileWriter1.write(csvValues1);
                } while(curCSV1.moveToNext());
                fileWriter1.close();
            }





            // Uploading the newly created file to Dropbox.
            FileInputStream fileInputStream = new FileInputStream(tempFileToUploadToDropbox);
            dropboxAPI.putFile(path + "TKT.csv", fileInputStream,
                    tempFileToUploadToDropbox.length(), null, null);
            tempFileToUploadToDropbox.delete();

            FileInputStream fileInputStream1 = new FileInputStream(tempFileToUploadToDropbox1);
            dropboxAPI.putFile(path + "IDR.csv", fileInputStream1,
                    tempFileToUploadToDropbox1.length(), null, null);
            tempFileToUploadToDropbox1.delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        return false;
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, "Data has been successfully uploaded!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "An error occured while processing the upload request." + result,
                    Toast.LENGTH_LONG).show();
        }
    }





    public static String DateToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        String formatDate = format.format(c.getTime());
        return formatDate;
    }

    public static String TimeToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String formatTime = format.format(c.getTime());
        return formatTime;
    }

    private void exportCSV1(){
        DatabaseHelper db = new DatabaseHelper(this.context);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "ticketTransaction_" + DateToday()+"_"+TimeToday() + ".csv");
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
            db.closeDB();
        }
    }
    private void exportCSV2(){
        DatabaseHelper db = new DatabaseHelper(this.context);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "inspectorTransaction_" + DateToday()+"_"+TimeToday() + ".csv");
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
            db.closeDB();
        }
    }


}
