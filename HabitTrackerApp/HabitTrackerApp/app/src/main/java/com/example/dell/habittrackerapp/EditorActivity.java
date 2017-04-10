package com.example.dell.habittrackerapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.habittrackerapp.data.StudentContract.StudentEntry;
import com.example.dell.habittrackerapp.data.StudentDbHelper;

/**
 * Created by dell on 3/17/2017.
 */

public class EditorActivity extends AppCompatActivity {

    private static final String TAG = EditorActivity.class.getSimpleName();

    private EditText mNameEditText;

    private EditText mRollNoEditText;

    private EditText mAgeEditText;

    private Spinner mGenderSpinner;

    private int mGender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = (EditText)findViewById(R.id.edit_student_name);
        mRollNoEditText = (EditText) findViewById(R.id.edit_student_rollno);
        mAgeEditText = (EditText) findViewById(R.id.edit_student_age);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();
    }

    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = StudentEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = StudentEntry.GENDER_FEMALE;
                    } else {
                        mGender = StudentEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = StudentEntry.GENDER_UNKNOWN;
            }
        });
    }

    private void insertPet(){
        String nameString = mNameEditText.getText().toString().trim();
        String rollnoString = mRollNoEditText.getText().toString().trim();
        String ageString = mAgeEditText.getText().toString().trim();
        int age = Integer.parseInt(ageString);



        StudentDbHelper mDbHelper = new StudentDbHelper(this);
        SQLiteDatabase db  = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentEntry.COLUMN_STUDENT_NAME,nameString);
        contentValues.put(StudentEntry.COLUMN_STUDENT_ROLLNO,rollnoString);
        contentValues.put(StudentEntry.COLUMN_STUDENT_GENDER,mGender);
        contentValues.put(StudentEntry.COLUMN_STUDENT_AGE,age);

        long newRowId = db.insert(StudentEntry.TABLE_NAME, null, contentValues);

        if (newRowId == -1){
            Toast.makeText(this,"Error with saving Student Info",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Student Info saved with row id: "+newRowId,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                Log.i(TAG,"Save Button Working");
                insertPet();
                finish();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
