package com.admoliva.metrobusticketing.Service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by jUniX on 11/1/2015.
 */
public class BluetoothResultReceiver extends ResultReceiver {

    private Receiver mReceiver;
    public BluetoothResultReceiver(Handler handler) {
        super(handler);
    }
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);

    }
    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
       if(mReceiver != null)
       {
           mReceiver.onReceiveResult(resultCode, resultData);
       }
    }
}
