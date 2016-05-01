package net.mc21.connections;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.mc21.attendancecheck.MainActivity;

import org.json.JSONObject;

public class HTTP {
    //public static String SERVER_IP = "https://hidden-shelf-43728.herokuapp.com/";
    public static String SERVER_IP = "http://91.139.243.106:3000/";

    public static void POST(String url, JSONObject sentData, Response.Listener<JSONObject> listener, Context context) {
        Response.ErrorListener errorListener = new CustomErrorListener();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, sentData,
                listener, errorListener);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(MainActivity.REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonRequest);
    }
}
