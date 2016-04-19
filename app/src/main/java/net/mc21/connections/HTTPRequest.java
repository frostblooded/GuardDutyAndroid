package net.mc21.connections;

import android.util.Log;

import net.mc21.attendancecheck.MainActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest {
    public static String SERVER_IP = "http://91.139.243.106:3000/";
    private static String response = new String();
    private static int TIMEOUT = 10000;

    public enum RequestType{
        GET,
        POST
    }

    public static String sendAsync(final String url, final String sentInfo, final RequestType requestType) {
        response = null;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch(requestType) {
                        case GET:
                            response = get(url, sentInfo);
                            break;
                        case POST:
                            response = post(url, sentInfo);
                            break;
                    }
                } catch (IOException e) {
                    Log.i(MainActivity.TAG, "HTTP request error: " + e.toString());
                    response = e.toString();
                    e.printStackTrace();
                }
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            response = e.toString();
            Log.i(MainActivity.TAG, "HTTP Request interrupted: " + e.toString());
        }

        return response;
    }

    public static String post(String to, String sentInfo) throws IOException {
        byte[] sentData = sentInfo.getBytes();
        URL url = new URL(to);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", "" +
                Integer.toString(sentData.length));

        DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
        printout.write(sentData);
        printout.flush();
        printout.close();

        Log.i(MainActivity.TAG, String.valueOf(connection.getResponseCode()));

        //Get Response
        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        StringBuilder response = new StringBuilder();

        int data = isr.read();
        while(data != -1) {
            char current = (char) data;
            data = isr.read();
            response.append(current);
        }

        return response.toString();
    }

    public static String get(String from, String query) throws IOException {
        URL url = new URL(from + '?' + query);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        Log.i(MainActivity.TAG, String.valueOf(connection.getResponseCode()));

        //Get Response
        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        StringBuilder response = new StringBuilder();

        int data = isr.read();
        while(data != -1) {
            char current = (char) data;
            data = isr.read();
            response.append(current);
        }

        return response.toString();
    }
}