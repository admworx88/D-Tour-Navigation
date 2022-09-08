package com.admoliva.metrobusticketing.ListViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.admoliva.metrobusticketing.R;

/**
 * Created by jUniX on 11/25/2015.
 */
public class Header implements Item {
    private final String         name;

    public Header(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return PassTypeListViewAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.passtype_columns2, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.HEADER);
        text.setText(name);

        return view;
    }
}
