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
 * Created by ektoplasma on 22/05/16.
 */
public final class ProfilePack {

    private static String username;
    private static String pursuit;
    private static String cookieInstance;
    private static double masterLong;
    private static double masterLat;

    public static String getUsername() { return username; }

    public static void setUsername(String username) { ProfilePack.username = username; }

    public static String getPursuit() { return pursuit; }

    public static void setPursuit(String pursuit) { ProfilePack.pursuit = pursuit; }

    public static String getCookieInstance() { return cookieInstance; }

    public static void setCookieInstance(String cookieInstance) { ProfilePack.cookieInstance = cookieInstance; }

    public static double getMasterLong() { return masterLong; }

    public static void setMasterLong(double masterLong) { ProfilePack.masterLong = masterLong; }

    public static double getMasterLat() { return masterLat; }

    public static void setMasterLat(double masterLat) { ProfilePack.masterLat = masterLat; }

    public static void getGeol(Context ctx){

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
                        masterLong = Float.valueOf(jsonResponse.getString("lon"));
                        masterLat = Float.valueOf(jsonResponse.getString("lat"));
                        System.out.println("Succes: "+succes+ " Long :"+masterLong+" Lat:"+masterLat);
                    }
                    else{
                        System.out.println("Could not read geolocalisation");
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
        DataRequest requestor = new DataRequest(Request.Method.POST, "http://"+ ctx.getResources().getString(R.string.hostname_server) +"/read.php",params, reponseListener, errorListener);
        //DataRequest requestor = new DataRequest(Request.Method.POST, "http://192.168.45.72/read.php",params, reponseListener, errorListener);

        QueueSingleton.getInstance(ctx).addToRequestQueue(requestor);

    }
}
