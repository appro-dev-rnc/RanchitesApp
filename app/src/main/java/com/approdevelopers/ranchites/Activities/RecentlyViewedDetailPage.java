package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.approdevelopers.ranchites.Adapters.DiffUtilsRecentlyViewedClass;
import com.approdevelopers.ranchites.Adapters.RecentlyViewedLocAdapter;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.RecentlyViewedDetViewModel;



public class RecentlyViewedDetailPage extends AppCompatActivity implements NetworkConnection.ReceiverListener {

    RecentlyViewedDetViewModel recentlyViewedDetViewModel;
    RecyclerView recyclerRecentlyViewedDet;
    RecentlyViewedLocAdapter recentlyViewedLocAdapter;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout recentDetParentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed_detail_page);

        networkConnection = new NetworkConnection();

        recyclerRecentlyViewedDet = findViewById(R.id.recycler_recently_viewed_det_page);
        recentDetParentView = findViewById(R.id.recent_det_parent_view);

        Toolbar toolbar = findViewById(R.id.recently_viewed_det_page_toolbar);
        toolbar.setTitle("Recently Viewed");
        setSupportActionBar(toolbar);


        recyclerRecentlyViewedDet.setLayoutManager(new GridLayoutManager(this,2));


        recentlyViewedDetViewModel = new RecentlyViewedDetViewModel();
        recentlyViewedLocAdapter = new RecentlyViewedLocAdapter(new DiffUtilsRecentlyViewedClass());

        recentlyViewedDetViewModel.getRecentlyViewedLocLiveResult().observe(RecentlyViewedDetailPage.this, recentlyViewedLocations -> recentlyViewedDetViewModel.getRecentlyViewedDetailLive(recentlyViewedLocations).observe(RecentlyViewedDetailPage.this, recentlyViewedDetailedModels -> {
            if (recentlyViewedDetailedModels!=null && !recentlyViewedDetailedModels.isEmpty()){
                recentlyViewedLocAdapter.submitList(recentlyViewedDetailedModels);
                recyclerRecentlyViewedDet.setAdapter(recentlyViewedLocAdapter);
            }
        }));


        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(RecentlyViewedDetailPage.this,networkConnection,recentDetParentView);
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