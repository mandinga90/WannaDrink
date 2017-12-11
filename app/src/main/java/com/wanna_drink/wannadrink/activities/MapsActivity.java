package com.wanna_drink.wannadrink.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.entities.Drink;
import com.wanna_drink.wannadrink.entities.User;
import com.wanna_drink.wannadrink.entities.UserBuilder;
import com.wanna_drink.wannadrink.functional.App;
import com.wanna_drink.wannadrink.functional.Consumer;
import com.wanna_drink.wannadrink.functional.RetainFragment;
import com.wanna_drink.wannadrink.functional.SafeConversions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.functional.App.mUser;
import static com.wanna_drink.wannadrink.functional.App.uId;
import static com.wanna_drink.wannadrink.functional.App.userList;
import static com.wanna_drink.wannadrink.functional.SafeConversions.toDouble;
import static com.wanna_drink.wannadrink.functional.SafeConversions.toInt;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener {
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    static private GoogleMap mMap;
    static Location currentLocation = new Location("Current");
    static LatLng newLatLng = new LatLng(51.52,-0.07);
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

            }
            else{
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }

        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        MapsInitializer.initialize(this);

        if(map.isMyLocationEnabled()){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                sendDataToApi(location.getLatitude(), location.getLongitude());
                                getUsers(mUser);
                                currentLocation = location;
                                newLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(newLatLng)      // Sets the center of the map to Mountain View
                                        .zoom(18)                   // Sets the zoom
                                        .bearing(0)
                                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        }
                    });
        }
    }



    private void sendDataToApi(double lat, double lng) {

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        String name = sharedPref.getString(getString(R.string.key_name), "");
        String email = sharedPref.getString(getString(R.string.key_email), "");
        String uId = sharedPref.getString("uId", App.uId);
        String drinkCode = String.valueOf(sharedPref.getInt(getString(R.string.key_drink), 0));
        String hours = String.valueOf(sharedPref.getInt(getString(R.string.key_hours), 0));
        String availableFrom = sharedPref.getString("availableFrom", "");
        String availableTill = sharedPref.getString("availableTill", "");

        User user = new UserBuilder()
                .addName(name)
                .addEmail(email)
                .addUId(uId)
                .addDrink(Drink.getDrink(drinkCode))
                .addHours(hours)
                .addLat(String.valueOf(lat))
                .addLng(String.valueOf(lng))
                .addAvailableFrom(availableFrom)
                .addAvailableTill(availableTill)
                .build();

        mUser = user;

        addUser(user);
    }

    private void addUser(final User user) {
        RetainFragment retainFragment = (RetainFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.NETWORK_FRAGMENT_TAG));
        if (retainFragment == null) {
            retainFragment = new RetainFragment();
            getSupportFragmentManager().beginTransaction().add(retainFragment, getString(R.string.NETWORK_FRAGMENT_TAG)).commit();
        }

        if(user != null) {

            retainFragment.registerUser(new Consumer<Void>() {

                @Override
                public void apply(Void v) {
                    startActivity(new Intent(MapsActivity.this, MapsActivity.class));
                }

                @Override
                public Object get() {
                    return user;
                }
            });
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public void onMapClick(LatLng point) {
//        mMap.addMarker(new MarkerOptions()
//                .position(point)
//                .snippet("Hey, let's drink!")
//                .title("sdfadsf"));
    }

    private void setMarkers() {

        RetainFragment retainFragment = (RetainFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.NETWORK_FRAGMENT_TAG));
        if (retainFragment == null) {
            retainFragment = new RetainFragment();
            getSupportFragmentManager().beginTransaction().add(retainFragment, getString(R.string.NETWORK_FRAGMENT_TAG)).commit();
        }

        Bitmap b, bhalfsize;
        Map userData;
        LatLng point;
        for (int i = 0; i < userList.size(); i++) {
            userData = userList.get(i);
            point = new LatLng(
                    toDouble(userData.get("lat")),
                    toDouble(userData.get("lon")));
            Double drinkId = toDouble(userData.get("drinkId"));
            b =((BitmapDrawable) getResources().getDrawable(Drink.getImage(drinkId.intValue()))).getBitmap();
            bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/2,b.getHeight()/2, false);
            int distanceKM = (int) toDouble(userData.get("distance"));
            int distanceM = 10 * (int) ((toDouble(userData.get("distance")) - distanceKM) * 100);
            mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .snippet( distanceKM + "km " + distanceM + "m " )
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(bhalfsize))
                    .title(userData.get("name").toString()));
        }
    }

    private void getUsers(User user) {
        userList.clear();

        RetainFragment retainFragment = (RetainFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.NETWORK_FRAGMENT_TAG));
        if (retainFragment == null) {
            retainFragment = new RetainFragment();
            getSupportFragmentManager().beginTransaction().add(retainFragment, getString(R.string.NETWORK_FRAGMENT_TAG)).commit();
        }

        retainFragment.getUsers(user, new Consumer<List<Map>>() {

            @Override
            public void apply(List<Map> users) {
                userList = users;
                setMarkers();
            }

            @Override
            public Object get() {
                return null;
            }
        });
    }

}
