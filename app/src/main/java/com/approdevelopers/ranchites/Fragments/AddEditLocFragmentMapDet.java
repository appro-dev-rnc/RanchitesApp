package com.approdevelopers.ranchites.Fragments;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModel;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModelFactory;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditLocFragmentMapDet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditLocFragmentMapDet extends Fragment implements View.OnClickListener, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener, GoogleMap.OnMapClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int LOCATION_REQUEST_CODE = 1000;
    private static final int GPS_REQUEST_CODE = 1005;

    private static final double CENTER_LATITUDE = 23.350226;
    private static final double CENTER_LONGITUDE = 85.298126;
    private static final int CIRCLE_RADIUS = 12000;
    private static final int MARKING_RADIUS_FROM_CURRENT_USER_LOC = 200;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton btnNextFragMapDet, btnPreviousFragMapDet;
    ViewPager2 addEditLocViewPager;
    private String locationDocId;

    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private AddNewLocViewModel viewModel;
    private SwitchCompat addLocationSwitch;

    private String[] locationPermissions;

    SupportMapFragment mapFragment;
    private SwitchCompat checkBoxLocationShare;
    LocationListener gpsLocationListener;
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager manager;


    public AddEditLocFragmentMapDet() {
        // Required empty public constructor
    }

    public AddEditLocFragmentMapDet(String locationDocId) {
        // Required empty public constructor
        this.locationDocId = locationDocId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditLocFragmentMapDet.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditLocFragmentMapDet newInstance(String param1, String param2) {
        AddEditLocFragmentMapDet fragment = new AddEditLocFragmentMapDet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        markerOptions = new MarkerOptions();

        //init permissions arrays
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};


        viewModel = new ViewModelProvider(requireActivity(), new AddNewLocViewModelFactory(locationDocId)).get(AddNewLocViewModel.class);

        addEditLocViewPager = requireActivity().findViewById(R.id.add_edit_view_pager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_loc_map_det, container, false);
        btnPreviousFragMapDet = view.findViewById(R.id.btn_previous_frag_map_det);
        btnNextFragMapDet = view.findViewById(R.id.btn_next_frag_map_det);
        addLocationSwitch = view.findViewById(R.id.add_location_switch);
        checkBoxLocationShare = view.findViewById(R.id.checkbox_location_share);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        manager = (LocationManager) requireActivity().getSystemService(LOCATION_SERVICE);

        //init Google Maps
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.add_loc_map_fragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        viewModel.getEditLocationModel().observe(requireActivity(), locationModel -> {
            if (locationModel != null) {
                GeoPoint locationGeopoint = locationModel.getLocGeopoint();
                if (locationGeopoint != null) {
                    LatLng locationLatLng = new LatLng(locationGeopoint.getLatitude(), locationGeopoint.getLongitude());
                    if (mGoogleMap != null) {

                        markerOptions.position(locationLatLng);

                        BitmapDrawable bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.ranchites_map_marker_black_mod, requireActivity().getTheme());
                        Bitmap b = null;
                        if (bitmapdraw != null) {
                            b = bitmapdraw.getBitmap();
                        }
                        if (b != null) {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                        }
                        mGoogleMap.addMarker(markerOptions);
                        CameraUpdate center = CameraUpdateFactory.newLatLng(locationLatLng);
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                        mGoogleMap.moveCamera(center);
                        mGoogleMap.animateCamera(zoom);
                    }
                }

                boolean shareEnabled = locationModel.getLocationShareEnabled();
                viewModel.setMarkedLocation(locationGeopoint);
                checkBoxLocationShare.setChecked(shareEnabled);

            } else {
                checkBoxLocationShare.setChecked(false);
                viewModel.setMarkedLocation(null);


            }
        });
        btnPreviousFragMapDet.setOnClickListener(this);
        btnNextFragMapDet.setOnClickListener(this);
        addLocationSwitch.setOnCheckedChangeListener(this);
        checkBoxLocationShare.setOnCheckedChangeListener(this);
        addLocationSwitch.setOnClickListener(this);


        //location listener
        gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                viewModel.getUserCurrentLocation().postValue(location);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(requireActivity(), "Location Service On", Toast.LENGTH_SHORT).show();
                    addLocationSwitch.setChecked(true);
                    initLocation();
                }
            }

            public void onProviderDisabled(String provider) {
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    if (addLocationSwitch.isChecked()) {
                        Toast.makeText(requireActivity(), "Location Service Off", Toast.LENGTH_SHORT).show();
                        addLocationSwitch.setChecked(false);
                        mGoogleMap.clear();
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, gpsLocationListener);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            addLocationSwitch.setChecked(true);

            new Handler().postDelayed(this::initLocation, 3000);

        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next_frag_map_det) {
            validateMapDetails();

        }
        if (view.getId() == R.id.btn_previous_frag_map_det) {
            addEditLocViewPager.setCurrentItem(1);
        }
        if (view.getId() == R.id.add_location_switch) {
            boolean b = addLocationSwitch.isChecked();
            if (b) {
                checkLocationPermissions();

            } else {
                Toast.makeText(requireActivity(), "To turn off location , go to settings page .", Toast.LENGTH_SHORT).show();
                addLocationSwitch.setChecked(true);
            }
        }
    }

    public void validateMapDetails() {

        if (checkBoxLocationShare.isChecked()) {
            if (viewModel.validateMarkedLocation()) {
                addEditLocViewPager.setCurrentItem(3);
            } else {
                Toast.makeText(requireActivity(), "No location marked on map", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!viewModel.validateMarkedLocation()) {
                addEditLocViewPager.setCurrentItem(3);
            } else {
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Location share disabled")
                        .setMessage("Continue without adding location details ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // Continue with delete operation

                            checkBoxLocationShare.setChecked(false);
                            viewModel.setMarkedLocation(null);
                            addEditLocViewPager.setCurrentItem(3);
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        drawCircle();
        mGoogleMap.setBuildingsEnabled(true);
        if (addLocationSwitch.isChecked()) {
            mGoogleMap.setOnMapClickListener(this);
        } else {
            mGoogleMap.setOnMapClickListener(null);
        }
        LatLng currentLatLng = new LatLng(CENTER_LATITUDE, CENTER_LONGITUDE);
        CameraPosition camposi = new CameraPosition.Builder().target(currentLatLng)
                .zoom(10).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));


        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);


    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isGpsEnabled();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), locationPermissions, LOCATION_REQUEST_CODE);
        }
    }

    //method to check if GPS service is enabled or not
    private void isGpsEnabled() {


        boolean result = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!result) {
            LocationRequest request = LocationRequest.create()
                    .setInterval(100).setFastestInterval(3000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMaxWaitTime(100);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(request);
            builder.setAlwaysShow(true);


            Task<LocationSettingsResponse> responseTask = LocationServices.getSettingsClient(requireActivity().getApplicationContext())
                    .checkLocationSettings(builder.build());

            responseTask.addOnSuccessListener(locationSettingsResponse -> {

            });
            responseTask.addOnFailureListener(e -> {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(requireActivity(),
                                GPS_REQUEST_CODE);

                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            });
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isGpsEnabled();
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (mGoogleMap!=null){
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }

            } else {
                Log.i("permissions", "onRequestPermissionsResult: ");
                Toast.makeText(requireActivity(), "Permission Denied\nCannot mark location", Toast.LENGTH_SHORT).show();
                addLocationSwitch.setChecked(false);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    mGoogleMap.setOnMapClickListener(this);
                   Toast.makeText(getContext(), "Location Turned On", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                   Toast.makeText(requireActivity(), "Turn On Location to continue", Toast.LENGTH_SHORT).show();
                    addLocationSwitch.setChecked(false);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId()==R.id.add_location_switch){
            if (b){
                if (mGoogleMap!=null){
                    mGoogleMap.setOnMapClickListener(this);
                }
            }else {
                if (mGoogleMap!=null){
                    mGoogleMap.clear();
                    mGoogleMap.setOnMapClickListener(null);
                }
            }
        }

        if (compoundButton.getId()==R.id.checkbox_location_share){
            viewModel.setLocationShareEnabled(b);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Location location = viewModel.getUserCurrentLocation().getValue();
        if (location!=null){
            if (isUserInCircle(location)){
                
                if (isMarkerInsideCircle(latLng)){
                    if (isMarkerAtUserLocation(location,latLng)){
                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title("Your Location");

                        BitmapDrawable bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ranchites_map_marker_black_mod,requireActivity().getTheme());
                        Bitmap b = null;
                        if (bitmapdraw != null) {
                            b = bitmapdraw.getBitmap();
                        }

                        if (markerOptions != null) {
                            if (b != null) {
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                            }
                        }

                        // Clears the previously touched position

                        // Animating to the touched position
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        mGoogleMap.clear();
                        drawCircle();
                        // Placing a marker on the touched position
                        mGoogleMap.addMarker(markerOptions);

                        GeoPoint geoPoint =  new GeoPoint(markerOptions.getPosition().latitude,markerOptions.getPosition().longitude);
                        viewModel.setMarkedLocation(geoPoint);
                    }else {
                        mGoogleMap.clear();
                        drawCircle();
                        Toast.makeText(requireActivity(), "Marked location is too far from your current location", Toast.LENGTH_SHORT).show();
                    }
                    
                }else {
                    mGoogleMap.clear();
                    drawCircle();
                    Toast.makeText(requireActivity(), "Please mark inside Ranchites circle", Toast.LENGTH_SHORT).show();
                }
               
            }else {
                mGoogleMap.clear();
                drawCircle();
                Toast.makeText(requireActivity(), "Your location must be inside Ranchites circle", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(requireActivity(), "Unable to fetch location\nPlease wait", Toast.LENGTH_SHORT).show();
            initLocation();
        }



    }

    private boolean isMarkerAtUserLocation(Location location, LatLng latLng) {
        Location markedLocation = new Location("Marked Location");
        markedLocation.setLatitude(latLng.latitude);
        markedLocation.setLongitude(latLng.longitude);
        
        float distance = location.distanceTo(markedLocation);
        
        return distance<=MARKING_RADIUS_FROM_CURRENT_USER_LOC;
    }

    private boolean isMarkerInsideCircle(LatLng latLng) {
        
        Location markedLocation = new Location("Marked Location");
        markedLocation.setLatitude(latLng.latitude);
        markedLocation.setLongitude(latLng.longitude);

        Location locationCenter = new Location("Center");
        locationCenter.setLatitude(CENTER_LATITUDE);
        locationCenter.setLongitude(CENTER_LONGITUDE);
        float distance = markedLocation.distanceTo(locationCenter);

        return distance<=CIRCLE_RADIUS;
        
    }

    private boolean isUserInCircle(Location location) {
        Location locationCenter = new Location("Center");
        locationCenter.setLatitude(CENTER_LATITUDE);
        locationCenter.setLongitude(CENTER_LONGITUDE);
        float distance = location.distanceTo(locationCenter);

        return distance<=CIRCLE_RADIUS;
    }

    private void initLocation() {
        CancellationTokenSource cts = new CancellationTokenSource();
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> fusedLocationTask = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.getToken());
        fusedLocationTask.addOnSuccessListener(location -> {

            if (location!=null){
                LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                CameraPosition camposi = new CameraPosition.Builder().target(currentLatLng)
                        .zoom(13).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));

            }

            viewModel.getUserCurrentLocation().postValue(location);


        });
    }

    private void drawCircle(){

        LatLng point= new LatLng(CENTER_LATITUDE,CENTER_LONGITUDE);
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(CIRCLE_RADIUS);

        // Border color of the circle
        circleOptions.strokeColor(0xFF0000FF);

        // Fill color of the circle
        circleOptions.fillColor(0x110000FF);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mGoogleMap.addCircle(circleOptions);

    }


}