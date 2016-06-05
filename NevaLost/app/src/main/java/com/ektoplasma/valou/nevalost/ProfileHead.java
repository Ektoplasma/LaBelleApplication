package com.ektoplasma.valou.nevalost;

import android.content.Context;

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
public final class ProfileHead {

    private static String username;
    private static String pursuit;
    private static String cookieInstance;
    private static double currentLong;
    private static double currentLat;
    private static boolean carry;


    /*On pourra rajouter d'autres informations comme la geol initiale, un avatar, etc...*/

    public static String getUsername() { return username; }

    public static void setUsername(String username) { ProfileHead.username = username; }

    public static String getPursuit() { return pursuit; }

    public static void setPursuit(String pursuit) { ProfileHead.pursuit = pursuit; }

    public static String getCookieInstance() { return cookieInstance; }

    public static void setCookieInstance(String cookieInstance) { ProfileHead.cookieInstance = cookieInstance; }

    public static double getCurrentLong() { return currentLong; }

    public static void setCurrentLong(double currentLong) { ProfileHead.currentLong = currentLong; }

    public static double getCurrentLat() { return currentLat; }

    public static void setCurrentLat(double currentLat) { ProfileHead.currentLat = currentLat; }

    public static boolean isCarry() { return carry; }

    public static void setCarry(boolean carry) { ProfileHead.carry = carry; }

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

        //DataRequest requestor = new DataRequest(Request.Method.POST, "http://valou.ddns.net/update.php",params, reponseListener, errorListener);
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://"+ ctx.getResources().getString(R.string.hostname_server) +"/update.php",params, reponseListener, errorListener);

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
        //DataRequest requestor = new DataRequest(Request.Method.POST, "http://valou.ddns.net/update.php",params, reponseListener, errorListener);
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://"+ ctx.getResources().getString(R.string.hostname_server) +"/update.php" ,params, reponseListener, errorListener);

        QueueSingleton.getInstance(ctx).addToRequestQueue(requestor);


    }

    public static void deletePursuit(Context ctx){
        Map<String, String> params = new HashMap<>();
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
                        System.out.println("Could not delete instance");
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
        //DataRequest requestor = new DataRequest(Request.Method.POST, "http://valou.ddns.net/delete.php",params, reponseListener, errorListener);
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://"+ ctx.getResources().getString(R.string.hostname_server) +"/delete.php",params, reponseListener, errorListener);

        QueueSingleton.getInstance(ctx).addToRequestQueue(requestor);
    }
}
