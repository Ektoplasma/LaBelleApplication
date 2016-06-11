package com.ektoplasma.valou.nevalost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
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
import java.util.concurrent.Semaphore;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String role;
    private final Context mContext = this;

    public static final String RECEIVE_JSON = "com.your.package.RECEIVE_JSON";

    Marker quelquepart;
    Marker ailleurs;
    MarkerOptions optionMarker;
    GetLocalisation malocalisation;
    LatLng dest;
    Polyline polyline;
    Timer t;

    public LatLng getDest() {
        return dest;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_JSON)) {

                try{
                    polyline.remove();
                }
                catch(Exception e){
                    Log.d(MapsActivity.class.getName(), "Aucun polyline");
                }

                quelquepart.setPosition(new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude()));
                float zoomlevel = (float) 16.0;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quelquepart.getPosition(), zoomlevel));
                if(role.matches("follower")){
                    String url = getDirectionsUrl(new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude()), dest);

                    DownloadTask downloadTask = new DownloadTask();

                    downloadTask.execute(url);
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);

        role = getIntent().getExtras().getString("role");

        assert(role != null);
        if(role.matches("creator")) {

            ProfileHead.setCarry(true);

            malocalisation = new GetLocalisation(getApplicationContext());

            ProfileHead.setCurrentLong(malocalisation.getLongitude());
            ProfileHead.setCurrentLat(malocalisation.getLatitude());

            ProfileHead.sendGeol(this);
        }
        else if(role.matches("follower")){

            ProfileHead.setCarry(false);

            malocalisation = new GetLocalisation(getApplicationContext());

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

                            dest = new LatLng(lat, lon);

                            try{
                                polyline.remove();
                            }
                            catch(Exception e){
                                Log.d(MapsActivity.class.getName(), "Aucun polyline");
                            }

                            if(ailleurs != null) ailleurs.setPosition(dest);
                            String url = getDirectionsUrl(new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude()), dest);

                            DownloadTask downloadTask = new DownloadTask();

                            downloadTask.execute(url);
                        }
                    });
                }
            }, 0, 10000);

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        optionMarker = new MarkerOptions()
                .position(new LatLng(malocalisation.getLatitude(),malocalisation.getLongitude()))
               // .position(new LatLng(47.079667, 2.399401))
                .title("Le beau marqueur");
        quelquepart = mMap.addMarker(optionMarker);
       // LatLng quelquepart = new LatLng(malocalisation.getLatitude(),malocalisation.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(quelquepart).title("Le beau marqueur"));
        // mMap.addMarker(new MarkerOptions().position(ailleur).title("Le second marquer"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(quelquepart.getPosition()));
        //float zoomlevel = (float) 16.0;
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quelquepart.getPosition(), zoomlevel));
        LatLng coordinate = new LatLng(malocalisation.getLatitude(),malocalisation.getLongitude());
        //LatLng coordinate = new LatLng(47.079667, 2.399401);
        /*CameraUpdate location = CameraUpdateFactory
                .newLatLngZoom(coordinate, 20);*/
        CameraPosition location = new CameraPosition.Builder()
                .target(coordinate)
                .zoom(5)
                .bearing(90)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(location));
        Log.d(MapsActivity.class.getName(), "Latitudemap -> " + malocalisation.getLatitude());
        Log.d(MapsActivity.class.getName(), "Longitudemap -> " + malocalisation.getLongitude());
        /*mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .tilt(45)
                        .zoom(16)
                        .build()));*/

        LatLng origin = new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude());
        //LatLng origin = new LatLng(47.079667, 2.399401);
        if(role.matches("follower")) {

            dest = new LatLng(malocalisation.getLatitude(), malocalisation.getLongitude());
            assert (dest != null);
            ailleurs = mMap.addMarker(new MarkerOptions().position(dest).title("Le second marquer"));
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            downloadTask.execute(url);


        }

    }

    /**********************************************************
     * Permet de récupérer l'itinéraire GoogleMap
     * @param origin
     * @param dest
     * @return
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&mode=walking";

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
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

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Downloading url", e.toString());
        } finally {
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
                points = new ArrayList<LatLng>();
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

            // Drawing polyline in the Google Map for the i-th route
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
}
