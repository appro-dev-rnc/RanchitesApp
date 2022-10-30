package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.approdevelopers.ranchites.Adapters.DiffUtilsFeaturedLocationClass;
import com.approdevelopers.ranchites.Adapters.FeaturedLocationAdapter;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.FeaturedLocDetViewModel;



public class FeaturedLocationsDetailPage extends AppCompatActivity implements NetworkConnection.ReceiverListener {

    RecyclerView recyclerFeaturedDet;
    FeaturedLocDetViewModel featuredLocDetViewModel;
    FeaturedLocationAdapter featuredAdapter;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout featuredDetParentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_locations_detail_page);

        networkConnection = new NetworkConnection();

        Toolbar toolbar = findViewById(R.id.featured_det_page_toolbar);
        toolbar.setTitle("Featured Places");
        setSupportActionBar(toolbar);

        featuredDetParentView = findViewById(R.id.featured_det_parent_view);

        recyclerFeaturedDet = findViewById(R.id.recycler_featured_det_page);
        recyclerFeaturedDet.setLayoutManager(new GridLayoutManager(this,2));
        featuredAdapter = new FeaturedLocationAdapter(new DiffUtilsFeaturedLocationClass());

        featuredLocDetViewModel = new ViewModelProvider(this).get(FeaturedLocDetViewModel.class);

        featuredLocDetViewModel.getFeaturedLocLiveResult().observe(this, featuredLocations -> {
            featuredAdapter.submitList(featuredLocations);
            recyclerFeaturedDet.setAdapter(featuredAdapter);
        });

        toolbar.setNavigationOnClickListener(view -> onBackPressed());


    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(FeaturedLocationsDetailPage.this,networkConnection,featuredDetParentView);
        NetworkConnection.Listener = this;
        connectionUtils.checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();

        this.unregisterReceiver(networkConnection);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }
}