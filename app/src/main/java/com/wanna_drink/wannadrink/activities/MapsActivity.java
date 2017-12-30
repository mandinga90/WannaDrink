package com.wanna_drink.wannadrink.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.wanna_drink.wannadrink.R;
import com.wanna_drink.wannadrink.entities.Drink;
import com.wanna_drink.wannadrink.entities.Session;
import com.wanna_drink.wannadrink.entities.User;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.wanna_drink.wannadrink.functional.App.buddyList;
import static com.wanna_drink.wannadrink.functional.App.currentUserId;
import static com.wanna_drink.wannadrink.functional.App.chatBuddy;
import static com.wanna_drink.wannadrink.functional.App.mUser;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {
    private static final String TAG = "MapsActivity";
    GoogleMap mMap;
    Location currentLocation = new Location("Current");
    FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference rootDB;
    DatabaseReference userDB;
    DatabaseReference currentUserDB;
    GeoFire geoFire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        rootDB = FirebaseDatabase.getInstance().getReference();
        userDB = FirebaseDatabase.getInstance().getReference("users");
        geoFire = new GeoFire(userDB);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUser != null && mUser.getSession() != null && !mUser.getSession().getTimestamp().equals(ServerValue.TIMESTAMP) && !mUser.isInDrinkMode()) {
            startActivity(new Intent(this, StartActivity.class));
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        //Setup mMap
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Set custom InfoWindowAdapter for markers
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView tv1 = (TextView) mWindow.findViewById(R.id.title);
                tv1.setText(marker.getTitle());
                TextView tv2 = (TextView) mWindow.findViewById(R.id.snippet);
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
                if (!currentUserId.equals(((User) marker.getTag()).getId())) {
                    chatBuddy = (User) marker.getTag();
                    startActivity(new Intent(MapsActivity.this, ChatActivity.class));
                } else {
                    Toast.makeText(MapsActivity.this, "Yup, it's you, " + mUser.getCurrentName() + "!", Toast.LENGTH_LONG).show();
                }
            }
        });

        findLocation();
    }

    /**
     * Creates current User and Session objects using current location
     * and data in Shared preferences
     * sends to Firebase using addUser method
     */
    private void saveUserSession() {
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        String name = sharedPref.getString("name", "WannaDrinker");
        String email = sharedPref.getString("email", "user@gmail.com");
        int drinkId = sharedPref.getInt("drinkId", 0);
        int duration = sharedPref.getInt("duration", 300);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Session session = new Session(name, drinkId, currentLocation.getLatitude(), currentLocation.getLongitude(), duration);
        mUser = new User(currentUserId, email, session);

        rootDB.child("sessionHistory").child(currentUserId).push().setValue(mUser.getSession());

        GeoLocation gl = new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        geoFire.setLocation(currentUserId, gl, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    Toast.makeText(MapsActivity.this, "There was an error saving the location to GeoFire: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
        currentUserDB = userDB.child(currentUserId);
        currentUserDB.child("user").setValue(mUser);
    }

    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "Current location:\n" + currentLocation, Toast.LENGTH_LONG).show();
//        Toast.makeText(this, "MyLocation button clicked" + currentLocation, Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /**
     * Checks permissions, sets currentLocation and calls onLocationFound
     */
    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                currentLocation = location;
                                onLocationFound();
                            } else {
                                Toast.makeText(MapsActivity.this, "Location services are not available...", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }
    }

    /**
     * Saves user, sets listener to get buddies, moves map to currentLocation.
     */
    private void onLocationFound() {
        saveUserSession();
        getUsers();
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Current location
                .zoom(18)                   // Sets the zoom
                .bearing(0)
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        if (mMap != null) mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            findLocation();
        }
    }

    private void getUsers() {
        buddyList.clear();
        GeoLocation geoLocation = new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        GeoQuery geoQuery = geoFire.queryAtLocation(geoLocation, 100);
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                User buddy = dataSnapshot.child("user").getValue(User.class);
                if (!buddy.getId().equals(currentUserId)) {
                    if (buddy.isVisible()) {
                        Marker marker = addMarker(buddy);
                        buddy.setMarker(marker);
                        buddyList.put(buddy.getId(), buddy);
                    }
                } else {
                    addMyMarker();
                }
            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {
                //Remove buddy from the list and marker from the map, if it's out of the range
                User buddy = dataSnapshot.child("user").getValue(User.class);
                if (!buddy.getId().equals(currentUserId)) {
                    buddyList.get(buddy.getId()).getMarker().remove();
                    buddyList.remove(buddy.getId());
                }
            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {
            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
                User buddy = dataSnapshot.child("user").getValue(User.class);

                if (buddy != null)
                    if (buddy.getId().equals(currentUserId)) {
                        if (mUser.getMarker() != null) {
                            mUser.getMarker().remove();
                            addMyMarker();
                        }
                    } else {
                        buddyList.get(buddy.getId()).getMarker().remove();
                        buddyList.remove(buddy.getId());
                        if (buddy.isVisible()) {
                            Marker marker = addMarker(buddy);
                            buddy.setMarker(marker);
                            buddyList.put(buddy.getId(), buddy);
                        }
                    }
            }

            @Override
            public void onGeoQueryReady() {
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }

    /**
     * Puts marker on the map for specified User
     *
     * @param buddy
     */
    private Marker addMarker(User buddy) {

        Location buddyLocation = new Location("");
        buddyLocation.setLatitude(buddy.getCurrentLat());
        buddyLocation.setLongitude(buddy.getCurrentLng());
        LatLng buddyLatLng = new LatLng(buddy.getCurrentLat(), buddy.getCurrentLng());

        // Calculate distance in meters, rounded by 10 meters
        int distance = 10 * (int) (currentLocation.distanceTo(buddyLocation) / 10);
        // Prepare the distance data for the snippet
        int distanceKM = (int) (distance / 1000);
        int distanceM = (int) (distance % 1000);

        //Create little icon for marker
        Bitmap bitmapLarge, bitmapSmall;

        // Get the Icon Image from drawable (grayed if not active anymore)
        if (!buddy.isInDrinkMode()) {
            bitmapLarge = ((BitmapDrawable) getResources().getDrawable(Drink.getImage(buddy.getCurrentDrinkId()))).getBitmap();
        } else {
            bitmapLarge = convertDrawableToGrayscaleBitmap((BitmapDrawable) getResources().getDrawable(Drink.getImage(buddy.getCurrentDrinkId())));
        }
        // Scale it to 50%
        bitmapSmall = Bitmap.createScaledBitmap(bitmapLarge, bitmapLarge.getWidth() / 2, bitmapLarge.getHeight() / 2, false);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(buddyLatLng)
                .snippet("Distance: " + distanceKM + "km " + distanceM + "m ")
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapSmall))
                .title(buddy.getCurrentName()));
        //Add user object to marker
        // to retrieve later: User myRestoredData = (User)marker.getTag(user);
        marker.setTag(buddy);
        return marker;
    }

    /**
     * Method to put my marker(icon) on the map
     */
    private void addMyMarker() {
        //Add myself to the map as a Basic Red Marker
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .snippet("Ready to drink!")
                .title(mUser.getCurrentName()));
        marker.setTag(mUser);
        mUser.setMarker(marker);
    }

    protected Bitmap convertDrawableToGrayscaleBitmap(BitmapDrawable drawable) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        drawable.setColorFilter(filter);
        return drawable.getBitmap();
    }
}

//TODO: Add moveCamera to the perfect point, and zoom-in/out
//TODO: so chatBuddy could see your personal marker and markers of 3 nearest people at once with optimal scale zoom-in.