package com.apoorv.glance.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by apoorv on 17/02/16.
 */
public class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    protected final TextView cardText;
    protected final ImageView cardIconImage;
    private final Context context;

    CardHolder(View v)
    {
        super(v);
        cardText = (TextView) v.findViewById(R.id.cardtitle);
        cardIconImage = (ImageView) v.findViewById(R.id.cardimage);
        context = v.getContext();
    }

    @Override
    public void onClick(View v)
    {
        Toast.makeText(context, "The Item Clicked is: " + getPosition(), Toast.LENGTH_SHORT).show();

    }
}