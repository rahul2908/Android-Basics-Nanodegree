package com.example.dell.habittrackerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.dell.habittrackerapp.data.StudentContract.StudentEntry;

/**
 * Created by dell on 3/17/2017.
 */

public class StudentDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "details.db";

    public StudentDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_STUDENT_TABLE =  "CREATE TABLE " + StudentEntry.TABLE_NAME + " ("
                + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StudentEntry.COLUMN_STUDENT_NAME + " TEXT NOT NULL, "
                + StudentEntry.COLUMN_STUDENT_ROLLNO + " TEXT NOT NULL, "
                + StudentEntry.COLUMN_STUDENT_GENDER + " INTEGER NOT NULL, "
                + StudentEntry.COLUMN_STUDENT_AGE + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_STUDENT_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
