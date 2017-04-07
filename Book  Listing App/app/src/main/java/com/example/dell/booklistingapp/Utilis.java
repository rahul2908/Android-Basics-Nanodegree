package com.example.dell.booklistingapp;

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
 * Created by dell on 3/5/2017.
 */

public final class Utilis {

    private Utilis(){

    }

    public static final String LOG_TAG = Utilis.class.getSimpleName();



    public static List<BookList> fetchBookListData(String requestUrl){
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            Log.e(TAG,"Error in Thread Sleeping",e);
        }

        URL url = createUrl(requestUrl);

        String jsonResponse =  null;
        try {
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(TAG,"Error in making HttpRequestMethod() ",e);

        }
        List<BookList> bookLists = extractFeaturesFromJson(jsonResponse);
        return bookLists;
    }

    private static URL createUrl(String StringUrl){

        URL url = null;
        try {
            url = new URL(StringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error in Creating url");
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url ==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.i(TAG,"makeHttpRequest: "+url);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                Log.i(TAG,"Error Response Code :"+urlConnection.getResponseCode());
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
        }
        else{
                Log.i(TAG,"Error Response Code :"+urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the earthquake JSON result");
        }
        finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream !=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static List<BookList> extractFeaturesFromJson(String bookListJson){

        if (TextUtils.isEmpty(bookListJson)){
            return null;
        }

        List<BookList> bookLists = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookListJson);

            JSONArray bookListArray = baseJsonResponse.getJSONArray("items");

            for (int i=0;i < bookListArray.length();  i++){

                JSONObject bookRecord = bookListArray.optJSONObject(i);
                // Get the volume info node from the Book record
                JSONObject bookVolumeInfo = bookRecord.optJSONObject("volumeInfo");
                // Get the title from the volume info
                String bookTitle = bookVolumeInfo.optString("title");
                // Get the Author name from the volume info
                String authorName = bookVolumeInfo.optString("authors");
                // Get the Publisher from the volume info
                String publisher = bookVolumeInfo.optString("publisher");

                bookLists.add(new BookList(bookTitle,authorName,publisher));
            }

        }
        catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return bookLists;
    }
}
