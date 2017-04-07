package com.example.dell.booklistingapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.booklistingapp.Utilis.LOG_TAG;


public class BookListActivity extends AppCompatActivity implements LoaderCallbacks<List<BookList>> {

    public static String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=30";

    private TextView emptyTextView;

    private int id = 1;

    private BookListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Button searchButton = (Button) findViewById(R.id.btn_search);

        ListView bookListView = (ListView) findViewById(R.id.list);

        emptyTextView = (TextView) findViewById(R.id.empty_view);

        mAdapter = new BookListAdapter(this,new ArrayList<BookList>());

        bookListView.setAdapter(mAdapter);

        LoaderManager loaderManager = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnected()){
            emptyTextView.setVisibility(View.GONE);
            loaderManager =getLoaderManager();
            loaderManager.initLoader(id, null, this);
        }
        else {
            View progressBar = findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = id + 1;
                EditText searchEditText = (EditText) findViewById(R.id.edit_text_search);
                String searchQuery = searchEditText.getText().toString();
                searchQuery = searchQuery.replaceAll(" ","").toLowerCase();
                Log.i(LOG_TAG,searchQuery);

                BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q="+searchQuery+"&maxResults=30";

                LoaderManager loaderManager = null;

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    emptyTextView.setVisibility(View.GONE);
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(id, null, BookListActivity.this);
                } else {
                    View progressBar = findViewById(R.id.loading_spinner);
                    progressBar.setVisibility(View.GONE);
                    emptyTextView.setText(R.string.no_internet_connection);
                }
            }
        });

    }

    @Override
    public Loader<List<BookList>> onCreateLoader(int i,Bundle bundle){
        Log.i("BookListActivtiy","Loader Created "+BOOK_REQUEST_URL);
        return new BookListLoader(this,BOOK_REQUEST_URL);
    }


    @Override
    public void onLoadFinished(Loader<List<BookList>> loader,List<BookList> data){

        emptyTextView.setText(R.string.no_book_found);

        mAdapter.clear();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);

        if (data !=null && !data.isEmpty()){
            mAdapter.addAll(data);
        }
        else {
            emptyTextView.setVisibility(View.VISIBLE);
        }
        Log.i(LOG_TAG,"Loader Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<BookList>> loader){
        mAdapter.clear();
        Log.i(LOG_TAG,"Loader Resetted");
    }
}
