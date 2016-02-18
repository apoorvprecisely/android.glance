package com.apoorv.glance.main;

/**
 * Created by apoorv on 07/02/16.
 */
public class NotificationObject
{
    private String packageName;
    private String title;
    private String contact;
    private String content;
    private long time;
    private int id;

    public NotificationObject(String packageName, String title, String content, String contact, long time)
    {
        this.packageName = packageName;
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.time = time;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }
}
