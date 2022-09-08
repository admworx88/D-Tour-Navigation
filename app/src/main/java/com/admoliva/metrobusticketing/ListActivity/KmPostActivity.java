package com.admoliva.metrobusticketing.ListActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.GlobalVariable.KmPostVariable;
import com.admoliva.metrobusticketing.GlobalVariable.PassTypeVariable;
import com.admoliva.metrobusticketing.ListViewAdapter.KmPostListViewAdapter;
import com.admoliva.metrobusticketing.Model.KmPost;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;
import com.admoliva.metrobusticketing.Service.BluetoothConnectionService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aljon Moliva on 8/6/2015.
 */
public class    KmPostActivity extends Activity {

    private ArrayList<HashMap> KmToCode;
    Boolean isLock = true;
    Context ctx = this;
    GlobalVariable gv = GlobalVariable.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kmpost_list);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);
        final ListView kmpost = (ListView)findViewById(R.id.KmPostListView);


        //Variable for Listview Scroll Positioning
        //kmpost.smoothScrollToPosition(gv.getKmPostPosition());

        KmToCode = new ArrayList<HashMap>();
        populateListView();
        KmPostListViewAdapter adapter = new KmPostListViewAdapter(this, KmToCode);
        kmpost.setAdapter(adapter);



        kmpost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                try {
                    KmPostVariable getkmpost = KmPostVariable.getInstance();
                    String kmfrn = KmToCode.get(position).get(KmPost.KMFRN).toString();
                    String kmfrt = KmToCode.get(position).get(KmPost.KMFRT).toString();
                    String kmton = KmToCode.get(position).get(KmPost.KMTON).toString();
                    String kmtot = KmToCode.get(position).get(KmPost.KMTOT).toString();
                    int fareg = Integer.parseInt(KmToCode.get(position).get(KmPost.FAREG).toString());
                    int fares = Integer.parseInt(KmToCode.get(position).get(KmPost.FARES).toString());
                    int farelet = Integer.parseInt(KmToCode.get(position).get(KmPost.FARELET).toString());
                    int faresak = Integer.parseInt(KmToCode.get(position).get(KmPost.FARESAK).toString());
                    int fareton = Integer.parseInt(KmToCode.get(position).get(KmPost.FARETON).toString());

                    getkmpost.setKmfrn(kmfrn);
                    getkmpost.setKmfrt(kmfrt);
                    getkmpost.setKmton(kmton);
                    getkmpost.setKmtot(kmtot);
                    getkmpost.setFareg(fareg);
                    getkmpost.setFares(fares);
                    getkmpost.setFarelet(farelet);
                    getkmpost.setFaresak(faresak);
                    getkmpost.setFareton(fareton);

                    PassTypeVariable passType = PassTypeVariable.getInstance();
                    InspectorAuditVariable inspector = InspectorAuditVariable.getInstance();
                    inspector.setAnmcode("X");
                    passType.setPasscode("X");
                    passType.setPassdesc("XXXX");

                    gv.setKmPostPosition(position);

                    finish();
               } catch (Exception e) {
                   Log.d("Error", e.toString());
              }


            }
        });
            kmpost.post(new Runnable() {
                @Override
                public void run() {
                    kmpost.setSelection(gv.getKmPostPosition());
                }
            });
    }
    void populateListView()
    {
        KmToCode = new ArrayList<HashMap>();

        GlobalVariable rtec = GlobalVariable.getInstance();
        String routec = rtec.getRoutecode();
        String bustype = rtec.getBustype();
        DatabaseHelper dbKmPost = new DatabaseHelper(ctx);
        Cursor cr = dbKmPost.getFareMatricsRouteCode(dbKmPost, routec, bustype);
        cr.moveToFirst();
        do
        {
            HashMap map = new HashMap();
            map.put(KmPost.KMFRN, cr.getString(0));
            map.put(KmPost.KMFRT, cr.getString(1));
            map.put(KmPost.KMTON, cr.getString(2));
            map.put(KmPost.KMTOT, cr.getString(3));
            map.put(KmPost.FAREG, cr.getString(4));
            map.put(KmPost.FARES, cr.getString(5));
            map.put(KmPost.FARELET, cr.getString(6));
            map.put(KmPost.FARESAK, cr.getString(7));
            map.put(KmPost.FARETON, cr.getString(8));
            KmToCode.add(map);
        }while (cr.moveToNext());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                KmPostVariable kmpost = KmPostVariable.getInstance();
                kmpost.setKmfrn("XXX");
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
