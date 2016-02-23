package com.apoorv.glance.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by apoorv on 10/02/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db101db";
    public static final String MAIN_TABLE = "NotificationList";
    private static final String MAIN_ARCHIVE_TABLE = "NotificationList_Archive";
    private static final String SEARCH_TABLE = "NotificationList_Fts";
    private static final String SEARCH_ARCHIVE_TABLE = "NotificationList_Fts_Archive";

    private static final String NOTIFICATION_ID = "id";
    private static final String NOTIFICATION_TITLE = "title";
    private static final String NOTIFICATION_PACKAGE = "package";
    private static final String NOTIFICATION_CONTENT = "content";
    private static final String NOTIFICATION_TIME = "time";
    private static final String NOTIFICATION_CONTACT = "contact";
    private static final String NOTIFICATION_ALL = "all_content";

    private static final String FILTER_TABLE = "FilterTable";

    public DatabaseHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createNotificationTable(db, MAIN_TABLE);
        createFilterTable(db, FILTER_TABLE);
        // createNotificationTable(db, MAIN_ARCHIVE_TABLE);
        //FTS TABLE
        //createSearchTable(db, SEARCH_TABLE);
        //createSearchTable(db, SEARCH_ARCHIVE_TABLE);
    }

    private void createFilterTable(SQLiteDatabase db, String filterTable)
    {
        String createQuery = "CREATE TABLE " + filterTable + " ( " + NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOTIFICATION_PACKAGE + " TEXT, " + NOTIFICATION_TITLE + " TEXT, " + NOTIFICATION_CONTENT + " TEXT )";
        db.execSQL(createQuery);
    }

    private void createNotificationTable(SQLiteDatabase db, String tableName)
    {
        String createQuery = "CREATE TABLE " + tableName + " ( " + NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOTIFICATION_PACKAGE + " TEXT, " + NOTIFICATION_TITLE + " TEXT, " + NOTIFICATION_CONTENT + " TEXT, "
                + NOTIFICATION_CONTACT + " TEXT, " + NOTIFICATION_TIME + " INTEGER )";
        db.execSQL(createQuery);
    }

    private void createSearchTable(SQLiteDatabase db, String tableName)
    {
        String createQuery = "CREATE VIRTUAL TABLE " + tableName + " USING fts3 ( " + NOTIFICATION_ID + " INTEGER PRIMARY KEY, "
                + NOTIFICATION_ALL + " TEXT )";
        db.execSQL(createQuery);
    }

    public LinkedHashMap<String, LinkedList<NotificationObject>> getAllNotificationFromTable(String tableName)
    {
        LinkedHashMap<String, LinkedList<NotificationObject>> map = new LinkedHashMap<>();
        String query = "SELECT  * FROM " + tableName + " ORDER BY " + NOTIFICATION_TIME + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        NotificationObject notificationObject = null;
        if (cursor.moveToFirst())
        {
            do
            {
                notificationObject = new NotificationObject(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getLong(5));
                if (!map.containsKey(notificationObject.getPackageName()))
                {
                    map.put(notificationObject.getPackageName(), new LinkedList<NotificationObject>());
                }
                map.get(notificationObject.getPackageName()).add(notificationObject);
            }
            while (cursor.moveToNext());
        }
        return map;
    }

    private ContentValues createContentValueFromObject(NotificationObject NotificationObject)
    {
        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_TITLE, NotificationObject.getTitle());
        values.put(NOTIFICATION_PACKAGE, NotificationObject.getPackageName());
        values.put(NOTIFICATION_CONTENT, NotificationObject.getContent());
        values.put(NOTIFICATION_CONTACT, NotificationObject.getContact());
        values.put(NOTIFICATION_TIME, NotificationObject.getTime());
        return values;
    }

    private ContentValues createFilterValueFromForm(String title, String packageName, String content)
    {
        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_TITLE, title);
        values.put(NOTIFICATION_PACKAGE, packageName);
        values.put(NOTIFICATION_CONTENT, content);
        return values;
    }

    public void addNotificationInDatabase(String tableName, NotificationObject notificationObject)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = createContentValueFromObject(notificationObject);
        db.insert(tableName, null, values);
        db.close();
    }

    public void addFilterInDatabase(String title, String packageName, String content)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = createFilterValueFromForm(title, packageName, content);
        db.insert(FILTER_TABLE, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
//TODO: implement
    }

    public LinkedHashMap<String, LinkedList<NotificationObject>> getAllContactNotificationsFromTable(String mainTable)
    {
        LinkedHashMap<String, LinkedList<NotificationObject>> map = new LinkedHashMap<>();
        String query = "SELECT  * FROM " + mainTable + " WHERE " + NOTIFICATION_CONTACT + " !=-1 ORDER BY " + NOTIFICATION_TIME + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        NotificationObject notificationObject = null;
        if (cursor.moveToFirst())
        {
            do
            {
                notificationObject = new NotificationObject(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getLong(5));
                if (notificationObject.getContact() == null)
                {
                    notificationObject.setContact("null");
                }
                if (!map.containsKey(notificationObject.getContact()))
                {
                    map.put(notificationObject.getContact(), new LinkedList<NotificationObject>());
                }
                map.get(notificationObject.getContact()).add(notificationObject);
            }
            while (cursor.moveToNext());
        }
        return map;
    }

    public LinkedHashMap<String, LinkedList<NotificationObject>> getAllFiltersFromTable()
    {
        LinkedHashMap<String, LinkedList<NotificationObject>> map = new LinkedHashMap<>();
        String query = "SELECT  * FROM " + FILTER_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        NotificationObject notificationObject = null;
        if (cursor.moveToFirst())
        {
            do
            {
                notificationObject = new NotificationObject(cursor.getString(1), cursor.getString(2), cursor.getString(3), "null", -1);
                //TODO: change this please
                if (notificationObject.getContact() == null)
                {
                    notificationObject.setContact("null");
                }
                if (!map.containsKey(notificationObject.getPackageName()))
                {
                    map.put(notificationObject.getPackageName(), new LinkedList<NotificationObject>());
                }
                map.get(notificationObject.getPackageName()).add(notificationObject);
            }
            while (cursor.moveToNext());
        }
        return map;
    }
}
