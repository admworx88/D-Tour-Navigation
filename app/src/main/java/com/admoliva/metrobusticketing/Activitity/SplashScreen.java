package com.admoliva.metrobusticketing.Activitity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.admoliva.metrobusticketing.R;

/**
 * Created by Aljon Moliva on 6/29/2015.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


            Thread background = new Thread(){
                public void run(){
                    try{
                        sleep(3000);
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                    }catch (Exception e)
                    {
                        Log.d("Splash Screen", e.toString());
                    }
                    finally {
                        finish();
                    }
                }


            };
        background.start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
