package com.example.teachingmanage.model;

public class Courses
{
    private String courseName, courseDate, courseRoom,  courseClassId;
    private int id, totalHours;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String  courseName)
    {
        this.courseName = courseName;
    }

    public int getTotalHours()
    {
        return totalHours;
    }

    public void setTotalHours(int totalHours)
    {
        this.totalHours = totalHours;
    }

    public String getCourseDate()
    {
        return courseDate;
    }

    public void setCourseDate(String courseDate)
    {
        this.courseDate = courseDate;
    }

    public String getCourseRoom()
    {
        return courseRoom;
    }

    public void setCourseRoom(String courseRoom)
    {
        this.courseRoom = courseRoom;
    }

    public String getCourseClassId()
    {
        return courseClassId;
    }

    public void setCourseClassId(String courseClassId)
    {
        this.courseClassId = courseClassId;
    }
}
