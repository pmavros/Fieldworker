package org.urbancortex.fieldworker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class locations implements LocationListener {

    public static double lat;
    public static double lon;
    public static double speed;
    public static double accuracy;
    public static double elevation;
    public static double bearing;

    // The minimum distance to change Updates in meters
    private static final long MIN_UPDATE_DISTANCE = 1;

    // The minimum time between updates in milliseconds
    private static final long MIN_UPDATE_TIME = 1000 * 1;

    // The minimum time between logs in milliseconds
    private static final long MIN_LOG_TIME = 1000 * 5;


    private static LocationManager lm;

    public enum ProviderType{
        NETWORK,
        GPS
    };
    private String provider;

    private Location lastLocation;
    private long lastTime;

    public locations(Context context, ProviderType type) {
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if(type == ProviderType.NETWORK){
            provider = LocationManager.NETWORK_PROVIDER;
        }
        else{
            provider = LocationManager.GPS_PROVIDER;
        }
    }
    private static boolean isGPSRunning = false;

    public void start(){

        if(isGPSRunning){
            //Already running, do nothing
            return;
        } else {

            //The provider is on, so start getting updates.  Update current location
            lm.requestLocationUpdates(provider, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
            lastLocation = null;
            lastTime = 0;
            isGPSRunning = false;
            return;

        }


    }

    public void stop(){
        if(isGPSRunning){
            lm.removeUpdates(this);
            isGPSRunning = false;
        }
    }

    public boolean hasLocation(){
        if(lastLocation == null){
            return false;
        }
        if(System.currentTimeMillis() - lastTime > 5 * MIN_UPDATE_TIME){
            return false; //stale
        }
        return true;
    }

    public boolean hasPossiblyStaleLocation(){
        if(lastLocation != null){
            return true;
        }
        return lm.getLastKnownLocation(provider)!= null;
    }

    public Location getLocation(){
        if(lastLocation == null){
            return null;
        }
        if(System.currentTimeMillis() - lastTime > 5 * MIN_UPDATE_TIME){
            return null; //stale
        }
        return lastLocation;
    }

    public Location getPossiblyStaleLocation(){
        if(lastLocation != null){
            return lastLocation;
        }
        return lm.getLastKnownLocation(provider);
    }

    public void onLocationChanged(Location newLoc) {
        long now = System.currentTimeMillis();
        if(newLoc != null){

            lat = newLoc.getLatitude();
            lon = newLoc.getLongitude();
            speed = newLoc.getSpeed();
            accuracy = newLoc.getAccuracy();
            elevation = newLoc.getAltitude();
            bearing = newLoc.getBearing();

        }
        lastLocation = newLoc;
        lastTime = now;
        if(newLoc.hasAccuracy()){
//            Buttons.updateGPS(newLoc.getAccuracy());
        }
    }

    public void onProviderDisabled(String arg0) {

    }

    public void onProviderEnabled(String arg0) {

    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }
}
