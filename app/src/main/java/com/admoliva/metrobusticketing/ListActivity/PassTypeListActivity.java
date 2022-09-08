package com.admoliva.metrobusticketing.ListActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.PassTypeVariable;
import com.admoliva.metrobusticketing.ListViewAdapter.Header;
import com.admoliva.metrobusticketing.ListViewAdapter.Item;
import com.admoliva.metrobusticketing.ListViewAdapter.ListItem;
import com.admoliva.metrobusticketing.ListViewAdapter.PassTypeListViewAdapter;
import com.admoliva.metrobusticketing.Model.PassType;
import com.admoliva.metrobusticketing.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aljon Moliva on 8/6/2015.
 */
public class PassTypeListActivity extends Activity {
    private List<Item> list;
    Boolean isLock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passtype_list);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.listViewPassType);
        list = new ArrayList<Item>();
        populateListview();

        PassTypeListViewAdapter adapter = new PassTypeListViewAdapter(this, list);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PassTypeVariable passType = PassTypeVariable.getInstance();
                TextView passcode= (TextView) view.findViewById(R.id.PASSCODE);
                TextView passdesc = (TextView) view.findViewById(R.id.PASSDESC);

                passType.setPasscode(passcode.getText().toString());
                passType.setPassdesc(passdesc.getText().toString());
                finish();
            }
        });

    }

    void populateListview(){
        list.add(new Header("Passenger Type"));
        list.add(new ListItem("F", "Full"));
        list.add(new ListItem("SP", "SP/DA/SC"));
        list.add(new ListItem("H", "HALF"));
        list.add(new ListItem("OP", "Other PaxType"));
        list.add(new Header("Baggage Type"));
        list.add(new ListItem("L", "Letter"));
        list.add(new ListItem("S", "Sacks"));
        list.add(new ListItem("K", "Kilos"));
        list.add(new ListItem("OB", "Other BagType"));
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
