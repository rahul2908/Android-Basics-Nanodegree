package com.example.dell.volleyball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int scoreTeamA;
    int scoreTeamB;
    String foulA="Foul";
    String foulB="Foul";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addOneForTeamA(View view){
        scoreTeamA=scoreTeamA + 1;
        displaya(scoreTeamA);
    }

    public void addOneForTeamB(View view){
        scoreTeamB = scoreTeamB + 1;
        displayb(scoreTeamB);
    }

    public void reset(View view){
        scoreTeamA = 0;
        scoreTeamB = 0;
        displaya(scoreTeamA);
        displayb(scoreTeamB);
    }

    private void displaya(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.scorea_text_view);
        quantityTextView.setText("" + number);
    }


    private void displaya1(String number) {
        TextView quantityTextView = (TextView) findViewById(R.id.scorea_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayb1(String number) {
        TextView quantityTextView = (TextView) findViewById(R.id.scoreb_text_view);
        quantityTextView.setText("" + number);
    }

    public void foulForTeamA(View view){
        displaya1(foulA);
    }
    public void foulforTeamB(View view){
        displayb1(foulB);
    }

    private void displayb(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.scoreb_text_view);
        quantityTextView.setText("" + number);
    }
}
