package com.example.teachingmanage.mySpinners;

public class ClassesSpinnerVO
{
    int classId = 0;
    String className = "";

    public ClassesSpinnerVO(int classId, String className)
    {
        this.classId = classId;
        this.className = className;
    }
    public String toString()
    {
        return className;
    }

    public int getClassId()
    {
        return classId;
    }

    public String getClassName()
    {
        return className;
    }
}
