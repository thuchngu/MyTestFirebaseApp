package com.mytestfirebaseapp;

import java.util.concurrent.TimeUnit;

public class Device {
    String name;
    String type;
    int idVal;
    boolean isActive;
    static double data;

    public Device(){}

    public Device(String myName, String myType, int myIdVal){
        name = myName;
        type = myType;
        idVal = myIdVal;
        isActive = true;
    }

    public void generateTempData() throws InterruptedException{
        double max = 75.0;
        double min = 70.0;
        while(isActive){
            data = (Math.random() * (max - min)) + min;
            TimeUnit.MINUTES.sleep(30);
        }
    }

    public void generateHumidData() throws InterruptedException{
        double max = 40.0;
        double min = 35.0;
        while(isActive){
            data = (Math.random() * (max - min)) + min;
            TimeUnit.MINUTES.sleep(30);
        }
    }

    public void endDevice(){
        isActive = false;
        data = 0;
    }
}