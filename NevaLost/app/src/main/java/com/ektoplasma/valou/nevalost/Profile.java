package com.ektoplasma.valou.nevalost;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ektoplasma on 07/05/16.
 */
public final class Profile {

    private static String username;
    private static String pursuit;
    private static String cookieInstance;
    private static double currentLong;
    private static double currentLat;

    /*On pourra rajouter d'autres informations comme la geol initiale, un avatar, etc...*/

    public static String getUsername() { return username; }

    public static void setUsername(String username) { Profile.username = username; }

    public static String getPursuit() { return pursuit; }

    public static void setPursuit(String pursuit) { Profile.pursuit = pursuit; }

    public static String getCookieInstance() { return cookieInstance; }

    public static void setCookieInstance(String cookieInstance) { Profile.cookieInstance = cookieInstance; }

    public static double getCurrentLong() { return currentLong; }

    public static void setCurrentLong(double currentLong) { Profile.currentLong = currentLong; }

    public static double getCurrentLat() { return currentLat; }

    public static void setCurrentLat(double currentLat) { Profile.currentLat = currentLat; }

    public static void sendGeol(Context ctx){

        Map<String, String> params = new HashMap<>();
        params.put("longitude", String.valueOf(currentLong));
        params.put("latitude", String.valueOf(currentLat));
        params.put("cookie",cookieInstance);

        Response.Listener<JSONObject> reponseListener= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject jsonResponse = response.getJSONObject("form");
                    JSONObject jsonResponse = response.getJSONObject("statut");
                    String succes = jsonResponse.getString("succes");

                    assert(succes != null);
                    if(succes.matches("true")) {
                        System.out.println("Succes: "+succes);
                    }
                    else{
                        System.out.println("Could not update geolocalisation");
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
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://valou.ddns.net/update.php",params, reponseListener, errorListener);

        QueueSingleton.getInstance(ctx).addToRequestQueue(requestor);

    }

    public static void sendGeol(double lon, double lat, Context ctx){
        currentLat = lat;
        currentLong = lon;

        Map<String, String> params = new HashMap<>();
        params.put("longitude", String.valueOf(currentLong));
        params.put("latitude", String.valueOf(currentLat));
        params.put("cookie",cookieInstance);

        Response.Listener<JSONObject> reponseListener= new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject jsonResponse = response.getJSONObject("form");
                    JSONObject jsonResponse = response.getJSONObject("statut");
                    String succes = jsonResponse.getString("succes");

                    assert(succes != null);
                    if(succes.matches("true")) {
                        System.out.println("Succes: "+succes);
                    }
                    else{
                        System.out.println("Could not update geolocalisation");
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
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://valou.ddns.net/update.php",params, reponseListener, errorListener);

        QueueSingleton.getInstance(ctx).addToRequestQueue(requestor);


    }
}
