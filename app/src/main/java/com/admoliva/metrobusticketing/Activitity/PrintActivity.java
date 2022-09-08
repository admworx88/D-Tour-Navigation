package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.Bluetooth;
import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.TicketGlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.zj.btsdk.BluetoothService;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Aljon Moliva on 8/7/2015.
 */
public class PrintActivity extends Activity {
    Context ctx = this;
    int amount = 0;
    String paxtype = "";
    String tcktlet = "";
    int tckt;
    LinearLayout printLinearLayout;
    Boolean isLock = true;
    Bitmap map;

    TextView ticketOR, dates;
    TextView trpNo, trpCode, busNo, busDriver, busCon;
    TextView txtFrom, txtTo, txtType, txtAmount;


    Button print;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    //Initialization of Global Variable

    GlobalVariable conid = GlobalVariable.getInstance();
    GlobalVariable drivid = GlobalVariable.getInstance();
    GlobalVariable busno = GlobalVariable.getInstance();

    TicketGlobalVariable kilometrFrom = TicketGlobalVariable.getInstance();
    TicketGlobalVariable kilometrFromDesc = TicketGlobalVariable.getInstance();
    TicketGlobalVariable kilometrToDesc = TicketGlobalVariable.getInstance();
    TicketGlobalVariable kilometrTo = TicketGlobalVariable.getInstance();
    TicketGlobalVariable tripCode = TicketGlobalVariable.getInstance();
    TicketGlobalVariable tripNo = TicketGlobalVariable.getInstance();

