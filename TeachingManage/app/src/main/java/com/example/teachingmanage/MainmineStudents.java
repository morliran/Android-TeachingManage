package com.example.teachingmanage;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teachingmanage.model.Attendance;
import com.example.teachingmanage.model.Courses;
import com.example.teachingmanage.model.Pictures;
import com.example.teachingmanage.model.Students;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class MainmineStudents extends AppCompatActivity {
    private static String STRING_EMPTY = "";
    private File IMGFile;
    private Uri uri;
    private String path, FileName;
    private Bitmap bitmap;
    EditText editId, editName, editLastName, editClassId;
    Button btnAddData, btnViewAll, btnDelete, btnUpdate;
    ImageView myImage;
    CheckBox cbWantEdit;
    myDbAdapter myHelper;
    String ClassId, StudId;
    Drawable OldPic;
    boolean flag, flag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmine_students);
        myHelper = new myDbAdapter(this);
        editId = (EditText) findViewById(R.id.editText_id);
        editName = (EditText) findViewById(R.id.editText_name);
        editLastName = (EditText) findViewById(R.id.editText_lastname);
        editClassId = (EditText) findViewById(R.id.editText_class);
        btnAddData = (Button) findViewById(R.id.button_add);
        btnViewAll = (Button) findViewById(R.id.button_viewAll);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnUpdate = (Button) findViewById(R.id.button_update);
        cbWantEdit = (CheckBox) findViewById(R.id.editCheck);
        myImage = (ImageView)findViewById(R.id.image_view);
        myImage.setImageResource(R.drawable.avatar);
        OldPic = myImage.getDrawable();
        Button closeMe = (Button) findViewById(R.id.btnClose);
        closeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainSystem.class);
                startActivity(i);
                finish();
            }
        });
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPic();
            }
        });
        AddData();
        viewAll();
        UpdateData();
        DeleteData();
        WantToMakeEdits();
    }

    public void WantToMakeEdits()
    {
        cbWantEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbWantEdit.isChecked() && STRING_EMPTY.equals(editId.getText().toString()))
                {
                    Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "To edit insert the student id and than check me again"));
                    cbWantEdit.setChecked(false);
                }
                else if (cbWantEdit.isChecked() && !STRING_EMPTY.equals(editId.getText().toString()))
                {
                    Students upStudent = myHelper.getStudent(Integer.parseInt(editId.getText().toString()));
                    if (upStudent != null)
                    {
                        StudId = editId.getText().toString();
                        ClassId = upStudent.getStudentClassId();
                        editName.setText(upStudent.getStudentName());
                        editLastName.setText(upStudent.getStudentLastName());
                        editClassId.setText(upStudent.getStudentClassId());
                        btnUpdate.setTextColor(Color.parseColor("red"));
                        Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Now you can click on the update button to save the changes"));
                    }
                    else
                    {
                        Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "There is not student with this id"));
                        btnUpdate.setTextColor(Color.parseColor("white"));
                        cbWantEdit.setChecked(false);
                        editId.setText("");
                    }
                }
                else
                    btnUpdate.setTextColor(Color.parseColor("white"));
            }
        });
    }

    public boolean isVailed()
    {
        if (STRING_EMPTY.equals(editId.getText().toString()) || STRING_EMPTY.equals(editName.getText().toString())
                || STRING_EMPTY.equals(editLastName.getText().toString()) || STRING_EMPTY.equals(editClassId.getText().toString()))
            return false;
        else
            return true;
    }

    private Students Arrange()
    {
        Students s = new Students();
        if (isVailed())
        {
            s.setId(Integer.parseInt(editId.getText().toString()));
            s.setStudentName(editName.getText().toString().substring(0, 1).toUpperCase() + editName.getText().toString().substring(1).toLowerCase());
            s.setStudentLastName(editLastName.getText().toString().substring(0, 1).toUpperCase() + editLastName.getText().toString().substring(1).toLowerCase());
            s.setStudentClassId(editClassId.getText().toString());
        }
        else
            Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "You have empty field's"));
        return s;
    }

    public void DeleteData()
    {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myHelper.deleteStudent(editId.getText().toString());
                if (deletedRows > 0)
                    Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Data deleted"));
                else
                    Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Data not deleted"));
            }
        });
    }

    public boolean CheckIdAndClassChange(Students s)
    {
        flag = myHelper.checkStudClassId(s.getStudentClassId());
        flag2 = (StudId.equals(String.valueOf(s.getId()))) ? true : false;//Check if the primary key table is updated
        if (flag && flag2)
            return true;
        else
            return false;
    }

    public void UpdateData()
    {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbWantEdit.isChecked() && btnUpdate.getCurrentTextColor() == Color.parseColor("red"))
                {
                    Students s = Arrange();
                    if(myImage.getDrawable() == OldPic)
                    {
                        s.setStudentHasPic(0);
                        s.setPic(null);
                    }
                    else
                    {
                        s.setStudentHasPic(1);
                        s.setPic(new Pictures(IMGFile.getPath(), IMGFile.getName()));
                    }
                    if (CheckIdAndClassChange(s))
                    {
                        myHelper.updateStudent(s);
                        Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Data updated successfully"));
                        editId.setText("");
                        editClassId.setText("");
                        editName.setText("");
                        editLastName.setText("");
                        myImage.setImageResource(R.drawable.avatar);
                    }
                    else
                    {
                        if (!flag)
                        {
                            Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Please insert existing class id"));
                            editClassId.setText(ClassId);
                        }
                        else if (!flag && !flag2)
                        {
                            Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Please enter existing class id\nAnd keep the id same as he was"));
                            editClassId.setText(ClassId);
                            editId.setText(StudId);
                        }
                        else
                        {
                            Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Please keep the id same as he was"));
                            editId.setText(StudId);
                        }
                    }
                    btnUpdate.setTextColor(Color.parseColor("white"));
                    cbWantEdit.setChecked(false);
                }
                else
                    Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Please insert student id first and click on the check box"));
            }
        });
    }

    public void AddData()
    {
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Students s = Arrange();
                if(myImage.getDrawable() == OldPic)
                {
                    s.setStudentHasPic(0);
                    s.setPic(null);
                }
                else
                {
                    s.setStudentHasPic(1);
                    s.setPic(new Pictures(IMGFile.getPath(), IMGFile.getName()));
                }
                flag = myHelper.checkStudClassId(s.getStudentClassId());
                if (flag)
                {
                    long id = myHelper.addStudent(s);
                    int cid;
                    if (id > 0)
                    {
                        Message.message(getApplicationContext(), "Insert successfully");
                        List classCourses = myHelper.getCourseByClass(Integer.parseInt(s.getStudentClassId()));
                        for (int i = 0; i < classCourses.size(); i++)
                        {
                            Courses Course = (Courses)classCourses.get(i);
                            Attendance attendance = new Attendance();
                            attendance.setAttendanceStudentId(s.getId());
                            attendance.setAttendanceStudentName(s.getStudentName() + ' ' + s.getStudentLastName());
                            attendance.setAttendanceCourseId(Course.getId());
                            cid = Course.getId();
                            attendance.setAttendanceClassId(Integer.parseInt(s.getStudentClassId()));
                            attendance.setAttendanceStatus(getString(R.string.notPresented));
                            if(s.getStudentHasPic() == 1)
                            {
                                attendance.setAttendancePicPath(s.getPic().getPath());
                                attendance.setAttendancePicName(s.getPic().getName());
                                attendance.setPictures(new Pictures(attendance.getAttendancePicPath(), attendance.getAttendancePicName()));
                            }
                            else
                                attendance.setPictures(null);
                            long id2 = myHelper.addAttendance(attendance);
                            editId.setText("");
                            editName.setText("");
                            editLastName.setText("");
                            editClassId.setText("");
                            myImage.setImageResource(R.drawable.avatar);
                            if (id2 <= 0)
                                Message.message(getApplicationContext(), getString(R.string.errorMesseage, "inserted attendance"));
                        }
                    }
                    else
                        Message.message(getApplicationContext(), getString(R.string.errorMesseage, "inserted -> id already exists"));
                }
                else
                    Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Please insert existing class id"));
            }
        });
    }

    public void viewAll()
    {
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myHelper.getAllStudents();
                if (res.getCount() == 0)
                {
                    showMessage("Error", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext())
                {
                    buffer.append(getString(R.string.studentId) + ": " + res.getString(0) + "\n");
                    buffer.append(getString(R.string.studentFirstName) + ": " + res.getString(1) + "\n");
                    buffer.append(getString(R.string.studentLastName) + ": " + res.getString(2) + "\n");
                    buffer.append(getString(R.string.studentClass) + ": " + res.getString(3) + "\n");
                    if(res.getString(4).equals("1"))
                        buffer.append(getString(R.string.studentPic) + ": " + getString(R.string.HasPic) + "\n\n");
                    else
                        buffer.append(getString(R.string.studentPic) + ": " + getString(R.string.NoPic) + "\n\n");
                }
                showMessage(getString(R.string.alertStudentTitle), buffer.toString());// Show all data
            }
        });
    }


    public void SaveFile(File Old, File NewL)
    {
        if (!Old.exists())
            return;
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(Old).getChannel();
            out = new FileOutputStream(NewL).getChannel();
            if(out != null && in != null)
                out.transferFrom(in, 0, in.size());
            if(in != null)
                in.close();
            if(out != null)
                out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SelectPic()
    {
        if (STRING_EMPTY.equals(editId.getText().toString()) || STRING_EMPTY.equals(editName.getText().toString())
                || STRING_EMPTY.equals(editLastName.getText().toString()))
            Message.message(getApplicationContext(), getString(R.string.noticeMesseage, "Please insert id and first name and last name"));
        else
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "Select Pic"), 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2 && data != null && data.getData() != null)
        {
            uri = data.getData();
            try {
                String[] Files = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, Files, null, null, null);
                cursor.moveToFirst();
                int Col = cursor.getColumnIndex(Files[0]);
                path = cursor.getString(Col);
                FileName = editName.getText().toString() + " " + editLastName.getText().toString() + editId.getText().toString() + ".jpg";
                cursor.close();
                IMGFile = new File(path);
                File NewLo = new File(Environment.getExternalStorageDirectory() + "/DCIM/TeachingManage", FileName);
                SaveFile(IMGFile, NewLo);
                IMGFile = NewLo;
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                myImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
