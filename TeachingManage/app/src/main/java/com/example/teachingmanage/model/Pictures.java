package com.example.teachingmanage.model;

public class Pictures
{
    private String Path, Name;

    public Pictures(String path, String name)
    {
        this.Path = path;
        this.Name = name;
    }

    public String getPath()
    {
        return Path;
    }

    public void setPath(String path)
    {
        this.Path = path;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        this.Name = name;
    }
}
