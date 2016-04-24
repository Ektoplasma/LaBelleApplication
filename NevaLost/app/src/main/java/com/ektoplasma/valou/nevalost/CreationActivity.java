package com.ektoplasma.valou.nevalost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.LocationManager;

import com.google.android.gms.location.LocationListener;

public class CreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        Button ButtonValide = (Button)findViewById(R.id.validerpoursuite);

        ButtonValide.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        EditText user = (EditText) findViewById(R.id.username);
                        String checkuser = user.getText().toString();
                        EditText pursuit = (EditText) findViewById(R.id.nom_poursuit);
                        String checkpursuit = pursuit.getText().toString();
                        EditText mdp = (EditText) findViewById(R.id.motdepasse);
                        String checkmdp = mdp.getText().toString();
                        if (checkuser.matches("")) {
                            Toast.makeText(getApplicationContext(), "Aucun nom d'utilisateur saisi", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (checkpursuit.matches("")) {
                            Toast.makeText(getApplicationContext(), "Aucun nom de poursuite saisi", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if (checkmdp.matches("")) {
                            Toast.makeText(getApplicationContext(), "Aucun mot de passe saisi", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                        {
                           //lancement poursuite et vu des autres participants
                            GetLocalisation malocalisation = new GetLocalisation(getApplicationContext());
                            Toast.makeText(getApplicationContext(), String.valueOf(malocalisation.latitude), Toast.LENGTH_SHORT).show();


                        }
                }
                }
        );
    }
}
