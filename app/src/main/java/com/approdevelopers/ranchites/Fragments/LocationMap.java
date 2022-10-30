package com.approdevelopers.ranchites.Fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.CustomMapFragmentClass;
import com.approdevelopers.ranchites.ViewModels.LocationDetailPageViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationMap extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String locationId;
    private GoogleMap mGoogleMap;
    SupportMapFragment mapNearMeSearch;
    TextView txtLocDetNoMapFoundError;
    private MaterialCardView locDetMapFragParent;
    private NestedScrollView scrollLocDetMapParent;


    LocationDetailPageViewModel viewModel;

    public LocationMap() {
        // Required empty public constructor
    }public LocationMap(String locationId) {
        // Required empty public constructor

        this.locationId = locationId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationMap.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationMap newInstance(String param1, String param2) {
        LocationMap fragment = new LocationMap();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        txtLocDetNoMapFoundError = view.findViewById(R.id.txt_loc_det_no_map_found_error);
        locDetMapFragParent = view.findViewById(R.id.loc_det_map_frag_parent);
        scrollLocDetMapParent =view.findViewById(R.id.scroll_view_loc_det_map_parent);
        //init Google Maps
        mapNearMeSearch = (CustomMapFragmentClass) getChildFragmentManager().findFragmentById(R.id.map_loc_det_fragment);

        if (mapNearMeSearch!=null){
            mapNearMeSearch.getMapAsync(this);
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= new ViewModelProvider(requireActivity()).get(LocationDetailPageViewModel.class);


        viewModel.getCurrentLocationDetailLive(locationId).observe(getViewLifecycleOwner(), new Observer<LocationModel>() {
            @Override
            public void onChanged(LocationModel locationModel) {
                if (locationModel!=null){
                    if (locationModel.getLocationShareEnabled() && locationModel.getLocGeopoint()!=null){
                        LatLng latLng = new LatLng(locationModel.getLocGeopoint().getLatitude(),locationModel.getLocGeopoint().getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(locationModel.getTitle());




                        BitmapDrawable bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ranchites_map_marker_black_mod,requireActivity().getTheme());
                        Bitmap b = null;
                        if (bitmapdraw != null) {
                            b = bitmapdraw.getBitmap();
                        }

                        if (b != null) {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                        }

                        if (mGoogleMap!=null){
                            Marker marker = mGoogleMap.addMarker(markerOptions);
                            mGoogleMap.addMarker(markerOptions);

                            CameraPosition camposi = new CameraPosition.Builder().target(latLng)
                                    .zoom(14).build();
                            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camposi));
                            if (marker != null) {
                                marker.showInfoWindow();
                            }
                        }
                        txtLocDetNoMapFoundError.setVisibility(View.GONE);
                        locDetMapFragParent.setVisibility(View.VISIBLE);
                    }else {
                        txtLocDetNoMapFoundError.setVisibility(View.VISIBLE);
                        locDetMapFragParent.setVisibility(View.GONE);

                    }



                }
            }
        });

        ((CustomMapFragmentClass) getChildFragmentManager()
                .findFragmentById(R.id.map_loc_det_fragment))
                .setListener(new CustomMapFragmentClass.OnTouchListener() {
                    @Override    public void onTouch() {
                        scrollLocDetMapParent.requestDisallowInterceptTouchEvent(true);
                    }
                });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }
    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}