package com.apoorv.glance.main;

/**
 * Created by apoorv on 12/02/16.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class CardViewRecyclerAdapter extends RecyclerView.Adapter<CardHolder>
{

    public static LinkedList<NotificationObject> currentNotificationObjects;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd MMM yyyy ");
    private final Context context;

    CardViewRecyclerAdapter(Context context, LinkedList<NotificationObject> notificationObjects)
    {
        currentNotificationObjects = notificationObjects;
        this.context = context;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);

        return new CardHolder(v);
    }

    public void updateReceiptsList()
    {
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CardHolder viewHolder, int i)
    {
        NotificationObject item = currentNotificationObjects.get(i);
        Date resultDate = new Date(item.getTime());

        //String content = item.getTitle() + "\n" + item.getContent() + "\n" + item.getContact() + "\n" + simpleDateFormat.format(resultDate);
        //TODO:add contact
        String content = "<b>" + item.getTitle() + "</b><br/>" + item.getContent() + "<br/>" + simpleDateFormat.format(resultDate);
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

    @Override
    public int getItemCount()
    {
        return currentNotificationObjects.size();
    }
}