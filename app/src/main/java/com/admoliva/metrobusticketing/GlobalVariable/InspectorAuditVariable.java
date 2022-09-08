package com.admoliva.metrobusticketing.GlobalVariable;

import android.content.Intent;

/**
 * Created by jUniX on 11/16/2015.
 */
public class InspectorAuditVariable {
    public static InspectorAuditVariable instance = null;

    String insId, insName, insType;
    String kmon, kmoff, kmonname, kmtoname, passdesc;
    String ticketNo, ticketLetter;
    String inspectorFlag;
    String tripNo, routefn, routetn, routecode, date, time, tktno, tktlet, busno, driverid, conid,
            kmfrom, kmto, fare, faretype, bagtype, anmcode, anmdesc, paxno, bagno,
            paxamt, bagamt, transNo;


    public String getPassdesc() {
        return passdesc;
    }

    public void setPassdesc(String passdesc) {
        this.passdesc = passdesc;
    }

    public String getKmonname() {
        return kmonname;
    }

    public void setKmonname(String kmonname) {
        this.kmonname = kmonname;
    }

    public String getKmtoname() {
        return kmtoname;
    }

    public void setKmtoname(String kmtoname) {
        this.kmtoname = kmtoname;
    }

    public String getInspectorFlag() {
        return inspectorFlag;
    }

    public String getAnmdesc() {
        return anmdesc;
    }

    public void setAnmdesc(String anmdesc) {
        this.anmdesc = anmdesc;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public String getTripNo() {
        return tripNo;
    }

    public void setTripNo(String tripNo) {
        this.tripNo = tripNo;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getRoutefn() {
        return routefn;
    }

    public void setRoutefn(String routefn) {
        this.routefn = routefn;
    }

    public String getRoutetn() {
        return routetn;
    }

    public void setRoutetn(String routetn) {
        this.routetn = routetn;
    }

    public String getRoutecode() {
        return routecode;
    }

    public void setRoutecode(String routecode) {
        this.routecode = routecode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getConid() {
        return conid;
    }

    public void setConid(String conid) {
        this.conid = conid;
    }

    public String getKmfrom() {
        return kmfrom;
    }

    public void setKmfrom(String kmfrom) {
        this.kmfrom = kmfrom;
    }

    public String getKmto() {
        return kmto;
    }

    public void setKmto(String kmto) {
        this.kmto = kmto;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getFaretype() {
        return faretype;
    }

    public void setFaretype(String faretype) {
        this.faretype = faretype;
    }

    public String getBagtype() {
        return bagtype;
    }

    public void setBagtype(String bagtype) {
        this.bagtype = bagtype;
    }

    public String getAnmcode() {
        return anmcode;
    }

    public void setAnmcode(String anmcode) {
        this.anmcode = anmcode;
    }

    public String getPaxno() {
        return paxno;
    }

    public void setPaxno(String paxno) {
        this.paxno = paxno;
    }

    public String getBagno() {
        return bagno;
    }

    public void setBagno(String bagno) {
        this.bagno = bagno;
    }

    public String getPaxamt() {
        return paxamt;
    }

    public void setPaxamt(String paxamt) {
        this.paxamt = paxamt;
    }

    public String getBagamt() {
        return bagamt;
    }

    public void setBagamt(String bagamt) {
        this.bagamt = bagamt;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getTicketLetter() {
        return ticketLetter;
    }

    public void setTicketLetter(String ticketLetter) {
        this.ticketLetter = ticketLetter;
    }

    public void setInspectorFlag(String inspectorFlag) {
        this.inspectorFlag = inspectorFlag;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    public String getInsType() {
        return insType;
    }

    public void setInsType(String insType) {
        this.insType = insType;
    }


    public String getKmon() {
        return kmon;
    }

    public void setKmon(String kmon) {
        this.kmon = kmon;
    }

    public String getKmoff() {
        return kmoff;
    }

    public void setKmoff(String kmoff) {
        this.kmoff = kmoff;
    }

    public static synchronized InspectorAuditVariable getInstance()
    {
        if(instance == null)
        {
            instance = new InspectorAuditVariable();
        }
        return instance;
    }
}
