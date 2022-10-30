package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Adapters.DiffUtilsSearchClass;
import com.approdevelopers.ranchites.Adapters.NearMeLocationsAdapter;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Interfaces.NearMeCategoryItemSelectInterface;
import com.approdevelopers.ranchites.Interfaces.NearMeRecyclerItemClickInterface;
import com.approdevelopers.ranchites.Models.SearchResultModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.Utils.SortLocationsWithDistance;
import com.approdevelopers.ranchites.ViewModels.NearMeSearchViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearMeSearchActivity extends AppCompatActivity implements OnMapReadyCallback, NearMeCategoryItemSelectInterface, NetworkConnection.ReceiverListener, NearMeRecyclerItemClickInterface, View.OnClickListener {

    private static final int GPS_REQUEST_CODE = 5005;
    private static final int LOCATION_REQUEST_CODE = 5000;


    private static final double CENTER_LATITUDE = 23.350226;
    private static final double CENTER_LONGITUDE = 85.298126;


    private String[] locationPermissions;

    RecyclerView  recyclerNearMeSearchResults;
    NearMeSearchViewModel nearMeSearchViewModel;
    SupportMapFragment mapNearMeSearch;

    NearMeLocationsAdapter nearMeLocationsAdapter;
    AppCompatAutoCompleteTextView autoCompleteNearMeSearchKey;
    TextInputLayout textLayoutNearMeSearch;
    ArrayAdapter<String> categoriesAdapter;

    LocationListener gpsLocationListener;
    LocationManager manager;
    List<SearchResultModel> searchResultModelsList;
    MaterialCardView noResultParent;
    RelativeLayout searchProgressParent;
    ProgressBar nearMeProgressBar;


    GoogleMap mGoogleMap;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout nearMeParentView;
    ConstraintLayout nearMeSubParentView;

    Snackbar gpsSnackBar;


    private List<Marker> markers;


    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_me_search_page_modified);
        nearMeSearchViewModel = new ViewModelProvider(this).get(NearMeSearchViewModel.class);

        networkConnection =new NetworkConnection();
        markers = new ArrayList<>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        checkLocationPermissions();


        recyclerNearMeSearchResults = findViewById(R.id.recycler_near_me_search_results);
        autoCompleteNearMeSearchKey = findViewById(R.id.auto_complete_near_me_search_key);
        textLayoutNearMeSearch = findViewById(R.id.textLayoutNearMeSearch);
        noResultParent = findViewById(R.id.no_results_found_parent);
        searchProgressParent = findViewById(R.id.search_progress_parent);
        nearMeProgressBar = findViewById(R.id.near_me_progress_bar);
        nearMeSubParentView=findViewById(R.id.near_me_sub_parent);
        CircleImageView imgBackBtnNearMe = findViewById(R.id.img_back_btn_near_me);

        nearMeParentView = findViewById(R.id.near_me_parent_view);


        manager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //location listener
        gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location==null){
                    initLocation();

                }else {
                    nearMeSearchViewModel.getUserLocation().postValue(location);
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    gpsSnackBar= Snackbar.make(nearMeParentView,"Location Service On",Snackbar.LENGTH_SHORT);
                    gpsSnackBar.setTextColor(Color.BLACK);
                    gpsSnackBar.setBackgroundTint(Color.GREEN);
                    gpsSnackBar.show();
                    initLocation();
                    if (!isNetworkAvailable()){
                        showNoInternetSnackBar();
                    }
                }
            }

            public void onProviderDisabled(String provider) {
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    gpsSnackBar= Snackbar.make(nearMeSubParentView,"Location Service Off",Snackbar.LENGTH_INDEFINITE);
                    gpsSnackBar.setTextColor(Color.BLACK);
                    gpsSnackBar.setBackgroundTint(Color.RED);
                    gpsSnackBar.show();

                }
            }
        };



        //init Google Maps
        mapNearMeSearch = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_near_me);
        if (mapNearMeSearch != null) {
            mapNearMeSearch.getMapAsync(NearMeSearchActivity.this);
        }

        nearMeLocationsAdapter = new NearMeLocationsAdapter(new DiffUtilsSearchClass(),this);

        recyclerNearMeSearchResults.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        nearMeSearchViewModel.getSearchGuideKeywords().observe(this, strings -> {
            if (strings!=null && !strings.isEmpty()){
                categoriesAdapter = new ArrayAdapter<>(NearMeSearchActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, strings);
                autoCompleteNearMeSearchKey.setAdapter(categoriesAdapter);
            }
        });

        nearMeSearchViewModel.getNearMeSearchResultLive().observe(this, searchResultModels -> {
            nearMeProgressBar.setVisibility(View.GONE);

            if (searchResultModels != null && !searchResultModels.isEmpty()) {
                noResultParent.setVisibility(View.GONE);
             updateRecyclerData(searchResultModels);

            }else {
                recyclerNearMeSearchResults.setVisibility(View.GONE);
                noResultParent.setVisibility(View.VISIBLE);

            }
        });

        textLayoutNearMeSearch.setEndIconOnClickListener(view -> {
            if (isLocationEnabled()){
                if (isNetworkAvailable()){
                    String query = autoCompleteNearMeSearchKey.getText().toString();
                    if (!query.equals("")){
                        noResultParent.setVisibility(View.GONE);
                        searchProgressParent.setVisibility(View.VISIBLE);
                        nearMeProgressBar.setVisibility(View.VISIBLE);
                        clearMapView();

                        nearMeSearchViewModel.searchNearMeKeyword(query);
                    }else {
                        textLayoutNearMeSearch.setErrorEnabled(true);
                        textLayoutNearMeSearch.setError("Empty Query");
                    }
                }else {
                    showNoInternetSnackBar();
                }


            }
            autoCompleteNearMeSearchKey.clearFocus();


        });
        autoCompleteNearMeSearchKey.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (isLocationEnabled()){
                    if (isNetworkAvailable()){
                        String queryKeyword = textView.getText().toString();
                        if (!queryKeyword.equals("")){
                            noResultParent.setVisibility(View.GONE);
                            searchProgressParent.setVisibility(View.VISIBLE);
                            nearMeProgressBar.setVisibility(View.VISIBLE);
                            clearMapView();
                            nearMeSearchViewModel.searchNearMeKeyword(queryKeyword);
                        }else {
                            textLayoutNearMeSearch.setErrorEnabled(true);
                            textLayoutNearMeSearch.setError("Empty Query");
                        }
                    }else {
                        showNoInternetSnackBar();
                    }




                }
                autoCompleteNearMeSearchKey.clearFocus();

                return true;
            }
            return false;
        });







        autoCompleteNearMeSearchKey.setOnItemClickListener((adapterView, view, i, l) -> {
            if (isLocationEnabled()){
                if (isNetworkAvailable()){
                    noResultParent.setVisibility(View.GONE);
                    searchProgressParent.setVisibility(View.VISIBLE);
                    nearMeProgressBar.setVisibility(View.VISIBLE);
                    String queryKeyword = (String) adapterView.getItemAtPosition(i);
                    nearMeSearchViewModel.searchNearMeKeyword(queryKeyword);
                    clearMapView();
                }else {
                    showNoInternetSnackBar();
                }



            }
           autoCompleteNearMeSearchKey.clearFocus();


        });

        autoCompleteNearMeSearchKey.setOnFocusChangeListener((view, b) -> {
            if (b){
                autoCompleteNearMeSearchKey.showDropDown();
            }
            if (!b){
                ((InputMethodManager) NearMeSearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(autoCompleteNearMeSearchKey.getWindowToken(), 0);
            }

        });

        nearMeSearchViewModel.getUserLocation().observe(this, location -> {
            if (location==null){
                if (isLocationEnabled()){
                    gpsSnackBar = Snackbar.make(nearMeParentView,"Fetching location", Snackbar.LENGTH_INDEFINITE);
                    gpsSnackBar.setBackgroundTint(Color.YELLOW);
                    gpsSnackBar.setTextColor(Color.BLACK);
                    gpsSnackBar.show();
                }else {
                   checkAndShowSnackBar();
                }

            }else {
                if (gpsSnackBar!=null){
                    gpsSnackBar.dismiss();

                }
                updateRecyclerData(searchResultModelsList);

            }
        });

        autoCompleteNearMeSearchKey.addTextChangedListener(searchTextWatcher);
        imgBackBtnNearMe.setOnClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearMeSearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, gpsLocationListener);

    }

    private void showNoInternetSnackBar() {
        String  message = "No Internet Connection";
        if (gpsSnackBar!=null ){
            gpsSnackBar= Snackbar.make( nearMeParentView,message, Snackbar.LENGTH_INDEFINITE);
            gpsSnackBar.setBackgroundTint(Color.RED);
            gpsSnackBar.setTextColor(Color.WHITE);
            gpsSnackBar.show();
        }

    }

    private void clearMapView() {
        if (mGoogleMap!=null){
            mGoogleMap.clear();

        }
        if (markers!=null){
            markers.clear();

        }
    }

    private void checkAndShowSnackBar() {

            if (gpsSnackBar!=null ){
                gpsSnackBar= Snackbar.make(nearMeSubParentView,"Location Service Off",Snackbar.LENGTH_INDEFINITE);
                gpsSnackBar.setTextColor(Color.BLACK);
                gpsSnackBar.setBackgroundTint(Color.RED);
                gpsSnackBar.show();
            }

    }

    private void updateRecyclerData(List<SearchResultModel> searchResultModels) {
        Location current = nearMeSearchViewModel.getUserLocation().getValue();


        if (searchResultModels!=null && !searchResultModels.isEmpty()){
            LatLng currentLatLng;

                if (current != null) {
                    currentLatLng = new LatLng(current.getLatitude(), current.getLongitude());

                    if (mGoogleMap != null) {

                        for (SearchResultModel model : searchResultModels) {

                            LatLng locationLatLng = new LatLng(model.getLocGeopoint().getLatitude(), model.getLocGeopoint().getLongitude());
                            float[] results = new float[1];
                            Location.distanceBetween(currentLatLng.latitude,currentLatLng.longitude,
                                    locationLatLng.latitude,locationLatLng.longitude,results);
                            model.setDistanceFromUser(results[0]);
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .title(model.getTitle())
                                    .snippet("Address:-"+"\n"+model.getAddress())
                            .position(locationLatLng)
                            );
                            if (marker != null) {
                                marker.setTag(model.getDocumentId());
                            }

                            BitmapDrawable bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ranchites_map_marker_black_mod,getTheme());
                            Bitmap b = null;
                            if (bitmapdraw != null) {
                                b = bitmapdraw.getBitmap();
                            }

                            if (marker != null) {
                                assert b != null;
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(b));
                            }


                            markers.add(marker);
                            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                @Override
                                public View getInfoWindow(@NonNull Marker arg0) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(@NonNull Marker marker) {

                                    LinearLayout info = new LinearLayout(NearMeSearchActivity.this);
                                    info.setOrientation(LinearLayout.VERTICAL);


                                    TextView title = new TextView(NearMeSearchActivity.this);
                                    title.setTextColor(Color.BLACK);
                                    title.setGravity(Gravity.CENTER);
                                    title.setTypeface(null, Typeface.BOLD);
                                    title.setText(marker.getTitle());

                                    TextView snippet = new TextView(NearMeSearchActivity.this);
                                    snippet.setTextColor(Color.GRAY);
                                    snippet.setGravity(Gravity.CENTER);
                                    snippet.setText(marker.getSnippet());
                                    snippet.setMaxWidth(240);


                                    info.addView(title);
                                    info.addView(snippet);
                                    return info;
                                }
                            });
                        }

                        Collections.sort(searchResultModels,new SortLocationsWithDistance());

                        Log.i("nhjn", "updateRecyclerData: "+searchResultModels);
                        recyclerNearMeSearchResults.setVisibility(View.VISIBLE);
                        nearMeLocationsAdapter.submitList(searchResultModels);
                        recyclerNearMeSearchResults.setAdapter(nearMeLocationsAdapter);
                        CameraPosition camposi = new CameraPosition.Builder().target(currentLatLng)
                                .zoom(14).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));
                    }
                }else {
                    if (nearMeSearchViewModel.getLocationCheckCount()==0){
                        if (isLocationEnabled()){
                            gpsSnackBar = Snackbar.make(nearMeSubParentView,"Please wait... Fetching Location",Snackbar.LENGTH_LONG);
                            gpsSnackBar.setBackgroundTint(Color.WHITE);
                            gpsSnackBar.setTextColor(Color.BLACK);
                            gpsSnackBar.show();
                            initLocation();
                            nearMeSearchViewModel.setLocationCheckCount(1);

                        }
                    }else {
                        gpsSnackBar = Snackbar.make(nearMeSubParentView,"Cannot fetch location ...Try again",Snackbar.LENGTH_SHORT);
                        gpsSnackBar.setBackgroundTint(Color.WHITE);
                        gpsSnackBar.setTextColor(Color.BLACK);
                        gpsSnackBar.show(); }


                }

        }



    }

    private void initLocation() {
        CancellationTokenSource cts = new CancellationTokenSource();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (nearMeSearchViewModel.getUserLocation()!=null){
                nearMeSearchViewModel.getUserLocation().postValue(location);
            }

            if (location!=null){
                LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                CameraPosition camposi = new CameraPosition.Builder().target(currentLatLng)
                        .zoom(12).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng currentLatLng = new LatLng(CENTER_LATITUDE,CENTER_LONGITUDE);
        CameraPosition camposi = new CameraPosition.Builder().target(currentLatLng)
                .zoom(10).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);


    }

    //method to check location permissions for the applications

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isGpsEnabled();
        } else {
            ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_REQUEST_CODE);
        }
    }

    //method to check if GPS service is enabled or not
    private void isGpsEnabled() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean result = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!result) {
            LocationRequest request = LocationRequest.create()
                    .setInterval(100).setFastestInterval(3000)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMaxWaitTime(100);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(request);
            builder.setAlwaysShow(true);


            Task<LocationSettingsResponse> responseTask = LocationServices.getSettingsClient(getApplicationContext())
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
                        resolvable.startResolutionForResult(NearMeSearchActivity.this,
                                GPS_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            });
        }else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isGpsEnabled();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                }

            } else {
                Toast.makeText(NearMeSearchActivity.this, "Permission Denied\nAllow location permission for this app to continue.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void categorySelected(String categoryName) {
        autoCompleteNearMeSearchKey.setText(categoryName);
        nearMeSearchViewModel.searchNearMeKeyword(categoryName);
        Objects.requireNonNull(textLayoutNearMeSearch.getEditText()).clearFocus();
    }

    TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence == ""){
                searchProgressParent.setVisibility(View.GONE);
            }
            textLayoutNearMeSearch.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(NearMeSearchActivity.this,networkConnection,nearMeParentView);
        NetworkConnection.Listener = this;
        connectionUtils.checkConnection();
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();

        this.unregisterReceiver(networkConnection);
    }




    @Override
    public void nearMeItemClicked(String tag) {
        if (tag!=null && !markers.isEmpty()){

            for (Marker marker : markers){
                if (marker.getTag()==tag){
                    if (marker.isInfoWindowShown()){
                        marker.hideInfoWindow();
                        Location location= nearMeSearchViewModel.getUserLocation().getValue();
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            CameraPosition camposi = new CameraPosition.Builder().target(latLng)
                                    .zoom(12).build();
                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));
                        }

                    }else {
                        marker.showInfoWindow();
                        CameraPosition camposi = new CameraPosition.Builder().target(marker.getPosition())
                                .zoom(15).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));

                    }

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Turn On Location to continue", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.img_back_btn_near_me){
            onBackPressed();
        }
    }


    private boolean isLocationEnabled(){
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            if (nearMeSearchViewModel.getUserLocation().getValue()==null){
                initLocation();
            }
            return true;
        }else {
            Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
            checkAndShowSnackBar();
        }
        return false;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof AppCompatAutoCompleteTextView ) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}