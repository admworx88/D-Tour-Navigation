package com.admoliva.metrobusticketing.Model;

/**
 * Created by jUniX on 10/12/2015.
 */
public class AnomalyData {

    /*public static final String ANOMALYCODE = "anmcod";
    public static final String ANOMALYDESC = "anmdesc";*/
    String AnomalyCode = null;
    String AnomalyDesc = null;

    public AnomalyData(String code, String desc){
        super();
        this.AnomalyCode = code;
        this.AnomalyDesc = desc;
    }

    public String getAnomalyCode() {
        return AnomalyCode;
    }

    public void setAnomalyCode(String anomalyCode) {
        this.AnomalyCode = anomalyCode;
    }

    public String getAnomalyDesc() {
        return AnomalyDesc;
    }

    public void setAnomalyDesc(String anomalyDesc) {
        this.AnomalyDesc = anomalyDesc;
    }
    @Override
    public String toString() {
        return  AnomalyCode + " " + AnomalyDesc;
    }

}
