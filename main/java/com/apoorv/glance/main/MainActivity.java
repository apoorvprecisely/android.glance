package com.apoorv.glance.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
{
    public static DatabaseHandler databaseHandler = null;
    public static CardViewRecyclerAdapter recentAdapter;
    public static CardViewRecyclerAdapter contactsAdapter;
    public static CardViewRecyclerAdapter filterAdapter;

    private NotificationReceiver notificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        if (databaseHandler == null)
        {
            databaseHandler = new DatabaseHandler(this);
        }
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.base_intent));
        registerReceiver(notificationReceiver, filter);
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, CreateFilterActivity.class);
                startActivity(i);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {

                switch (position)
                {
                    case 0:
                        fab.show();
                        break;

                    default:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ResultFragment extends Fragment
    {
        private static final String TAB_POSITION = "tab_position";

        public ResultFragment()
        {

        }

        public static ResultFragment newInstance(int tabPosition)
        {
            ResultFragment fragment = new ResultFragment();
            Bundle args = new Bundle();
            args.putInt(TAB_POSITION, tabPosition);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            Bundle args = getArguments();
            int tabPosition = args.getInt(TAB_POSITION);

            if (tabPosition == 1)
            {
                LinkedHashMap<String, LinkedList<NotificationObject>> map = new LinkedHashMap<>();

                View v = inflater.inflate(R.layout.fragment_list_view, container, false);
                map = databaseHandler.getAllNotificationFromTable(DatabaseHandler.MAIN_TABLE);
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recentAdapter = new CardViewRecyclerAdapter(tabPosition, getContext(), map);
                Log.v("MainActivity",map.size()+" size");
                recyclerView.setAdapter(recentAdapter);
                return v;
            }
            else if (tabPosition == 2)
            {
                LinkedHashMap<String, LinkedList<NotificationObject>> map = new LinkedHashMap<>();

                View v = inflater.inflate(R.layout.contacts_list_view, container, false);
                map = databaseHandler.getAllContactNotificationsFromTable(DatabaseHandler.MAIN_TABLE);
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.contactsview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                contactsAdapter = new CardViewRecyclerAdapter(tabPosition, getContext(), map);
                recyclerView.setAdapter(contactsAdapter);
                return v;
            }
            else if (tabPosition == 0)
            {
                LinkedHashMap<String, LinkedList<NotificationObject>> map = new LinkedHashMap<>();

                //TODO: have a different then map and different recycler adapter
                View v = inflater.inflate(R.layout.filter_list_view, container, false);
                map = databaseHandler.getAllFiltersFromTable();
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.filterview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                filterAdapter = new CardViewRecyclerAdapter(tabPosition, getContext(), map);
                recyclerView.setAdapter(filterAdapter);
                return v;
            }
            else
            {
                return null;
            }
        }
    }

    class NotificationReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            NotificationObject temp = new NotificationObject(intent.getStringExtra(getString(R.string.notification_package)), intent.getStringExtra(getString(R.string.notification_title)),
                    intent.getStringExtra(getString(R.string.notification_content)), intent.getStringExtra(getString(R.string.notification_contact)),
                    intent.getLongExtra(getString(R.string.notification_time), 0));
            if (recentAdapter != null)
            {
                recentAdapter.addNotificationObject(temp, 1);
            }
            if (contactsAdapter != null)
            {
                contactsAdapter.addNotificationObject(temp, 2);
            }
        }
    }

    static class TabViewPagerAdapter extends FragmentStatePagerAdapter
    {

        public TabViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return ResultFragment.newInstance(position);
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "Filters";
                case 1:
                    return "Recents";
                case 2:
                    return "Contacts";
                default:
                    return "Tab";
            }
        }
    }
}

