package com.apoorv.glance.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by apoorv on 17/02/16.
 */
public class CardHolder extends RecyclerView.ViewHolder
{

    protected final TextView cardText;
    protected final ImageView cardIconImage;

    CardHolder(View v)
    {
        super(v);
        cardText = (TextView) v.findViewById(R.id.cardtitle);
        cardIconImage = (ImageView) v.findViewById(R.id.cardimage);
    }
}