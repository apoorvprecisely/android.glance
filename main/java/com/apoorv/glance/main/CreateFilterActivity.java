package com.apoorv.glance.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class CreateFilterActivity extends AppCompatActivity
{
    private String TAG = this.getClass().getSimpleName();
    private EditText filterName;
    private EditText packageName;
    private EditText matchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        filterName = (EditText) findViewById(R.id.input_filter_name);
        packageName = (EditText) findViewById(R.id.input_app_package);
        matchContent = (EditText) findViewById(R.id.input_content);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onFormSubmit();
            }
        });

    }

    private void onFormSubmit()
    {
        String filterString = filterName.getText().toString();
        String packageString = packageName.getText().toString();
        String matchString = matchContent.getText().toString();
        Log.v(TAG, filterString + " " + packageString + " " + matchString);
        MainActivity.databaseHandler.addFilterInDatabase(filterString, packageString, matchString);
        Intent i = new Intent(CreateFilterActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_filter, menu);
        return true;
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
}
