package com.example.android.vihaanhack.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.example.android.vihaanhack.Activities.MapActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by ishaandhamija on 28/10/17.
 */

public class GPSTracker2 extends Service implements LocationListener {

    private final Context mContext;

    private static final int LOC_REQ_CODE = 9876;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    boolean isGPSTrackingEnabled = false;

    boolean markerPut = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    protected LocationManager locationManager;

    public GPSTracker2(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled){
                this.isGPSTrackingEnabled = true;
            }

            if (isNetworkEnabled){
                this.isGPSTrackingEnabled = true;
            }

            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        //noinspection MissingPermission
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            MapActivity.latitude = latitude;
                            MapActivity.longitude = longitude;
                            MapActivity.getLocation.onSuccess();
                            markerPut = true;
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        //noinspection MissingPermission
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            //noinspection MissingPermission
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                MapActivity.latitude = latitude;
                                MapActivity.longitude = longitude;
                                MapActivity.getLocation.onSuccess();
                                markerPut = true;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
    @Override
    public void onLocationChanged(Location location) {

        if (!markerPut) {

            this.location = location;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            MapActivity.latitude = latitude;
            MapActivity.longitude = longitude;
            MapActivity.getLocation.onSuccess();
//
//
            markerPut = true;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ((Activity) mContext).startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), LOC_REQ_CODE);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public boolean getIsGPSTrackingEnabled() {

        return this.isGPSTrackingEnabled;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
//            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public String getAddressLine(Context context, double thisLan, double thisLon) {
        List<Address> addresses = getGeocoderAddress(context, thisLan, thisLon);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        } else {
            return null;
        }
    }

    public List<Address> getGeocoderAddress(Context context, double thisLat, double thisLon) {
        if (location != null) {

            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                /**
                 * Geocoder.getFromLocation - Returns an array of Addresses
                 * that are known to describe the area immediately surrounding the given latitude and longitude.
                 */
                List<Address> addresses = geocoder.getFromLocation(thisLat, thisLon, 1);

                return addresses;
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e(TAG, "Impossible to connect to Geocoder", e);
            }
        }

        return null;
    }

    public String getLocality(Context context, double thisLat, double thisLon) {
        List<Address> addresses = getGeocoderAddress(context, thisLat, thisLon);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getLocality();

            Log.d("OO", "getLocality: " + locality);
            return locality;
        }
        else {
            return null;
        }
    }
}