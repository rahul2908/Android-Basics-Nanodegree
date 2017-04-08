package com.example.dell.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dell on 3/7/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    private static final String SEPERATOR = "T";

    public NewsAdapter(Activity context, ArrayList<News> news){
        super(context,0,news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView sectionName = (TextView) listItemView.findViewById(R.id.section_name);
        sectionName.setText(currentNews.getSectionName());

        TextView webTitle = (TextView) listItemView.findViewById(R.id.web_title);
        webTitle.setText(currentNews.getWebTitle());

        String givenDate = currentNews.getWebPublicationDate();
        String date1="";
        String time="";
        if (givenDate.contains(SEPERATOR)){
            String[] parts = givenDate.split(SEPERATOR);
            date1 = parts[0];
            time = parts[1];
        }


        TextView publishDate = (TextView) listItemView.findViewById(R.id.date);
        publishDate.setText(date1);

        time = time.replaceAll("Z","");
        TextView publishTime = (TextView) listItemView.findViewById(R.id.time);
        publishTime.setText(time);

        return listItemView;

    }


}
