package com.admoliva.metrobusticketing.ListViewAdapter;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by jUniX on 11/15/2015.
 */
public interface Item {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
