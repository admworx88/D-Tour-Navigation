package com.admoliva.metrobusticketing.GlobalVariable;

/**
 * Created by jUniX on 10/12/2015.
 */
public class PaxTicketVariable {
    public static PaxTicketVariable instance;
    String TripNo, RouteC, RouteCF, RouteCT, KmPostF, KmPostFN, KmPostT, KmPostTN, PaxType;
    int amount;

    public PaxTicketVariable(){}


    public String getTripNo() {
        return TripNo;
    }

    public void setTripNo(String tripNo) {
        this.TripNo = tripNo;
    }

    public String getRouteC() {
        return RouteC;
    }

    public void setRouteC(String routeC) {
        this.RouteC = routeC;
    }

    public String getRouteCF() {
        return RouteCF;
    }

    public void setRouteCF(String routeCF) {
        this.RouteCF = routeCF;
    }

    public String getRouteCT() {
        return RouteCT;
    }

    public void setRouteCT(String routeCT) {
        this.RouteCT = routeCT;
    }

    public String getKmPostF() {
        return KmPostF;
    }

    public void setKmPostF(String kmPostF) {
        this.KmPostF = kmPostF;
    }

    public String getKmPostFN() {
        return KmPostFN;
    }

    public void setKmPostFN(String kmPostFN) {
        this.KmPostFN = kmPostFN;
    }

    public String getKmPostT() {
        return KmPostT;
    }

    public void setKmPostT(String kmPostT) {
        this.KmPostT = kmPostT;
    }

    public String getKmPostTN() {
        return KmPostTN;
    }

    public void setKmPostTN(String kmPostTN) {
        this.KmPostTN = kmPostTN;
    }

    public String getPaxType() {
        return PaxType;
    }

    public void setPaxType(String paxType) {
        this.PaxType = paxType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static synchronized PaxTicketVariable getInstance()
    {
        if(instance == null)
        {
            instance = new PaxTicketVariable();
        }
        return instance;
    }
}
