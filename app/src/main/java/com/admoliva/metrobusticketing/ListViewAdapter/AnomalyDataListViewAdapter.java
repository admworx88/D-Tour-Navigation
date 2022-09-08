package com.admoliva.metrobusticketing.ListViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.admoliva.metrobusticketing.Model.AnomalyData;
import com.admoliva.metrobusticketing.Model.RouteCode;
import com.admoliva.metrobusticketing.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jUniX on 10/12/2015.
 */
public class AnomalyDataListViewAdapter extends ArrayAdapter<AnomalyData> {


    private ArrayList<AnomalyData> searchResult;
    private ArrayList<AnomalyData> anomalyList;
    private AnomalyFilter filter;


    public AnomalyDataListViewAdapter(Activity activity, int textViewResourceId, ArrayList<AnomalyData> list)
    {
        super(activity, textViewResourceId, list);
        this.anomalyList = new ArrayList<AnomalyData>();
        this.anomalyList.addAll(list);
        this.searchResult = new ArrayList<AnomalyData>();
        this.searchResult.addAll(list);


    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new AnomalyFilter();
        }
        return filter;
    }

    public class TextHolder
    {
        TextView txtFirst;
        TextView txtSecond;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //LayoutInflater inflater = getContext().getLayoutInflater();
        TextHolder textHolder = null;
        if(view == null)
        {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.anomalydata_columns, null);

            textHolder = new TextHolder();
            textHolder.txtFirst = (TextView) view.findViewById(R.id.ANOMALYCODE);
            textHolder.txtSecond = (TextView) view.findViewById(R.id.ANOMALYDESC);
            view.setTag(textHolder);
        }else{
            textHolder = (TextHolder) view.getTag();
        }

        AnomalyData anomalyData = anomalyList.get(position);
        textHolder.txtFirst.setText(anomalyData.getAnomalyCode());
        textHolder.txtSecond.setText(anomalyData.getAnomalyDesc());

        return view;
    }

    private class AnomalyFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<AnomalyData> filteredItems = new ArrayList<AnomalyData>();

                for(int i = 0, l = searchResult.size(); i < l; i++)
                {
                    AnomalyData anomaly = searchResult.get(i);
                    if(anomaly.toString().toLowerCase().contains(constraint))
                        filteredItems.add(anomaly);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = searchResult;
                    result.count = searchResult.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      Filter.FilterResults results) {
           /* anomalyList =(ArrayList<AnomalyData>) results.values;
            notifyDataSetChanged();*/
            anomalyList = (ArrayList<AnomalyData>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = anomalyList.size(); i < l; i++)
                add(anomalyList.get(i));
            notifyDataSetInvalidated();

            /*for(AnomalyData anomalyData : anomalyList)
                add(anomalyData);*/
        }
    }


}
