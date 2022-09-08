package com.admoliva.metrobusticketing.ListViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admoliva.metrobusticketing.Model.KmPost;
import com.admoliva.metrobusticketing.Model.RouteCode;
import com.admoliva.metrobusticketing.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jUniX on 10/15/2015.
 */
public class KmPostListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;

    public  KmPostListViewAdapter(Activity activity, ArrayList<HashMap> list)
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
        //KMFRN, KMTON, KMFRT, KMTOT, FAREG, FARES, FARELET, FARESAK, FARETON

        TextView KMFRN;
        TextView KMTON;
        TextView KMFRT;
        TextView KMTOT;
        TextView FAREG;
        TextView FARES;
        TextView FARELET;
        TextView FARESAK;
        TextView FARETON;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        TextHolder textHolder;
        if(view == null)
        {
            view = inflater.inflate(R.layout.kmpost_columns, null);
            textHolder = new TextHolder();
            textHolder.KMFRN = (TextView) view.findViewById(R.id.KMFRN);
            textHolder.KMFRT = (TextView) view.findViewById(R.id.KMFRT);
            textHolder.KMTON = (TextView) view.findViewById(R.id.KMTON);
            textHolder.KMTOT = (TextView) view.findViewById(R.id.KMTOT);
            textHolder.FAREG = (TextView) view.findViewById(R.id.FAREG);
            textHolder.FARES = (TextView) view.findViewById(R.id.FARES);
            textHolder.FARELET = (TextView) view.findViewById(R.id.FARELET);
            textHolder.FARESAK = (TextView) view.findViewById(R.id.FARESAK);
            textHolder.FARETON = (TextView) view.findViewById(R.id.FARETON);
            view.setTag(textHolder);
        }else{
            textHolder = (TextHolder) view.getTag();
        }

        HashMap<String, String> map=list.get(position);
        textHolder.KMFRN.setText(map.get(KmPost.KMFRN));
        textHolder.KMFRT.setText(map.get(KmPost.KMFRT));
        textHolder.KMTON.setText(map.get(KmPost.KMTON));
        textHolder.KMTOT.setText(map.get(KmPost.KMTOT));
        textHolder.FAREG.setText(map.get(KmPost.FAREG));
        textHolder.FARES.setText(map.get(KmPost.FARES));
        textHolder.FARELET.setText(map.get(KmPost.FARELET));
        textHolder.FARESAK.setText(map.get(KmPost.FARESAK));
        textHolder.FARETON.setText(map.get(KmPost.FARETON));
        return view;
    }
}
