package com.apoorv.glance.main;

import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by apoorv on 06/02/16.
 */
public class NotificationHandler extends NotificationListenerService
{
    private String TAG = this.getClass().getSimpleName();
    DatabaseHandler databaseHandler = new DatabaseHandler(this);
    private static final LinkedList<String> BLACKLISTED_PACKAGES = new LinkedList<>();
    public static HashMap<String, String> contactVsKey = null;
    public static HashMap<String, String> keyVsContact = null;

    static
    {
        BLACKLISTED_PACKAGES.add("com.android.systemui");
        BLACKLISTED_PACKAGES.add("com.google.android.googlequicksearchbox");
        BLACKLISTED_PACKAGES.add("com.android.providers.downloads");
        BLACKLISTED_PACKAGES.add("com.android.vending");
    }

    private void initContacts()
    {
        if (contactVsKey == null)
        {
            contactVsKey = new HashMap<>();
            keyVsContact = new HashMap<>();
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (phones.moveToNext())
            {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String key = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                contactVsKey.put(name, key);
                keyVsContact.put(key, name);
                // Log.v(TAG, name + " " + key);
            }
            phones.close();
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(), 1, 2);
        initContacts();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn)
    {
        //TODO: handleNotificationFiltering
        NotificationObject notificationObject = generateNotificationObject(sbn);
        if (notificationObject != null)
        {
            databaseHandler.addNotificationInDatabase(DatabaseHandler.MAIN_TABLE, notificationObject);
            Intent intent = generateIntentFromNotificationObject(notificationObject);
            sendBroadcast(intent);
        }
        Log.v(TAG, "NotificationPosted called");
    }

    private Intent generateIntentFromNotificationObject(NotificationObject notificationObject)
    {
        Intent outIntent = new Intent(getString(R.string.base_intent));
        outIntent.putExtra(getString(R.string.notification_title), notificationObject.getTitle());
        outIntent.putExtra(getString(R.string.notification_content), notificationObject.getContent());
        outIntent.putExtra(getString(R.string.notification_contact), notificationObject.getContact());
        outIntent.putExtra(getString(R.string.notification_time), notificationObject.getTime());
        outIntent.putExtra(getString(R.string.notification_package), notificationObject.getPackageName());
        return outIntent;
    }

    private NotificationObject generateNotificationObject(StatusBarNotification sbn)
    {
        Notification notification = sbn.getNotification();
        if (notification == null)
        {
            return null;
        }
        else
        {
            if (BLACKLISTED_PACKAGES.contains(sbn.getPackageName()))
            {
                return null;
            }

            Bundle bundledData = notification.extras;
            String packageName = sbn.getPackageName();
            String notificationTitle = bundledData.getString(Notification.EXTRA_TITLE);
//TODO: handle spannable string youtube
            if (notificationTitle == null)
            {
                return null;
            }
            CharSequence notificationContent = bundledData.getCharSequence(Notification.EXTRA_TEXT);
            String notificationContentString = "";
            if (notificationContent != null)
            {
                notificationContentString = Utility.charSequenceToString(notificationContent);
            }
            //TODO:check why youtube is not handled
            notificationContentString = filterBlackListedContents(notificationContentString);
            if (notificationContentString == null)
            {
                return null;
            }
            String contactId = processDataForContact(notificationTitle, notificationContentString);
            Log.v(TAG, "matched" + contactId);
            return new NotificationObject(packageName, notificationTitle, notificationContentString, contactId, System.currentTimeMillis());
        }

    }

    private String processDataForContact(String notificationTitle, String notificationContentString)
    {
        Iterator iter = contactVsKey.keySet().iterator();
        while (iter.hasNext())
        {
            String name = iter.next().toString();
            if (notificationTitle.contains(name) || notificationContentString.contains(name))
            {
                return contactVsKey.get(name);
            }
        }
        return "-1";
    }

    private String filterBlackListedContents(String notificationContentString)
    {
        if (notificationContentString.contains("Downloading") && notificationContentString.contains("%"))
        {
            return null;
        }
        else
        {
            return notificationContentString;
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn)
    {

    }
}