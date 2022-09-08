package com.admoliva.metrobusticketing.Model;

/**
 * Created by Aljon Moliva on 7/22/2015.
 */
public class TicketNoModel {

    String ticketno;
    String ticketLet;



    public TicketNoModel(){}

    public TicketNoModel(String ticketno, String ticketLet) {

        this.ticketno = ticketno;
        this.ticketLet = ticketLet;
    }


    public String getTicketLet() {
        return ticketLet;
    }

    public void setTicketLet(String ticketLet) {
        this.ticketLet = ticketLet;
    }

    public TicketNoModel(String ticketno) {
        this.ticketno = ticketno;
    }

    public String getTicketno() {
        return ticketno;
    }

    public void setTicketno(String ticketno) {
        this.ticketno = ticketno;
    }

    @Override
    public String toString() {
        return ticketno;
    }
}
