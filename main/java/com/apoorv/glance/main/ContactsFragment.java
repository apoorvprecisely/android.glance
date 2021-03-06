package com.apoorv.glance.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by apoorv on 27/02/16.
 */
public class ContactsFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinkedHashMap<String, LinkedList<NotificationObject>> map = MainActivity.getDatabaseHandler().getAllContactNotificationsFromTable(DatabaseHandler.MAIN_TABLE);
        //TODO: have a different then map and different recycler adapter
        View v = inflater.inflate(R.layout.contacts_list_view, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.contactsview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new CardViewRecyclerAdapter(2, getContext(), map));
        return v;
    }

}
