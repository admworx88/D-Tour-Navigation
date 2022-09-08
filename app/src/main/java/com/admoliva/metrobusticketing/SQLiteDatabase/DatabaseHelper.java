package com.admoliva.metrobusticketing.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.ConsumerIrManager;
import android.provider.ContactsContract;
import android.util.Log;

import com.admoliva.metrobusticketing.Activitity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by jUniX on 10/7/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String LOG = "DatabaseHelper";

    public static final String DATABASE_NAME = "MetroBusTicketing";

    private static final int DATABASE_VERSION = 1;

    //PROGRAM USER
    public static final String CONDUCTOR_ID = "conid";
    public static final String CONDUCTOR_NAME = "conname";
    public static final String DRIVER_ID = "drivid";
    public static final String DRIVER_NAME = "drivname";
    public static final String BUS_NO = "busno";
    public static final String BUS_NAME= "busname";
    public static final String BUS_TYPE = "bustype";
    public static final String ROUTE_CODE = "routec";
    public static final String TABLE_PROGRAMUSER = "programuser";
    public static final String PASSWORD = "password";

    //TicketOR
    public static final String TABLE_TICKETOR = "ticket_or";
    public static final String OR_FROM = "orfrom";
    public static final String OR_TO = "orto";
    public static final String USED_FROM = "usedfrom";
    public static final String USED_TO = "usedto";
    public static final String OR_LETTER = "orletter";
    public static final String OR_USED_LETTER = "orusedletter";


    //PassengerTicket
    public static final String TABLE_PASSENGER = "tkt_trans";
    /*public static final String TRIP_NO = "tripno";*/
    /*public static final String ROUTE_CODE = "tripcode";*/
    public static final String KM_FROM_CODE = "kmfromcode";
    public static final String KM_FROM_DESC = "kmfromdesc";
    public static final String KM_TO_CODE = "kmtocode";
    public static final String KM_TO_DESC = "kmtodesc";
    public static final String FARE_TYPE = "faretype";
    public static final String FARE = "fare";
    public static final String TRIP_FROM = "tripfrom";
    public static final String TRIP_TO = "tripto";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String TICKET_NO = "ticketno";
    public static final String TICKET_LETTER = "ticketlet";
    public static final String BAG_TYPE = "bagtype";
    public static final String INSID = "insid";
    public static final String INS_TYPE = "instype";
    public static final String KM_ON = "kmon";
    public static final String KM_OFF = "kmoff";
    public static final String ANMCOD = "anmcod";
    public static final String PAX_NO = "paxno";
    public static final String BAG_NO = "bagno";
    public static final String PAX_AMT = "paxamt";
    public static final String BAG_AMT = "bagamt";
    public static final String REMARKS = "remarks";
    public static final String REMARKS2 = "remarks2";
    public static final String STATUS = "status";


    //BluetoothDevice
    public static final String TABLE_BLUETOOTH = "bluetooth";
    public static final String DEVICE_NAME = "devicename";

    //TripNo
    public static final String TABLE_TRIPNO = "trips";
    public static final String TRIP_NO = "tripno";

    //TripValidation
    public static final String TABLE_VALIDATION= "tripvalidation";
    /*public static final String TRIP_NO = "tripno";*/
    public static final String VALIDATION_KEYS = "validationkeys";


    //ROUTES
    public static final String TABLE_ROUTES = "routes";
    public static final String ROUTEC = "routec";
    public static final String ROUTEF = "routef";
    public static final String ROUTET = "routet";
    public static final String ROUTEFN = "routefn";
    public static final String ROUTETN = "routetn";
    public static final String SNROUT = "snrout";
    public static final String BTYPE = "btype";
    public static final String VALIDR = "validr";

    //FARE MATRICS
    public static final String TABLE_FAREMATRICS = "farematrix";
    public static final String GROUP = "grp";
    public static final String KMFRN = "kmfrn";
    public static final String KMTON = "kmton";
    public static final String KMFRT = "kmfrt";
    public static final String KMTOT = "kmtot";
    public static final String FAREG = "fareg";
    public static final String FARES = "fares";
    public static final String FARELET = "farelet";
    public static final String FARESAK = "faresak";
    public static final String FARETON = "fareton";


    //INSPECTOR
    public static final String TABLE_INSPECTORDATA = "inspectordata";
    public static final String INSUSER_ID = "userid";
    public static final String INSTYPE = "instype";
    public static final String INSPWORD = "password";
    public static final String INSNAME = "name";

    //INSPECTOR TRANSACTION
    public static final String TABLE_INSPECTORTRANS = "idtrn";
    public static final String TRANSNO = "transno";
    public static final String IATYPE = "iatype";
    public static final String KMON = "kmon";
    public static final String KMOFF = "kmoff";
    public static final String EXPENSES = "expenses";
    public static final String CASHONHAND = "cashonhand";
    public static final String SHORTAGE = "shortage";
    public static final String DEPOSIT = "deposit";


    //ANOMALY DATA
    public static final String TABLE_ANOMALYDATA = "anomalydata";
    public static final String ANOMALY_DESC = "anmdesc";
    public static final String ANOMALY_TYPE = "anmtype";
    public static final String ANOMALY_OPERATOR = "operator";
    public static final String ANOMALY_AMOUNT = "anmamt";

    //ADMIN
    public static final String TABLE_ADMIN = "admin";
    public static final String ADMIN_USER = "uname";
    public static final String ADMIN_PASSWORD = "pword";
    public static final String ADMIN_NAME = "name";
    public static final String ADMIN_POSITION = "position";

    //CURRENT TRIP ROUTE
    public static final String TABLE_TRIPROUTE = "triproute";
    public static final String ROUTE_TICKET_NO = "routeticketno";

    //ADJUST TICKET
    public static final String TABLE_ADJUSTICKET = "ticket_transaction";
    public static final String KMONNAME = "kmonname";
    public static final String KMTONAME = "kmtoname";
    public static final String PASSTYPEDESC = "passtypedesc";

    //BIR
    public static final String TABLE_BIR = "bir";
    public static final String TIN = "tin";

    //String Table Query
    public String CREATE_TABLE_BIR = "CREATE TABLE IF NOT EXISTS " +TABLE_BIR+ " (RecordNo INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TIN + " TEXT)";
    public String CREATE_TABLE_ADJUSTICKET = "CREATE TABLE IF NOT EXISTS " + TABLE_ADJUSTICKET +
            "(RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," + TICKET_NO + " INTEGER," + KMONNAME
            + " TEXT," + KMTONAME + " TEXT," + PASSTYPEDESC + " TEXT)";
    public String CREATE_TABLE_ADMIN = "CREATE TABLE IF NOT EXISTS " + TABLE_ADMIN +
            "(RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," + ADMIN_USER + " TEXT," + ADMIN_PASSWORD
            + " TEXT," + ADMIN_NAME + " TEXT," + ADMIN_POSITION + " TEXT)";
    public String CREATE_TABLE_ANOMALYDATA = "CREATE TABLE IF NOT EXISTS " + TABLE_ANOMALYDATA +
            "(RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," + ANMCOD + " TEXT," + ANOMALY_DESC + " TEXT," +
            ANOMALY_TYPE + " TEXT," + ANOMALY_OPERATOR + " TEXT," + ANOMALY_AMOUNT + " TEXT)";
    public String CREATE_TABLE_INSPECTORDATA = "CREATE TABLE IF NOT EXISTS " + TABLE_INSPECTORDATA +
            "(RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," + INSUSER_ID + " TEXT," + INSTYPE + " TEXT," +
            INSPWORD + " TEXT," + INSNAME + " TEXT)";
    public String CREATE_TABLE_TRIPROUTE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIPROUTE +
            "(RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," + TRIP_NO + " TEXT," + ROUTE_CODE + " TEXT," +
            ROUTEFN + " TEXT," + ROUTETN + " TEXT," + ROUTE_TICKET_NO + " TEXT)";
    public String CREATE_TABLE_FAREMATRICS = "CREATE TABLE IF NOT EXISTS " + TABLE_FAREMATRICS +
            "(RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," + ROUTEC + " TEXT," + BUS_TYPE + " TEXT," + GROUP + " TEXT," +
            KMFRN + " TEXT," + KMTON + " TEXT," + KMFRT + " TEXT," + KMTOT + " TEXT," + FAREG + " INTEGER," + FARES + " INTEGER," +
            FARELET + " INTEGER," + FARESAK + " INTEGER," + FARETON + " INTEGER)";
    public String CREATE_TABLE_BLUETOOTHNAME = "CREATE TABLE IF NOT EXISTS  " + TABLE_BLUETOOTH + "(" + DEVICE_NAME + " TEXT)";
    public String CREATE_TABLE_PROGRAMUSER= "CREATE TABLE IF NOT EXISTS  " + TABLE_PROGRAMUSER+ "(RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," +
            CONDUCTOR_ID +" TEXT," + CONDUCTOR_NAME +" TEXT,"+ DRIVER_ID +" TEXT," + DRIVER_NAME +
            " TEXT," + BUS_NO + " TEXT," + BUS_NAME+" TEXT, " + BUS_TYPE + " TEXT, " + ROUTE_CODE + " TEXT," + PASSWORD +" TEXT)";
    public String CREATE_TABLE_PASSENGERTICKET = "CREATE TABLE IF NOT EXISTS  " +
            TABLE_PASSENGER +
            "(recordno INTEGER PRIMARY KEY,"
            + TRIP_NO + " TEXT,"
            + ROUTEF + " TEXT,"
            + ROUTET +  " TEXT,"
            + ROUTE_CODE + " TEXT,"
            + DATE + " TEXT,"
            + TIME + " INTEGER,"
            + TICKET_NO + " INTEGER,"
            + TICKET_LETTER + " TEXT,"
            + BUS_NO + " TEXT,"
            + DRIVER_ID + " TEXT,"
            + CONDUCTOR_ID + " TEXT,"
            + KM_FROM_CODE + " INTEGER,"
            + KM_TO_CODE + " INTEGER,"
            + FARE + " DECIMAL(8, 2),"
            + FARE_TYPE + " TEXT,"
            + BAG_TYPE + " TEXT,"
            + INSID + " TEXT,"
            + INSTYPE + " TEXT,"
            + STATUS + " TEXT)";

    public String CREATE_TABLE_INSPECTORTRANSACTION = "CREATE TABLE IF NOT EXISTS  " +
            TABLE_INSPECTORTRANS +
            "(recordno INTEGER PRIMARY KEY,"
            + TRANSNO + " TEXT,"
            + TRIP_NO + " TEXT,"
            + ROUTEFN + " TEXT,"
            + ROUTETN +  " TEXT,"
            + ROUTE_CODE + " TEXT,"
            + DATE + " TEXT,"
            + TIME + " INTEGER,"
            + TICKET_NO + " INTEGER,"
            + TICKET_LETTER + " TEXT,"
            + BUS_NO + " TEXT,"
            + DRIVER_ID + " TEXT,"
            + CONDUCTOR_ID + " TEXT,"
            + KM_FROM_CODE + " TEXT,"
            + KM_TO_CODE + " TEXT,"
            + FARE + " DECIMAL(8, 2),"
            + FARE_TYPE + " TEXT,"
            + BAG_TYPE + " TEXT,"
            + INSID + " TEXT,"
            + INS_TYPE + " TEXT,"
            + IATYPE + " TEXT,"
            + KMON + " TEXT,"
            + KMOFF + " TEXT,"
            + ANMCOD + " TEXT,"
            + PAX_NO + " INTEGER,"
            + BAG_NO + " INTEGER,"
            + PAX_AMT + " INTEGER,"
            + BAG_AMT + " INTEGER,"
            + EXPENSES + " INTEGER,"
            + CASHONHAND + " INTEGER,"
            + DEPOSIT + " INTEGER,"
            + SHORTAGE + " INTEGER,"
            + REMARKS + " TEXT,"
            + REMARKS2 + " TEXT)";

    private String CREATE_TABLE_TICKETS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TICKETOR
            + " (RecordNo INTEGER PRIMARY KEY AUTOINCREMENT," +
            OR_FROM +  " INTEGER," +
            OR_TO + " INTEGER," +
            USED_FROM + " INTEGER," +
            USED_TO + " INTEGER," +
            OR_LETTER + " TEXT," +
            OR_USED_LETTER + " TEXT)";
    public String CREATE_TABLE_TRIPNO = "CREATE TABLE IF NOT EXISTS  " + TABLE_TRIPNO + "(" + TRIP_NO + " TEXT)";
    public String CREATE_TABLE_VALIDATION = "CREATE TABLE IF NOT EXISTS  " + TABLE_VALIDATION + "(" + TRIP_NO + " TEXT," + VALIDATION_KEYS + " TEXT)";
    public String CREATE_TABLE_ROUTES = "CREATE TABLE IF NOT EXISTS " + TABLE_ROUTES + "(" + ROUTEC + " TEXT," + ROUTEF + " TEXT," + ROUTET + " TEXT,"
            + ROUTEFN + " TEXT," + ROUTETN + " TEXT," + SNROUT + " TEXT," + BTYPE + " TEXT," + VALIDR + " TEXT)";

    private static DatabaseHelper mInstance = null;

    public static DatabaseHelper getInstance(Context ctx)
    {
        if(mInstance == null)
        {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public DatabaseHelper(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BIR);
        db.execSQL(CREATE_TABLE_ADJUSTICKET);
        db.execSQL(CREATE_TABLE_ADMIN);
        db.execSQL(CREATE_TABLE_ANOMALYDATA);
        db.execSQL(CREATE_TABLE_INSPECTORTRANSACTION);
        db.execSQL(CREATE_TABLE_INSPECTORDATA);
        db.execSQL(CREATE_TABLE_TRIPROUTE);
        db.execSQL(CREATE_TABLE_BLUETOOTHNAME);
        db.execSQL(CREATE_TABLE_PROGRAMUSER);
        db.execSQL(CREATE_TABLE_PASSENGERTICKET);
        //db.execSQL(CREATE_TABLE_TICKETNOTRANS);
        db.execSQL(CREATE_TABLE_TICKETS);
        db.execSQL(CREATE_TABLE_TRIPNO);
        db.execSQL(CREATE_TABLE_VALIDATION);
        db.execSQL(CREATE_TABLE_ROUTES);
        db.execSQL(CREATE_TABLE_FAREMATRICS);
        Log.d("Database Operations", "Database and Tables Created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADJUSTICKET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANOMALYDATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTORTRANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTORDATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUETOOTH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMUSER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSENGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPNO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALIDATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAREMATRICS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BIR);
        db.execSQL(CREATE_TABLE_ADJUSTICKET);
        db.execSQL(CREATE_TABLE_ADMIN);
        db.execSQL(CREATE_TABLE_ANOMALYDATA);
        db.execSQL(CREATE_TABLE_INSPECTORTRANSACTION);
        db.execSQL(CREATE_TABLE_INSPECTORDATA);
        db.execSQL(CREATE_TABLE_TRIPROUTE);
        db.execSQL(CREATE_TABLE_BLUETOOTHNAME);
        db.execSQL(CREATE_TABLE_PROGRAMUSER);
        db.execSQL(CREATE_TABLE_PASSENGERTICKET);
        db.execSQL(CREATE_TABLE_TICKETS);
        db.execSQL(CREATE_TABLE_TRIPNO);
        db.execSQL(CREATE_TABLE_VALIDATION);
        db.execSQL(CREATE_TABLE_ROUTES);
        db.execSQL(CREATE_TABLE_FAREMATRICS);


    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()){
            db.close();

        }

    }

    //Passeng Ticket Transaction
    public void insertTicketTransaction(DatabaseHelper dbHelper, String tripno, String tripfrom, String tripto,
                                     String tripcode, String today, String tme, int ticketno, String ticketletter,
                                     String busno, String driverid, String conid, String kmfrom, String kmto,
                                     String faretype, double fare, String bagtype, String insid, String instype, String status)
    {
        SQLiteDatabase sq = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRIP_NO, tripno);//1
        cv.put(ROUTEF, tripfrom);//2
        cv.put(ROUTET, tripto);//3
        cv.put(ROUTE_CODE, tripcode);//4
        cv.put(DATE, today);//5
        cv.put(TIME, tme);//6
        cv.put(TICKET_NO, ticketno);//7
        cv.put(TICKET_LETTER, ticketletter);//8
        cv.put(BUS_NO, busno);//9
        cv.put(DRIVER_ID, driverid);//10
        cv.put(CONDUCTOR_ID, conid);//11
        cv.put(KM_FROM_CODE, Integer.parseInt(kmfrom));//12
        cv.put(KM_TO_CODE, Integer.parseInt(kmto));//13
        cv.put(FARE, fare);//14
        cv.put(FARE_TYPE, faretype);//15
        cv.put(BAG_TYPE, bagtype);//16
        cv.put(INSID, insid);//17
        cv.put(INSTYPE, instype);//18
        cv.put(STATUS, status);//19


        long k = sq.insert(TABLE_PASSENGER, null, cv);
        Log.d("Database Operation", k + " successfully added");
    }

    public void insertOldInspectorTicketTransaction(DatabaseHelper dbHelper, String transno, String tripno, String routefn, String routetn,
                                                 String routecode, String today, String tme, int ticketno, String ticketletter,
                                                 String busno, String driverid, String conid, String kmfrom, String kmto,
                                                 double fare, String faretype, String bagtype, String insid, String instype,
                                                 String iatype, String kmon, String kmoff, String anmcod, int paxno, int bagno, int paxamt, int bagamt,
                                                 int expenses, int cashonhand, int deposits, int shortage,
                                                 String remarks, String remarks2)
    {
        SQLiteDatabase sq = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSNO, transno);//1
        cv.put(TRIP_NO, tripno);//2
        cv.put(ROUTEFN, routefn);//3
        cv.put(ROUTETN, routetn);//4
        cv.put(ROUTE_CODE, routecode);//5
        cv.put(DATE, today);//6
        cv.put(TIME, tme);//7
        cv.put(TICKET_NO, ticketno);//8
        cv.put(TICKET_LETTER, ticketletter);//9
        cv.put(BUS_NO, busno);//10
        cv.put(DRIVER_ID, driverid);//11
        cv.put(CONDUCTOR_ID, conid);//12
        cv.put(KM_FROM_CODE, Integer.parseInt(kmfrom));//13
        cv.put(KM_TO_CODE, Integer.parseInt(kmto));//14
        cv.put(FARE, fare);//15
        cv.put(FARE_TYPE, faretype);//16
        cv.put(BAG_TYPE, bagtype);//17
        cv.put(INSID, insid);//18
        cv.put(INS_TYPE, instype);//19
        cv.put(IATYPE, iatype);//20
        cv.put(KMON, kmon);//21
        cv.put(KMOFF, kmoff);//22
        cv.put(ANMCOD, anmcod);//23
        cv.put(PAX_NO, paxno);//24
        cv.put(BAG_NO, bagno);//25
        cv.put(PAX_AMT, paxamt);//26
        cv.put(BAG_AMT, bagamt);//27
        cv.put(EXPENSES, expenses);//28
        cv.put(CASHONHAND, cashonhand);//29
        cv.put(DEPOSIT, deposits);//30
        cv.put(SHORTAGE, shortage);//31
        cv.put(REMARKS, remarks);//32
        cv.put(REMARKS2, remarks2);//33
        long k = sq.insert(TABLE_INSPECTORTRANS, null, cv);
        Log.d("Database Operation", k + " successfully added");
    }
    public void insertInspectorTicketTransaction(DatabaseHelper dbHelper, String transno, String tripno, String routefn, String routetn,
                                                 String routecode, String today, String tme, int ticketno, String ticketletter,
                                                 String busno, String driverid, String conid, String kmfrom, String kmto,
                                                 double fare, String faretype, String bagtype, String insid, String instype,
                                                 String iatype, String kmon, String kmoff, String anmcod, int paxno, int bagno, int paxamt, int bagamt,
                                                 int expenses, int cashonhand, int deposits, int shortage,
                                                 String remarks, String remarks2) {
        SQLiteDatabase sq = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSNO, transno);//1
        cv.put(TRIP_NO, tripno);//2
        cv.put(ROUTEFN, routefn);//3
        cv.put(ROUTETN, routetn);//4
        cv.put(ROUTE_CODE, routecode);//5
        cv.put(DATE, today);//6
        cv.put(TIME, tme);//7
        cv.put(TICKET_NO, ticketno);//8
        cv.put(TICKET_LETTER, ticketletter);//9
        cv.put(BUS_NO, busno);//10
        cv.put(DRIVER_ID, driverid);//11
        cv.put(CONDUCTOR_ID, conid);//12
        cv.put(KM_FROM_CODE, Integer.parseInt(kmfrom));//13
        cv.put(KM_TO_CODE, Integer.parseInt(kmto));//14
        cv.put(FARE, fare);//15
        cv.put(FARE_TYPE, faretype);//16
        cv.put(BAG_TYPE, bagtype);//17
        cv.put(INSID, insid);//18
        cv.put(INS_TYPE, instype);//19
        cv.put(IATYPE, iatype);//20
        cv.put(KMON, kmon);//21
        cv.put(KMOFF, kmoff);//22
        cv.put(ANMCOD, anmcod);//23
        cv.put(PAX_NO, paxno);//24
        cv.put(BAG_NO, bagno);//25
        cv.put(PAX_AMT, paxamt);//26
        cv.put(BAG_AMT, bagamt);//27
        cv.put(EXPENSES, expenses);//28
        cv.put(CASHONHAND, cashonhand);//29
        cv.put(DEPOSIT, deposits);//30
        cv.put(SHORTAGE, shortage);//31
        cv.put(REMARKS, remarks);//32
        cv.put(REMARKS2, remarks2);//33
        long k = sq.insert(TABLE_INSPECTORTRANS, null, cv);
        Log.d("Database Operation", k + " successfully added");
    }

    public void insertBIR(DatabaseHelper db, String tin){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TIN, tin);
        long k = sq.insert(TABLE_BIR, null, cv);
        Log.d("DATABASE INSERT", "BIR INSERTED!");
    }
    public void updateBIR(DatabaseHelper db, String tin){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TIN, tin);
        long k = sq.update(TABLE_BIR, cv, null, null);
        Log.d("DATABASE INSERT", "BIR INSERTED!");
    }
    public Cursor getBIR(DatabaseHelper db){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from bir", null);
        return cr;
    }
    public void updateTickets(DatabaseHelper db, int orfrom, int orto, int usedfrom, int usedto, String ticketlet, String usedlet){
        SQLiteDatabase sq = db.getWritableDatabase();
        String qry = "";
        qry += "Update "+TABLE_TICKETOR;
        qry += " set orfrom="+orfrom;
        qry += ", orto="+orto;
        qry += ", usedfrom="+usedfrom;
        qry += ", usedto="+usedto;
        qry += ", orletter="+ticketlet;
        qry += ", orusedletter="+usedlet;
        qry += " where RecordNo=1";
        sq.execSQL(qry);
    }

    public Cursor getInspectorTrans(DatabaseHelper db){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from idtrn", null);
        return cr;
    }
    public Cursor getInspectorTransByID(DatabaseHelper db, String uid){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from idtrn WHERE TRIM(insid)='" + uid + "' ORDER BY recordno ASC", null);
        return cr;
    }
    public Cursor searchAnomalyData(DatabaseHelper db, String anmcode)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from anomalydata where TRIM(anmcod)='"+ anmcode.trim() +"'", null);
        return cr;
    }
    public Cursor getAnomalyData(DatabaseHelper db)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from anomalydata", null);
        return cr;
    }
    public Cursor getAllTransData(DatabaseHelper db)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from tkt_trans", null);
        return cr;
    }
    public Cursor getTransData(DatabaseHelper db, String paxticket)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from tkt_trans where ticketno="+ Integer.parseInt(paxticket.trim()) +"", null);
        return cr;
    }
    public void cancelTransData(DatabaseHelper db, String paxticket, String faretype, String bagType, double fare, String date, String time,String insid, String instype, String status)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "UPDATE tkt_trans set faretype='" + faretype +"', bagtype='" + bagType + "', fare=" + fare + ", date='" + date
                + "', time='" + time + "', insid='"+ insid.trim() +"',instype='"+ instype.trim() +"', status='"+ status.trim() +"'   where ticketno="+ Integer.parseInt(paxticket.trim())  + "";
        sq.execSQL(qry);
    }
    public void updateTransData(DatabaseHelper db, String paxticket, String kmfrom, String kmto, String faretype, String bagType, double fare, String date, String time,String insid, String instype, String status)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "UPDATE tkt_trans set kmfromcode=" + Integer.parseInt(kmfrom) + ", kmtocode=" + Integer.parseInt(kmto)
                + ", faretype='" + faretype +"', bagtype='" + bagType + "', fare=" + fare + ", date='" + date
                + "', time='" + time + "', insid='"+ insid.trim() +"',instype='"+ instype.trim() +"', status='"+ status.trim() +"'  where ticketno="+ Integer.parseInt(paxticket.trim()) + "";
        sq.execSQL(qry);
    }


    //CUT OFF KM POST SELF AUDIT
    public  Cursor getPaxTicketFull(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where faretype = 'F' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }
    public  Cursor getPaxTicketSP(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where faretype = 'SP' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }public  Cursor getPaxTicketHalf(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where faretype = 'H' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }public  Cursor getPaxTicketOP(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where faretype = 'OP' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }
    public  Cursor getPaxTicketLetter(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where bagtype = 'L' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }
    public  Cursor getPaxTicketSacks(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where bagtype = 'S' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }public  Cursor getPaxTicketKilos(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where bagtype = 'K' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }public  Cursor getPaxTicketOB(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where bagtype = 'OB' and kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }
    public Cursor getPaxTicketTransCountBag(DatabaseHelper db, String tripno,  String kmpost)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0) from tkt_trans where faretype = 'B' and kmtocode=";
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }
    public Cursor getPaxTicketTransCountPax(DatabaseHelper db, String tripno,  String kmpost)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0) from tkt_trans where faretype != 'B' and kmtocode=";
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }
    public  Cursor getAdjustData(DatabaseHelper db, String tripno, String kmpost, String symbol){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry += "Select ifnull(count(*), 0), ifnull(sum(fare), 0) from tkt_trans where kmtocode"+symbol;
        qry += "" + Integer.parseInt(kmpost) + " and tripno='" + tripno + "'";
        Cursor cr =sq.rawQuery(qry, null);
        return cr;
    }
    public Cursor getPaxTicketTransCurrentTrip(DatabaseHelper db, String tripno)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr =
                sq.rawQuery("Select routec, (Select IFNULL(count(*),0) from tkt_trans where faretype = 'F' and TRIM(tripno)='"
                        + tripno.trim() + "')as Full, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'F' and TRIM(tripno)='"+tripno.trim()+"')as FullAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where faretype = 'SP' and TRIM(tripno)='"+tripno.trim()+"')as SP, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'SP' and TRIM(tripno)='"+tripno.trim()+"')as SPAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where faretype = 'H' and TRIM(tripno)='"+tripno.trim()+"')as Half, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'H' and TRIM(tripno)='"+tripno.trim()+"')as HalfAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where faretype = 'OP' and TRIM(tripno)='"+tripno.trim()+"')as OtherPaxType, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'OP' and TRIM(tripno)='"+tripno.trim()+"')as OPAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'L' and TRIM(tripno)='"+tripno.trim()+"')as Letter, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'L' and TRIM(tripno)='"+tripno.trim()+"')as LetterAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'S' and TRIM(tripno)='"+tripno.trim()+"')as Sacks, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'S' and TRIM(tripno)='"+tripno.trim()+"')as SacksAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'K' and TRIM(tripno)='"+tripno.trim()+"')as Kilos, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'K' and TRIM(tripno)='"+tripno.trim()+"')as KilosAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'OB' and TRIM(tripno)='"+tripno.trim()+"')as OtherBaggage, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'OB' and TRIM(tripno)='"+tripno.trim()+"')as OBAmount " +
                        "from tkt_trans where TRIM(tripno)= '"+tripno.trim()+"' GROUP BY routec", null);
        return cr;
    }
    public Cursor getPaxTicketTransAllTrips(DatabaseHelper db)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr =
                sq.rawQuery("Select routec, (Select IFNULL(count(*),0) from tkt_trans where faretype = 'F')as Full, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'F')as FullAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where faretype = 'SP')as SP, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'SP')as SPAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where faretype = 'H')as Half, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'H')as HalfAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where faretype = 'OP')as OtherPaxType, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where faretype = 'OP')as OPAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'L')as Letter, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'L')as LetterAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'S')as Sacks, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'S')as SacksAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'K')as Kilos, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'K')as KilosAmount, " +
                        "(Select IFNULL(count(*),0) from tkt_trans where bagtype = 'OB')as OtherBaggage, " +
                        "(Select IFNULL(sum(fare),0) from tkt_trans where bagtype = 'OB')as OBAmount " +
                        "from tkt_trans GROUP BY routec", null);
        return cr;
    }
    public void insertTripRoute(DatabaseHelper dbHelper, String tripno, String routec, String routefn, String routetn, String routeticketno)
    {
        SQLiteDatabase sq = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRIP_NO, tripno);
        cv.put(ROUTE_CODE, routec);
        cv.put(ROUTEFN, routefn);
        cv.put(ROUTETN, routetn);
        cv.put(ROUTE_TICKET_NO, routeticketno);
        long k = sq.insert(TABLE_TRIPROUTE, null, cv);
        Log.d("Database Operation", k + " successfully triproute added");

    }
    public void insertAdmin(DatabaseHelper dbHelper, String uname, String pword, String name, String position)
    {
        SQLiteDatabase sq = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ADMIN_USER, uname);
        cv.put(ADMIN_PASSWORD, pword);
        cv.put(ADMIN_NAME, name);
        cv.put(ADMIN_POSITION, position);
        long k = sq.insert(TABLE_ADMIN, null, cv);
        Log.d("Database Operation", k + " successfully admin added");

    }

    public Cursor selectAdmin(DatabaseHelper db){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from admin", null);
        return cr;
    }
    public Cursor getAdmin(DatabaseHelper db, String uname, String pword){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from admin where TRIM(uname)='" + uname.trim() + "' and TRIM(pword)='" + pword + "'", null);
        return cr;
    }
    public Cursor getAdminApproval(DatabaseHelper db, String pword){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from admin where TRIM(pword)='" + pword + "'", null);
        return cr;
    }

    public void deleteTicketTransaction(DatabaseHelper db){
        SQLiteDatabase sq = db.getWritableDatabase();
        sq.beginTransaction();
        long l = sq.delete(TABLE_PASSENGER, null, null);
        long m = sq.delete(TABLE_TRIPROUTE, null, null);
        //long n = sq.delete(TABLE_TRIPNO, null, null);
        sq.setTransactionSuccessful();
        sq.endTransaction();
    }
    public void deleteInspectorTransaction(DatabaseHelper db){
        SQLiteDatabase sq = db.getWritableDatabase();
        sq.beginTransaction();
        long k = sq.delete(TABLE_INSPECTORTRANS, null, null);
        //long n = sq.delete(TABLE_TRIPNO, null, null);
        sq.setTransactionSuccessful();
        sq.endTransaction();
    }
    public Cursor getTripRoute(DatabaseHelper dbHelper)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String[] columns = {TRIP_NO, ROUTE_CODE, ROUTEFN, ROUTETN, ROUTE_TICKET_NO};
        Cursor cr = sq.query(TABLE_TRIPROUTE, columns, null, null, null, null, null);
        return cr;
    }

    //BLUETOOTH DEVICE
    public void insertBTDevice(DatabaseHelper dbHelper, String device_name)
    {
        SQLiteDatabase sq = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DEVICE_NAME, device_name);
        long k = sq.insert(TABLE_BLUETOOTH, null, cv);
        Log.d("Database Operation", k + " successfully added");

    }
    public Cursor getDevice(DatabaseHelper dbHelper)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String[] columns = {DEVICE_NAME};
        Cursor cr = sq.query(TABLE_BLUETOOTH, columns, null, null, null, null, null);
        return cr;
    }
    public void deleteDevice()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BLUETOOTH);
    }

    //PROGRAM USER
    public Cursor getProgramUser(DatabaseHelper dbHEHelper)
    {
        SQLiteDatabase sq = dbHEHelper.getReadableDatabase();
        String[] columns = {CONDUCTOR_ID, CONDUCTOR_NAME, DRIVER_ID, DRIVER_NAME, BUS_NO, BUS_NAME, BUS_TYPE, ROUTE_CODE, PASSWORD};
        Cursor cr = sq.query(TABLE_PROGRAMUSER, columns, null, null, null, null, null);
        return cr;
    }
    public void updateProgramUser(DatabaseHelper db, String conid, String conname, String driverid, String drivername, String busno, String busname, String bustype, String routecode, String password){
        SQLiteDatabase sq = db.getWritableDatabase();
        String query = "";
        query += "UPDATE programuser ";
        query += "set conid='" + conid + "', ";
        query += "conname='" + conname + "', ";
        query += "drivid='" + driverid +  "', ";
        query += "drivname='" + drivername + "', ";
        query += "busno='" + busno +  "', ";
        query += "busname='" + busname + "', ";
        query += "bustype='" + bustype + "', ";
        query += "routec='" + routecode + "', ";
        query += "password='" + password + "'";
        query += " where RecordNo=1";
        sq.execSQL(query);

    }

    //SEARCH VALIDATION FOR KM CODE IF EXISTS
    public Cursor getKmCode(DatabaseHelper db, String kmcode){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("Select * from tkt_Trans where kmtocode='" + kmcode.trim() + "'", null);
        return cr;
    }

    //ROUTES INFORMATION
    public Cursor getRoutes(DatabaseHelper dbHelper)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String[] columns = {ROUTEC,ROUTEF,ROUTET,ROUTEFN,ROUTETN,SNROUT,VALIDR};
        Cursor cr = sq.query(TABLE_ROUTES, columns, null, null, null, null, ROUTEC);
        return cr;
    }

    public Cursor RouteCode(DatabaseHelper db, String rc, String btype)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor c = sq.rawQuery("SELECT * FROM routes WHERE TRIM(routec) = '" + rc.trim() + "' and TRIM(btype) = '" + btype.trim() + "'", null);
        return c;
    }
    public Cursor getKMPostRouteCode(DatabaseHelper db, String rc, String btype)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor c = sq.rawQuery("SELECT routefn, routetn, validr FROM routes WHERE TRIM(routec) = '" + rc.trim() + "' and TRIM(btype) = '" + btype.trim() + "'", null);
        return c;
    }
    //FARE MATRICS
    public Cursor getFareMatrics(DatabaseHelper dbHelper)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String[] columns = {ROUTEC, BUS_TYPE, GROUP, KMFRN, KMTON, KMFRT, KMTOT, FAREG, FARES, FARELET, FARESAK, FARETON};
        Cursor cr = sq.query(TABLE_FAREMATRICS, columns, null, null, null, null, null);
        return cr;
    }
    //INSPECTOR DATA
    public Cursor getInspectorData(DatabaseHelper dbHelper)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String[] columns = {INSUSER_ID, INSTYPE, INSPWORD , INSNAME};
        Cursor cr = sq.query(TABLE_INSPECTORDATA, columns, null, null, null, null, null);
        return cr;
    }

    public Cursor loginInspector(DatabaseHelper db, String uid, String pword)
    {
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("SELECT * from inspectordata where TRIM(userid)='" + uid.trim() +
                "' and TRIM(password)='" + pword.trim() + "'", null);
        return cr;
    }
    public Cursor getFareMatricsRouteCode(DatabaseHelper dbHelper, String routec, String bustype)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        Cursor cr = sq.rawQuery("SELECT kmfrn, kmfrt, kmton, kmtot, fareg, fares, farelet, faresak, fareton FROM farematrix WHERE TRIM(routec) = '"
                + routec.trim() + "'and TRIM(bustype)='" + bustype.trim() + "'", null);
        return cr;
    }


    public Cursor getTicketInfo(DatabaseHelper dbHelper){
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String[] columns = {USED_TO, OR_USED_LETTER, OR_TO};
        Cursor cr = sq.query(TABLE_TICKETOR, columns, null, null, null, null, null);
        return cr;
    }
    public Cursor getAllTicket(DatabaseHelper dbHelper){
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String qry = "";
        qry = "Select * from "+TABLE_TICKETOR;
        Cursor cr = sq.rawQuery(qry, null);
        return cr;
    }
    public void insertAdjustTicket(DatabaseHelper dbHelper, int ticketno, String kmonname, String kmtoname, String passdesc){
        SQLiteDatabase sq = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TICKET_NO, ticketno);
        cv.put(KMONNAME, kmonname);
        cv.put(KMTONAME, kmtoname);
        cv.put(PASSTYPEDESC, passdesc);
        long k = sq.insert(TABLE_ADJUSTICKET, null, cv);

    }


    public void updateAdjustTicketInfo(DatabaseHelper db, int ticketno, String kmonname, String kmtoname, String passdesc){
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KMONNAME, kmonname);
        cv.put(KMTONAME, kmtoname);
        cv.put(PASSTYPEDESC, passdesc);
        long k = sq.update(TABLE_ADJUSTICKET, cv, "ticketno" + "=" + ticketno, null);
    }
    public Cursor getAdjustTicketInfo(DatabaseHelper db, int ticketno){
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor cr = sq.rawQuery("SELECT * FROM " + TABLE_ADJUSTICKET + " WHERE " + TICKET_NO + "=" + ticketno, null);
        return cr;

    }

    public void deleteAdjustTicketInfo(DatabaseHelper db, int ticketno){
        SQLiteDatabase sq = db.getWritableDatabase();
        sq.execSQL("DELETE from "+TABLE_ADJUSTICKET+" Where ticketno="+ticketno);
    }
    public void delAdjustTicketInfo(DatabaseHelper db){
        SQLiteDatabase sq = db.getWritableDatabase();
        sq.execSQL("DELETE from "+TABLE_ADJUSTICKET);
    }
    public void updateTicketInfo(DatabaseHelper dbHelper, int ticket){
        SQLiteDatabase sq = dbHelper.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put(USED_TO, ticket);
            long k = sq.update(TABLE_TICKETOR, cv, "RecordNo" + "=" + 1, null);
            //sq.rawQuery("Update ticket_or set usedto=",null);
            Log.d("Database Operations", "Successfully Update!");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public Cursor getValidationKeys(DatabaseHelper dbHelper, String tripno)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        Cursor cr = sq.rawQuery("SELECT * FROM tripvalidation WHERE TRIM(tripno) = '"+tripno.trim()+"'", null);
        return cr;
    }
    public Cursor readValidationKeys(DatabaseHelper dbHelper)
    {
        SQLiteDatabase sq = dbHelper.getReadableDatabase();
        String[] columns = {TRIP_NO,VALIDATION_KEYS};
        Cursor cr = sq.query(TABLE_VALIDATION, columns, null,null, null,null,null);
        return cr;
    }
    public void insertDataFromTextFile(String query)
    {
        String msg = "";
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);

        }catch (Exception e)
        {
            e.printStackTrace();
            msg = e.toString();
        }
        //return msg;

    }

    public Cursor getBustype(DatabaseHelper db){
        SQLiteDatabase sq = db.getReadableDatabase();
        String qry = "";
        qry = "Select bustype from programuser";
        Cursor cr = sq.rawQuery(qry, null);
        return cr;
    }
}
