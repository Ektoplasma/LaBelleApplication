package com.ektoplasma.valou.nevalost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ektoplasma on 20/06/16.
 */
public class FollowerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final Context mContext = this;

    public static final String RECEIVE_JSON = "com.your.package.RECEIVE_JSON";

    Marker itself;
    Marker target;
    MarkerOptions optionMarker;
    GetLocalisation malocalisation;
    LatLng targetlocalisation;
    Polyline polyline;
    Timer t;


    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_JSON)) {

                itself.setPosition(new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude()));
                float zoomlevel = (float) 16.0;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itself.getPosition(), zoomlevel));

                String url = getDirectionsUrl(new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude()), targetlocalisation);

                DownloadTask downloadTask = new DownloadTask();

                downloadTask.execute(url);

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_follower_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);
        malocalisation = new GetLocalisation(getApplicationContext());

        ProfileHead.setCarry(false);

        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        ProfilePack.getGeol(mContext);

                        double lon = ProfilePack.getMasterLong();
                        double lat = ProfilePack.getMasterLat();

                        targetlocalisation = new LatLng(lat, lon);

                        if(target != null) target.setPosition(targetlocalisation);
                        String url = getDirectionsUrl(new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude()), targetlocalisation);

                        DownloadTask downloadTask = new DownloadTask();

                        downloadTask.execute(url);
                    }
                });
            }
        }, 0, 10000);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        optionMarker = new MarkerOptions();
        optionMarker.position(new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude()));
        optionMarker.title(ProfilePack.getUsername());
        itself = mMap.addMarker(optionMarker);

        LatLng coordinate = new LatLng(malocalisation.getLatitude(),malocalisation.getLongitude());

        CameraPosition location = new CameraPosition.Builder()
                .target(coordinate)
                .zoom(20)
                .bearing(90)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(location));
        Log.d(FollowerMapsActivity.class.getName(), "Latitudemap -> " + malocalisation.getLatitude());
        Log.d(FollowerMapsActivity.class.getName(), "Longitudemap -> " + malocalisation.getLongitude());

        LatLng origin = new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude());

        targetlocalisation = new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude());
        target = mMap.addMarker(new MarkerOptions().position(targetlocalisation).title(ProfileHead.getUsername()));
        String url = getDirectionsUrl(origin, targetlocalisation);

        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(url);

    }

    /**********************************************************
     * Permet de récupérer l'itinéraire GoogleMap
     * @param p_origin
     * @param p_dest
     * @return url
     */
    private String getDirectionsUrl(LatLng p_origin, LatLng p_dest) {

        String str_origin = "origin=" + p_origin.latitude + "," + p_origin.longitude;

        String str_dest = "destination=" + p_dest.latitude + "," + p_dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&mode=walking";

        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    /**********************************************************
     * Permet de télécharger l'itinéraire
     * @param strUrl
     * @return
     * @throws IOException
     */

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Downloading url", e.toString());
        } finally {
            assert iStream != null;
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /***********************************************
     * Permet le calcul et l'affichage de l'itinéraire
     */

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);


                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            try{
                polyline.remove();
            }
            catch(Exception e){
                Log.d(FollowerMapsActivity.class.getName(), "Aucun polyline");
            }

            polyline = mMap.addPolyline(lineOptions);

        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    public void updateCamera(float bearing) {
        CameraPosition oldPos = mMap.getCameraPosition();

        CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }
}
