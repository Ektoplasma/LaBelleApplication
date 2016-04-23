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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    // le beau commentaire
    // oui il est beau
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ButtonPursuit = (Button)findViewById(R.id.ButtonCreatePursuit);
        Button ButtonJoin = (Button)findViewById(R.id.ButtonJoinPursuit);
        Button ButtonSettings = (Button)findViewById(R.id.ButtonSettings);

        int i = 0;

        while(Geo() && i<2)
        {
            i++;
        }

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

    private boolean Geo(){
        LocationManager locManager;
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createGpsDisabledAlert();
        }

        return  true;
    }

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

    private void showGpsOptions() {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        finish();
    }
}

