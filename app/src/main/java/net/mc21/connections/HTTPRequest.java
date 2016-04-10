package net.mc21.connections;

import android.util.Log;

import net.mc21.attendancecheck.MainActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

public class HTTPRequest {
    public static String SERVER_IP = "http://91.139.243.106:3000/";

    public enum RequestType{
        GET,
        POST
    }

    public static String sendAsync(String data, final String to, final RequestType requestType) {
        final byte[] sentInfo = data.getBytes();
        final StringBuilder response = new StringBuilder();
        final Resty r = new Resty();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONResource res = r.json(to);
                    response.append(res.get("name"));
                } catch (IOException e) {
                    Log.i(MainActivity.TAG, "HTTP request error: " + e.toString());
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.i(MainActivity.TAG, "Exception: " + e.toString());
                    e.printStackTrace();
                }
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i(MainActivity.TAG, "HTTP Request interrupted: " + e.toString());
        }

        return response.toString();
    }

    public static String send(byte[] sentInfo, String to, RequestType requestType) throws IOException {
        URL url;
        HttpURLConnection connection;

        //Create connection
        url = new URL(to);
        connection = (HttpURLConnection)url.openConnection();

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // Send POST output.
        if(requestType == RequestType.POST) {
            connection.setRequestMethod(requestType.toString());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(sentInfo.length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setDoOutput(true);

            DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
            printout.write(sentInfo);
            printout.flush();
            printout.close();
        }

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

        connection.disconnect();
        return response.toString();
    }
}