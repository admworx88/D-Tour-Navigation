package com.admoliva.metrobusticketing.GlobalVariable;

/**
 * Created by jUniX on 10/19/2015.
 */
public class KmPostVariable {
    private static KmPostVariable instance = null;

    String kmfrn, kmton, kmfrt, kmtot;
    int fares, fareg, farelet, faresak, fareton;

    public KmPostVariable(){}

    public String getKmfrn() {
        return kmfrn;
    }

    public void setKmfrn(String kmfrn) {
        this.kmfrn = kmfrn;
    }

    public String getKmton() {
        return kmton;
    }

    public void setKmton(String kmton) {
        this.kmton = kmton;
    }

    public String getKmfrt() {
        return kmfrt;
    }

    public void setKmfrt(String kmfrt) {
        this.kmfrt = kmfrt;
    }

    public String getKmtot() {
        return kmtot;
    }

    public void setKmtot(String kmtot) {
        this.kmtot = kmtot;
    }

    public int getFares() {
        return fares;
    }

    public void setFares(int fares) {
        this.fares = fares;
    }

    public int getFareg() {
        return fareg;
    }

    public void setFareg(int fareg) {
        this.fareg = fareg;
    }

    public int getFarelet() {
        return farelet;
    }

    public void setFarelet(int farelet) {
        this.farelet = farelet;
    }

    public int getFaresak() {
        return faresak;
    }

    public void setFaresak(int faresak) {
        this.faresak = faresak;
    }

    public int getFareton() {
        return fareton;
    }

    public void setFareton(int fareton) {
        this.fareton = fareton;
    }

    public static synchronized  KmPostVariable getInstance()
    {
        if(instance == null)
        {
            instance = new KmPostVariable();
        }
        return instance;
    }
}
