package com.example.teachingmanage.mySpinners;

public class CourseSpinnerVO
{
    int courseId = 0;
    String courseName = "";

    public CourseSpinnerVO(int courseId, String courseName)
    {
        this.courseId = courseId;
        this.courseName = courseName;
    }
    public String toString()
    {
        return courseName;
    }

    public int getCourseId()
    {
        return courseId;
    }

    public String getCourseName()
    {
        return courseName;
    }
}
