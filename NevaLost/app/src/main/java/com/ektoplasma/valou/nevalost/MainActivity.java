package com.ektoplasma.valou.nevalost;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Verification de l'autorisation de l'appli dans android (obligatoire pour API > 23)
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    protected void onStart() {//Chaque fois que l'on retourne sur la fenetre
        super.onStart();
        Button ButtonPursuit = (Button)findViewById(R.id.ButtonCreatePursuit);
        Button ButtonJoin = (Button)findViewById(R.id.ButtonJoinPursuit);
        Button ButtonSettings = (Button)findViewById(R.id.ButtonSettings);

        Geo(MainActivity.this);//Verification de la localisation

        ButtonPursuit.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, CreationActivity.class));
                    }
                }
        );

        ButtonJoin.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, JoinActivity.class));
                    }
                }
        );

        ButtonSettings.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                }
        );
    }

    /*Permet la verifier si la localisation est active*/
    /*Si elle n'y est pas on lance createGpsDisabledAlert*/
    private void Geo(Activity active){
        LocationManager locManager;
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createGpsDisabledAlert(active);
        }

    }

    /*Affiche une boîte de dialogue permettant l'accés aux paramètres de localisation*/
    /*Si l'utilisateur refuse de l'activer l'application se ferme*/
    private void createGpsDisabledAlert(Activity active) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(active);
        localBuilder
                .setMessage("Le GPS est inactif, voulez-vous l'activer ?\nNote : Si le GPS n'est pas activé l'application est inutilisable")
                .setCancelable(false)
                .setPositiveButton("Activer GPS ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                showGpsOptions();
                            }
                        }
                );
        localBuilder.setNegativeButton("Ne pas l'activer ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                        finish();
                    }
                }
        );
        localBuilder.create().show();
    }
    /*Affiche les parametres android de localisation*/
    private void showGpsOptions() {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
    }
}

