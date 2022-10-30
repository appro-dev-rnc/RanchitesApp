package com.approdevelopers.ranchites.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.CustomMapFragmentClass;
import com.approdevelopers.ranchites.ViewModels.MyLocFullDetailViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLocationMapView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationMapView extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SupportMapFragment mapFragment;

    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private String locationDocId;
    private MyLocFullDetailViewModel viewModel;
    private TextView txtMyLocNoLocationFoundError;
    private MaterialCardView myLocMapMaterialParent;
    private NestedScrollView myLocMapNestedParent;

    public MyLocationMapView() {
        // Required empty public constructor
    }public MyLocationMapView(String locationDocId) {
        // Required empty public constructor
        this.locationDocId = locationDocId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLocationMapView.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLocationMapView newInstance(String param1, String param2) {
        MyLocationMapView fragment = new MyLocationMapView();
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
        View view = inflater.inflate(R.layout.fragment_my_location_map_view, container, false);
        txtMyLocNoLocationFoundError = view.findViewById(R.id.txt_my_loc_no_location_found_error);
        myLocMapMaterialParent = view.findViewById(R.id.my_loc_map_parent_material);
        myLocMapNestedParent = view.findViewById(R.id.my_loc_map_nested_parent);

        //init Google Maps
         mapFragment = (CustomMapFragmentClass) getChildFragmentManager().findFragmentById(R.id.map_fragement_my_loc_details);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        markerOptions = new MarkerOptions();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (locationDocId!=null)
            viewModel = new ViewModelProvider(requireActivity())
                    .get(MyLocFullDetailViewModel.class);

        ((CustomMapFragmentClass) Objects.requireNonNull(getChildFragmentManager()
                .findFragmentById(R.id.map_fragement_my_loc_details)))
                .setListener(() -> myLocMapNestedParent.requestDisallowInterceptTouchEvent(true));


        viewModel.getCurrentLocationDetailLiveData(locationDocId).observe(requireActivity(), locationModel -> {
            if (locationModel!=null&& !locationModel.toString().isEmpty()){
                if (locationModel.getLocationShareEnabled() && locationModel.getLocGeopoint()!=null){
                    GeoPoint locationGeopoint = locationModel.getLocGeopoint();

                        LatLng locationLatLng = new LatLng(locationGeopoint.getLatitude(),locationGeopoint.getLongitude());

                    BitmapDrawable bitmapdraw = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.ranchites_map_marker_black_mod,requireActivity().getTheme());
                    Bitmap b = null;
                    if (bitmapdraw != null) {
                        b = bitmapdraw.getBitmap();
                    }
                    if (b != null) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                    }


                    if (mGoogleMap!=null){
                            mGoogleMap.clear();
                            markerOptions.position(locationLatLng);
                            mGoogleMap.addMarker(markerOptions);
                            CameraUpdate center = CameraUpdateFactory.newLatLng(locationLatLng);
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
                            mGoogleMap.moveCamera(center);
                            mGoogleMap.animateCamera(zoom);
                        }

                        txtMyLocNoLocationFoundError.setVisibility(View.GONE);
                        myLocMapMaterialParent.setVisibility(View.VISIBLE);


                }
              else {
                    txtMyLocNoLocationFoundError.setVisibility(View.VISIBLE);
                    myLocMapMaterialParent.setVisibility(View.GONE);

                }

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}