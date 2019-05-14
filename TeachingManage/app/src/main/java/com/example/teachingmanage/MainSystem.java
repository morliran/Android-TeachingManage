package com.example.teachingmanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainSystem extends AppCompatActivity implements View.OnClickListener {
    private CardView classCard, attCard, courseCard, studentCard, logoutCard, aboutCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_system);
        classCard = (CardView)findViewById(R.id.myclassId);
        courseCard = (CardView)findViewById(R.id.mycourseId);
        studentCard = (CardView)findViewById(R.id.mystudentId);
        attCard = (CardView)findViewById(R.id.myattendanceId);
        logoutCard = (CardView)findViewById(R.id.mylogoutId);
        aboutCard = (CardView)findViewById(R.id.myaboutId);
        classCard.setOnClickListener(this);
        courseCard.setOnClickListener(this);
        studentCard.setOnClickListener(this);
        attCard.setOnClickListener(this);
        logoutCard.setOnClickListener(this);
        aboutCard.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId())
        {
            case R.id.myclassId : i = new Intent(this, MainmineClass.class); startActivity(i); break;
            case R.id.myattendanceId : i = new Intent(this, MainAttendance.class); startActivity(i); break;
            case R.id.mycourseId : i = new Intent(this, MainmineCourses.class); startActivity(i); break;
            case R.id.mystudentId : i = new Intent(this, MainmineStudents.class); startActivity(i); break;
            case R.id.mylogoutId : i = new Intent(this, MainActivity.class); startActivity(i); finish(); break;
            case R.id.myaboutId : Message.message(this, "This app was made by Liran Mor ©"); break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(this, MainSearch.class);
        switch (item.getItemId())
        {
            case R.id.item1:
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
