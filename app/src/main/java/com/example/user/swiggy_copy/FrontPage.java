package com.example.user.swiggy_copy;








import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.location.LocationListener;


import android.support.v7.app.ActionBarDrawerToggle;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.swiggy_copy.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;


public class FrontPage extends FragmentActivity implements android.content.DialogInterface.OnClickListener{
    DrawerLayout dl;
    ActionBarDrawerToggle ac;
    String ProfileName;
    private LocationListener locListener = new MyLocationListener();
    ListView lv;
    String title="";
    String items[]=new String[2];
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;


    LocationManager locManager;
    Boolean getLoc=false;
    int doubleBackToExitPressedOnce =0;
    private void showLocation() {

        try {
            getLoc = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            throw e;
        }
      /*  try {
            getNet = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception ex) {
            throw ex;
        }*/
        if (!getLoc /*&& !getNet*/) {
            AlertDialog.Builder builder = new Builder(this);
            builder.setTitle("Attention!");
            builder.setMessage("Sorry, location is not determined. Please enable location providers");
            builder.setPositiveButton("OK", this);
            builder.setNeutralButton("Cancel", this);
            builder.create().show();


        }
        if (getLoc) {
            if (Build.VERSION.SDK_INT >= 23) {
                if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                }
                else  if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED)
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);

            }
            else
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);






        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce==2) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            return;
        }
        doubleBackToExitPressedOnce ++;


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = 0;
                myOwnBackPress();
            }
        }, 2000);
    }

    private void myOwnBackPress() {
        if(!isFinishing()) {
            super.onBackPressed();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED)
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // This needs to stop getting the location data and save the battery power.
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                    }
                }
                locManager.removeUpdates(locListener);

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                LocationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());

            }
        }
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_NEUTRAL){
            getActionBar().setTitle("setting location");
        }else if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = "Unable To Fetch Location";
            }
            getActionBar().setTitle(locationAddress);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        items[1]="Item2";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ProfileName = extras.getString("fbLogin");

        }


        if(ProfileName!=null)
            items[0]="LogOut";
        else
            items[0]="LogIn";



        locManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        showLocation();
        title=(String)getTitle();
        dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        lv=(ListView)findViewById(R.id.drawer_list);
        FragmentManager fm = getSupportFragmentManager();
        Fragment_Activity fr= new Fragment_Activity();
        fm.beginTransaction().add(R.id.drawer_layout,fr,"frag1").commit();

        // Getting reference to the ActionBarDrawerToggle
        ac = new ActionBarDrawerToggle(this,dl,R.string.drawer_open,R.string.drawer_closed){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                lv.bringToFront();
               dl.requestLayout();
                invalidateOptionsMenu();

            }
        };
        // Setting DrawerToggle on DrawerLayout
        dl.setDrawerListener(ac);

        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                R.layout.drawer_list_items ,
                items
        );

        // Setting the adapter on mDrawerList
        lv.setAdapter(adapter);

        // Enabling Home button
        getActionBar().setHomeButtonEnabled(true);

        // Enabling Up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);


        // Setting item click listener for the listview mDrawerList
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {


              //  String[] items = getResources().getStringArray(R.array.select);

                title = items[position];

                if(title.equals("LogIn")){
                    Intent i= new Intent(getApplicationContext(),MainActivity.class);
                    i.putExtra("fbLogin",ProfileName);
                    startActivity(i);
            }

                if(title.equals("LogOut")){
                   ProfileName=null;
                    FacebookSdk.sdkInitialize(getApplicationContext());
                    LoginManager.getInstance().logOut();

                    Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                    items[position]="LogIn";

                }



                // Creating an ArrayAdapter to add items to the listview mDrawerList
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getBaseContext(),
                        R.layout.drawer_list_items ,
                        items
                );

                // Setting the adapter on mDrawerList
                lv.setAdapter(adapter);

                dl.closeDrawer(lv);
            }
        });

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ac.syncState();
    }

    /** Handling the touch event of app icon */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ac.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = dl.isDrawerOpen(lv);

        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
