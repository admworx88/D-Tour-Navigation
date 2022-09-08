package com.admoliva.metrobusticketing.GlobalVariable;

/**
 * Created by jUniX on 12/29/2015.
 */
public class AdjustTicketGlobalVariable {
    private static AdjustTicketGlobalVariable instance;

    String routefrom, routeto, faretype;
    int amount;

    public AdjustTicketGlobalVariable(){}

    public String getRoutefrom() {
        return routefrom;
    }

    public void setRoutefrom(String routefrom) {
        this.routefrom = routefrom;
    }

    public String getRouteto() {
        return routeto;
    }

    public void setRouteto(String routeto) {
        this.routeto = routeto;
    }

    public String getFaretype() {
        return faretype;
    }

    public void setFaretype(String faretype) {
        this.faretype = faretype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static synchronized AdjustTicketGlobalVariable getInstance(){
        if(instance==null){
            instance=new AdjustTicketGlobalVariable();
        }
        return instance;
    }

}
