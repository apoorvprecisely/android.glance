package com.apoorv.glance.main;

/**
 * Created by apoorv on 12/02/16.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CardViewRecyclerAdapter extends RecyclerView.Adapter<CardHolder>
{

    private LinkedHashMap<String, LinkedList<NotificationObject>> notifMap;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd MMM yyyy ");
    private final Context context;
    private static int tabPosition = -1;

    CardViewRecyclerAdapter(int tabPosition, Context context, LinkedHashMap<String, LinkedList<NotificationObject>> notificationObjects)
    {
        notifMap = notificationObjects;
        this.context = context;
        this.tabPosition = tabPosition;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);

        return new CardHolder(v);
    }

    @Override
    public void onBindViewHolder(CardHolder viewHolder, int i)
    {
        NotificationObject item = notifMap.get(new ArrayList<String>(notifMap.keySet()).get(i)).get(0);
        Date resultDate = new Date(item.getTime());

        //String content = item.getTitle() + "\n" + item.getContent() + "\n" + item.getContact() + "\n" + simpleDateFormat.format(resultDate);
        //TODO:add contact
        if (tabPosition == 1)
        {
            PackageManager packageManager = context.getPackageManager();
            String content = "";
            try
            {
                String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(item.getPackageName(), PackageManager.GET_META_DATA));
                content = "<b>" + appName + "</b><br/>" + item.getContent() + "<br/>" + simpleDateFormat.format(resultDate);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                content = "<b>" + item.getPackageName() + "</b><br/>" + item.getContent() + "<br/>" + simpleDateFormat.format(resultDate);
            }
            viewHolder.cardText.setText(Html.fromHtml(content));
            Drawable icon = null;
            try
            {
                icon = context.getPackageManager().getApplicationIcon(item.getPackageName());
            }
            catch (PackageManager.NameNotFoundException e)
            {
                try
                {
                    icon = context.getPackageManager().getApplicationIcon("com.android.systemui");
                }
                catch (PackageManager.NameNotFoundException e1)
                {
                    e1.printStackTrace();
                }
            }
            viewHolder.cardIconImage.setImageDrawable(icon);
        }
        else if (tabPosition == 2)
        {
            String content = "<b>" + NotificationHandler.keyVsContact.get(item.getContact()) + "</b><br/>" + item.getContent() + "<br/>" + simpleDateFormat.format(resultDate);
            viewHolder.cardText.setText(Html.fromHtml(content));
            if (!"-1".equals(item.getContact()) && item.getContact() != null)
            {
                viewHolder.cardIconImage.setImageBitmap(BitmapFactory.decodeStream(Utility.openPhoto(context, Long.parseLong(item.getContact()))));
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return notifMap.size();
    }

    public void addNotificationObject(NotificationObject temp, int position)
    {
        String key;
        if (position == 1)
        {
            key = temp.getPackageName();
        }
        else
        {
            key = temp.getContact();
        }
        if (notifMap.containsKey(key))
        {
            notifMap.get(key).addFirst(temp);
        }
        else
        {
            LinkedList<NotificationObject> list = new LinkedList();
            list.add(temp);
            notifMap.put(key, list);
            //TODO: add in the beginning
        }
        this.notifyDataSetChanged();
    }
}