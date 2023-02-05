package com.example.crowddatacollection;

public class Details {

    Details()
    {

    }
    Details(float longi,float lati, String time)
    {
        this.longitude=longi;
        this.latitude = lati;
        this.dateAndTime=time;

    }


    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public float longitude, latitude;
    public String dateAndTime;


}
