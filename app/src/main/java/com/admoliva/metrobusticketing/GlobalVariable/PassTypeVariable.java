package com.admoliva.metrobusticketing.GlobalVariable;

import com.admoliva.metrobusticketing.Model.PassType;

/**
 * Created by jUniX on 10/19/2015.
 */
public class PassTypeVariable {
    public static PassTypeVariable instance = null;

    String passcode, passdesc;
    int amount;

    public PassTypeVariable(){}

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getPassdesc() {
        return passdesc;
    }

    public void setPassdesc(String passdesc) {
        this.passdesc = passdesc;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static synchronized PassTypeVariable getInstance()
    {
        if(instance == null)
        {
            instance = new PassTypeVariable();
        }
        return instance;
    }
}
