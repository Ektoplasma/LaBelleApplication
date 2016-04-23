package com.ektoplasma.valou.nevalost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    // le beau commentaire
    // oui il est beau
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart() {//Chaque fois que l'on retourne sur la fenetre
        super.onStart();
        Button ButtonPursuit = (Button)findViewById(R.id.ButtonCreatePursuit);
        Button ButtonJoin = (Button)findViewById(R.id.ButtonJoinPursuit);
        Button ButtonSettings = (Button)findViewById(R.id.ButtonSettings);

        Geo();//Verification de la localisation

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
    private void Geo(){
        LocationManager locManager;
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createGpsDisabledAlert();
        }

    }

    /*Affiche une boîte de dialogue permettant l'accés aux paramètres de localisation*/
    /*Si l'utilisateur refuse de l'activer l'application se ferme*/
    private void createGpsDisabledAlert() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
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
        finish();
    }
}

