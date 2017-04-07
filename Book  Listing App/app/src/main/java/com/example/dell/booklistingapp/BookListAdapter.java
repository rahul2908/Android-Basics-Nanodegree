package com.example.dell.booklistingapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 3/4/2017.
 */

public class BookListAdapter extends ArrayAdapter<BookList> {

    public static final String LOG_TAG = BookListAdapter.class.getSimpleName();

    public BookListAdapter(Activity context, List<BookList> bookLists){
        super(context,0,bookLists);
    }
    @NonNull
    @Override

    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent , false);
        }

        BookList currentBookList = getItem(position);

        TextView bookName = (TextView) listItemView.findViewById(R.id.book_name);
        bookName.setText(currentBookList.getBookName());

        TextView authorName = (TextView) listItemView.findViewById(R.id.author_name);
        authorName.setText(currentBookList.getAuthorName());

        TextView publisherName = (TextView) listItemView.findViewById(R.id.publisher_name);
        publisherName.setText(currentBookList.getPublisher());

        return listItemView;
    }
}
