package com.example.teachingmanage;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.teachingmanage.adapters.RecycleAdapter;
import com.example.teachingmanage.model.Attendance;
import com.example.teachingmanage.model.Courses;
import com.example.teachingmanage.model.Pictures;
import com.example.teachingmanage.model.Students;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MainAttendanceFill extends AppCompatActivity {
    myDbAdapter myHelper;
    Button show, exportMyExcel, importMyExcel;
    TextView t, t2;
    RecyclerView recyclerView;
    RecycleAdapter recycler;
    List<Attendance> datamodel;
    String courseDate, courseName, studentName, studentStatus, pathPic, namePic;
    String[] studsStatus = null;
    String[] studsNames = null;
    int classIdToSend, courseId, studentId, studHavePic;
    int[] studIds = null;
    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /*
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_attendance_fill);
        myHelper = new myDbAdapter(this);
        show = (Button) findViewById(R.id.view);
        exportMyExcel = (Button) findViewById(R.id.exportExcel);
        importMyExcel = (Button) findViewById(R.id.importExcel);
        t = (TextView) findViewById(R.id.textCourseInfo);
        t2 = (TextView) findViewById(R.id.textOnDate);
        datamodel = new ArrayList<Attendance>();
        courseName = getIntent().getStringExtra("Course name");
        courseDate = getIntent().getStringExtra("Course date");
        classIdToSend = Integer.parseInt(getIntent().getStringExtra("Class id"));
        courseId = Integer.parseInt(getIntent().getStringExtra("Course id"));
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        verifyStoragePermissions(this);
        t.setText(t.getText().toString() + " " + courseName + " " + t2.getText().toString() + " " + courseDate);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datamodel = myHelper.getAttendancedata(classIdToSend, courseId);
                recycler = new RecycleAdapter(datamodel);
                RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(reLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(recycler);
            }
        });
        exportMyExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Cursor cursor = myHelper.getAttendancedataCursor(classIdToSend, courseId);
                File sd = Environment.getExternalStorageDirectory();
                String csvFile = "myData.xls";
                File directory = new File(sd.getAbsolutePath());
                if (!directory.isDirectory())//create directory if not exist
                {
                    directory.mkdirs();
                    Log.v("Error", String.valueOf(directory.mkdirs()));
                }
                try {
                    //file path
                    File file = new File(directory, csvFile);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));
                    WritableWorkbook workbook;
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    //Excel sheet name. 0 represents first sheet
                    WritableSheet sheet = workbook.createSheet("attendanceList", 0);
                    sheet.addCell(new Label(0, 0, "Id")); // column and row
                    sheet.addCell(new Label(1, 0, "Name"));
                    sheet.addCell(new Label(2, 0, "Status"));
                    if (cursor.moveToFirst())
                    {
                        do {
                            String studentId = cursor.getString(cursor.getColumnIndex("Attendance_Student_Id"));
                            String studentName = cursor.getString(cursor.getColumnIndex("Attendance_Student_Name"));
                            String Status = cursor.getString(cursor.getColumnIndex("Attendance_Attendance_Status"));
                            int i = cursor.getPosition() + 1;
                            sheet.addCell(new Label(0, i, studentId));
                            sheet.addCell(new Label(1, i, studentName));
                            sheet.addCell(new Label(2, i, Status));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();//closing cursor
                    workbook.write();
                    workbook.close();
                    Message.message(getApplicationContext(), "Your file myData.xls saved successfully to your external storage");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        importMyExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                long ins = 0;
                readExcel();
                for (int i = 0; i < studIds.length; i++)
                {
                    studentId = studIds[i];
                    studentName = studsNames[i];
                    studentStatus = studsStatus[i];
                    Students s = new Students();
                    s = myHelper.getStudent(studentId);
                    if (s == null)//If there is student in the excel sheet that doesn't existing in the database
                    {
                        Students newStud = new Students();
                        newStud.setId(studentId);
                        int spaceIndex = studentName.indexOf(" ");
                        String firstName = studentName.substring(0, spaceIndex);
                        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                        String lastName = studentName.substring(spaceIndex + 1);
                        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
                        newStud.setStudentName(firstName.trim());
                        newStud.setStudentLastName(lastName.trim());
                        newStud.setStudentClassId(String.valueOf(classIdToSend));
                        newStud.setStudentHasPic(0);
                        newStud.setPic(null);//Set the new student profile picture to the default one
                        long inserted = myHelper.addStudent(newStud);
                        if (inserted > 0)
                        {
                            Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Student inserted successfully"));
                            //Adding attendance to all class courses for the new student
                            List classCourses = myHelper.getCourseByClass(Integer.parseInt(newStud.getStudentClassId()));
                            for (int j = 0; j < classCourses.size(); j++)
                            {
                                Courses Course = (Courses)classCourses.get(j);
                                Attendance a = new Attendance();
                                a.setAttendanceStudentId(studentId);
                                a.setAttendanceStudentName(newStud.getStudentName() + ' ' + newStud.getStudentLastName());
                                a.setAttendanceCourseId(Course.getId());
                                a.setAttendanceClassId(classIdToSend);
                                a.setAttendanceStatus(studentStatus);
                                a.setPictures(null);
                                long inse = myHelper.addAttendance(a);//Add student attendance for each class courses
                                if (inse <= 0)
                                    Message.message(getApplicationContext(), getString(R.string.errorMesseage, "inserted attendance"));
                            }
                            return;
                        }
                        else
                            return;
                    }
                    Log.e("studIds => ", String.valueOf(studentId));//For debugging check
                    Log.e("studsNames => ", studentName);//For debugging check
                    int remove, attendanceId = myHelper.getAttendanceId(studentId, courseId, classIdToSend);
                    Log.v("attend id => ", String.valueOf(attendanceId));//For debugging check
                    if (attendanceId > 0)
                    {
                        studHavePic = myHelper.checkStudentPic(studentId);
                        if(studHavePic == 1)//If the student have picture save his picture path and name
                        {
                            pathPic = Environment.getExternalStorageDirectory() + "/DCIM/TeachingManage";
                            namePic = s.getStudentName() + " " + s.getStudentLastName() + s.getId() + ".jpg";
                        }
                        remove = myHelper.deleteAttendance(attendanceId);
                    }
                    else
                    {
                        Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Not exists id attendance"));
                        return;
                    }
                    if (remove > 0)//Here i am updated the new attendance after deleting the previous one
                    {
                        Attendance a = new Attendance();
                        a.setId(attendanceId);
                        a.setAttendanceStudentName(s.getStudentName() + " " + s.getStudentLastName());
                        a.setAttendanceStudentId(s.getId());
                        a.setAttendanceClassId(classIdToSend);
                        a.setAttendanceCourseId(courseId);
                        a.setAttendanceStatus(studentStatus);
                        if(studHavePic == 1)//Here i add again the picture file path and name to the updated student
                        {
                            a.setAttendancePicPath(pathPic);
                            a.setAttendancePicName(namePic);
                            a.setPictures(new Pictures(a.getAttendancePicPath(), a.getAttendancePicName()));
                        }
                        else
                            a.setPictures(null);//If the student doesn't have picture set his profile picture to the default one
                        ins = myHelper.addAttendanceFromExcel(a);
                    }
                    else
                    {
                        Message.message(getApplicationContext(), getString(R.string.errorMesseage, "removing"));
                        return;
                    }
                }
                if (ins > 0)//Displaying on the RecyclerView the import attendance
                {
                    datamodel = myHelper.getAttendancedata(classIdToSend, courseId);
                    recycler = new RecycleAdapter(datamodel);
                    RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(reLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(recycler);
                }
                else
                {
                    Message.message(getApplicationContext(), getString(R.string.errorMesseage, "inserting update data"));
                    return;
                }
            }
        });
    }

    public void readExcel()
    {
        int rowCount = 0;
        String[][] res = null;
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "myData.xls";
        File directory = new File(sd.getAbsolutePath());
        File inputWorkbook = new File(directory, csvFile);
        if (inputWorkbook.exists())
        {
            Workbook w = null;
            try {
                w = Workbook.getWorkbook(inputWorkbook);
                // Get the first sheet
                Sheet sheet = w.getSheet(0);
                rowCount = sheet.getRows() - 1;//Do not include header line
                res = new String[rowCount][];
                // Loop over column and lines
                for (int i = 0; i < rowCount; i++)
                {
                    Cell[] row = sheet.getRow(i + 1);//So i will not get the header line
                    res[i] = new String[row.length];
                    for (int j = 0; j < row.length; j++) {
                        res[i][j] = row[j].getContents();
                    }
                }
            } catch (BiffException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                w.close();
            }
            setIds(res, rowCount);
            setNames(res, rowCount);
            setStatus(res, rowCount);
        }
    }

    public void setIds(String[][] res, int size)
    {
        studIds = new int[size];
        for (int i = 0; i < res.length; i++)
        {
            for (int j = 0; j < res[i].length; j++)
            {
                studIds[i] = Integer.parseInt(res[i][0]);
            }
        }
    }

    public void setNames(String[][] res, int size)
    {
        studsNames = new String[size];
        for (int i = 0; i < res.length; i++)
        {
            for (int j = 0; j < res[i].length; j++)
                studsNames[i] = res[i][1];
        }
    }

    public void setStatus(String[][] res, int size)
    {
        studsStatus = new String[size];
        for (int i = 0; i < res.length; i++)
        {
            for (int j = 0; j < res[i].length; j++)
            {
                if (res[i][2].equals(getString(R.string.presented)) || res[i][2].equals(getString(R.string.lateStats)) || res[i][2].equals(getString(R.string.notPresented)))
                    studsStatus[i] = res[i][2];
                else
                    studsStatus[i] = getString(R.string.notPresented);
            }
        }
    }
}
