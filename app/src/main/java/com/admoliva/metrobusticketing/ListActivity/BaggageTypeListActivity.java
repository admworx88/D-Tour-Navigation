package com.admoliva.metrobusticketing.ListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.admoliva.metrobusticketing.Activitity.IssueTicketActivity;
import com.admoliva.metrobusticketing.GlobalVariable.TicketGlobalVariable;
import com.admoliva.metrobusticketing.R;

import java.util.ArrayList;

/**
 * Created by Aljon Moliva on 8/6/2015.
 */
public class BaggageTypeListActivity extends Activity {
    ArrayList<String> PassType;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripcode_list);

        ListView kmfrom = (ListView)findViewById(R.id.listViewRouteCode);

        PassType = new ArrayList<String>();

        getTripNo();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, PassType);
        kmfrom.setAdapter(arrayAdapter);

        kmfrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            TicketGlobalVariable paxtype = TicketGlobalVariable.getInstance();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = PassType.get(position);
                paxtype.setPassType(selected);
                startActivity(new Intent(BaggageTypeListActivity.this, IssueTicketActivity.class));
                finish();


            }
        });

    }
    void getTripNo()
    {
        PassType.add("Letter");
        PassType.add("Sacks");
        PassType.add("Cartoon");
        PassType.add("Other");
    }
}
