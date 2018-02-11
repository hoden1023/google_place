package com.example.icecream.new_job.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IceCream on 2/11/2018.
 */

public class CustomAsyncTask extends AsyncTask<String, String, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    private OnCustomEventListener listener;

    public interface OnCustomEventListener {
        void onEvent(String s);
    }

    public CustomAsyncTask(OnCustomEventListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... param) {
        String result ="";
        try {
            result =sendRequest(param[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onEvent(s);
    }

    private String sendRequest(String strUrl) throws IOException {
        Log.d("request",strUrl);
        URL myUrl = new URL(strUrl);
        String inputLine;
        //Create a connection
        HttpURLConnection connection =(HttpURLConnection)
                myUrl.openConnection();
        //Set methods and timeouts
        connection.setRequestMethod(REQUEST_METHOD);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);

        //Connect to our url
        connection.connect();
        //Create a new InputStreamReader
        InputStreamReader streamReader = new
                InputStreamReader(connection.getInputStream());
        //Create a new buffered reader and String Builder
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        //Check if the line we are reading is not null
        while((inputLine = reader.readLine()) != null){
            stringBuilder.append(inputLine);
        }
        //Close our InputStream and Buffered reader
        reader.close();
        streamReader.close();
        //Set our result equal to our stringBuilder
        Log.d("result",stringBuilder.toString());
        return stringBuilder.toString();
    }
}
