package com.example.dell.habittrackerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dell.habittrackerapp.data.StudentContract.StudentEntry;
import com.example.dell.habittrackerapp.data.StudentDbHelper;

/**
 * Created by dell on 3/17/2017.
 */


public class CatalogActivity extends AppCompatActivity {

    public static final String TAG = CatalogActivity.class.getSimpleName();

    private StudentDbHelper mDbHelper;

    SQLiteDatabase db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new StudentDbHelper(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        db = mDbHelper.getReadableDatabase();

        Cursor cursor = readCursor();
        try {

            TextView displayView = (TextView) findViewById(R.id.text_view_student);
            displayView.setText("Number of rows in students database table is: " + cursor.getCount());

            displayView.setText("\nThe students table contains "+cursor.getCount() + " students data.\n\n\n");
            displayView.append(StudentEntry._ID + "-" + StudentEntry.COLUMN_STUDENT_NAME + "-" + StudentEntry.COLUMN_STUDENT_ROLLNO + "-" + StudentEntry.COLUMN_STUDENT_GENDER + "-" + StudentEntry.COLUMN_STUDENT_AGE);

            int idColumnIndex = cursor.getColumnIndex(StudentEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_NAME);
            int rollnoColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_ROLLNO);
            int genderColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_GENDER);
            int ageColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_AGE);

            while (cursor.moveToNext()){
                Log.i(TAG,"Entered cursor section");
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentRollNo = cursor.getString(rollnoColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentAge = cursor.getInt(ageColumnIndex);

                displayView.append("\n" + currentId + "-" + currentName + "-" + currentRollNo + "-" + currentGender + "-" + currentAge);
            }

        } finally {
            cursor.close();
        }
    }

    public Cursor readCursor(){
        String[] projection ={StudentEntry._ID,StudentEntry.COLUMN_STUDENT_NAME,StudentEntry.COLUMN_STUDENT_ROLLNO,StudentEntry.COLUMN_STUDENT_GENDER, StudentEntry.COLUMN_STUDENT_AGE};
        Cursor cursor = db.query(StudentEntry.TABLE_NAME, projection, null, null, null, null, null);

        return cursor;
    }

    private void insertStudent(){

        SQLiteDatabase db1  = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentEntry.COLUMN_STUDENT_NAME,"Rahul");
        contentValues.put(StudentEntry.COLUMN_STUDENT_ROLLNO,"CU1510991492");
        contentValues.put(StudentEntry.COLUMN_STUDENT_GENDER,StudentEntry.GENDER_MALE);
        contentValues.put(StudentEntry.COLUMN_STUDENT_AGE,20);

        long newRowId = db1.insert(StudentEntry.TABLE_NAME,null,contentValues);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertStudent();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
