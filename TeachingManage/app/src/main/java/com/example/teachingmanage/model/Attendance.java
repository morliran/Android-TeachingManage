package com.example.teachingmanage.model;

public class Attendance
{
    private String attendanceStatus, attendanceStudentName, attendancePicPath, attendancePicName;
    private int id;
    private int attendanceStudentId;
    private int attendanceClassId;
    private int attendanceCourseId;
    private Pictures pictures;

    public String getAttendancePicPath()
    {
        return attendancePicPath;
    }

    public void setAttendancePicPath(String attendancePicPath)
    {
        this.attendancePicPath = attendancePicPath;
    }

    public String getAttendancePicName()
    {
        return attendancePicName;
    }

    public void setAttendancePicName(String attendancePicName)
    {
        this.attendancePicName = attendancePicName;
    }

    public Pictures getPictures()
    {
        return pictures;
    }

    public void setPictures(Pictures pictures)
    {
        this.pictures = pictures;
    }

    public int getAttendanceStudentPic()
    {
        return attendanceStudentPic;
    }

    public void setAttendanceStudentPic(int attendanceStudentPic)
    {
        this.attendanceStudentPic = attendanceStudentPic;
    }

    private int attendanceStudentPic;

    public String getAttendanceStatus()
    {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus)
    {
        this.attendanceStatus = attendanceStatus;
    }

    public String getAttendanceStudentName()
    {
        return attendanceStudentName;
    }

    public void setAttendanceStudentName(String attendanceStudentName)
    {
        this.attendanceStudentName = attendanceStudentName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAttendanceStudentId()
    {
        return attendanceStudentId;
    }

    public void setAttendanceStudentId(int attendaceStudentId)
    {
        this.attendanceStudentId = attendaceStudentId;
    }

    public int getAttendanceClassId()
    {
        return attendanceClassId;
    }

    public void setAttendanceClassId(int attendanceClassId)
    {
        this.attendanceClassId = attendanceClassId;
    }

    public int getAttendanceCourseId()
    {
        return attendanceCourseId;
    }

    public void setAttendanceCourseId(int attendanceCourseId)
    {
        this.attendanceCourseId = attendanceCourseId;
    }
}
