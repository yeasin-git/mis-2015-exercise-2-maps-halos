// Muhammad Yeasin --- 115315
// Moitree Chowdhury --- 115316

package com.example.mmbuw.hellomaps;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Debug;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.Set;

public class MapsActivity extends Activity implements GoogleMap.OnMapLongClickListener{

    TextView locationContent;
    String strLocationContent = "";
    String locationLatLon = "";

    double lat;
    double lon;
    String markerTitle = "";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    GPSManager gpsManager;

    Boolean gpsStatus;

    Set locationData=new HashSet();
    Set locationName=new HashSet();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationContent = (TextView) findViewById(R.id.etLocationContent);

        gpsStatus = false;
        lat = 0;
        lon = 0;





        gpsManager = new GPSManager(MapsActivity.this);

        if(gpsManager.canGetLocation){
            lat = gpsManager.getLatitude();
            lon = gpsManager.getLongitude();
            gpsStatus = true;
            markerTitle = "Your Current Location";
        }
        else{
            print("Please Enable GPS");
            gpsManager.showSettingsAlert();
        }

        setUpMapIfNeeded();

      //  mMap.moveCamera( CameraUpdateFactory.zoomTo(13.2f));
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                strLocationContent = locationContent.getText().toString();
                locationContent.setText("");
                mMap.addMarker(new MarkerOptions().position(latLng).title("" + strLocationContent));
                locationLatLon = "" + latLng.latitude + "," + latLng.longitude;
                print(locationLatLon);

                saveAtSharedPreference(locationLatLon,strLocationContent);

                CircleOptions circle=new CircleOptions();
                circle.center(latLng).fillColor(0x40ff0000).radius(10000).strokeColor(Color.BLUE).strokeWidth(5);;
                mMap.addCircle(circle);
            }
        });

    }

    private void saveAtSharedPreference(String locationLatLon,String message){

        locationData.add("" + locationLatLon);
        locationName.add("" + message);

        SharedPreferences prefs = getSharedPreferences("MapPrefs",
                Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putStringSet("locationData",locationData);
        editor.putStringSet("locationName",locationName);

        Boolean flag = editor.commit();

        if(flag==true){
            print("Preferenced Saved");
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng){

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
           // mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
             //       .getMap();

            mMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        if(gpsStatus) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("" + markerTitle));
        }
    }

    private void print(String message){

        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

 }
