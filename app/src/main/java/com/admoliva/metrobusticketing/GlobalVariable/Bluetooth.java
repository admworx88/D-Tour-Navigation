package com.admoliva.metrobusticketing.GlobalVariable;

/**
 * Created by Aljon Moliva on 8/9/2015.
 */
    public class Bluetooth {
        private static Bluetooth instance;
        String address;

        private Bluetooth(){}

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public static synchronized Bluetooth getInstance(){
            if(instance==null){
                instance=new Bluetooth();
            }
            return instance;
        }

    }
