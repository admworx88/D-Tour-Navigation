package com.admoliva.metrobusticketing.Service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

/**
 * Created by jUniX on 11/1/2015.
 */
public class BluetoothConnectionService extends Service{

    public BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private final IBinder mBinder = new LocalBinder();
    public int flag;

    @Override
    public IBinder onBind(Intent intent) {
            return mBinder;
    }
    public class LocalBinder extends Binder {
        public BluetoothConnectionService getService() {
            return BluetoothConnectionService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mService = new BluetoothService(this, mHandler);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connectBT();
        return START_STICKY;
    }

    @Override
    public boolean stopService(Intent name) {
        stopService();
        return super.stopService(name);
    }

    public void stopService() {
        if (mService != null){
            mService.stop();
        }
        mService = null;
        stopSelf();
        flag = 0;
        Log.d("Service Stop", "BT Destroy Connection");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
        Log.d("Service Destroy","BT Destroy Connection");
    }
    public void connectBT()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cr = dbHelper.getDevice(dbHelper);
        try
        {
            if(cr.getCount() == 0)
            {
                Toast.makeText(BluetoothConnectionService.this, "No Bluetooth Device Found!", Toast.LENGTH_SHORT).show();
                stopService();
            }else
            {
                if(mService != null){
                    try {
                        cr.moveToFirst();
                        String macAddress = cr.getString(0);
                        con_dev = mService.getDevByMac(macAddress);
                        mService.connect(con_dev);
                        flag = 1;
                    }catch (Exception e)
                    {
                        Log.d("",e.toString());
                    }finally {
                        dbHelper.closeDB();
                        cr.close();
                    }
                }else
                {

                    flag = 0;
                    stopSelf();
                    Toast.makeText(BluetoothConnectionService.this, "Bluetooth Service Stop!", Toast.LENGTH_SHORT).show();
                    mService.stop();
                    cr.close();
                    dbHelper.closeDB();
                }
            }


        }catch (Exception e)
        {
            Toast.makeText(BluetoothConnectionService.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void printing(String msg)
    {
        String lang = getString(R.string.strLang);
        byte[] arrayOfByte1 = { 27, 33, 0 };
        byte[] cmd = { 27, 33, 0 };
        //cmd[0] = 0x1b;
        //cmd[1] = 0x21;
        cmd[2] |= ((byte)(0x1 | arrayOfByte1[2]));
        if ((lang.compareTo("en")) == 0) {
            mService.write(cmd);
            mService.sendMessage("DAVAO METRO SHUTTLE CORPORATION", "GBK");
            //cmd[2] &= 0xEF;
            mService.write(cmd);
            mService.sendMessage(msg, "gbk");
        }
    }


    public void samplePrint() {
        String msg = "";
        String lang = getString(R.string.strLang);
        //printImage();
        byte[] arrayOfByte1 = { 27, 33, 0 };
        byte[] cmd = { 27, 33, 0 };//new byte[3];
        cmd[2] = ((byte)(0x1 | arrayOfByte1[2]));//0x1b;
        //cmd[1] = 0x21;
        if ((lang.compareTo("en")) == 0) {
            //cmd[2] |= 0x10;
           // printImage();
            mService.write(cmd);
            mService.sendMessage("Congratulations!\n", "GBK");
            //cmd[2] &= 0xEF;
            mService.write(cmd);
            msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
                    + "  the company is a high-tech enterprise which specializes" +
                    " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";
            mService.sendMessage(msg, "GBK");
        }
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
                            Log.d("Bluetooth Connection", "Connect Successfully");
                            flag = 1;
                            break;
                        case BluetoothService.STATE_CONNECTING:  //��������
                            Log.d("��������", "��������.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //�������ӵĵ���
                        case BluetoothService.STATE_NONE:
                            Log.d("��������", "�ȴ�����.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    flag = 0;
                    //connectBT();
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    flag = 2;
                    break;
            }
        }

    };
}
