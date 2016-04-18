package net.mc21.connections;

import android.util.Log;

import net.mc21.attendancecheck.MainActivity;

import java.io.IOException;

import us.monoid.web.Resty;

public class HTTPRequest {
    public static String SERVER_IP = "http://91.139.243.106:3000/";
    private static String response;

    public static String GET(final String from) {
        final Resty r = new Resty();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = r.text(from).toString();
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

        return response;
    }
}