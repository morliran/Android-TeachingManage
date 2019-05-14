package com.example.teachingmanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teachingmanage.model.Attendance;
import com.example.teachingmanage.model.Courses;
import com.example.teachingmanage.model.Students;
import com.example.teachingmanage.mySpinners.CourseSpinnerVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainmineCourses extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static String STRING_EMPTY = "";
    static Spinner sItems = null;
    private static int selectedCourseId = 0;
    private static boolean isEdit = false;
    myDbAdapter myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmine_courses);
        myHelper = new myDbAdapter(this);
        populateSpinner();

        Button addCourse = (Button) findViewById(R.id.btnAdd);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCourse();
            }
        });
        Button closeMe = (Button) findViewById(R.id.btnClose);
        closeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainSystem.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void addNewCourse() {
        Courses courseDetails = new Courses();
        EditText courseId = (EditText) findViewById(R.id.txtCourseIdAdd);
        EditText courseRoom = (EditText) findViewById(R.id.txtCourseRoomAdd);
        EditText courseName = (EditText) findViewById(R.id.txtCourseNameAdd);
        EditText courseDate = (EditText) findViewById(R.id.txtCourseDateAdd);
        EditText courseHours = (EditText) findViewById(R.id.txtCourseHoursAdd);
        EditText courseClass = (EditText) findViewById(R.id.txtCourseClassAdd);
        boolean flag = myHelper.checkCourseClassId(courseClass.getText().toString());
        if (flag) {
            if (!STRING_EMPTY.equals(courseId.getText().toString()) &&
                    !STRING_EMPTY.equals(courseRoom.getText().toString()) &&
                    !STRING_EMPTY.equals(courseName.getText().toString()) &&
                    !STRING_EMPTY.equals(courseDate.getText().toString()) &&
                    !STRING_EMPTY.equals(courseHours.getText().toString()) &&
                    !STRING_EMPTY.equals(courseClass.getText().toString())) {
                courseDetails.setId(Integer.parseInt(courseId.getText().toString()));
                courseDetails.setCourseRoom(courseRoom.getText().toString().substring(0, 1).toUpperCase() + courseRoom.getText().toString().substring(1).toLowerCase());
                courseDetails.setCourseName(courseName.getText().toString().substring(0, 1).toUpperCase() + courseName.getText().toString().substring(1).toLowerCase());
                courseDetails.setCourseDate(courseDate.getText().toString());
                courseDetails.setTotalHours(Integer.parseInt(courseHours.getText().toString()));
                courseDetails.setCourseClassId(courseClass.getText().toString());
                myHelper.addCourse(courseDetails);
                addCourseToExistingStudentsClass(courseDetails);
                populateSpinner();
                courseId.setText("");
                courseRoom.setText("");
                courseName.setText("");
                courseDate.setText("");
                courseHours.setText("");
                courseClass.setText("");
            } else {
                Message.message(this, getString(R.string.noticeMesseage, "One or more fields left empty!"));
            }
        } else
            Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Please insert existing class id"));
    }

    private void addCourseToExistingStudentsClass(Courses c)
    {
        List studentList = myHelper.getStudentByClass(Integer.parseInt(c.getCourseClassId()));
        if (studentList.size() <= 0)//If the list is empty
            return;
        for (int i = 0; i < studentList.size(); i++)
        {
            Students s = (Students) studentList.get(i);
            Attendance attendance = new Attendance();
            attendance.setAttendanceStudentId(s.getId());
            attendance.setAttendanceStudentName(s.getStudentName() + ' ' + s.getStudentLastName());
            attendance.setAttendanceCourseId(c.getId());
            attendance.setAttendanceClassId(Integer.parseInt(s.getStudentClassId()));
            attendance.setAttendanceStatus(getString(R.string.notPresented));
            long id2 = myHelper.addAttendance(attendance);
            if(id2 <= 0)
            {
                Message.message(getApplicationContext(), getString(R.string.errorMesseage, "Inserted new course to existing students"));
                return;
            }
        }
    }

    private void populateSpinner()
    {
        List courseDetailsList = myHelper.getAllCourses();
        List courseSpinnerList = new ArrayList<>();
        for (int i = 0; i < courseDetailsList.size(); i++)
        {
            Courses Course = (Courses) courseDetailsList.get(i);
            CourseSpinnerVO courseSpinnerVO = new CourseSpinnerVO(Course.getId(), Course.getCourseName());
            courseSpinnerList.add(courseSpinnerVO);
        }
        if (courseDetailsList.size() == 0)
        {
            LinearLayout displayArea = (LinearLayout) findViewById(R.id.displayArea);
            displayArea.setVisibility(LinearLayout.GONE);

            LinearLayout editButton = (LinearLayout) findViewById(R.id.editButton);
            editButton.setVisibility(LinearLayout.GONE);

            LinearLayout editArea = (LinearLayout) findViewById(R.id.editArea);
            editArea.setVisibility(LinearLayout.GONE);

            LinearLayout courseSpinnerLayout = (LinearLayout) findViewById(R.id.courseSpinnerLayout);
            courseSpinnerLayout.setVisibility(LinearLayout.GONE);
        }
        else
        {
            LinearLayout courseSpinnerLayout = (LinearLayout) findViewById(R.id.courseSpinnerLayout);
            courseSpinnerLayout.setVisibility(LinearLayout.VISIBLE);

        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courseSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) findViewById(R.id.courseSpinner);
        sItems.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        CourseSpinnerVO courseSpinnerVO = (CourseSpinnerVO) sItems.getSelectedItem();
        int courseId = courseSpinnerVO.getCourseId();
        selectedCourseId = courseId;
        Courses CourseDetails = myHelper.getCourse(courseId);
        TextView txtCourseIdDisplay = (TextView) findViewById(R.id.txtCourseIdDisplay);

        TextView txtCourseRoomDisplay = (TextView) findViewById(R.id.txtCourseRoomDisplay);
        txtCourseRoomDisplay.setText(CourseDetails.getCourseRoom());

        TextView txtCourseNameDisplay = (TextView) findViewById(R.id.txtCourseNameDisplay);
        txtCourseNameDisplay.setText(CourseDetails.getCourseName());

        TextView txtCourseDateDisplay = (TextView) findViewById(R.id.txtCourseDateDisplay);
        txtCourseDateDisplay.setText(CourseDetails.getCourseDate());

        TextView txtCourseHourDisplay = (TextView) findViewById(R.id.txtCourseHoursDisplay);

        TextView txtCourseClassDisplay = (TextView) findViewById(R.id.txtCourseClassDisplay);
        txtCourseClassDisplay.setText(CourseDetails.getCourseClassId());

        String CourseId = Integer.toString(CourseDetails.getId());
        txtCourseIdDisplay.setText(CourseId);

        String hours = Integer.toString(CourseDetails.getTotalHours());
        txtCourseHourDisplay.setText(hours);

        LinearLayout displayArea = (LinearLayout) findViewById(R.id.displayArea);
        displayArea.setVisibility(LinearLayout.VISIBLE);

        LinearLayout editButton = (LinearLayout) findViewById(R.id.editButton);
        editButton.setVisibility(LinearLayout.VISIBLE);

        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCourse();
            }
        });
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
            }
        });
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void editCourse()
    {
        Courses CourseDetails = null;
        EditText courseId = (EditText) findViewById(R.id.txtCourseIdEdit);
        EditText courseRoom = (EditText) findViewById(R.id.txtCourseRoomEdit);
        EditText courseName = (EditText) findViewById(R.id.txtCourseNameEdit);
        EditText courseDate = (EditText) findViewById(R.id.txtCourseDateEdit);
        EditText courseHours = (EditText) findViewById(R.id.txtCourseHoursEdit);
        EditText courseClass = (EditText) findViewById(R.id.txtCourseClassEdit);
        if (isEdit)
        {
            if (!STRING_EMPTY.equals(courseId.getText().toString()) &&
                    !STRING_EMPTY.equals(courseRoom.getText().toString()) &&
                    !STRING_EMPTY.equals(courseName.getText().toString()) &&
                    !STRING_EMPTY.equals(courseDate.getText().toString()) &&
                    !STRING_EMPTY.equals(courseHours.getText().toString()) &&
                    !STRING_EMPTY.equals(courseClass.getText().toString()))
            {
                CourseDetails = new Courses();
                LinearLayout displayArea = (LinearLayout) findViewById(R.id.displayArea);
                displayArea.setVisibility(LinearLayout.VISIBLE);
                LinearLayout editArea = (LinearLayout) findViewById(R.id.editArea);
                editArea.setVisibility(LinearLayout.GONE);
                Button btnDelete = (Button) findViewById(R.id.btnDelete);
                btnDelete.setVisibility(Button.VISIBLE);

                CourseDetails.setId(selectedCourseId);
                CourseDetails.setCourseRoom(courseRoom.getText().toString().substring(0, 1).toUpperCase() + courseRoom.getText().toString().substring(1).toLowerCase());
                CourseDetails.setCourseName(courseName.getText().toString().substring(0, 1).toUpperCase() + courseName.getText().toString().substring(1).toLowerCase());
                CourseDetails.setCourseDate(courseDate.getText().toString());
                CourseDetails.setTotalHours(Integer.parseInt(courseHours.getText().toString()));
                CourseDetails.setCourseClassId(courseClass.getText().toString());
                myHelper.updateCourse(CourseDetails);
                populateSpinner();
                sItems = (Spinner) findViewById(R.id.courseSpinner);
                sItems.setSelection(getSpinnerPosition(sItems));
                isEdit = false;
            }
            else
            {
                Message.message(this, getString(R.string.noticeMesseage, "One or more fields left empty!"));
            }
        }
        else
        {
            CourseDetails = myHelper.getCourse(selectedCourseId);
            LinearLayout displayArea = (LinearLayout) findViewById(R.id.displayArea);
            displayArea.setVisibility(LinearLayout.GONE);
            LinearLayout editArea = (LinearLayout) findViewById(R.id.editArea);
            editArea.setVisibility(LinearLayout.VISIBLE);
            Button btnDelete = (Button) findViewById(R.id.btnDelete);
            btnDelete.setVisibility(Button.INVISIBLE);
            courseId.setText(Integer.toString(CourseDetails.getId()));
            courseRoom.setText(CourseDetails.getCourseRoom());
            courseName.setText(CourseDetails.getCourseName());
            courseDate.setText(CourseDetails.getCourseDate());
            courseHours.setText(Integer.toString(CourseDetails.getTotalHours()));
            courseClass.setText(CourseDetails.getCourseClassId());
            isEdit = true;
        }
    }

    private int getSpinnerPosition(Spinner spinner)
    {
        Adapter adapter = spinner.getAdapter();
        int i = 0;
        for (; i < adapter.getCount(); i++)
        {
            CourseSpinnerVO courseSpinnerVO = (CourseSpinnerVO) adapter.getItem(i);
            if (selectedCourseId == courseSpinnerVO.getCourseId())
            {
                return i;
            }
        }
        return 0;
    }

    private void deleteCourse()
    {
        myHelper.deleteCourse(selectedCourseId);
        populateSpinner();
    }
}
