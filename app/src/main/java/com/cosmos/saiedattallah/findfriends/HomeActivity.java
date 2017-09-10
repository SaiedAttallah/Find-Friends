package com.cosmos.saiedattallah.findfriends;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cosmos.saiedattallah.findfriends.adapters.FriendsListAdapter;
import com.cosmos.saiedattallah.findfriends.injection.BaseActivity;
import com.cosmos.saiedattallah.findfriends.models.Friend;
import com.cosmos.saiedattallah.findfriends.providers.FriendsProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends BaseActivity implements OnMapReadyCallback, FriendsProvider.OnRetrieveFriendsListListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @Inject
    FriendsProvider friendsProvider;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 132;

    Toolbar toolbar;

    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    LatLng userLocationLatLng;
    Marker userLocationMarker;
    HashMap<Integer, Marker> markerHashMap;

    FriendsListAdapter friendsListAdapter;
    ArrayList<Friend> friendsList;
    RecyclerView rvFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        markerHashMap = new HashMap<>();
        friendsList = new ArrayList<>();
        friendsProvider.setOnRetrieveFriendsListListener(this);
        friendsProvider.retrieveFriendsList();

        rvFriends = (RecyclerView) findViewById(R.id.rv_friends);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(friendsProvider != null)
            friendsProvider.removeOnRetrieveFriendsListListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_friends_list) {
            Intent loginIntent = new Intent(HomeActivity.this, FriendsListActivity.class);
            startActivity(loginIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mGoogleMap.setMyLocationEnabled(true);
        this.googleApiClient.connect();

        this.setupMap();

    }

    @Override
    public void onFriendsListRetrieved(List<Friend> friendsList) {
        friendsListAdapter = new FriendsListAdapter(this, friendsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFriends.setLayoutManager(layoutManager);

        // Attach the adapter to the recycler view to populate items
        rvFriends.setAdapter(friendsListAdapter);
        // Set layout manager to position the items
        friendsListAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });
    }

    @Override
    public void onRetrieveFriendsListFailure(int failureReason, int failureMessage) {

    }

    protected synchronized void buildGoogleApiClient() {
        this.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

    }

    private void setupMap() {
        this.googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getLayoutInflater().inflate(R.layout.info_window, null);

                TextView tvFirstName = (TextView) v.findViewById(R.id.tv_map_friend_first_name);

                try {
                    int friendId = Integer.parseInt(marker.getTitle());
                    Friend friend = friendsList.get(friendId);


                    LatLng latLng = marker.getPosition();
                    tvFirstName.setText(friend.getFirstName());

                } catch (NumberFormatException e) {

                }
                return v;
            }
        });

        // this do the trick for loading image on infowindow
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker marker) {
                marker.showInfoWindow();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        marker.showInfoWindow();

                    }
                }, 200);

                return true;
            }
        });


        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.checkLocationPermission();
            return;
        }
            Location userLastLocation = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
            if (userLastLocation != null) {
                //place marker at current position
                //mGoogleMap.clear();
                userLocationLatLng = new LatLng(userLastLocation.getLatitude(), userLastLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(userLocationLatLng);
//                markerOptions.title(getString(R.string.current_location));
                userLocationMarker = googleMap.addMarker(markerOptions);

                //zoom to current position:
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(userLocationLatLng).zoom(17).tilt(0)
                        .build();
                this.googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }

        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(100);
        this.locationRequest.setFastestInterval(100);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setSmallestDisplacement(0.01F);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
