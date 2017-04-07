package com.example.dell.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by dell on 3/5/2017.
 */

public class BookListLoader extends AsyncTaskLoader<List<BookList>> {

    private static final String LOG_TAG = BookListLoader.class.getName();

    private String urls;

    public BookListLoader(Context context,String url){
        super(context);
        urls = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
        Log.i(LOG_TAG,"Loader starts Loading");
    }

    @Override
    public List<BookList> loadInBackground(){
        if (urls ==null){
            return null;
        }


        List<BookList> result = Utilis.fetchBookListData(urls);
        Log.i(LOG_TAG,"Loader Loading in Background");
        return result;
    }
}
