package com.ektoplasma.valou.nevalost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
                        System.out.println("Cookie: "+cookie);
                        ProfilePack.setCookieInstance(cookie);
                        Intent mapIntent = new Intent(JoinActivity.this, MapsActivity.class);
                        mapIntent.putExtra("role","follower");
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
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://192.168.45.72/join.php",params, reponseListener, errorListener);

        QueueSingleton.getInstance(this).addToRequestQueue(requestor);
        //Volley.newRequestQueue(this).add(requestor);
    }
}
