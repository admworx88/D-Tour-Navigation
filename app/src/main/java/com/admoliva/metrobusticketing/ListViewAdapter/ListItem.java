package com.admoliva.metrobusticketing.ListViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.admoliva.metrobusticketing.Model.PassType;
import com.admoliva.metrobusticketing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jUniX on 11/25/2015.
 */
public class ListItem implements Item {
    private final String         str1;
    private final String         str2;

    public ListItem(String text1, String text2) {
        this.str1 = text1;
        this.str2 = text2;
    }

    @Override
    public int getViewType() {
        return PassTypeListViewAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.passtype_columns, null);
            // Do some initialization
        } else {
            view = convertView;

        }

        TextView text1 = (TextView) view.findViewById(R.id.PASSCODE);
        TextView text2 = (TextView) view.findViewById(R.id.PASSDESC);
        text1.setText(str1);
        text2.setText(str2);

        return view;
    }
}
