package com.example.dell.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.newsapp.QueryUtilis.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    public static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?page-size=10&q=news&api-key=0fc305c0-c13b-40dd-b619-cc5149f58189";

    private NewsAdapter mAdapter;

    private TextView emptyTextView;

    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of earthquake locations.
        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        newsListView.setEmptyView(emptyTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getWebUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            Log.i(LOG_TAG, "Initialising the loader");
        }
        else {
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);

        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id,Bundle bundle){
        Log.i(LOG_TAG,"Creating the Loader");
        return new NewsLoader(this,NEWS_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data){
        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        // Clear the adapter of previous earthquake data
        emptyTextView.setText(R.string.no_news);
        mAdapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        Log.i(LOG_TAG,"Loader Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // TODO: Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.i(LOG_TAG,"Loader reseted");
    }
}
