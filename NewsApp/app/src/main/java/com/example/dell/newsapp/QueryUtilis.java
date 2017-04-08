package com.example.dell.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 3/7/2017.
 */

public final class QueryUtilis {

    private QueryUtilis(){

    }

    public static final String LOG_TAG = QueryUtilis.class.getSimpleName();

    public static List<News> fetchNewsData (String requestUrl){
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            Log.e(TAG,"Error in thread sleeping",e);
        }
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse= makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream",e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse);
        Log.i(LOG_TAG,"Fetching Earthquake Data");
        return news;
    }

    private static URL createUrl(String StringUrl){
        URL url = null;
        try {
            url = new URL(StringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating url");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.i(TAG, "makeHttpRequest: "+url);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }



    private static String readFromStream(InputStream inputStream) throws  IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<News> earthquakes = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray NewsArray = response.getJSONArray("results");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < NewsArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentNews = NewsArray.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.

                // Extract the value for the key called "mag"
                String sectionName = currentNews.optString("sectionName");

                // Extract the value for the key called "place"
                String webTitle = currentNews.optString("webTitle");

                // Extract the value for the key called "time"
                String date = currentNews.optString("webPublicationDate");

                // Extract the value for the key called "url"
                String url = currentNews.optString("webUrl");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                News news = new News(sectionName, webTitle, date, url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                earthquakes.add(news);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


}
