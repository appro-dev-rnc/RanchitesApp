package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.approdevelopers.ranchites.Adapters.BestRatedLocAdapter;
import com.approdevelopers.ranchites.Adapters.DiffUtilsBestRatedClass;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.BestRatedDetViewModel;


public class BestRatedDetailPage extends AppCompatActivity implements NetworkConnection.ReceiverListener {

    BestRatedDetViewModel bestRatedDetViewModel;
    RecyclerView recyclerBestRatedDet;
    BestRatedLocAdapter bestRatedLocAdapter;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout bestRatedParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_rated_detail_page);

        Toolbar toolbar = findViewById(R.id.best_rated_det_page_toolbar);
        toolbar.setTitle("Best Rated Places");
        setSupportActionBar(toolbar);

        networkConnection = new NetworkConnection();

        //hooks
        recyclerBestRatedDet = findViewById(R.id.recycler_best_rated_det);
        bestRatedParentView = findViewById(R.id.best_rated_parent_view);


        recyclerBestRatedDet.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        bestRatedDetViewModel = new ViewModelProvider(this).get(BestRatedDetViewModel.class);
        bestRatedLocAdapter= new BestRatedLocAdapter(new DiffUtilsBestRatedClass(),R.layout.best_rated_detailed_item);

        bestRatedDetViewModel.getBestRatedLocLiveResult().observe(this, bestRatedLocations -> {
            bestRatedLocAdapter.submitList(bestRatedLocations);
            recyclerBestRatedDet.setAdapter(bestRatedLocAdapter);
        });

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }


    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(BestRatedDetailPage.this,networkConnection,bestRatedParentView);
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