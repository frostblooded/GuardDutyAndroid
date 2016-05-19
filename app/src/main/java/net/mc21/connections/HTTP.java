package net.mc21.connections;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.mc21.attendancecheck.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class HTTP {
    public final static int REQUEST_TIMEOUT = 5000;
    //public static String SERVER_IP = "https://hidden-shelf-43728.herokuapp.com/";
    public static String SERVER_IP = "http://91.139.243.106:3000/";

    // JSON object
    public static void GET(String url, Response.Listener<JSONObject> listener, ProgressDialog progressDialog, Context context) {
        makeJsonObjectRequest(Request.Method.GET, url, null, listener, progressDialog, context);
    }

    public static void POST (String url, JSONObject sentData, Response.Listener<JSONObject> listener, ProgressDialog progressDialog,  Context context) {
        makeJsonObjectRequest(Request.Method.POST, url, sentData, listener, progressDialog, context);
    }

    public static void PUT(String url, JSONObject sentData, Response.Listener<JSONObject> listener, ProgressDialog progressDialog,  Context context) {
        makeJsonObjectRequest(Request.Method.PUT, url, sentData, listener, progressDialog, context);
    }

    public static void DELETE(String url, Response.Listener<JSONObject> listener, ProgressDialog progressDialog,  Context context) {
        makeJsonObjectRequest(Request.Method.DELETE, url, null, listener, progressDialog, context);
    }

    private static void makeJsonObjectRequest(int method, String url, JSONObject sentData,
                                    Response.Listener<JSONObject> listener, ProgressDialog progressDialog, Context context) {
        Response.ErrorListener errorListener = new CustomErrorListener(progressDialog, context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(method, url, sentData,
                listener, errorListener);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonRequest);
    }

    // JSON array
    public static void GETArray(String url, Response.Listener<JSONArray> listener, ProgressDialog progressDialog,  Context context) {
        makeJsonArrayRequest(Request.Method.GET, url, null, listener, progressDialog, context);
    }

    public static void POSTArray (String url, JSONArray sentData, Response.Listener<JSONArray> listener, ProgressDialog progressDialog, Context context) {
        makeJsonArrayRequest(Request.Method.POST, url, sentData, listener, progressDialog, context);
    }

    public static void PUTArray(String url, JSONArray sentData, Response.Listener<JSONArray> listener, ProgressDialog progressDialog, Context context) {
        makeJsonArrayRequest(Request.Method.PUT, url, sentData, listener, progressDialog, context);
    }

    public static void DELETEArray(String url, Response.Listener<JSONArray> listener, ProgressDialog progressDialog, Context context) {
        makeJsonArrayRequest(Request.Method.DELETE, url, null, listener, progressDialog, context);
    }

    private static void makeJsonArrayRequest(int method, String url, JSONArray sentData,
                                             Response.Listener<JSONArray> listener, ProgressDialog progressDialog, Context context) {
        Response.ErrorListener errorListener = new CustomErrorListener(progressDialog, context);
        JsonArrayRequest jsonRequest = new JsonArrayRequest(method, url, sentData,
                listener, errorListener);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonRequest);
    }
}
