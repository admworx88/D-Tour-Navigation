package com.admoliva.metrobusticketing.ListActivity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.GlobalVariable.PaxTicketVariable;
import com.admoliva.metrobusticketing.ListViewAdapter.RouteCodeListViewAdapter;
import com.admoliva.metrobusticketing.Model.RouteCode;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aljon Moliva on 8/6/2015.
 */
public class RouteCodeListActivity extends Activity {

    private ArrayList<HashMap> list;
    Context ctx = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripcode_list);

        ListView listRouteCode = (ListView)findViewById(R.id.listViewRouteCode);

        populateList();

        RouteCodeListViewAdapter adapter = new RouteCodeListViewAdapter(this, list);
        listRouteCode.setAdapter(adapter);

        listRouteCode.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>
parent,final View view, int position, long id) {

                PaxTicketVariable getRoute = PaxTicketVariable.getInstance();
                //int post = position+1;
                String routec = list.get(position).get(RouteCode.ROUTEC).toString();
                String routef = list.get(position).get(RouteCode.ROUTEF).toString();
                String routet = list.get(position).get(RouteCode.ROUTET).toString();

                getRoute.setRouteC(routec);
                getRoute.setRouteCF(routef);
                getRoute.setRouteCT(routet);
                Toast.makeText(RouteCodeListActivity.this, ""+getRoute.getRouteC() + getRoute.getRouteCF() + getRoute.getRouteCT(), Toast.LENGTH_SHORT).show();


            }
        });

    }

    void populateList()
    {
        list = new ArrayList<HashMap>();
        GlobalVariable rtec = GlobalVariable.getInstance();
        String routec = rtec.getRoutecode();
        DatabaseHelper dbRouteCode = new DatabaseHelper(ctx);
        Cursor cr = dbRouteCode.RouteCode(dbRouteCode, routec, rtec.getBustype());
        cr.moveToFirst();
        do
        {
            HashMap map = new HashMap();
            map.put(RouteCode.ROUTEC, cr.getString(0));
            map.put(RouteCode.ROUTEF, cr.getString(3) + " " + cr.getString(1));
            map.put(RouteCode.ROUTET, cr.getString(4) + " " + cr.getString(2));
            list.add(map);
        }while (cr.moveToNext());
    }

}
