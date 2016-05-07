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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creation);

        Button ButtonValide = (Button)findViewById(R.id.validerpoursuite);

        assert ButtonValide != null;
        ButtonValide.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        EditText user = (EditText) findViewById(R.id.username);
                        assert user != null;
                        String checkuser = user.getText().toString();
                        EditText pursuit = (EditText) findViewById(R.id.nom_poursuit);
                        assert pursuit != null;
                        String checkpursuit = pursuit.getText().toString();
                        EditText mdp = (EditText) findViewById(R.id.motdepasse);
                        assert mdp != null;
                        String checkmdp = mdp.getText().toString();
                        if (checkuser.matches("")) {
                            Toast.makeText(getApplicationContext(), "Aucun nom d'utilisateur saisi", Toast.LENGTH_SHORT).show();
                        }
                        else if (checkpursuit.matches("")) {
                            Toast.makeText(getApplicationContext(), "Aucun nom de poursuite saisi", Toast.LENGTH_SHORT).show();
                        }
                        else if (checkmdp.matches("")) {
                            Toast.makeText(getApplicationContext(), "Aucun mot de passe saisi", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                           //lancement poursuite et vu des autres participants
                            //GetLocalisation malocalisation = new GetLocalisation(getApplicationContext());
                            //Toast.makeText(getApplicationContext(), String.valueOf(malocalisation.latitude), Toast.LENGTH_SHORT).show();
                            requestInstance(checkuser, checkpursuit, checkmdp);
                        }
                }
                }
        );
    }

    protected void requestInstance(String user, String pursuit, String password){
        Map<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("pursuit", pursuit);
        params.put("password",password);

        Profile.setUsername(user);
        Profile.setPursuit(pursuit);

        Response.Listener<JSONObject> reponseListener= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject jsonResponse = response.getJSONObject("form");
                    JSONObject jsonResponse = response.getJSONObject("statut");
                    String succes = jsonResponse.getString("succes");

                    System.out.println("Succes: "+succes);

                    assert(succes != null);
                    if(succes.matches("true")) {
                        String cookie = jsonResponse.getString("cookie");
                        System.out.println("Cookie: "+cookie);
                        Profile.setCookieInstance(cookie);
                        startActivity(new Intent(CreationActivity.this, TestMap.class));
                    }
                    else{
                        Profile.setUsername("");
                        Profile.setPursuit("");
                        System.out.println("Try again please.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://10.0.2.2/create.php",params, reponseListener, errorListener);

        QueueSingleton.getInstance(this).addToRequestQueue(requestor);
        //Volley.newRequestQueue(this).add(requestor);
    }
}