    //Bluetooth Global Variable
    Bluetooth macAdd = Bluetooth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printticket);
        mService = new BluetoothService(this, mHandler);

        getActionBar().setDisplayHomeAsUpEnabled(true);





    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(!mService.isAvailable()){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();

        }*/
        //connectBT();
        printLinearLayout = (LinearLayout)findViewById(R.id.printThis);
        //Button Initialization

        print = (Button)this.findViewById(R.id.printTicket);
        print.setOnClickListener(new ClickEvent());
        print.setEnabled(false);
        print.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));

        ticketOR = (TextView)this.findViewById(R.id.txtOR);
        dates = (TextView)this.findViewById(R.id.txtDate);
        trpNo = (TextView)this.findViewById(R.id.txtTripNo);
        trpCode = (TextView)this.findViewById(R.id.txtTripCode);
        busNo = (TextView)this.findViewById(R.id.txtBus);
        busDriver = (TextView)this.findViewById(R.id.txtDriver);
        busCon = (TextView)this.findViewById(R.id.txtConductor);
        txtFrom = (TextView)this.findViewById(R.id.txtKmFrom);
        txtTo = (TextView)this.findViewById(R.id.txtKmTo);
        txtType = (TextView)this.findViewById(R.id.txtPaxType);
        txtAmount = (TextView)this.findViewById(R.id.txtAmount);

        displayPrint();

    }

    class ClickEvent implements View.OnClickListener
    {
        public void onClick(View v) {
            if(v == print)
            {
                DatabaseHelper db = new DatabaseHelper(ctx);
                Cursor cr = db.getTicketInfo(db);
                cr.moveToFirst();
                tckt = Integer.parseInt(cr.getString(0));
                tcktlet = cr.getString(1);
                try {

                    printLinearLayout.setDrawingCacheEnabled(true);
                    printLinearLayout.buildDrawingCache();
                    map = printLinearLayout.getDrawingCache();
                    printing();
                    //insertTicketTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    db.updateTicketInfo(db, tckt + 1);
                    finish();
                }
            }

        }
    }

    private void connectBT()
    {
        String macAddress = macAdd.getAddress();
        mService.start();
        con_dev = mService.getDevByMac(macAddress);
        mService.connect(con_dev);
        Log.d("Bluetooth Connection", "Connect Successfully");
        //Toast.makeText(PrintActivity.this, "Connect successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mService != null)
        {
            mService.stop();
        }
        mService = null;
    }

   /* private void insertTicketTransaction()
    {
        DatabaseHelper ticketTrans = new DatabaseHelper(ctx);
        ticketTrans.putTicketTransaction(ticketTrans, tripNo.getTripNo(), kilometrFrom.getKilometerFrom(),
                kilometrTo.getKilometerTo(), tripCode.getTripCode(), DateToday()
                , TimeToday(), tckt, tcktlet, busno.getBusno(), drivid.getDrivid(), conid.getConid(), kilometrFrom.getKilometerFrom(),
                kilometrFromDesc.getKilometerFromDesc(), kilometrTo.getKilometerTo(),
                kilometrToDesc.getKilometerTo(), paxtype, amount);
        ticketTrans.putTicketTransaction(ticketTrans, );
        DatabaseHelper ticketNo = new DatabaseHelper(ctx);
        ticketNo.insertTicketNoTrans(ticketNo, tckt, tcktlet);
    }*/

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

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //������
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            print.setEnabled(true);
                            print.setBackgroundResource(R.drawable.button_selector);
                            break;
                        case BluetoothService.STATE_CONNECTING:  //��������
                            Log.d("��������", "��������.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //�������ӵĵ���
                        case BluetoothService.STATE_NONE:
                            Log.d("��������","�ȴ�����.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    print.setEnabled(false);
                    print.setBackgroundColor(getResources().getColor(R.color.counter_text_bg));
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    /*private void printImage() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root +  "/mnt/sdcard/print.jpg");
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(0, 0, myDir.toString());
        sendData = pg.printDraw();
        mService.write(sendData);   //��ӡbyte������
        mService.stop();
        finish();
    }*/



    private void SaveImage(Bitmap finalBitmap) {

        TicketGlobalVariable ticketOR = TicketGlobalVariable.getInstance();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/mnt/sdcard/");
        myDir.mkdirs();
        // Random generator = new Random();
        /*int n = 10000;*/
        /*n = generator.nextInt(n);*/
        String n = ticketOR.getTicketOR();
        String fname = "ticket"+ n +".jpg";
        //String fname = "print.jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void printing()
    {
        GlobalVariable BUSNO = GlobalVariable.getInstance();
        GlobalVariable BUSNAME = GlobalVariable.getInstance();
        GlobalVariable CONDUCTOR = GlobalVariable.getInstance();
        GlobalVariable CONDUCTORID = GlobalVariable.getInstance();
        GlobalVariable DRIVER = GlobalVariable.getInstance();
        GlobalVariable DRIVERID = GlobalVariable.getInstance();

        TicketGlobalVariable kilometerFrom = TicketGlobalVariable.getInstance();
        TicketGlobalVariable kilometerTo = TicketGlobalVariable.getInstance();
        TicketGlobalVariable TripNo = TicketGlobalVariable.getInstance();
        TicketGlobalVariable TripCode = TicketGlobalVariable.getInstance();
        TicketGlobalVariable PaxType = TicketGlobalVariable.getInstance();
        TicketGlobalVariable ticketOR = TicketGlobalVariable.getInstance();
        TicketGlobalVariable amnt = TicketGlobalVariable.getInstance();


        String msg = "";
        String lang = getString(R.string.strLang);

        byte[] cmd = new byte[3];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        if ((lang.compareTo("en")) == 0) {
            cmd[2] |= 0x10;
            mService.write(cmd);
            mService.sendMessage("DAVAO METRO SHUTTLE CORPORATION", "GBK");
            cmd[2] &= 0xEF;
            mService.write(cmd);
                 msg = "--------------------------------\n"
                     + "| O.R : " + ticketOR.getTicketOR() + " Date : " + DateToday() + "\n"
                     + "| Bus No : " + BUSNO.getBusno() + " " + BUSNAME.getBusname() + "\n"
                     + "| Driver : " + DRIVERID.getDrivid() + " " + DRIVER.getDrivername() + "\n"
                     + "| Conductor : " + CONDUCTORID.getConid() + " " + CONDUCTOR.getConductorname() + "\n"
                     + "| Trip No : " + TripNo.getTripNo() + " Trip Code : "+ TripCode.getTripCode() + "\n"
                     + "| KM From : " + kilometerFrom.getKilometerFrom() + " " + kilometerFrom.getKilometerFromDesc() + "\n"
                     + "| KM To : " + kilometerTo.getKilometerTo() + " " + kilometerTo.getKilometerToDesc() + "\n"
                     + "| Passenger : " + PaxType.getPassType() + " Amount : " + amnt.getAmount()  + "\n"
                     + "--------------------------------\n";
           /* msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
                    + "  the company is a high-tech enterprise which specializes" +
                    " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";*/
            mService.sendMessage(msg, "GBK");

        }
        SaveImage(map);
    }

    void displayPrint()
    {
        GlobalVariable BUSNO = GlobalVariable.getInstance();
        GlobalVariable BUSNAME = GlobalVariable.getInstance();
        GlobalVariable CONDUCTOR = GlobalVariable.getInstance();
        GlobalVariable CONDUCTORID = GlobalVariable.getInstance();
        GlobalVariable DRIVER = GlobalVariable.getInstance();
        GlobalVariable DRIVERID = GlobalVariable.getInstance();

        TicketGlobalVariable kilometerFrom = TicketGlobalVariable.getInstance();
        TicketGlobalVariable kilometerTo = TicketGlobalVariable.getInstance();
        TicketGlobalVariable TripNo = TicketGlobalVariable.getInstance();
        TicketGlobalVariable TripCode = TicketGlobalVariable.getInstance();
        TicketGlobalVariable PaxType = TicketGlobalVariable.getInstance();
        TicketGlobalVariable tcktOR = TicketGlobalVariable.getInstance();
        TicketGlobalVariable amnt = TicketGlobalVariable.getInstance();


        ticketOR.setText("O.R : " + tcktOR.getTicketOR());
        dates.setText("Date : " + DateToday());
        trpNo.setText(TripNo.getTripNo());
        trpCode.setText(TripCode.getTripCode());
        busNo.setText(BUSNO.getBusno() + "   " + BUSNAME.getBusname());
        busDriver.setText(DRIVERID.getDrivid() + "   " + DRIVER.getDrivername());
        busCon.setText(CONDUCTORID.getConid() + "   " + CONDUCTOR.getConductorname());
        txtFrom.setText(kilometerFrom.getKilometerFrom() + "   " + kilometerFrom.getKilometerFromDesc());
        txtTo.setText(kilometerTo.getKilometerTo() + "   " + kilometerFrom.getKilometerToDesc());
        txtType.setText(PaxType.getPassType());
        txtAmount.setText(amnt.getAmount());


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetoothmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
