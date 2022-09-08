package com.admoliva.metrobusticketing.ListViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.Model.RouteCode;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jUniX on 10/12/2015.
 */
public class RouteCodeListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap> list;
    Activity activity;


    public  RouteCodeListViewAdapter(Activity activity, ArrayList<HashMap> list)
    {
        super();
        this.activity = activity;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class TextHolder
    {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        TextHolder textHolder;
        if(view == null)
        {
            view = inflater.inflate(R.layout.routecode_columns, null);
            textHolder = new TextHolder();
            textHolder.txtFirst = (TextView) view.findViewById(R.id.ROUTEC);
            textHolder.txtSecond = (TextView) view.findViewById(R.id.ROUTEF);
            textHolder.txtThird = (TextView) view.findViewById(R.id.ROUTET);
            view.setTag(textHolder);
        }else{
            textHolder = (TextHolder) view.getTag();
        }

        HashMap<String, String> map=list.get(position);
        textHolder.txtFirst.setText(map.get(RouteCode.ROUTEC));
        textHolder.txtSecond.setText(map.get(RouteCode.ROUTEF));
        textHolder.txtThird.setText(map.get(RouteCode.ROUTET));
        return view;
    }
}
