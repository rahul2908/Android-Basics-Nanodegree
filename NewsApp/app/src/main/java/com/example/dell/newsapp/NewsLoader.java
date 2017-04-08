package com.example.dell.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by dell on 3/7/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    private String urls;

    public NewsLoader(Context context,String url){
        super(context);
        urls = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
        Log.i(LOG_TAG,"Loader Starts Loading");
    }

    @Override
    public List<News> loadInBackground(){
        if (urls == null){
            return null;
        }
        List<News> result = QueryUtilis.fetchNewsData(urls);
        Log.i(LOG_TAG,"Loader loaded in Background");
        return result;
    }
}
