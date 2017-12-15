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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.entities.Drink;
import com.wanna_drink.wannadrink.entities.User;
import com.wanna_drink.wannadrink.entities.UserBuilder;
import com.wanna_drink.wannadrink.functional.App;
import com.wanna_drink.wannadrink.functional.Consumer;
import com.wanna_drink.wannadrink.functional.RetainFragment;

import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.functional.App.buddies;
import static com.wanna_drink.wannadrink.functional.App.currentUserUId;
import static com.wanna_drink.wannadrink.functional.App.talkBuddy;
import static com.wanna_drink.wannadrink.functional.App.mUser;
import static com.wanna_drink.wannadrink.functional.App.userList;
import static com.wanna_drink.wannadrink.functional.SafeConversions.toDouble;
import static com.wanna_drink.wannadrink.functional.SafeConversions.toInt;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {
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
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "OnMapClick", Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "OnMapLongClick", Toast.LENGTH_SHORT).show();
            }
        });

        //Check and request GEO-positioning permissions
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

        //Setup mMap
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Set custom InfoWindowAdapter for markers
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

            @Override
            public View getInfoWindow(Marker marker) {
                View mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView tv1 = (TextView)mWindow.findViewById(R.id.title);
                tv1.setText(marker.getTitle());
                TextView tv2 = (TextView)mWindow.findViewById(R.id.snippet);
                tv2.setText(marker.getSnippet());
                return mWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {

                return null;
            }


        });
        // When InfoWindow clicked get user object from selected marker and go Chat to ChatActivity
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Check if I clicked my own marker
                if (!mUser.getUId().equals(((User) marker.getTag()).getUId())) {
                    //TODO: We have to check if the user available himself before chating to any buddy
                    //TODO: if not, make him go open himself
                    talkBuddy = (User) marker.getTag();

                    //If chat already created just open it, if not - create activity
                        Intent intent = new Intent(MapsActivity.this, ChatActivity.class);
                        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                } else {
                    Toast.makeText(MapsActivity.this, "Yup, it's you, " + mUser.getName() +"!", Toast.LENGTH_LONG).show();
                }
            }
        });


        MapsInitializer.initialize(this);

        //Get current location data
        // when got new location
        //          get nearby users from database
        //          move camera to this position
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
                                        .target(newLatLng)      // Sets the center of the map to Current location
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

    /**Creates current User object using current location
     *  and data in Shared preferences
     *  sends (adds) it to MSSQL Database using addUser method
     * @param lat - current latitude
     * @param lng - current latitude
     */
    private void sendDataToApi(double lat, double lng) {

        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        String name = sharedPref.getString(getString(R.string.key_name), "");
        String email = sharedPref.getString(getString(R.string.key_email), "");
        String uId = sharedPref.getString("uId", currentUserUId);
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


    /**Sends (adds) to MSSQL Database using WannaDrink.API
     * @param user - User object to be sent
     */
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
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Current location:\n" + currentLocation, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "MyLocation button clicked" + currentLocation, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "You have declined permission request\nYou can't register now, sorry", Toast.LENGTH_LONG).show();
            }
        }
    }

    /** Method getUsers gets from API users
     *  in a certain radius from current user.
     *  We get it through te overridden 'apply' method of Consumer
     *      then save it to global userList (App's level: List<Map> userList = new ArrayList<>();)
     *      then call setMarkers (put icons on the map)
     * @param me - current User, near which we request users from MSSQL
     */
    private void getUsers(User me) {
        userList.clear();
        // TODO: from Max: What does it do? Do we need this block in every method? Can we define it globally in this activity or in App?
        RetainFragment retainFragment = (RetainFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.NETWORK_FRAGMENT_TAG));
        if (retainFragment == null) {
            retainFragment = new RetainFragment();
            getSupportFragmentManager().beginTransaction().add(retainFragment, getString(R.string.NETWORK_FRAGMENT_TAG)).commit();
        }

        retainFragment.getUsers(me, new Consumer<List<Map>>() {
            @Override
            public void apply(List<Map> users) {
                userList = users;
                //Create an ArrayList with User objects
                buddies.clear();
                for (Map userData : userList) {
                    //If not myself, add user to the list of buddies
                    if (! userData.get("FbId").toString().equals(currentUserUId)) {
                        User buddy = new UserBuilder()
                                .addName(userData.get("name").toString())
//                                .addEmail(userData.get("email").toString())
                                .addUId(userData.get("FbId").toString())
                                .addDrink(Drink.getDrink(Integer.toString((int)toDouble(userData.get("drinkId")))))
                                .addLat(userData.get("lat").toString())
                                .addLng(userData.get("lon").toString())
                                .addDistance(userData.get("distance").toString())
                                .addAvailableFrom(userData.get("AvailableFrom").toString())
                                .addAvailableTill(userData.get("AvailableTill").toString())
                                .build();
                        buddies.add(buddy);
                    }
                }
                setMarkers();
            }
            @Override
            public Object get() {
                return null;
            }
        });
    }

    /**
     * Method to put user markers(icons) on the map
     * users' data is iterated through global userList (List of Maps)
     */
    private void setMarkers() {
        // TODO: from Max: What does it do? Do we need this block in every method? Can we define it globally in this activity or in App?
        // TODO: or may be just create a separate little method for this?
        RetainFragment retainFragment = (RetainFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.NETWORK_FRAGMENT_TAG));
        if (retainFragment == null) {
            retainFragment = new RetainFragment();
            getSupportFragmentManager().beginTransaction().add(retainFragment, getString(R.string.NETWORK_FRAGMENT_TAG)).commit();
        }

        //Create little icon for marker
        Bitmap bitmapLarge, bitmapSmall;
        LatLng point;
        // userData is a Map with single user's data from userList
        for (User buddy : buddies) {
            point = new LatLng(
                    toDouble(buddy.getAvailable().getLat()),
                    toDouble(buddy.getAvailable().getLng()));

            // Get the Icon Image from drawable
//            Double drinkId = toDouble(buddy.getDrinkID());
            bitmapLarge =((BitmapDrawable) getResources().getDrawable(Drink.getImage(buddy.getDrinkID()))).getBitmap();
            // Scale it to 50%
            bitmapSmall=Bitmap.createScaledBitmap(bitmapLarge, bitmapLarge.getWidth()/2,bitmapLarge.getHeight()/2, false);
            // Prepare the distance data for the snippet
            int distanceKM = (int) toDouble(buddy.getDistance());
            int distanceM = 10 * (int) ((toDouble(buddy.getDistance()) - distanceKM) * 100);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .snippet( "Distance: " + distanceKM + "km " + distanceM + "m " )
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmapSmall))
                    .title(buddy.getName()));
            //Add user to marker
            // to retrieve later: User myRestoredData = (User)marker.getTag(user);
            // https://stackoverflow.com/questions/13884105/android-google-maps-v2-add-object-to-marker
            marker.setTag(buddy);
        }

        //Add myself to the map as a Basic Red Marker
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(newLatLng)
                .snippet( "Ready to drink!" )
                .title(mUser.getName()));
        marker.setTag(mUser);

        //TODO: Add moveCamera to the perfect point, and zoom-in/out
        //TODO: so buddy could see your personal marker and markers of 3 nearest people at once with optimal scale zoom-in.
    }

}
