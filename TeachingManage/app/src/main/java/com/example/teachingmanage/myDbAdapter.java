package com.example.teachingmanage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.teachingmanage.model.Attendance;
import com.example.teachingmanage.model.Classes;
import com.example.teachingmanage.model.Courses;
import com.example.teachingmanage.model.Pictures;
import com.example.teachingmanage.model.Students;

import java.util.ArrayList;
import java.util.List;

public class myDbAdapter {
    myDbHelper helper;

    public myDbAdapter(Context context)
    {
        helper = new myDbHelper(context);
    }
    /*Classes_Table_Functions*/
    public long addClasses(Classes classes) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.CLASES_ID, classes.getId());
        contentValues.put(myDbHelper.CLASES_NAME, classes.getClassesName());
        long newRowId = db.insert(myDbHelper.TABLE_CLASSES, null, contentValues);
        db.close();
        return newRowId;
    }
    //Function to get class details by sending her id
    public Classes getClasses(int classes_id) {
        Classes classesDetails = new Classes();
        SQLiteDatabase db = helper.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {myDbHelper.CLASES_ID, myDbHelper.CLASES_NAME};
        String selection = myDbHelper.CLASES_ID + " = ?";//Select condition
        String[] selectionArgs = {String.valueOf(classes_id)};//Arguments for selection
        Cursor cursor = db.query(myDbHelper.TABLE_CLASSES, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {
            classesDetails.setId(cursor.getInt(0));
            classesDetails.setClassesName(cursor.getString(1));
        }
        db.close();
        return classesDetails;
    }
    //Function to get all classes that store inside the database
    public List getAllClasses() {
        List classesDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_CLASSES + " ORDER BY " + myDbHelper.CLASES_ID + " DESC";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//If TABLE has rows
        {
            //Loop through the table rows
            do {
                Classes classesDetails = new Classes();
                classesDetails.setId(cursor.getInt(0));
                classesDetails.setClassesName(cursor.getString(1));
                classesDetailsList.add(classesDetails);//Add classes details to list
            } while (cursor.moveToNext());
        }
        db.close();
        return classesDetailsList;
    }
    public void updateClasses(Classes classes) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String classesIds[] = {String.valueOf(classes.getId())};
        ContentValues classes_details = new ContentValues();
        classes_details.put(myDbHelper.CLASES_NAME, classes.getClassesName());
        db.update(myDbHelper.TABLE_CLASSES, classes_details, myDbHelper.CLASES_ID + " = ?", classesIds);
        db.close();
    }
    public void deleteClasses(int classesId) {
        String classesIds[] = {String.valueOf(classesId)};
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(myDbHelper.TABLE_CLASSES, myDbHelper.CLASES_ID + " = ?", classesIds);
        db.close();
    }

    /*Course_Table_Functions*/
    public long addCourse(Courses course) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.COURSE_ID, course.getId());
        contentValues.put(myDbHelper.COURSE_ROOM, course.getCourseRoom());
        contentValues.put(myDbHelper.COURSE_NAME, course.getCourseName());
        contentValues.put(myDbHelper.COURSE_DATE, course.getCourseDate());
        contentValues.put(myDbHelper.COURSE_HOURS, course.getTotalHours());
        contentValues.put(myDbHelper.COURSE_CLASS, course.getCourseClassId());
        long newRowId = db.insert(myDbHelper.TABLE_COURSES, null, contentValues);
        db.close();
        return newRowId;
    }
    //Function to get course details by sending his id
    public Courses getCourse(int course_id) {
        Courses courseDetails = new Courses();
        SQLiteDatabase db = helper.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {myDbHelper.COURSE_ID, myDbHelper.COURSE_ROOM, myDbHelper.COURSE_NAME, myDbHelper.COURSE_DATE, myDbHelper.COURSE_HOURS, myDbHelper.COURSE_CLASS};
        String selection = myDbHelper.COURSE_ID + " = ?";//Select condition
        String[] selectionArgs = {String.valueOf(course_id)};//Arguments for selection
        Cursor cursor = db.query(myDbHelper.TABLE_COURSES, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {
            courseDetails.setId(cursor.getInt(0));
            courseDetails.setCourseRoom(cursor.getString(1));
            courseDetails.setCourseName(cursor.getString(2));
            courseDetails.setCourseDate(cursor.getString(3));
            courseDetails.setTotalHours(cursor.getInt(4));
            courseDetails.setCourseClassId(cursor.getString(5));
        }
        db.close();
        return courseDetails;
    }
    //Function to get course details by sending only his name
    public Courses getCourseByName(String name) {
        Courses courseDetails = new Courses();
        SQLiteDatabase db = helper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_COURSES + " Where " + myDbHelper.COURSE_NAME + " = ' " + name + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//If TABLE has rows
        {
            //Loop through the table rows
            do {
                courseDetails.setId(cursor.getInt(0));
                courseDetails.setCourseRoom(cursor.getString(1));
                courseDetails.setCourseName(cursor.getString(2));
                courseDetails.setCourseDate(cursor.getString(3));
                courseDetails.setTotalHours(cursor.getInt(4));
                courseDetails.setCourseClassId(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        db.close();
        return courseDetails;
    }
    //Function to get the courseDate by sending id of course and name
    public String getCourseDate(int course_id, String name){
        Courses courseDetails = new Courses();
        SQLiteDatabase db = helper.getReadableDatabase();
        String Query = "Select * From "+ myDbHelper.TABLE_COURSES + " Where " + myDbHelper.COURSE_ID + " = " + course_id +
                " And " + myDbHelper.COURSE_NAME + " = '" + name + "'";
        Cursor cursor = db.rawQuery(Query,null);
        if (cursor.moveToFirst())//if TABLE has rows
        {
            //Loop through the table rows
            do {
                courseDetails.setId(cursor.getInt(0));
                courseDetails.setCourseRoom(cursor.getString(1));
                courseDetails.setCourseName(cursor.getString(2));
                courseDetails.setCourseDate(cursor.getString(3));
                courseDetails.setTotalHours(cursor.getInt(4));
                courseDetails.setCourseClassId(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        db.close();
        return courseDetails.getCourseDate();
    }
    //Function to get course id by sending some course name
    public int getCourseId(String name) {
        Courses courseDetails = new Courses();
        SQLiteDatabase db = helper.getReadableDatabase();
        String Query = "Select * From "+ myDbHelper.TABLE_COURSES + " Where " + myDbHelper.COURSE_NAME + " = '" + name + "'";
        Cursor cursor = db.rawQuery(Query,null);
        if (cursor.moveToFirst())//If TABLE has rows
        {
            //Loop through the table rows
            do {
                courseDetails.setId(cursor.getInt(0));
                courseDetails.setCourseRoom(cursor.getString(1));
                courseDetails.setCourseName(cursor.getString(2));
                courseDetails.setCourseDate(cursor.getString(3));
                courseDetails.setTotalHours(cursor.getInt(4));
                courseDetails.setCourseClassId(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        db.close();
        return courseDetails.getId();
    }
    //Function to get all courses of some class
    public List getCourseByClass(int course_class_id) {
        List courseDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_COURSES + " Where " + myDbHelper.COURSE_CLASS + " = " + course_class_id;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//If TABLE has rows
        {
            //Loop through the table rows
            do {
                Courses courseDetails = new Courses();
                courseDetails.setId(cursor.getInt(0));
                courseDetails.setCourseRoom(cursor.getString(1));
                courseDetails.setCourseName(cursor.getString(2));
                courseDetails.setCourseDate(cursor.getString(3));
                courseDetails.setTotalHours(cursor.getInt(4));
                courseDetails.setCourseClassId(cursor.getString(5));
                courseDetailsList.add(courseDetails);//Add course details to list
            } while (cursor.moveToNext());
        }
        db.close();
        return courseDetailsList;
    }
    //Function to get all courses that store in the database
    public List getAllCourses() {
        List courseDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_COURSES + " ORDER BY " + myDbHelper.COURSE_ID + " DESC";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//If TABLE has rows
        {
            //Loop through the table rows
            do {
                Courses courseDetails = new Courses();
                courseDetails.setId(cursor.getInt(0));
                courseDetails.setCourseRoom(cursor.getString(1));
                courseDetails.setCourseName(cursor.getString(2));
                courseDetails.setCourseDate(cursor.getString(3));
                courseDetails.setTotalHours(cursor.getInt(4));
                courseDetails.setCourseClassId(cursor.getString(5));
                courseDetailsList.add(courseDetails);//Add course details to list
            } while (cursor.moveToNext());
        }
        db.close();
        return courseDetailsList;
    }
    public void updateCourse(Courses course) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String courseIds[] = {String.valueOf(course.getId())};
        ContentValues course_details = new ContentValues();
        course_details.put(myDbHelper.COURSE_ROOM, course.getCourseRoom());
        course_details.put(myDbHelper.COURSE_NAME, course.getCourseName());
        course_details.put(myDbHelper.COURSE_DATE, course.getCourseDate());
        course_details.put(myDbHelper.COURSE_HOURS, course.getTotalHours());
        course_details.put(myDbHelper.COURSE_CLASS, course.getCourseClassId());
        db.update(myDbHelper.TABLE_COURSES, course_details, myDbHelper.COURSE_ID + " = ?", courseIds);
        db.close();
    }
    public void deleteCourse(int courseId) {
        String courseIds[] = {String.valueOf(courseId)};
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(myDbHelper.TABLE_COURSES, myDbHelper.COURSE_ID + " = ?", courseIds);
        db.close();
    }
    //Function to check for existing class id for new course
    public boolean checkCourseClassId(String id) {
        boolean flag = false;
        int temp;
        List classesDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_CLASSES + " ORDER BY " + myDbHelper.CLASES_ID + " ASC";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//If TABLE has rows
        {
            //Loop through the table rows
            do {
                Classes classesDetails = new Classes();
                classesDetails.setId(cursor.getInt(0));
                temp = cursor.getInt(0);
                if(temp == Integer.parseInt(id))
                    flag = true;
                classesDetails.setClassesName(cursor.getString(1));
                classesDetailsList.add(classesDetails);//Add classes details to list
            } while (cursor.moveToNext());
        }
        db.close();
        return flag;
    }

    /*Students_Table Functions*/
    public long addStudent(Students student) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.STUDENT_ID, student.getId());
        contentValues.put(myDbHelper.STUDENT_NAME, student.getStudentName());
        contentValues.put(myDbHelper.STUDENT_LAST_NAME, student.getStudentLastName());
        contentValues.put(myDbHelper.STUDENT_CLASS, student.getStudentClassId());
        contentValues.put(myDbHelper.STUDENT_PICTURE, student.getStudentHasPic());
        long newRowId = db.insert(myDbHelper.TABLE_STUDENTS, null, contentValues);
        db.close();
        return newRowId;
    }
    //Function to get all students that inside the database
    public Cursor getAllStudents(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor res = db.rawQuery("Select * From "+ myDbHelper.TABLE_STUDENTS,null);
        return res;
    }
    //Function to get student details by sending his id
    public Students getStudent(int student_id) {
        Students studentDetails = new Students();
        SQLiteDatabase db = helper.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {myDbHelper.STUDENT_ID, myDbHelper.STUDENT_NAME, myDbHelper.STUDENT_LAST_NAME, myDbHelper.STUDENT_CLASS, myDbHelper.STUDENT_PICTURE};
        String selection = myDbHelper.STUDENT_ID + " = ?";//Select condition
        String[] selectionArgs = {String.valueOf(student_id)};//Arguments for selection
        Cursor cursor = db.query(myDbHelper.TABLE_STUDENTS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {
            studentDetails.setId(cursor.getInt(0));
            studentDetails.setStudentName(cursor.getString(1));
            studentDetails.setStudentLastName(cursor.getString(2));
            studentDetails.setStudentClassId(cursor.getString(3));
            studentDetails.setStudentHasPic(cursor.getInt(4));
            db.close();
            return studentDetails;
        }
        db.close();
        return null;
    }
    //Function to get all students that inside some class
    public List getStudentByClass(int course_class_id) {
        List studentDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_STUDENTS + " Where " + myDbHelper.STUDENT_CLASS + " = " + course_class_id;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//if TABLE has rows
        {
            //Loop through the table rows
            do {
                Students studentDetails = new Students();
                studentDetails.setId(cursor.getInt(0));
                studentDetails.setStudentName(cursor.getString(1));
                studentDetails.setStudentLastName(cursor.getString(2));
                studentDetails.setStudentClassId(cursor.getString(3));
                studentDetails.setStudentHasPic(cursor.getInt(4));
                studentDetailsList.add(studentDetails);//Add course details to list
            } while (cursor.moveToNext());
        }
        db.close();
        return studentDetailsList;
    }
    public void updateStudent(Students student) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String studentsIds[] = {String.valueOf(student.getId())};
        ContentValues student_details = new ContentValues();
        student_details.put(myDbHelper.STUDENT_NAME, student.getStudentName());
        student_details.put(myDbHelper.STUDENT_LAST_NAME, student.getStudentLastName());
        student_details.put(myDbHelper.STUDENT_CLASS, student.getStudentClassId());
        student_details.put(myDbHelper.STUDENT_PICTURE, student.getStudentHasPic());
        db.update(myDbHelper.TABLE_STUDENTS, student_details, myDbHelper.STUDENT_ID + " = ?", studentsIds);
        db.close();
    }
    public Integer deleteStudent(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String studentsIds[] = {id};
        Integer res = db.delete(myDbHelper.TABLE_STUDENTS,myDbHelper.STUDENT_ID + " = ?",studentsIds);
        db.close();
        return res;
    }
    //Function to check if student have picture
    public Integer checkStudentPic(int studId) {
        Students studentDetails = new Students();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_STUDENTS + " Where " + myDbHelper.STUDENT_ID + " = " + studId;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//if TABLE has rows
        {
            //Loop through the table rows
            do {
                studentDetails.setId(cursor.getInt(0));
                studentDetails.setStudentName(cursor.getString(1));
                studentDetails.setStudentLastName(cursor.getString(2));
                studentDetails.setStudentClassId(cursor.getString(3));
                studentDetails.setStudentHasPic(cursor.getInt(4));
            } while (cursor.moveToNext());
        }
        db.close();
        return studentDetails.getStudentHasPic();
    }
    //Function to check for existing class id for new student
    public boolean checkStudClassId(String id) {
        boolean flag = false;
        int temp;
        List classesDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_CLASSES + " ORDER BY " + myDbHelper.CLASES_ID + " DESC";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())//if TABLE has rows
        {
            //Loop through the table rows
            do {
                Classes classesDetails = new Classes();
                classesDetails.setId(cursor.getInt(0));
                temp = cursor.getInt(0);
                if(temp == Integer.parseInt(id))
                    flag = true;
                classesDetails.setClassesName(cursor.getString(1));
                classesDetailsList.add(classesDetails);//Add classes details to list
            } while (cursor.moveToNext());
        }
        db.close();
        return flag;
    }
    //Function to get total students in some class
    public int getCntClassStudents(int class_id) {
        String countQuery = "SELECT  * FROM " + myDbHelper.TABLE_STUDENTS + " Where " + myDbHelper.STUDENT_CLASS + " = " + class_id;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    //Get total students in the database
    public int getStudentCount() {
        String countQuery = "SELECT  * FROM " + myDbHelper.TABLE_STUDENTS;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /*Attendance_Table Functions*/
    public long addAttendance(Attendance attendance) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_ID, attendance.getAttendanceStudentId());
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_NAME, attendance.getAttendanceStudentName());
        contentValues.put(myDbHelper.ATTENDANCE_CLASS_ID, attendance.getAttendanceClassId());
        contentValues.put(myDbHelper.ATTENDANCE_COURSE_ID, attendance.getAttendanceCourseId());
        contentValues.put(myDbHelper.ATTENDANCE_STATUS, attendance.getAttendanceStatus());
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_PIC_PATH, attendance.getAttendancePicPath());
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_PIC_NAME, attendance.getAttendancePicName());
        long newRowId = db.insert(myDbHelper.TABLE_ATTENDANCE, null, contentValues);
        db.close();
        return newRowId;
    }
    public Integer deleteAttendance(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String attendanceIds[] = {String.valueOf(id)};
        Integer res = db.delete(myDbHelper.TABLE_ATTENDANCE,myDbHelper.ATTENDANCE_ID + " = ?",attendanceIds);
        db.close();
        return res;
    }
    //Function to add updater attendance from excel file
    public long addAttendanceFromExcel(Attendance attendance) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.ATTENDANCE_ID, attendance.getId());
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_ID, attendance.getAttendanceStudentId());
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_NAME, attendance.getAttendanceStudentName());
        contentValues.put(myDbHelper.ATTENDANCE_CLASS_ID, attendance.getAttendanceClassId());
        contentValues.put(myDbHelper.ATTENDANCE_COURSE_ID, attendance.getAttendanceCourseId());
        contentValues.put(myDbHelper.ATTENDANCE_STATUS, attendance.getAttendanceStatus());
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_PIC_PATH, attendance.getAttendancePicPath());
        contentValues.put(myDbHelper.ATTENDANCE_STUDENT_PIC_NAME, attendance.getAttendancePicName());
        long newRowId = db.insert(myDbHelper.TABLE_ATTENDANCE, null, contentValues);
        db.close();
        return newRowId;
    }
    public int getAttendanceId(int stud_id, int course_id, int class_id) {
        Attendance attendanceDetails = new Attendance();
        SQLiteDatabase db = helper.getReadableDatabase();
        String Query = "Select * From " + myDbHelper.TABLE_ATTENDANCE + " Where " + myDbHelper.ATTENDANCE_STUDENT_ID + " = " + stud_id
                + " And " + myDbHelper.ATTENDANCE_CLASS_ID + " = " + class_id + " And "
                + myDbHelper.ATTENDANCE_COURSE_ID + " = " +  course_id;
        Cursor cursor = db.rawQuery(Query,null);
        if (cursor.moveToFirst())//If TABLE has rows
        {
            //Loop through the table rows
            do {
                attendanceDetails.setId(cursor.getInt(0));
                attendanceDetails.setAttendanceStudentId(cursor.getInt(1));
                attendanceDetails.setAttendanceStudentName(cursor.getString(2));
                attendanceDetails.setAttendanceCourseId(cursor.getInt(3));
                attendanceDetails.setAttendanceClassId(cursor.getInt(4));
                attendanceDetails.setAttendanceStatus(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        db.close();
        return attendanceDetails.getId();
    }
    //Function to get list of attendance of students inside class and course
    public List<Attendance> getAttendancedata(int class_id, int course_id){
        List<Attendance> data = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_ATTENDANCE + " Where " + myDbHelper.ATTENDANCE_CLASS_ID + " = " + class_id
                + " And " + myDbHelper.ATTENDANCE_COURSE_ID + " = " + course_id;
        Cursor cursor = db.rawQuery(selectQuery,null);
        StringBuffer stringBuffer = new StringBuffer();
        Attendance dataModel = null;
        while (cursor.moveToNext())
        {
            dataModel= new Attendance();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("Attendance_Id"));
            int StudentId = cursor.getInt(cursor.getColumnIndexOrThrow("Attendance_Student_Id"));
            Students s = getStudent(StudentId);
            String StudentName = cursor.getString(cursor.getColumnIndexOrThrow("Attendance_Student_Name"));
            int ClassId = cursor.getInt(cursor.getColumnIndexOrThrow("Attendance_Class_Id"));
            int CourseId = cursor.getInt(cursor.getColumnIndexOrThrow("Attendance_Course_Id"));
            int studentPic = checkStudentPic(StudentId);
            String AttendanceStatus = cursor.getString(cursor.getColumnIndexOrThrow("Attendance_Attendance_Status"));
            dataModel.setId(id);
            dataModel.setAttendanceStudentId(StudentId);
            dataModel.setAttendanceStudentName(StudentName);
            dataModel.setAttendanceClassId(ClassId);
            dataModel.setAttendanceCourseId(CourseId);
            dataModel.setAttendanceStatus(AttendanceStatus);
            dataModel.setAttendanceStudentPic(studentPic);
            if(studentPic == 1)
            {
                dataModel.setAttendancePicPath(Environment.getExternalStorageDirectory() + "/DCIM/TeachingManage");
                dataModel.setAttendancePicName(s.getStudentName() + " " + s.getStudentLastName() + s.getId() + ".jpg");
                dataModel.setPictures(new Pictures(dataModel.getAttendancePicPath(), dataModel.getAttendancePicName()));
            }
            else
                dataModel.setPictures(null);
            stringBuffer.append(dataModel);
            data.add(dataModel);
        }
        return data;
    }
    public Cursor getAttendancedataCursor(int class_id, int course_id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + myDbHelper.TABLE_ATTENDANCE + " Where " + myDbHelper.ATTENDANCE_CLASS_ID + " = " + class_id
                + " And " + myDbHelper.ATTENDANCE_COURSE_ID + " = " + course_id;
        Cursor res = db.rawQuery(selectQuery,null);
        return res;
    }
    //Function to get all attendance inside the database using Cursor
    public Cursor getAllAttendanceWithCursor(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor res = db.rawQuery("Select * From "+ myDbHelper.TABLE_ATTENDANCE,null);
        return res;
    }
    public void updateAttendance(Attendance attendance) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String attendanceIds[] = {String.valueOf(attendance.getId())};
        ContentValues attendance_details = new ContentValues();
        attendance_details.put(myDbHelper.ATTENDANCE_STUDENT_ID, attendance.getAttendanceStudentId());
        attendance_details.put(myDbHelper.ATTENDANCE_STUDENT_NAME, attendance.getAttendanceStudentName());
        attendance_details.put(myDbHelper.ATTENDANCE_CLASS_ID, attendance.getAttendanceClassId());
        attendance_details.put(myDbHelper.ATTENDANCE_COURSE_ID, attendance.getAttendanceCourseId());
        attendance_details.put(myDbHelper.ATTENDANCE_STATUS, attendance.getAttendanceStatus());
        attendance_details.put(myDbHelper.ATTENDANCE_STUDENT_PIC_PATH, attendance.getAttendancePicPath());
        attendance_details.put(myDbHelper.ATTENDANCE_STUDENT_PIC_NAME, attendance.getAttendancePicName());
        db.update(myDbHelper.TABLE_ATTENDANCE, attendance_details, myDbHelper.ATTENDANCE_ID + " = ?", attendanceIds);
        db.close();
    }
    //Get total attendance of class in the database
    public int getAttendanceClassCount(int attendanceClassId, String status) {
        String Present = status;
        String countQuery = "SELECT  * FROM " + myDbHelper.TABLE_ATTENDANCE + " Where " + myDbHelper.ATTENDANCE_CLASS_ID + " = "
                + attendanceClassId + " And " + myDbHelper.ATTENDANCE_STATUS + " = '" + Present + "'";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    //Function to get total inserted attendance in some course
    public int getAttendanceCourseCount(int attendanceCourseId, String status) {
        String Present = status;
        String countQuery = "SELECT  * FROM " + myDbHelper.TABLE_ATTENDANCE + " Where " + myDbHelper.ATTENDANCE_COURSE_ID + " = "
                + attendanceCourseId + " And " + myDbHelper.ATTENDANCE_STATUS + " = '" + Present + "'";;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    static class myDbHelper extends SQLiteOpenHelper {
        // Logcat tag
        private static final String LOG = "DatabaseHelper";

        // Database Version
        private static final int DATABASE_VERSION = 25;
        // Database Name
        private static final String DATABASE_NAME = "teachingmanageDB.db";

        // Table Names
        private static final String TABLE_CLASSES = "classes";
        private static final String TABLE_COURSES = "courses";
        private static final String TABLE_STUDENTS = "students";
        private static final String TABLE_ATTENDANCE = "attendance";
        private Context context;

        // Classes Table - column names
        private static final String CLASES_ID = "Class_Id";
        private static final String CLASES_NAME = "Class_Name";

        // COURSES Table - column names
        private static final String COURSE_ID = "Course_Id";
        private static final String COURSE_ROOM = "Course_Room";
        private static final String COURSE_NAME = "Course_Name";
        private static final String COURSE_DATE = "Course_Date";
        private static final String COURSE_HOURS = "Course_Hours";
        private static final String COURSE_CLASS = "Course_Class_Id";

        // STUDENTS Table - column names
        private static final String STUDENT_ID = "Student_Id";
        private static final String STUDENT_NAME = "Student_Name";
        private static final String STUDENT_LAST_NAME = "Student_Lastname";
        private static final String STUDENT_CLASS = "Student_class";
        private static final String STUDENT_PICTURE = "Student_picture";

        // ATTENDANCE Table - column names
        private static final String ATTENDANCE_ID = "Attendance_Id";
        private static final String ATTENDANCE_STUDENT_ID = "Attendance_Student_Id";
        private static final String ATTENDANCE_STUDENT_NAME = "Attendance_Student_Name";
        private static final String ATTENDANCE_CLASS_ID = "Attendance_Class_Id";
        private static final String ATTENDANCE_COURSE_ID = "Attendance_Course_Id";
        private static final String ATTENDANCE_STATUS = "Attendance_Attendance_Status";
        private static final String ATTENDANCE_STUDENT_PIC_PATH = "Attendance_Student_Pic_Path";
        private static final String ATTENDANCE_STUDENT_PIC_NAME = "Attendance_Student_Pic_Name";

        // Table Create Statements
        //Classes table create statement
        private static final String CREATE_TABLE_CLASSES= "CREATE TABLE " + TABLE_CLASSES
                + " (" + CLASES_ID + " INTEGER PRIMARY KEY, " + CLASES_NAME + " TEXT);";
        private static final String DROP_TABLE_CLASSES = "DROP TABLE IF EXISTS " + TABLE_CLASSES;

        //Courses table create statement
        private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES
                + " (" + COURSE_ID + " INTEGER PRIMARY KEY, " + COURSE_ROOM + " TEXT, " + COURSE_NAME + " TEXT, "
                + COURSE_DATE + " VARCHAR(30), " + COURSE_HOURS + " INTEGER, " + COURSE_CLASS + " TEXT);";
        private static final String DROP_TABLE_COURSES = "DROP TABLE IF EXISTS " + TABLE_COURSES;

        //Students table create statement
        private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS
                + " (" + STUDENT_ID + " INTEGER PRIMARY KEY, " + STUDENT_NAME + " TEXT, " + STUDENT_LAST_NAME + " TEXT, "
                + STUDENT_CLASS + " TEXT, " + STUDENT_PICTURE + " INTEGER);";
        private static final String DROP_TABLE_STUDENTS = "DROP TABLE IF EXISTS " + TABLE_STUDENTS;

        //Attendance table create statement
        private static final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + TABLE_ATTENDANCE
                + " (" + ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ATTENDANCE_STUDENT_ID + " INTEGER, "
                + ATTENDANCE_STUDENT_NAME + " TEXT, " + ATTENDANCE_CLASS_ID + " INTEGER, " + ATTENDANCE_COURSE_ID + " INTEGER, "
                + ATTENDANCE_STATUS + " TEXT, " + ATTENDANCE_STUDENT_PIC_PATH + " TEXT, " + ATTENDANCE_STUDENT_PIC_NAME + " TEXT);";
        private static final String DROP_TABLE_ATTENDANCE = "DROP TABLE IF EXISTS " + TABLE_ATTENDANCE;



        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //creating required tables
            try {
                db.execSQL(CREATE_TABLE_CLASSES);
                db.execSQL(CREATE_TABLE_COURSES);
                db.execSQL(CREATE_TABLE_STUDENTS);
                db.execSQL(CREATE_TABLE_ATTENDANCE);
            } catch (Exception e) {
                Message.message(context, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //on upgrade drop older tables
            try {
                Message.message(context, "OnUpgrade");
                db.execSQL(DROP_TABLE_CLASSES);
                db.execSQL(DROP_TABLE_COURSES);
                db.execSQL(DROP_TABLE_STUDENTS);
                db.execSQL(DROP_TABLE_ATTENDANCE);
            } catch (Exception e) {
                Message.message(context, "" + e);
            }
            //create new tables
            onCreate(db);
        }

        //Closing database
        public void closeDB()
        {
            SQLiteDatabase db = this.getReadableDatabase();
            if (db != null && db.isOpen())
                db.close();
        }
    }
}