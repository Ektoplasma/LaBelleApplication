package com.ektoplasma.valou.nevalost;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ektoplasma on 06/05/16.
 */
public class QueueSingleton {
    private static QueueSingleton ourInstance;
    private RequestQueue ourRequestQueue;
    private static Context ourCtx;

    public static synchronized QueueSingleton getInstance(Context context) {

        if(ourInstance == null){
            ourInstance = new QueueSingleton(context);
        }

        return ourInstance;
    }

    private QueueSingleton(Context context) {
        ourCtx = context;
        ourRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(ourRequestQueue == null){
            ourRequestQueue = Volley.newRequestQueue(ourCtx.getApplicationContext());
        }
        return ourRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

}