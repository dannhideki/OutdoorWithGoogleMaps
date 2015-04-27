package com.example.hideki.outdoorwithgooglemaps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hideki on 4/26/15.
 */
public class Gps implements LocationListener{

    private Location location;
    private LocationManager locationManager;
    private Context context;
    private GoogleMap googleMap;

    public Gps(Context c, GoogleMap mMap){
        location = null;
        context = c;
        googleMap = mMap;
    }

    public void setUpMap() {
        // Enable MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        location = locationManager.getLastKnownLocation(provider);

        // request that the provider send this activity GPS updates every 0 seconds
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get current position to get camera position
        getCameraPosition(getCurrentPosition());
    }

    @Override
    public void onLocationChanged(Location location) {
        //update location
        this.location = location;

        //verify if googleMap is not null to draw marker in map
        if(googleMap != null) {
            drawMarker(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void drawMarker(Location location){
        // clear googleMap
        googleMap.clear();

        // add a marker to the map indicating our current position
        googleMap.addMarker(new MarkerOptions()
                .position(getCurrentPosition())
                .title("Sua localizacao!")
                .snippet("Latitude :" + location.getLatitude() + " | Longitude:" + location.getLongitude()));
    }

    private LatLng getCurrentPosition(){
        //create a currentPosition with LatLng
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        return currentPosition;
    }

    private void getCameraPosition(LatLng currentPosition){
        // Zoom in the Google Map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentPosition)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(18)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
