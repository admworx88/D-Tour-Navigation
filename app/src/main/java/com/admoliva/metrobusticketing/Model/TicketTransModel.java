package com.admoliva.metrobusticketing.Model;

/**
 * Created by Aljon Moliva on 7/22/2015.
 */
public class TicketTransModel  {

    String tripno, tripcode, kmfromcode, kmfromdesc;
    String kmtocode, kmtodesc, faretype, fare, tripfrom;
    String tripto, date, time, ticketno, ticketletter, bno, driverid, conid;

    public TicketTransModel(){}

    public TicketTransModel(String tripno, String tripcode, String kmfromcode, String kmfromdesc, String kmtocode, String kmtodesc, String faretype, String fare, String tripfrom, String tripto, String date, String time, String ticketno, String ticketletter, String bno, String driverid, String conid) {
        this.tripno = tripno;
        this.tripcode = tripcode;
        this.kmfromcode = kmfromcode;
        this.kmfromdesc = kmfromdesc;
        this.kmtocode = kmtocode;
        this.kmtodesc = kmtodesc;
        this.faretype = faretype;
        this.fare = fare;
        this.tripfrom = tripfrom;
        this.tripto = tripto;
        this.date = date;
        this.time = time;
        this.ticketno = ticketno;
        this.ticketletter = ticketletter;
        this.bno = bno;
        this.driverid = driverid;
        this.conid = conid;
    }

    public String getBno() {
        return bno;
    }

    public void setBno(String bno) {
        this.bno = bno;
    }

    public String getTripno() {
        return tripno;
    }

    public void setTripno(String tripno) {
        this.tripno = tripno;
    }

    public String getTripcode() {
        return tripcode;
    }

    public void setTripcode(String tripcode) {
        this.tripcode = tripcode;
    }

    public String getKmfromcode() {
        return kmfromcode;
    }

    public void setKmfromcode(String kmfromcode) {
        this.kmfromcode = kmfromcode;
    }

    public String getKmfromdesc() {
        return kmfromdesc;
    }

    public void setKmfromdesc(String kmfromdesc) {
        this.kmfromdesc = kmfromdesc;
    }

    public String getKmtocode() {
        return kmtocode;
    }

    public void setKmtocode(String kmtocode) {
        this.kmtocode = kmtocode;
    }

    public String getKmtodesc() {
        return kmtodesc;
    }

    public void setKmtodesc(String kmtodesc) {
        this.kmtodesc = kmtodesc;
    }

    public String getFaretype() {
        return faretype;
    }

    public void setFaretype(String faretype) {
        this.faretype = faretype;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getTripfrom() {
        return tripfrom;
    }

    public void setTripfrom(String tripfrom) {
        this.tripfrom = tripfrom;
    }

    public String getTripto() {
        return tripto;
    }

    public void setTripto(String tripto) {
        this.tripto = tripto;
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

    public String getTicketno() {
        return ticketno;
    }

    public void setTicketno(String ticketno) {
        this.ticketno = ticketno;
    }

    public String getTicketletter() {
        return ticketletter;
    }

    public void setTicketletter(String ticketletter) {
        this.ticketletter = ticketletter;
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

}

