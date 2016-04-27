package com.ektoplasma.valou.nevalost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TestMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    IntentFilter filter;
    MyReceiver receiver;

    //Your activity will respond to this action String
    public static final String RECEIVE_JSON = "com.your.package.RECEIVE_JSON";

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_JSON)) {
                String serviceJsonString = intent.getStringExtra("json");
                GetLocalisation malocalisation = new GetLocalisation(getApplicationContext());

                LatLng quelquepart = new LatLng(malocalisation.latitude, malocalisation.longitude);
                LatLng ailleur = new LatLng(12.80, 3.50);
                mMap.addMarker(new MarkerOptions().position(quelquepart).title("Le beau marqueur"));
                mMap.addMarker(new MarkerOptions().position(ailleur).title("Le second marquer"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(quelquepart));
                //Do something with the string
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*filter = new IntentFilter("com.NevaLost.GetLoc");
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);*/
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GetLocalisation malocalisation = new GetLocalisation(getApplicationContext());

        LatLng quelquepart = new LatLng(malocalisation.latitude, malocalisation.longitude);
        LatLng ailleur = new LatLng(12.80, 3.50);
        mMap.addMarker(new MarkerOptions().position(quelquepart).title("Le beau marqueur"));
        mMap.addMarker(new MarkerOptions().position(ailleur).title("Le second marquer"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(quelquepart));

    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            GetLocalisation malocalisation = new GetLocalisation(getApplicationContext());

            LatLng quelquepart = new LatLng(malocalisation.latitude, malocalisation.longitude);
            LatLng ailleur = new LatLng(12.80, 3.50);
            mMap.addMarker(new MarkerOptions().position(quelquepart).title("Le beau marqueur"));
            mMap.addMarker(new MarkerOptions().position(ailleur).title("Le second marquer"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(quelquepart));
            Log.d(TestMap.class.getName(), "Je suis dans le receiver");
        }
    }
}
