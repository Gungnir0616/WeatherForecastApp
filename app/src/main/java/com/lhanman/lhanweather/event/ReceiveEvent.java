package com.lhanman.lhanweather.event;

public class ReceiveEvent {

    public final String weatherId;
    public ReceiveEvent(String weatherId){
        this.weatherId = weatherId;
    }
}
