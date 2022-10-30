package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.approdevelopers.ranchites.Adapters.CityDetImagesAdapter;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.CityUpdatesDetViewModel;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;


public class CityUpdatesDetailPage extends AppCompatActivity {

    private String cityUpdateId;
    private CityUpdatesDetViewModel viewModel;
    private CityDetImagesAdapter cityDetImagesAdapter;

    //Ui elements
    TextView txtCityDetPageTitle,txtCityDetPageDesc;
    ToggleButton cityUpdateDetPageLikeToggleBtn;
    TextView txtCityUpdateDetPageLikesCount;
    ViewPager2 recyclerCityDetPageImages;
    TextView txtCityUpdDetNoImageFound;
    DotsIndicator cityDetsPageDotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_updates_detail_page);

        Toolbar toolbar = findViewById(R.id.city_update_det_page_toolbar);
        toolbar.setTitle("Updates & Advertisements");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent!=null){
            cityUpdateId = intent.getStringExtra("cityUpdateId");
        }

        if (cityUpdateId.equals("")){
            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            finish();
        }

        viewModel = new ViewModelProvider(this).get(CityUpdatesDetViewModel.class);


        //hooks
        txtCityDetPageTitle = findViewById(R.id.txt_city_update_det_page_title);
        txtCityDetPageDesc = findViewById(R.id.txt_city_update_det_page_desc);
        cityUpdateDetPageLikeToggleBtn = findViewById(R.id.toggle_city_upd_det_page_like_btn);
        txtCityUpdateDetPageLikesCount = findViewById(R.id.txt_city_update_det_like_count);
        recyclerCityDetPageImages = findViewById(R.id.recycler_city_update_det_page);
        txtCityUpdDetNoImageFound = findViewById(R.id.txt_city_upd_det_no_image_found);
        cityDetsPageDotsIndicator = findViewById(R.id.city_det_page_view_pager_dots);




        viewModel.getCityUpdateModel(cityUpdateId).observe(this, cityUpdatesModel -> {
            if (cityUpdatesModel!=null){
                txtCityDetPageTitle.setText(cityUpdatesModel.getTitleText());
                txtCityDetPageDesc.setText(cityUpdatesModel.getDescText());
            }
        });

        viewModel.getLikedByUser(cityUpdateId).observe(this, aBoolean -> cityUpdateDetPageLikeToggleBtn.setChecked(aBoolean));

        viewModel.getLikesCount(cityUpdateId).observe(this, integer -> {
            String likesTag;
            if (integer ==1){
                likesTag = integer+ " like";
            }else {
                likesTag = integer+" likes";
            }
            txtCityUpdateDetPageLikesCount.setText(likesTag);
        });

        viewModel.getCityUpdateImagesUrlList(cityUpdateId).observe(this, strings -> {
            if (strings!=null && !strings.isEmpty()){
                cityDetImagesAdapter = new CityDetImagesAdapter(strings);
                recyclerCityDetPageImages.setAdapter(cityDetImagesAdapter);
                recyclerCityDetPageImages.setVisibility(View.VISIBLE);
                txtCityUpdDetNoImageFound.setVisibility(View.GONE);
                cityDetsPageDotsIndicator.setViewPager2(recyclerCityDetPageImages);
                cityDetsPageDotsIndicator.setVisibility(View.VISIBLE);
            }else {
                recyclerCityDetPageImages.setVisibility(View.GONE);
                txtCityUpdDetNoImageFound.setVisibility(View.VISIBLE);
                cityDetsPageDotsIndicator.setVisibility(View.GONE);
            }

        });


        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        cityUpdateDetPageLikeToggleBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                viewModel.addToLiked(cityUpdateId);
            }else {
                viewModel.removeFromLiked(cityUpdateId);
            }
        });


    }
}