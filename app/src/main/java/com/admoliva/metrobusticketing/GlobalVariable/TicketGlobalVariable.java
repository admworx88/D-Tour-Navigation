package com.admoliva.metrobusticketing.GlobalVariable;

/**
 * Created by Aljon Moliva on 7/14/2015.
 */
public class TicketGlobalVariable {
    private static TicketGlobalVariable instance;
    private int flag = 0;
    String ticketOR, dates;
    String kilometerFrom, kilometerFromDesc, kilometerTo, kilometerToDesc;
    String tripNo, tripCode, PassType;
    String Amount;

    private TicketGlobalVariable(){}

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        this.Amount = amount;
    }

    public String getTicketOR() {
        return ticketOR;
    }

    public void setTicketOR(String ticketOR) {
        this.ticketOR = ticketOR;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getKilometerFrom() {
        return kilometerFrom;
    }


    public String getKilometerTo() {
        return kilometerTo;
    }

    public void setKilometerTo(String kilometerTo) {
        this.kilometerTo = kilometerTo;
    }

    public void setKilometerFrom(String kilometerFrom) {
        this.kilometerFrom = kilometerFrom;
    }

    public String getKilometerFromDesc() {
        return kilometerFromDesc;
    }

    public void setKilometerFromDesc(String kilometerFromDesc) {
        this.kilometerFromDesc = kilometerFromDesc;
    }

    public String getKilometerToDesc() {
        return kilometerToDesc;
    }

    public void setKilometerToDesc(String kilometerToDesc) {
        this.kilometerToDesc = kilometerToDesc;
    }

    public String getTripNo() {
        return tripNo;
    }

    public void setTripNo(String tripNo) {
        this.tripNo = tripNo;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public String getPassType() {
        return PassType;
    }

    public void setPassType(String passType) {
        PassType = passType;
    }

    public static synchronized TicketGlobalVariable getInstance(){
        if(instance==null){
            instance=new TicketGlobalVariable();
        }
        return instance;
    }
}
