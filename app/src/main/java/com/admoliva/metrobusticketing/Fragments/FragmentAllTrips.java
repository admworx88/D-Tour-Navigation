package com.admoliva.metrobusticketing.Fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

/**
 * Created by jUniX on 11/5/2015.
 */
public class FragmentAllTrips extends Fragment {
    TextView busNo, driver, conductor, tripno, routecode;
    TextView text1, text2, text3, text4, text5;
    TextView text8, text9, text10, text11, text12, text13, text14, text15, text16, text17, text18, text19, text20, text21;
    TextView paxOnBoard, full, sp, half, other, totalOnBoard, totalFull, totalSP, totalHalf, totalOther;
    TextView baggage, letters, sacks, kilos, others;
    TextView totalBaggage, totalLetters, totalSacks, totalKilos, totalOthers, totalCash;
    TextView TripTotals;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_alltrips, container, false);
        text1 = (TextView)rootView.findViewById(R.id.TextView1);
        busNo = (TextView)rootView.findViewById(R.id.TextView2);
        text2 = (TextView)rootView.findViewById(R.id.TextView3);
        driver = (TextView)rootView.findViewById(R.id.TextView4);
        text3 = (TextView)rootView.findViewById(R.id.TextView5);
        conductor = (TextView)rootView.findViewById(R.id.TextView6);
        text4 = (TextView)rootView.findViewById(R.id.TextView7);
        tripno = (TextView)rootView.findViewById(R.id.TextView8);
        text5 = (TextView)rootView.findViewById(R.id.TextView9);
        routecode = (TextView)rootView.findViewById(R.id.TextView10);
        //text6 = (TextView)rootView.findViewById(R.id.TextView11);
        //kmfrom = (TextView)rootView.findViewById(R.id.TextView12);
        //text7 = (TextView)rootView.findViewById(R.id.TextView13);
        //kmto = (TextView)rootView.findViewById(R.id.TextView14);

        text8 = (TextView)rootView.findViewById(R.id.TextView15);
        text9 = (TextView)rootView.findViewById(R.id.TextView16);
        text10 = (TextView)rootView.findViewById(R.id.TextView17);
        text11 = (TextView)rootView.findViewById(R.id.TextView18);
        text12 = (TextView)rootView.findViewById(R.id.TextView19);
        text13 = (TextView)rootView.findViewById(R.id.TextView30);
        text14 = (TextView)rootView.findViewById(R.id.TextView31);
        text15 = (TextView)rootView.findViewById(R.id.TextView32);
        text16 = (TextView)rootView.findViewById(R.id.TextView33);
        text17 = (TextView)rootView.findViewById(R.id.TextView34);
        text18 = (TextView)rootView.findViewById(R.id.TextView35);


        paxOnBoard = (TextView)rootView.findViewById(R.id.TextView20);
        full = (TextView)rootView.findViewById(R.id.TextView21);
        sp = (TextView)rootView.findViewById(R.id.TextView22);
        half = (TextView)rootView.findViewById(R.id.TextView23);
        other = (TextView)rootView.findViewById(R.id.TextView24);
        totalOnBoard = (TextView)rootView.findViewById(R.id.TextView25);
        totalFull = (TextView)rootView.findViewById(R.id.TextView26);
        totalSP = (TextView)rootView.findViewById(R.id.TextView27);
        totalHalf = (TextView)rootView.findViewById(R.id.TextView28);
        totalOther = (TextView)rootView.findViewById(R.id.TextView29);
        baggage = (TextView)rootView.findViewById(R.id.TextView36);
        letters = (TextView)rootView.findViewById(R.id.TextView37);
        sacks = (TextView)rootView.findViewById(R.id.TextView38);
        kilos = (TextView)rootView.findViewById(R.id.TextView39);
        others = (TextView)rootView.findViewById(R.id.TextView40);
        totalBaggage = (TextView)rootView.findViewById(R.id.TextView41);
        totalLetters = (TextView)rootView.findViewById(R.id.TextView42);
        totalSacks = (TextView)rootView.findViewById(R.id.TextView43);
        totalKilos = (TextView)rootView.findViewById(R.id.TextView44);
        totalOthers = (TextView)rootView.findViewById(R.id.TextView45);
        totalCash = (TextView)rootView.findViewById(R.id.TextView46);
        TripTotals = (TextView)rootView.findViewById(R.id.TextViewTripTotals);
        //TripTotalsNum = (TextView)rootView.findViewById(R.id.TextViewTripTotalsNum);

        Typeface typeFace= Typeface.createFromAsset(getActivity().getAssets(), "fonts/calibri.ttf");

        text1.setTypeface(typeFace);
        busNo.setTypeface(typeFace);
        text2.setTypeface(typeFace);
        driver.setTypeface(typeFace);
        text3.setTypeface(typeFace);
        conductor.setTypeface(typeFace);
        text4.setTypeface(typeFace);
        tripno.setTypeface(typeFace);
        text5.setTypeface(typeFace);
        routecode.setTypeface(typeFace);
        //text6.setTypeface(typeFace);
        //kmfrom.setTypeface(typeFace);
        //text7.setTypeface(typeFace);
        //kmto.setTypeface(typeFace);

        text8.setTypeface(typeFace);
        text9.setTypeface(typeFace);
        text10.setTypeface(typeFace);
        text11.setTypeface(typeFace);
        text12.setTypeface(typeFace);
        text13.setTypeface(typeFace);
        text14.setTypeface(typeFace);
        text15.setTypeface(typeFace);
        text16.setTypeface(typeFace);
        text17.setTypeface(typeFace);
        text18.setTypeface(typeFace);

        TripTotals.setTypeface(typeFace);
        //TripTotalsNum.setTypeface(typeFace);

        paxOnBoard.setTypeface(typeFace);
        full.setTypeface(typeFace);
        sp.setTypeface(typeFace);
        half.setTypeface(typeFace);
        other.setTypeface(typeFace);
        totalOnBoard.setTypeface(typeFace);
        totalFull.setTypeface(typeFace);
        totalSP.setTypeface(typeFace);
        totalHalf.setTypeface(typeFace);
        totalOther.setTypeface(typeFace);
        baggage.setTypeface(typeFace);
        letters.setTypeface(typeFace);
        sacks.setTypeface(typeFace);
        kilos.setTypeface(typeFace);
        others.setTypeface(typeFace);
        totalBaggage.setTypeface(typeFace);
        totalLetters.setTypeface(typeFace);
        totalSacks.setTypeface(typeFace);
        totalKilos.setTypeface(typeFace);
        totalOthers.setTypeface(typeFace);
        totalCash.setTypeface(typeFace);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getAccountInfo();
        getTripNo();
        getRouteValidation();
    }

    void getAccountInfo()
    {
        GlobalVariable accountInfo = GlobalVariable.getInstance();

        busNo.setText(accountInfo.getBusno() + "  " + accountInfo.getBusname() + "  " + accountInfo.getBustype());
        driver.setText(accountInfo.getDrivid() + "  " + accountInfo.getDrivername());
        conductor.setText(accountInfo.getConid() + "  " + accountInfo.getConductorname());
        routecode.setText(accountInfo.getRoutecode());
    }
    private void getTripNo()
    {
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplication());
        Cursor cr = db.getTripRoute(db);
        GlobalVariable accountInfo = GlobalVariable.getInstance();

        try
        {
            cr.moveToFirst();
            int tripNo = cr.getCount();
            tripno.setText(""+tripNo);
            accountInfo.setTripNo(String.valueOf(tripNo));
        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            db.closeDB();
            cr.close();
        }

    }
    private void getRouteValidation()
    {
        GlobalVariable rc = GlobalVariable.getInstance();
        String rtec = rc.getRoutecode();
        String trip = tripno.getText().toString();
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplication());
        Cursor cr = db.getTripRoute(db);
        cr.moveToFirst();
        String routecodes = "";
        try
        {

            cr.moveToFirst();
            do
            {
                routecodes += cr.getString(1) + " ";
            }while (cr.moveToNext());
            routecode.setText(routecodes);

        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            db.closeDB();
            cr.close();
            getTicketTransaction();
        }
    }

    private void getTicketTransaction()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplication());
        try
        {
            Cursor cr = dbHelper.getPaxTicketTransAllTrips(dbHelper);

            if(cr.getCount() == 0){

                Toast.makeText(getActivity().getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }else{
                cr.moveToFirst();
                String F, SP, H, OP, L, S, K, OB;
                String FullAmount, SPAmount, HAmount, OPAmount, LAmount, SAmount, KAmount, OBAmount;

                F = cr.getString(1);
                FullAmount = cr.getString(2);
                SP = cr.getString(3);
                SPAmount = cr.getString(4);
                H = cr.getString(5);
                HAmount = cr.getString(6);
                OP = cr.getString(7);
                OPAmount = cr.getString(8);
                L = cr.getString(9);
                LAmount = cr.getString(10);
                S = cr.getString(11);
                SAmount = cr.getString(12);
                K = cr.getString(13);
                KAmount = cr.getString(14);
                OB = cr.getString(15);
                OBAmount = cr.getString(16);

                full.setText(F);
                sp.setText(SP);
                half.setText(H);
                other.setText(OP);
                letters.setText(L);
                sacks.setText(S);
                kilos.setText(K);
                others.setText(OB);


                totalFull.setText(FullAmount);
                totalSP.setText(SPAmount);
                totalHalf.setText(HAmount);
                totalOther.setText(OPAmount);
                totalLetters.setText(LAmount);
                totalSacks.setText(SAmount);
                totalKilos.setText(KAmount);
                totalOthers.setText(OBAmount);

                int cash1 = Integer.parseInt(FullAmount)+Integer.parseInt(SPAmount)+Integer.parseInt(HAmount)+Integer.parseInt(OPAmount);
                totalOnBoard.setText(""+cash1);
                int cash2 = Integer.parseInt(LAmount)+Integer.parseInt(SAmount)+Integer.parseInt(KAmount)+Integer.parseInt(OBAmount);
                totalBaggage.setText(""+cash2);
                int totCash = cash1 + cash2;
                totalCash.setText(""+totCash);

                int totOnBoard = Integer.parseInt(F)+Integer.parseInt(SP)+Integer.parseInt(H)+Integer.parseInt(OP);
                paxOnBoard.setText("" + totOnBoard);
                int totBag = Integer.parseInt(L)+Integer.parseInt(S)+Integer.parseInt(K)+Integer.parseInt(OB);
                baggage.setText(""+totBag);
                cr.close();
            }


        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            dbHelper.closeDB();
        }

    }

}
