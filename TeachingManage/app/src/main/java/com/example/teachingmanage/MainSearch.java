package com.example.teachingmanage;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.teachingmanage.model.Classes;
import com.example.teachingmanage.model.Courses;
import com.example.teachingmanage.model.Students;

public class MainSearch extends AppCompatActivity implements View.OnClickListener {
    private static String STRING_EMPTY = "";
    myDbAdapter myHelper;
    EditText search;
    Button btn1, btn2, btn3, closeMe, cntStudents, cntStudentsClass;
    ImageView img;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        myHelper = new myDbAdapter(this);
        search = (EditText)findViewById(R.id.searchFor);
        btn1 = (Button)findViewById(R.id.searchClass);
        btn2 = (Button)findViewById(R.id.searchCourse);
        btn3 = (Button)findViewById(R.id.searchStudent);
        cntStudents = (Button)findViewById(R.id.searchCntStudents);
        cntStudentsClass = (Button)findViewById(R.id.searchCntStudentsInClass);
        closeMe = (Button)findViewById(R.id.btnClose);
        img = (ImageView)findViewById(R.id.searchPic);
        img.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        cntStudents.setOnClickListener(this);
        cntStudentsClass.setOnClickListener(this);
        closeMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int cnt;
        switch (v.getId())
        {
            case R.id.searchPic:
                if (!STRING_EMPTY.equals(search.getText().toString()))
                {
                    switch (flag)
                    {
                        case 1:
                            viewAll("Class");
                            break;
                        case 2:
                            viewAll("Course");
                            break;
                        case 3:
                            viewAll("Student");
                            break;
                    }
                    search.setText("");
                    search.setHint(getString(R.string.searchFor));
                    setFlag(0);
                }
                else
                    Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Search field cannot been empty!"));
                break;
            case R.id.searchClass:
                search.setHint(getString(R.string.searchByClass));
                setFlag(1);
                break;
            case R.id.searchCourse:
                search.setHint(getString(R.string.searchByCourse));
                setFlag(2);
                break;
            case R.id.searchStudent:
                search.setHint(getString(R.string.searchByStudent));
                setFlag(3);
                break;
            case R.id.searchCntStudents:
                cnt = myHelper.getStudentCount();
                Message.message(getApplicationContext(), "Total student in data base = " + cnt);
                break;
            case R.id.searchCntStudentsInClass:
                if (!STRING_EMPTY.equals(search.getText().toString()))
                {
                    cnt = myHelper.getCntClassStudents(Integer.parseInt(search.getText().toString()));
                    Message.message(getApplicationContext(), "Total student in class " + search.getText().toString() + " = " + cnt);
                }
                else
                    Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Search field cannot been empty!"));
                break;
            case R.id.btnClose: Intent i = new Intent(this, MainSystem.class); startActivity(i); finish(); break;
        }
    }
    public void setFlag(int f)
    {
        this.flag = f;
    }

    public void viewAll(String type)
    {
        StringBuffer buffer = new StringBuffer();
        switch (type)
        {
            case "Class":
                Classes clas = myHelper.getClasses(Integer.parseInt(search.getText().toString()));
                buffer.append(getString(R.string.classId) + ": " + clas.getId() + "\n");
                buffer.append(getString(R.string.className) + ": " + clas.getClassesName() + "\n\n");
                showMessage(getString(R.string.alertClassTitle), buffer.toString());
                break;
            case "Course":
                Courses cour = myHelper.getCourse(Integer.parseInt(search.getText().toString()));
                buffer.append(getString(R.string.courseId) + ": " + cour.getId() + "\n");
                buffer.append(getString(R.string.courseName) + ": " + cour.getCourseName() + "\n");
                buffer.append(getString(R.string.courseRoom) + ": " + cour.getCourseRoom() + "\n");
                buffer.append(getString(R.string.courseDate) + ": " + cour.getCourseDate() + "\n");
                buffer.append(getString(R.string.courseClass) + ": " + cour.getCourseClassId() + "\n\n");
                showMessage(getString(R.string.alertCourseTitle), buffer.toString());
                break;
            case "Student":
                Students stud = myHelper.getStudent(Integer.parseInt(search.getText().toString()));
                buffer.append(getString(R.string.studentId) + ": " + stud.getId() + "\n");
                buffer.append(getString(R.string.studentFirstName) + ": " + stud.getStudentName() + "\n");
                buffer.append(getString(R.string.studentLastName) + ": " + stud.getStudentLastName() + "\n");
                buffer.append(getString(R.string.studentClass) + ": " + stud.getStudentClassId() + "\n\n");
                showMessage(getString(R.string.alertStudentTitle), buffer.toString());
                break;
        }
    }
    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
