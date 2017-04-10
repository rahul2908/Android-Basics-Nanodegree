package com.example.dell.habittrackerapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dell on 3/17/2017.
 */

public class StudentContract {


    public static final String CONTENT_AUTHORITY = "com.example.dell.habittrackerapp.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    private StudentContract(){}

    public static final class StudentEntry implements BaseColumns{


        public static final String TABLE_NAME = "students";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_STUDENT_NAME = "name";

        public static final String COLUMN_STUDENT_ROLLNO = "rollno";

        public static final String COLUMN_STUDENT_GENDER = "gender";

        public static final String COLUMN_STUDENT_AGE = "age";

        public static final int GENDER_UNKNOWN = 0;

        public static final int GENDER_MALE = 1;

        public static final int GENDER_FEMALE = 2;
    }
}
