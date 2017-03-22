package com.example.gadfly.projectgadfly;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private double LatAdd = 0;
    private double LngAdd = 0;

    private EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, homeFragment).commit();
/*
        edit = (EditText) findViewById(R.id.addressfield);

        edit.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    clickAction(v);
                    Log.e("TRUE", "Works?");
                    return true;
                }
                Log.d("FALSE", "FALSE");
                return false;
            }

        });
*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_about) {
            AboutFragment fragment = new AboutFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, fragment).commit();
        } else if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, homeFragment).commit();

        } else if (id == R.id.nav_team) {
            TeamFragment teamFragment = new TeamFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, teamFragment).commit();
        }

            // Highlight the selected item has been done by NavigationView
            item.setChecked(true);
            // Set action bar title
            setTitle(item.getTitle());
            // Close the navigation dra

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getLocationFromAddress(String strAddress){
        //Creates new geocoder object
        Geocoder coder = new Geocoder(this);

        List<Address> address = null;

        // Attempt to get Location from entered address
        try {
            address = coder.getFromLocationName(strAddress,5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address location = null;

        //Checks if address is valid and gets coordinates
        if (address.size() != 0) {
            location = address.get(0);
            LatAdd = location.getLatitude();
            LngAdd = location.getLongitude();
        } else {
            final Toast toast = Toast.makeText(getApplicationContext(), R.string.invalid_address, Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void clickAction(View v) {
        // Get entered address from text field
        String address = edit.getText().toString();
        //Store Application Context for easy referencing later
        Context context = getApplicationContext();

        //Check if the user has entered any text
        if (!address.isEmpty()) {
            Toast toast = Toast.makeText(context, address, Toast.LENGTH_LONG);

            // Checks to see whether we have an active internet connection
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            // Searches for entered location if there is an internet connection
            if (isConnected) {
                getLocationFromAddress(address);
                toast.show();
                //final Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                // Create a bundle containing the longitude and latitude to be passed to WebPage
                Bundle b = new Bundle();
                b.putDouble("lat", LatAdd);
                b.putDouble("lng", LngAdd);
               // intent.putExtras(b);
              //  startActivity(intent);
            } else {
                toast = Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            final Toast toast = Toast.makeText(getApplicationContext(), R.string.ask_for_address, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void clickWebsite(View v) {
        final Intent intent = new Intent(getApplicationContext(), WebPage.class);
        startActivity(intent);
    }

    public void clickLegislator(View v) {
        LegislatorDisplay fragment = new LegislatorDisplay();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, fragment).commit();
    }
}
