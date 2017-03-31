package com.example.dell.reportcard1;

/**
 * Created by dell on 3/17/2017.
 */

public class ReportCard {

    private String mStudentName;
    private String mRollNo;
    private int mEnglishMarks;
    private int mChemistryMarks;
    private int mPhysicsMarks;
    private int mMathsMarks;
    private int sum;
    private float perc;
    private String grade;

    public ReportCard(String studentName,String rollNo,int englishMarks,int chemistryMarks,int physicsMarks,int mathMarks){
        this.mStudentName = studentName;
        this.mRollNo = rollNo;
        this.mEnglishMarks = englishMarks;
        this.mChemistryMarks = chemistryMarks;
        this.mPhysicsMarks = physicsMarks;
        this.mMathsMarks = mathMarks;
    }

    public String getmStudentName(){
        return mStudentName;
    }

    public void setmStudentName(String studentName){
        this.mStudentName = studentName;
    }

    public String getmRollNo(){
        return mRollNo;
    }

    public void setmRollNo(String rollNo){
        this.mRollNo = rollNo;
    }

    public int getmChemistryMarks() {
        return mChemistryMarks;
    }

    public void  setmChemistryMarks(int chemistryMarks){
        this.mChemistryMarks = chemistryMarks;
    }

    public int getmEnglishMarks() {
        return mEnglishMarks;
    }

    public void setmEnglishMarks(int englishMarks){
        this.mEnglishMarks = englishMarks;
    }

    public int getmPhysicsMarks() {
        return mPhysicsMarks;
    }

    public void setmPhysicsMarks(int physicsMarks){
        this.mPhysicsMarks = physicsMarks;
    }

    public int getmMathsMarks() {
        return mMathsMarks;
    }

    public void setmMathsMarks(int mathsMarks){
        this.mMathsMarks = mathsMarks;
    }

    private String displayResult(int mEnglishMarks,int mChemistryMarks,int mPhysicsMarks,int mMathsMarks){
        sum = mEnglishMarks + mChemistryMarks + mPhysicsMarks + mMathsMarks;
        perc = (sum/400)*100;

        if (perc>=90){
            grade = "A";
        }
        else if (perc>=80 && perc<90){
            grade = "B";
        }
        else if (perc>=70 && perc<80){
            grade = "C";
        }
        else if (perc>=60 && perc<70){

            grade = "D";
        }
        else if (perc>=50 && perc<60){
            grade = "E";
        }
        else {
            grade = "Fail";
        }
        return grade;
    }

    @Override
    public String toString() {
        return "ReportCard{" +
                "studentName='" + mStudentName + '\'' +
                ", rollNumber=" + mRollNo +
                ", englishMarks=" + mEnglishMarks +
                ", mathMarks=" + mMathsMarks +
                ", physicsMarks=" + mPhysicsMarks +
                ", chemistryMarks=" + mChemistryMarks +
                ", grade='" + displayResult(getmEnglishMarks(), getmChemistryMarks(), getmPhysicsMarks(), getmMathsMarks())  + '\'' +
                '}';
    }
}
