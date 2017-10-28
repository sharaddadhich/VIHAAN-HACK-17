package com.example.android.vihaanhack.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.vihaanhack.Interfaces.OnGetLocation;
import com.example.android.vihaanhack.R;
import com.example.android.vihaanhack.Utils.DirectionsJSONParser;
import com.example.android.vihaanhack.Utils.GPSTracker2;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.android.vihaanhack.R.id.map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment mapFragment;
    public static GPSTracker2 gps;
    public static Double latitude = null, longitude = null;
    public static OnGetLocation getLocation;
    public static Boolean gpsIsEnabled = false;

    ProgressDialog progressDialog;

    GoogleMap mapp;
    Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportActionBar().hide();

        gps = new GPSTracker2(MapActivity.this);

        progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage("Fetching Location...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

//        getLoc();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mapp = googleMap;
        getLocation = new OnGetLocation() {
            @Override
            public void onSuccess() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude)));
                googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(28.7461, 77.1161)));
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.7501, 77.1177), 15.0f));
//                String url = getDirectionsUrl(new LatLng(latitude, longitude),
//                        new LatLng(28.7461, 77.1161));
                String url = getDirectionsUrl(new LatLng(28.7501, 77.1177),
                        new LatLng(28.7461, 77.1161));

                DownloadTask downloadTask = new DownloadTask();

                downloadTask.execute(url);
            }
        };

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(28.7501, 77.1177)));
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(28.7461, 77.1161)));
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.7501, 77.1177), 15.0f));
//                String url = getDirectionsUrl(new LatLng(latitude, longitude),
//                        new LatLng(28.7461, 77.1161));
                String url = getDirectionsUrl(new LatLng(28.7501, 77.1177),
                        new LatLng(28.7461, 77.1161));

                DownloadTask downloadTask = new DownloadTask();

                downloadTask.execute(url);
            }
        }, 5000);

//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(28.7501, 77.1177)));
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(28.7461, 77.1161)));
////                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.7501, 77.1177), 15.0f));
////                String url = getDirectionsUrl(new LatLng(latitude, longitude),
////                        new LatLng(28.7461, 77.1161));
//        String url = getDirectionsUrl(new LatLng(28.7501, 77.1177),
//                new LatLng(28.7461, 77.1161));
//
//        DownloadTask downloadTask = new DownloadTask();
//
//        downloadTask.execute(url);
    }

    private void getLoc() {
        if (gps.getIsGPSTrackingEnabled() && gps.canGetLocation()){
            gpsIsEnabled = true;
            gps.getLocation();
        }
        else{
            gps.showSettingsAlert();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9876){

            gps = new GPSTracker2(MapActivity.this);

            if (gps.getIsGPSTrackingEnabled()){
                gpsIsEnabled = true;
                gps.getLocation();
            }

            else{
//                Toast.makeText(MapActivity.this, "Turn on Location", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.GREEN);
            }

            if (polyline != null){
                polyline.remove();
            }

            if (lineOptions != null) {
                polyline = mapp.addPolyline(lineOptions);

//                Integer i = sharedpreferences.getInt("markerIndex", -1);
//
//                if (i > -1) {
//                    DecimalFormat df = new DecimalFormat("#.##");
//                    Toast.makeText(HomeScreenActivity.this, df.format(distance(latitude, longitude, cycleLocList.get(i).getLatitude(), cycleLocList.get(i).getLongitude(), 'K')).toString() + " km away", Toast.LENGTH_LONG).show();
//                }
            }
        }
    }
}