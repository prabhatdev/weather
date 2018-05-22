package com.example.prabh.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class Loc_Listener implements LocationListener{
    Location loc;
    public double lat=19.09,lon=72.89;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null)
        {
            Log.d("Lat:",String.format("%s",location.getLatitude()));
            Log.d("Lon:",String.format("%s",location.getLongitude()));
            this.lat=location.getLatitude();
            this.lon=location.getLongitude();
        }
    }
   /* public String generateUrl()
    {
        String url=String.format("http://api.openweathermap.org/data/2.5/weather?lat=%.2f&lon=%.2f&appid=d141c211a6099a6460b108c1c1b86335",this.lat,this.lon);
        return url;
    }*/
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
