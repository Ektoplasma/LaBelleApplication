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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TestMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String RECEIVE_JSON = "com.your.package.RECEIVE_JSON";

    Marker quelquepart;
    MarkerOptions optionMarker;
    GetLocalisation malocalisation;

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_JSON)) {
                quelquepart.setPosition(new LatLng(malocalisation.latitude, malocalisation.longitude));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);
        malocalisation = new GetLocalisation(getApplicationContext());
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
        //LatLng quelquepart = new LatLng(malocalisation.latitude, malocalisation.longitude);
        //LatLng ailleur = new LatLng(12.80, 3.50);
       optionMarker = new MarkerOptions()
                .position(new LatLng(malocalisation.latitude,malocalisation.longitude));
        quelquepart = mMap.addMarker(optionMarker);
        //mMap.addMarker(new MarkerOptions().position(quelquepart).title("Le beau marqueur"));
       // mMap.addMarker(new MarkerOptions().position(ailleur).title("Le second marquer"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(quelquepart));

    }
}
