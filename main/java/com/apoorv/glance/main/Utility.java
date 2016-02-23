package com.apoorv.glance.main;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by apoorv on 06/02/16.
 */
public class Utility
{

    public static String charSequenceToString(CharSequence charSequence)
    {
        try
        {
            StringBuilder sb = new StringBuilder(charSequence);
            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Error:Utility", e.toString());
            return "";
        }
    }

    public static byte[] bitmapToIntentValue(Bitmap bmp)
    {
        try
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Error:Utility", e.toString());
            return new byte[2];
        }
    }

    public static Bitmap intentValueToBitmap(byte[] byteArray)
    {
        try
        {
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            return bmp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Error:Utility", e.toString());
            return null;
        }
    }

    public static InputStream openPhoto(Context context, long contactId)
    {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null)
        {
            return null;
        }
        try
        {
            if (cursor.moveToFirst())
            {
                byte[] data = cursor.getBlob(0);
                if (data != null)
                {
                    return new ByteArrayInputStream(data);
                }
            }
        }
        finally
        {
            cursor.close();
        }
        return null;
    }
}
