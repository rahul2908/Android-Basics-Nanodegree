package com.example.dell.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int rightAns = 0;
    RadioButton r1,r2,r3;
    CheckBox c1,c2,c3,c4;
    CheckBox cc1,cc2,cc3,cc4;
    EditText e1,e2;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r1= (RadioButton) findViewById(R.id.the_chicago_bulls_radio_button);
        r2= (RadioButton) findViewById(R.id.fivetwoeightzero_radio_button);
        r3= (RadioButton) findViewById(R.id.five_radio_button);

        c1= (CheckBox) findViewById(R.id.sixtyfour_checkbox);
        c2= (CheckBox) findViewById(R.id.fiftyseven_checkbox);
        c3= (CheckBox) findViewById(R.id.thirtySix_checkbox);
        c4= (CheckBox) findViewById(R.id.fourtytwo_checkbox);

        cc1= (CheckBox) findViewById(R.id.south_Africa_checkbox);
        cc2= (CheckBox) findViewById(R.id.sri_Lanka_checkbox);
        cc3= (CheckBox) findViewById(R.id.india_checkbox);
        cc4= (CheckBox) findViewById(R.id.england_checkbox);

        e1= (EditText) findViewById(R.id.text1_edit_text);
        e2= (EditText) findViewById(R.id.text2_edit_text);

        t=(TextView) findViewById(R.id.result_text_view);

    }

    public void submitAnswers(View view){
        rightAns = 0;
        if (r1.isChecked()){
            rightAns++;
        }
        if (r2.isChecked()){
            rightAns++;
        }
        if (r3.isChecked()){
            rightAns++;
        }

        if (c1.isChecked() && c3.isChecked() && !c2.isChecked() && !c4.isChecked()){
            rightAns++;
        }
        if (cc2.isChecked() && cc3.isChecked() && !cc1.isChecked() && !cc4.isChecked()){
            rightAns++;
        }
        String textAns1 = "Rajendra Prasad";
        if(e1.getText().toString().equalsIgnoreCase(textAns1)){
            rightAns++;
        }
        String textAns2 = "Barack Obama";
        if (e2.getText().toString().equalsIgnoreCase(textAns2)){
            rightAns++;
            Toast.makeText(this,"Correct Answer",Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this,"Incorrect Answer",Toast.LENGTH_SHORT).show();
        }

        t.setText("You gave "+ rightAns + " right answers out of 7 questions");
    }

}
