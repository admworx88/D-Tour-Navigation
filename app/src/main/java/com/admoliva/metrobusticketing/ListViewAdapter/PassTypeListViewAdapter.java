package com.admoliva.metrobusticketing.ListViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admoliva.metrobusticketing.Model.PassType;
import com.admoliva.metrobusticketing.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by jUniX on 10/19/2015.
 */
public class PassTypeListViewAdapter extends ArrayAdapter<Item> {
    private LayoutInflater mInflater;
    public ArrayList<List> list;
    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public PassTypeListViewAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }
    @Override public boolean isEnabled(int position)
    {
        return (getItem(position).getViewType() == PassTypeListViewAdapter.RowType.LIST_ITEM.ordinal());
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    public class TextHolder
    {

    }

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        ViewHolder holder;
        int rowType = getItemViewType(position);
        View View;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.passtype_columns, null);
                    holder.View=getItem(position).getView(mInflater, convertView);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.passtype_columns2, null);
                    holder.View=getItem(position).getView(mInflater, convertView);
                    break;
            }
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
    public static class ViewHolder {

        public  View View;
    }
}
