package net.mc21.connections;

import android.util.Log;

import net.mc21.attendancecheck.MainActivity;

import org.json.JSONObject;

import java.io.IOException;

import us.monoid.web.Resty;

public class HTTPRequest {
    public static String SERVER_IP = "http://91.139.243.106:3000/";
    private static String response = new String();

    public static String GET(final String from) {
        response = "";
        final Resty r = new Resty();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = r.text(from).toString();
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

    public static String POST(final String to, JSONObject json) {
        response = "";
        final Resty r = new Resty();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = r.text(to, Resty.form(Resty.data("company_name", "frostblooded"),
                                                     Resty.data("password_digest", "$2a$10$q67BfKTG3lHP7/UuJbFoXuWHPbCVLeS5sXo2mnMuqIWdGjuUrqKCS")
                                                    )).toString();
                } catch (IOException e) {
                    Log.i(MainActivity.TAG, "HTTP request error: " + e.toString());
                    response = e.getMessage();
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