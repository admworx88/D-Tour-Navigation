package com.admoliva.metrobusticketing.GlobalVariable;

/**
 * Created by Aljon Moliva on 7/14/2015.
 */
public class GlobalVariable  {
    private static GlobalVariable instance;
    private int KmPostPosition = 0;
    String busno, busname, bustype;
    String conductorname, drivername, conid, drivid;
    String routecode;
    String routeticketno;

    String tripNo;

    private  GlobalVariable(){}

    public String getRouteticketno() {
        return routeticketno;
    }

    public void setRouteticketno(String routeticketno) {
        this.routeticketno = routeticketno;
    }

    public int getKmPostPosition() {
        return KmPostPosition;
    }

    public void setKmPostPosition(int kmPostPosition) {
        this.KmPostPosition = kmPostPosition;
    }

    public String getRoutecode() {
        return routecode;
    }

    public void setRoutecode(String routecode) {
        this.routecode = routecode;
    }

    public String getTripNo() {
        return tripNo;
    }

    public void setTripNo(String tripNo) {
        this.tripNo = tripNo;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getBusname() {
        return busname;
    }

    public String getBustype() {
        return bustype;
    }

    public void setBustype(String bustype) {
        this.bustype = bustype;
    }

    public void setBusname(String busname) {
        this.busname = busname;
    }

    public String getConductorname() {
        return conductorname;
    }

    public void setConductorname(String conductorname) {
        this.conductorname = conductorname;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getConid() {
        return conid;
    }

    public void setConid(String conid) {
        this.conid = conid;
    }

    public String getDrivid() {
        return drivid;
    }

    public void setDrivid(String drivid) {
        this.drivid = drivid;
    }

    public static synchronized GlobalVariable getInstance(){
        if(instance==null){
            instance=new GlobalVariable();
        }
        return instance;
    }
}
