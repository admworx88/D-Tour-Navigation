package com.admoliva.metrobusticketing.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.admoliva.metrobusticketing.GlobalVariable.GlobalVariable;
import com.admoliva.metrobusticketing.R;
import com.admoliva.metrobusticketing.SQLiteDatabase.DatabaseHelper;

import org.w3c.dom.Text;

import java.io.IOException;

/**
 * Created by jUniX on 11/5/2015.
 */
public class FragmentCutOffKmPost extends Fragment {
    TextView busNo, driver, conductor, tripno, routecode, kmfrom, kmto;
    TextView text1, text2, text3, text4, text5, text6, text7;
    TextView text8, text9, text10, text11, text12, text13, text14, text15, text16, text17, text18, text19, text20, text21;
    TextView paxOnBoard, full, sp, half, other, totalOnBoard, totalFull, totalSP, totalHalf, totalOther;
    TextView baggage, letters, sacks, kilos, others;
    TextView totalBaggage, totalLetters, totalSacks, totalKilos, totalOthers, totalCash;
    TextView textKmPost, kmPostNum;
    TextView disembark, pax, baggage2;
    String routefn, routetn;
    Button search;
    EditText editText;
    String routefrom, routeto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_cutoffkmpost, container, false);

        editText = (EditText)rootView.findViewById(R.id.editText1);
        search = (Button)rootView.findViewById(R.id.search);

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
        text6 = (TextView)rootView.findViewById(R.id.TextView11);
        kmfrom = (TextView)rootView.findViewById(R.id.TextView12);
        text7 = (TextView)rootView.findViewById(R.id.TextView13);
        kmto = (TextView)rootView.findViewById(R.id.TextView14);

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
        text19 = (TextView)rootView.findViewById(R.id.TextView47);
        text20 = (TextView)rootView.findViewById(R.id.TextView48);
        text21 = (TextView)rootView.findViewById(R.id.TextView51);

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
        disembark = (TextView)rootView.findViewById(R.id.TextView49);
        pax = (TextView)rootView.findViewById(R.id.TextView50);
        baggage2 = (TextView)rootView.findViewById(R.id.TextView52);

        textKmPost = (TextView)rootView.findViewById(R.id.TextViewKmPost);
        kmPostNum = (TextView)rootView.findViewById(R.id.TextViewKmPostNum);

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
        text6.setTypeface(typeFace);
        kmfrom.setTypeface(typeFace);
        text7.setTypeface(typeFace);
        kmto.setTypeface(typeFace);

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
        text19.setTypeface(typeFace);
        text20.setTypeface(typeFace);
        text21.setTypeface(typeFace);

        textKmPost.setTypeface(typeFace);
        kmPostNum.setTypeface(typeFace);

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

        disembark.setTypeface(typeFace);
        pax.setTypeface(typeFace);
        baggage2.setTypeface(typeFace);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "Please input Km Code!", Toast.LENGTH_SHORT).show();
                }else{
                    editText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    searchValidation();
                }

            }
        });

        return rootView;
    }

    /*private void searchKmCodeExist(){
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplication());
        Cursor cr = db.getKmCode(db, editText.getText().toString());
        try{
            if(cr.getCount() == 0){
                Toast.makeText(getActivity().getApplicationContext(), "Km Code not found!", Toast.LENGTH_SHORT).show();
                clear();
            }else{
                searchValidation();
            }
        }catch (Exception e){
            Log.d("CUT OFF KM POST", "ERROR " + e.toString());

        }finally {
            cr.close();
            db.closeDB();
        }
    }*/



    @Override
    public void onStart() {
        super.onStart();
        getAccountInfo();
        getTripNo();
        getRouteValidation();
        routeValidation();
    }

    private void searchValidation() {
        if (Integer.parseInt(routefn) < Integer.parseInt(routetn)) {
            if (Integer.parseInt(editText.getText().toString()) > Integer.parseInt(routetn)) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid Km Post!", Toast.LENGTH_SHORT).show();
                clear();
            } else if (Integer.parseInt(editText.getText().toString()) < Integer.parseInt(routefn)) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid Km Post!", Toast.LENGTH_SHORT).show();
                clear();
            } else {
                searchProcess();
            }
        } else if (Integer.parseInt(routefn) > Integer.parseInt(routetn)) {
            if (Integer.parseInt(editText.getText().toString()) < Integer.parseInt(routetn)) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid Km Post!", Toast.LENGTH_SHORT).show();
                clear();
            } else if (Integer.parseInt(editText.getText().toString()) > Integer.parseInt(routefn)) {
                Toast.makeText(getActivity().getApplicationContext(), "Invalid Km Post!", Toast.LENGTH_SHORT).show();
                clear();
            }else {
                searchProcess();
            }
        }
    }

    private void searchProcess(){
        kmPostNum.setText(editText.getText().toString());
        disembark.setText(editText.getText().toString());
        clear();
        String tripNumber = tripno.getText().toString();
        getTicketTransaction(routefn, routetn, tripNumber, editText.getText().toString());
    }
    void clear()
    {
        paxOnBoard.setText("00");
        full.setText("00");
        sp.setText("00");
        half.setText("00");
        other.setText("00");
        totalOnBoard.setText("000.00");
        totalFull.setText("000.00");
        totalSP.setText("000.00");
        totalHalf.setText("000.00");
        totalOther.setText("000.00");
        paxOnBoard.setText("000.00");

        baggage.setText("00");
        letters.setText("00");
        sacks.setText("00");
        kilos.setText("00");
        others.setText("00");
        totalCash.setText("000.00");
        totalBaggage.setText("000.00");
        totalLetters.setText("000.00");
        totalSacks.setText("000.00");
        totalKilos.setText("000.00");
        totalOthers.setText("000.00");
        pax.setText("00");
        baggage2.setText("00");


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
        DatabaseHelper getTripNo = new DatabaseHelper(getActivity().getApplication());
        Cursor cr = getTripNo.getTripRoute(getTripNo);
        GlobalVariable accountInfo = GlobalVariable.getInstance();
        try
        {
            cr.moveToLast();
            String tripNo = cr.getString(0);
            tripno.setText(tripNo);
            accountInfo.setTripNo(tripNo);
        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            getTripNo.closeDB();
            cr.close();
        }

    }
    private void routeValidation(){
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplication());
        Cursor cr = db.getTripRoute(db);
        try{
            cr.moveToLast();
            routefrom = cr.getString(2);
            routeto = cr.getString(3);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            cr.close();
            db.closeDB();
        }
    }
    private void getRouteValidation()
    {
        GlobalVariable rc = GlobalVariable.getInstance();
        String rtec = rc.getRoutecode();
        String trip = tripno.getText().toString();
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplication());
        Cursor cr = db.RouteCode(db, rtec, rc.getBustype());
        cr.moveToFirst();
        try
        {

                kmfrom.setText(cr.getString(3) + " " + cr.getString(1));
                kmto.setText(cr.getString(4) + " " + cr.getString(2));
                routefn = cr.getString(3);
                routetn = cr.getString(4);
        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            db.closeDB();
            cr.close();
        }
    }

    private String getTicketTransaction(String routefn, String routetn, String tripno, String kmpost)
    {
        String val = "";
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplication());
        try
        {
            if(Integer.parseInt(routefn) > Integer.parseInt(routetn))
            {
                Cursor crPax = dbHelper.getPaxTicketTransCountPax(dbHelper, tripno, kmpost);
                Cursor crBag = dbHelper.getPaxTicketTransCountBag(dbHelper, tripno, kmpost);

                Cursor crFull = dbHelper.getPaxTicketFull(dbHelper, tripno, kmpost, "<");
                Cursor crSP = dbHelper.getPaxTicketSP(dbHelper, tripno, kmpost, "<");
                Cursor crHalf = dbHelper.getPaxTicketHalf(dbHelper, tripno, kmpost, "<");
                Cursor crOP = dbHelper.getPaxTicketOP(dbHelper, tripno, kmpost, "<");

                Cursor crLetter = dbHelper.getPaxTicketLetter(dbHelper, tripno, kmpost, "<");
                Cursor crSacks = dbHelper.getPaxTicketSacks(dbHelper, tripno, kmpost, "<");
                Cursor crKilos = dbHelper.getPaxTicketKilos(dbHelper, tripno, kmpost, "<");
                Cursor crOB = dbHelper.getPaxTicketOB(dbHelper, tripno, kmpost, "<");

                crFull.moveToFirst();
                crSP.moveToFirst();
                crHalf.moveToFirst();
                crOP.moveToFirst();

                crLetter.moveToFirst();
                crSacks.moveToFirst();
                crKilos.moveToFirst();
                crOB.moveToFirst();

                crPax.moveToFirst();
                crBag.moveToFirst();


                String F, SP, H, OP, L, S, K, OB;
                String FullAmount, SPAmount, HAmount, OPAmount, LAmount, SAmount, KAmount, OBAmount;


                F = crFull.getString(0);
                FullAmount = crFull.getString(1);
                SP = crSP.getString(0);
                SPAmount = crSP.getString(1);
                H = crHalf.getString(0);
                HAmount = crHalf.getString(1);
                OP = crOP.getString(0);
                OPAmount = crOP.getString(1);
                L = crLetter.getString(0);
                LAmount = crLetter.getString(1);
                S = crSacks.getString(0);
                SAmount = crSacks.getString(1);
                K = crKilos.getString(0);
                KAmount = crKilos.getString(1);
                OB = crOB.getString(0);
                OBAmount = crOB.getString(1);


                String bagCount = crBag.getString(0);
                String paxCount = crPax.getString(0);

                baggage2.setText(bagCount);
                pax.setText(paxCount);

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
                val = "1";



                crBag.close();
                crFull.close();

            }else if(Integer.parseInt(routefn) <  Integer.parseInt(routetn)){
                Cursor crPax = dbHelper.getPaxTicketTransCountPax(dbHelper, tripno, kmpost);
                Cursor crBag = dbHelper.getPaxTicketTransCountBag(dbHelper, tripno, kmpost);


                Cursor crFull = dbHelper.getPaxTicketFull(dbHelper, tripno, kmpost, ">");
                Cursor crSP = dbHelper.getPaxTicketSP(dbHelper, tripno, kmpost, ">");
                Cursor crHalf = dbHelper.getPaxTicketHalf(dbHelper, tripno, kmpost, ">");
                Cursor crOP = dbHelper.getPaxTicketOP(dbHelper, tripno, kmpost, ">");

                Cursor crLetter = dbHelper.getPaxTicketLetter(dbHelper, tripno, kmpost, ">");
                Cursor crSacks = dbHelper.getPaxTicketSacks(dbHelper, tripno, kmpost, ">");
                Cursor crKilos = dbHelper.getPaxTicketKilos(dbHelper, tripno, kmpost, ">");
                Cursor crOB = dbHelper.getPaxTicketOB(dbHelper, tripno, kmpost, ">");


                crFull.moveToFirst();
                crSP.moveToFirst();
                crHalf.moveToFirst();
                crOP.moveToFirst();

                crLetter.moveToFirst();
                crSacks.moveToFirst();
                crKilos.moveToFirst();
                crOB.moveToFirst();

                crPax.moveToFirst();
                crBag.moveToFirst();

                String F, SP, H, OP, L, S, K, OB;
                String FullAmount, SPAmount, HAmount, OPAmount, LAmount, SAmount, KAmount, OBAmount;

                F = crFull.getString(0);
                FullAmount = crFull.getString(1);
                SP = crSP.getString(0);
                SPAmount = crSP.getString(1);
                H = crHalf.getString(0);
                HAmount = crHalf.getString(1);
                OP = crOP.getString(0);
                OPAmount = crOP.getString(1);
                L = crLetter.getString(0);
                LAmount = crLetter.getString(1);
                S = crSacks.getString(0);
                SAmount = crSacks.getString(1);
                K = crKilos.getString(0);
                KAmount = crKilos.getString(1);
                OB = crOB.getString(0);
                OBAmount = crOB.getString(1);

                String paxCount = crPax.getString(0);
                String bagCount = crBag.getString(0);
                pax.setText(paxCount);
                baggage2.setText(bagCount);

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
                val = "1";



                //Toast.makeText(getActivity().getApplicationContext(), F + S, Toast.LENGTH_SHORT).show();
                crPax.close();
                crBag.close();
                crFull.close();
                crSP.close();
                crHalf.close();
                crOB.close();
                crLetter.close();
                crSacks.close();
                crKilos.close();
                crOP.close();

            }
        }catch (Exception e)
        {
           Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }finally {
            dbHelper.closeDB();
        }

        return val;
    }
}
