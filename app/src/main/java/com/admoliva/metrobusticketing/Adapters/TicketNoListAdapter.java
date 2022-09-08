package com.admoliva.metrobusticketing.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.Model.TicketNoModel;

import java.util.ArrayList;

/**
 * Created by Aljon Moliva on 7/24/2015.
 */
public class TicketNoListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TicketNoModel> ticketNoModels;

    public TicketNoListAdapter(Context context, ArrayList<TicketNoModel> ticketNoModels) {
        this.context = context;
        this.ticketNoModels = ticketNoModels;
    }

    @Override
    public int getCount() {
        return ticketNoModels.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketNoModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_item_layout, null);
            }

        TextView ticketNo = (TextView)convertView.findViewById(R.id.ticketUsed);
        //TextView ticketLet = (TextView)convertView.findViewById(R.id.ticketLet);

        ticketNo.setText(ticketNoModels.get(position).getTicketno());
        //ticketLet.setText(ticketNoModels.get(position).getTicketLet());

        return convertView;
        }

}
