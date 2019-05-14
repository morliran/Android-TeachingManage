package com.example.teachingmanage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teachingmanage.model.Classes;
import com.example.teachingmanage.model.Courses;
import com.example.teachingmanage.model.Students;
import com.example.teachingmanage.mySpinners.ClassesSpinnerVO;
import com.example.teachingmanage.mySpinners.CourseSpinnerVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainAttendance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static String STRING_EMPTY = "";
    static Spinner sItems = null, sItems2 = null;
    private static int selectedClassId = 0;
    myDbAdapter myHelper;
    LinearLayout lMessage;
    Button takeAttendance, cntClassAttendance, cntCourseAttendance, CloseMe, ShowMe;
    TextView textM, currentDate;
    Calendar toDayCalendar = Calendar.getInstance();
    int day = toDayCalendar.get(Calendar.DAY_OF_MONTH), month = toDayCalendar.get(Calendar.MONTH) + 1, year = toDayCalendar.get(Calendar.YEAR);
    int classIdToSend, courseId, studentPic;
    String temp = "", res, courseDate, courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_attendance);
        myHelper = new myDbAdapter(this);
        populateSpinner();
        takeAttendance = (Button)findViewById(R.id.button_viewAll);
        cntClassAttendance = (Button)findViewById(R.id.btnClass);
        cntCourseAttendance = (Button)findViewById(R.id.btnCourse);
        ShowMe = (Button)findViewById(R.id.button_show);
        lMessage = (LinearLayout)findViewById(R.id.infoMessage);
        currentDate = (TextView)findViewById(R.id.checker1);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");
        textM = (TextView)findViewById(R.id.txtMessage);
        textM.setTypeface(custom_font);
        String str2 = getDateAsString(day, month, year);
        currentDate.setText(str2);
        CloseMe = (Button)findViewById(R.id.button_close);
        CloseMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainSystem.class);
                startActivity(i);
                finish();
            }
        });
        viewAll();
        TotalAttendanceInClass();
        TotalAttendanceInCourse();
    }
    private String getDateAsString(int d, int m, int y)
    {
        if(d < 10)
            temp += "0" + d;
        else
            temp += "" + d;
        if(m < 10)
            temp += "/0" + m;
        else
            temp += "/" + m;
        temp += "/" + y;
        return temp;
    }
    private Date getDateAsDate(String d)//for comparing between two dates
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(d);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }
    private String com(Date one, Date two)//compare function
    {
        if(one.before(two))
            return "one before two";
        if(one.equals(two))
            return "the dates are equal";
        if(one.after(two))
            return "one after two";
        return "NULL";
    }

    public void TotalAttendanceInClass()
    {
        cntClassAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt = myHelper.getAttendanceClassCount(selectedClassId, getString(R.string.presented));
                showMessage("Class " + sItems.getSelectedItem() + " total attendance", "Equals = " + cnt);
            }
        });
    }

    public void TotalAttendanceInCourse()
    {
        cntCourseAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cnt = myHelper.getAttendanceCourseCount(courseId, getString(R.string.presented));
                showMessage("Course " + sItems2.getSelectedItem() + " total attendance", "Equals = " + cnt);
            }
        });
    }

    public void viewAll()
    {
        ShowMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myHelper.getAllAttendanceWithCursor();
                if(res.getCount() == 0)
                {
                    showMessage("Error", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext())
                {
                    buffer.append(getString(R.string.serialNum) + ": " + res.getString(0) + "\n");
                    buffer.append(getString(R.string.studentId) + ": " + res.getString(1) + "\n");
                    buffer.append(getString(R.string.studentFullName) + ": " + res.getString(2) + "\n");
                    buffer.append(getString(R.string.classId) + ": " + res.getString(3) + "\n");
                    buffer.append(getString(R.string.courseId) + ": " + res.getString(4) + "\n");
                    buffer.append(getString(R.string.attendanceStatus) + ": " + res.getString(5) + "\n\n");
                }
                showMessage(getString(R.string.alertAttendanceTitle),buffer.toString());// Show all data
            }
        });
    }

    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    private void populateSpinner()
    {
        List classesDetailsList = myHelper.getAllClasses();
        List classesSpinnerList = new ArrayList<>();
        for (int i = 0; i < classesDetailsList.size(); i++)
        {
            Classes Classes = (Classes)classesDetailsList.get(i);
            ClassesSpinnerVO classesSpinnerVO = new ClassesSpinnerVO(Classes.getId(), Classes.getClassesName());
            classesSpinnerList.add(classesSpinnerVO);
        }
        if(classesDetailsList.size() == 0)
        {
            LinearLayout classesSpinnerLayout = (LinearLayout)findViewById(R.id.classSpinnerLayout);
            classesSpinnerLayout.setVisibility(LinearLayout.GONE);
        }
        else
        {
            LinearLayout classesSpinnerLayout = (LinearLayout)findViewById(R.id.classSpinnerLayout);
            classesSpinnerLayout.setVisibility(LinearLayout.VISIBLE);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, classesSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner)findViewById(R.id.classSpinner);
        sItems.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        ClassesSpinnerVO classesSpinnerVO = (ClassesSpinnerVO)sItems.getSelectedItem();
        int classesId = classesSpinnerVO.getClassId();
        selectedClassId = classesId;
        classIdToSend = classesId;
        populateClassCourses(classesId);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void populateClassCourses(int class_id)
    {
        List courseDetailsList = myHelper.getCourseByClass(class_id);
        List courseSpinnerList = new ArrayList<>();
        for (int i = 0; i < courseDetailsList.size(); i++)
        {
            Courses Course = (Courses)courseDetailsList.get(i);
            CourseSpinnerVO courseSpinnerVO = new CourseSpinnerVO(Course.getId(), Course.getCourseName());
            courseSpinnerList.add(courseSpinnerVO);
        }
        if(courseDetailsList.size() == 0)
        {
            LinearLayout courseSpinnerLayout = (LinearLayout)findViewById(R.id.courseSpinnerLayout);
            courseSpinnerLayout.setVisibility(LinearLayout.GONE);
        }
        else
        {
            LinearLayout courseSpinnerLayout = (LinearLayout)findViewById(R.id.courseSpinnerLayout);
            courseSpinnerLayout.setVisibility(LinearLayout.VISIBLE);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courseSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems2 = (Spinner) findViewById(R.id.courseSpinner);
        sItems2.setAdapter(adapter);
        sItems2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CheckEnabledAttendance();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void CheckEnabledAttendance()
    {
        courseName = sItems2.getSelectedItem().toString();
        courseId = myHelper.getCourseId(courseName);
        courseDate = myHelper.getCourseDate(courseId, courseName);
        res = com(getDateAsDate(courseDate), getDateAsDate(currentDate.getText().toString()));
        if(res.equals("the dates are equal") || res.equals("one before two"))
        {
            takeAttendance.setEnabled(true);
            lMessage.setVisibility(LinearLayout.INVISIBLE);
            takeAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MainAttendanceFill.class);
                    i.putExtra("Course name", courseName);
                    i.putExtra("Course date", courseDate);
                    i.putExtra("Class id", String.valueOf(classIdToSend));
                    i.putExtra("Course id", String.valueOf(courseId));
                    startActivity(i);
                }
            });
        }
        else
        {
            lMessage.setVisibility(LinearLayout.VISIBLE);
            takeAttendance.setEnabled(false);
        }
    }
}
