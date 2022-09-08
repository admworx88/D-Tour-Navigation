package com.admoliva.metrobusticketing.ListActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.InspectorAuditVariable;
import com.admoliva.metrobusticketing.ListViewAdapter.AnomalyDataListViewAdapter;
import com.admoliva.metrobusticketing.ListViewAdapter.KmPostListViewAdapter;
import com.admoliva.metrobusticketing.Model.AnomalyData;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Aljon Moliva on 8/6/2015.
 */
public class AnomalyDataActivity extends Activity {

    //ArrayList<HashMap> anomalyCode;
    Boolean isLock = true;
    Context ctx = this;
    AnomalyDataListViewAdapter adapter = null;

    ArrayList<AnomalyData> anomalyCode;
    AnomalyData anomalyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anomalydata_list);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView anomalyListView = (ListView)findViewById(R.id.anomalydatatListView);


        anomalyCode = new ArrayList<AnomalyData>();
        populateListView();
        adapter = new AnomalyDataListViewAdapter(this, R.layout.anomalydata_columns, anomalyCode);
        anomalyListView.setAdapter(adapter);

        anomalyListView.setTextFilterEnabled(true);

        anomalyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                try {
                    InspectorAuditVariable getAnomaly = InspectorAuditVariable.getInstance();
                    AnomalyData anomalyData = (AnomalyData) parent.getItemAtPosition(position);
                    String anmCode = anomalyData.getAnomalyCode();
                    String anmDesc = anomalyData.getAnomalyDesc();

                    getAnomaly.setAnmcode(anmCode);
                    getAnomaly.setAnmdesc(anmDesc);

                    finish();
                } catch (Exception e) {
                    Log.d("Error", e.toString());
                    Toast.makeText(AnomalyDataActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int count, int after) {
                adapter.getFilter().filter(cs.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    void populateListView()
    {
        anomalyCode = new ArrayList<AnomalyData>();
        DatabaseHelper dbAnomaly = new DatabaseHelper(ctx);
        Cursor cr = dbAnomaly.getAnomalyData(dbAnomaly);
        cr.moveToFirst();
        do
        {
            anomalyData = new AnomalyData(cr.getString(1), cr.getString(2));
            anomalyCode.add(anomalyData);

        }while (cr.moveToNext());
        dbAnomaly.closeDB();
        cr.close();
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
