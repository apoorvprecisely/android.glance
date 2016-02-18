package com.apoorv.glance.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

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
}
