package com.example.teachingmanage.model;

public class Students
{
    private String studentName, studentLastName, studentClassId;
    private int id, studenthasPic;
    private Pictures pic;

    public int getStudentHasPic()
    {
        return studenthasPic;
    }

    public void setStudentHasPic(int hasPic)
    {
        this.studenthasPic = hasPic;
    }

    public Pictures getPic()
    {
        return pic;
    }

    public void setPic(Pictures pic)
    {
        this.pic = pic;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getStudentLastName()
    {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName)
    {
        this.studentLastName = studentLastName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getStudentClassId()
    {
        return studentClassId;
    }

    public void setStudentClassId(String studentClassId)
    {
        this.studentClassId = studentClassId;
    }
}
