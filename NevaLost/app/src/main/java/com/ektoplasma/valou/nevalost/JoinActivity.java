package com.ektoplasma.valou.nevalost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

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
                            joinInstance(checkuser, checkpursuit, checkmdp);
                        }
                    }
                }
        );
    }

    protected void joinInstance(String user, String pursuit, String password){
        Map<String, String> params = new HashMap<>();
        params.put("user", user);
        params.put("pursuit", pursuit);
        params.put("password",password);

        ProfilePack.setUsername(user);
        ProfilePack.setPursuit(pursuit);

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
                        String creator = jsonResponse.getString("creator");
                        System.out.println("Cookie: "+cookie + "; Creator: "+creator);
                        ProfilePack.setCookieInstance(cookie);
                        ProfileHead.setUsername(creator);
                        Intent mapIntent = new Intent(JoinActivity.this, FollowerMapsActivity.class);
                        startActivity(mapIntent);
                    }
                    else{
                        ProfilePack.setUsername("");
                        ProfilePack.setPursuit("");
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
        //DataRequest requestor = new DataRequest(Request.Method.POST, "http://valou.ddns.net/join.php",params, reponseListener, errorListener);
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://"+ getString(R.string.hostname_server) +"/join.php",params, reponseListener, errorListener);

        QueueSingleton.getInstance(this).addToRequestQueue(requestor);
        //Volley.newRequestQueue(this).add(requestor);
    }
}
