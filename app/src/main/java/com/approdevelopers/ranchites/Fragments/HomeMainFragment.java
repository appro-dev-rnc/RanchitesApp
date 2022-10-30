package com.approdevelopers.ranchites.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.approdevelopers.ranchites.Activities.BestRatedDetailPage;
import com.approdevelopers.ranchites.Activities.FeaturedLocationsDetailPage;
import com.approdevelopers.ranchites.Activities.NearMeSearchActivity;
import com.approdevelopers.ranchites.Activities.ProfessionalsDetailPage;
import com.approdevelopers.ranchites.Activities.RecentlyViewedDetailPage;
import com.approdevelopers.ranchites.Adapters.BestRatedLocAdapter;
import com.approdevelopers.ranchites.Adapters.CityProfessionalsAdapter;
import com.approdevelopers.ranchites.Adapters.CityUpdatesAdapter;
import com.approdevelopers.ranchites.Adapters.DiffUtilsAllCategoriesClass;
import com.approdevelopers.ranchites.Adapters.DiffUtilsBestRatedClass;
import com.approdevelopers.ranchites.Adapters.DiffUtilsCityProfessionals;
import com.approdevelopers.ranchites.Adapters.DiffUtilsCityUpdateModelClass;
import com.approdevelopers.ranchites.Adapters.DiffUtilsFeaturedLocationClass;
import com.approdevelopers.ranchites.Adapters.DiffUtilsMostSearchedClass;
import com.approdevelopers.ranchites.Adapters.DiffUtilsRecentlyViewedClass;
import com.approdevelopers.ranchites.Adapters.FeaturedLocationAdapter;
import com.approdevelopers.ranchites.Adapters.HomepageCategoriesAdapter;
import com.approdevelopers.ranchites.Adapters.MostSearchedLocationAdapter;
import com.approdevelopers.ranchites.Adapters.RecentlyViewedLocAdapter;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.CategoriesModel;
import com.approdevelopers.ranchites.Models.CityUpdatesModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.MainViewModel;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMainFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int VIEW_TIME_LIMIT = 5000;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerHomePageCategories;
    private MainViewModel viewModel;
    private ViewPager2 cityUpdateViewPager;
    private RecyclerView recyclerFeaturedLoc;


    //adapters
    private FeaturedLocationAdapter featuredLocationAdapter;
    private HomepageCategoriesAdapter homepageCategoriesAdapter;
    private CityUpdatesAdapter cityUpdatesAdapter;

    Handler handler ;
    Runnable timeCounter;

    //Dots indicator instances for viewPager and recyclerViews
    private DotsIndicator cityViewpagerDots;


    MaterialCardView cityUpdBannerSmallParent, cityUpdBannerLargeParent;
    ImageView imgCityUpdBannerSmall, imgCityUpdBannerLarge;

    LinearLayoutCompat cityProfessionalTagParent, bestRatedTagParent,mostSearchedTagParent,featuredTagParent,recentlyViewedTagParent;


    //recyclerViews instances
    private RecyclerView recyclerMostSearchedLoc;
    private RecyclerView recyclerBestRatedLoc;
    private RecyclerView recyclerRecentlyViewedLoc;
    private RecyclerView recyclerCityProfessionals;




    //Adapters instances
    private MostSearchedLocationAdapter mostSearchedLocationAdapter;
    private BestRatedLocAdapter bestRatedLocAdapter;
    private RecentlyViewedLocAdapter recentlyViewedLocAdapter;
    private CityProfessionalsAdapter cityProfessionalsAdapter;


    //TextViews
    private TextView txtRecentlyViewedHeader;


    //Buttons
    Button btnFeaturedLocViewAll, btnRecentlyViewedViewAll, btnBestRatedViewAll, btnCityProfessionalViewAll;


    ExtendedFloatingActionButton floatBtnNearMeSearch;


    ScrollView homePageScrollView;






    public HomeMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeMainFragment newInstance(String param1, String param2) {
        HomeMainFragment fragment = new HomeMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("FragmentCheck", "onCreate: ");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        handler = new Handler();
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("FragmentCheck", "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_main, container, false);
        recyclerHomePageCategories = view.findViewById(R.id.recycler_home_page_categories);
        cityUpdateViewPager = view.findViewById(R.id.city_update_view_pager);
        cityViewpagerDots = view.findViewById(R.id.city_view_pager_dots);
        cityUpdBannerLargeParent = view.findViewById(R.id.city_upd_banner_large_parent);
        cityUpdBannerSmallParent = view.findViewById(R.id.city_upd_banner_small_parent);
        imgCityUpdBannerSmall = view.findViewById(R.id.img_city_upd_banner_small);
        imgCityUpdBannerLarge = view.findViewById(R.id.img_city_upd_banner_large);
        recyclerFeaturedLoc = view.findViewById(R.id.recycler_featured_loc);
        //hooks
        recyclerMostSearchedLoc = view.findViewById(R.id.recycler_most_searched_loc);
        recyclerBestRatedLoc = view.findViewById(R.id.recycler_best_rated_loc);
        recyclerRecentlyViewedLoc = view.findViewById(R.id.recycler_recently_viewed);
        recyclerCityProfessionals = view.findViewById(R.id.recycler_city_professional);
        recentlyViewedTagParent = view.findViewById(R.id.recently_viewed_tag_parent);


        btnFeaturedLocViewAll = view.findViewById(R.id.btn_featured_loc_view_all);
        btnRecentlyViewedViewAll = view.findViewById(R.id.btn_recently_viewed_view_all);
        btnBestRatedViewAll = view.findViewById(R.id.btn_best_rated_view_all);
        btnCityProfessionalViewAll = view.findViewById(R.id.btn_city_professional_view_all);
        txtRecentlyViewedHeader = view.findViewById(R.id.txt_recently_viewed_header);
        homePageScrollView = view.findViewById(R.id.home_page_scroll_view);

        floatBtnNearMeSearch = view.findViewById(R.id.floatBtnNearMeSearch);


        //view parent hooks
        cityProfessionalTagParent = view.findViewById(R.id.city_professionals_tag_parent);
        bestRatedTagParent = view.findViewById(R.id.best_rated_tag_parent);
        mostSearchedTagParent = view.findViewById(R.id.most_searched_tag_parent);
        featuredTagParent = view.findViewById(R.id.featured_loc_tag_parent);




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("FragmentCheck", "onViewCreated: ");

        recyclerHomePageCategories.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        cityUpdatesAdapter = new CityUpdatesAdapter(new DiffUtilsCityUpdateModelClass(), viewModel.getCurrentUserId());
        recyclerFeaturedLoc.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerMostSearchedLoc.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        //recyclerBestRatedLoc.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerBestRatedLoc.setLayoutManager(new GridLayoutManager(requireActivity(),2));

        recyclerCityProfessionals.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerRecentlyViewedLoc.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));


        homepageCategoriesAdapter = new HomepageCategoriesAdapter(new DiffUtilsAllCategoriesClass());
        featuredLocationAdapter = new FeaturedLocationAdapter(new DiffUtilsFeaturedLocationClass());
        //init adapters
        mostSearchedLocationAdapter = new MostSearchedLocationAdapter(new DiffUtilsMostSearchedClass(), requireActivity());
        bestRatedLocAdapter = new BestRatedLocAdapter(new DiffUtilsBestRatedClass(), R.layout.best_rated_item);
        recentlyViewedLocAdapter = new RecentlyViewedLocAdapter(new DiffUtilsRecentlyViewedClass());
        cityProfessionalsAdapter = new CityProfessionalsAdapter(new DiffUtilsCityProfessionals());




        viewModel.getCategoriesLive(requireActivity().getApplicationContext()).observe(requireActivity(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                Collections.shuffle(categories);
                List<CategoriesModel> categoriesModels = new ArrayList<>();
                if (categories.size()>15){
                    for (int i =0;i<15;i++){
                        categoriesModels.add(categories.get(i));
                    }
                }else {
                    categoriesModels.addAll(categories);
                }
                homepageCategoriesAdapter.submitList(categoriesModels);
                recyclerHomePageCategories.setAdapter(homepageCategoriesAdapter);
                recyclerHomePageCategories.setVisibility(View.VISIBLE);
            } else {
                recyclerHomePageCategories.setVisibility(View.GONE);
            }
        });


        //livedata observers
        viewModel.getCityUpdateLiveResult(requireActivity().getApplicationContext()).observe(requireActivity(), cityUpdatesModels -> {


            if (cityUpdatesModels==null || cityUpdatesModels.isEmpty()){
                return;
            }
            List<CityUpdatesModel> cityUpdatesMain = new ArrayList<>();

            viewModel.setCityUpdateBannerSmall(null);
            viewModel.setCityUpdateBannerLarge(null);
            for (CityUpdatesModel model : cityUpdatesModels) {
                if (model.getDocumentType() == 1) {
                    cityUpdatesMain.add(model);
                }
                if (model.getDocumentType() == 2) {
                    viewModel.setCityUpdateBannerSmall(model);
                }
                if (model.getDocumentType() == 3) {
                    viewModel.setCityUpdateBannerLarge(model);
                }
            }
            if (!cityUpdatesMain.isEmpty()){
                cityUpdatesAdapter.submitList(cityUpdatesMain);
                cityUpdateViewPager.setAdapter(cityUpdatesAdapter);

                //init view pager design anim
                initViewPagerDesign(cityUpdatesMain.size());
                cityViewpagerDots.setViewPager2(cityUpdateViewPager);

            }else {
                cityUpdateViewPager.setVisibility(View.GONE);
                cityViewpagerDots.setVisibility(View.GONE);


            }


            if (viewModel.getCityUpdateBannerLarge() != null) {
                cityUpdBannerLargeParent.setVisibility(View.VISIBLE);
                GlideApp.with(this).load(viewModel.getCityUpdateBannerLarge().getImageUrl())
                        .thumbnail(GlideApp.with(this).load(viewModel.getCityUpdateBannerLarge().getImageUrl()).override(30)).timeout(20000)
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        cityUpdBannerLargeParent.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        cityUpdBannerLargeParent.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(imgCityUpdBannerLarge);
            } else {
                cityUpdBannerLargeParent.setVisibility(View.GONE);
            }

            if (viewModel.getCityUpdateBannerSmall() != null) {
                cityUpdBannerSmallParent.setVisibility(View.VISIBLE);

                GlideApp.with(this).load(viewModel.getCityUpdateBannerSmall().getImageUrl())
                        .thumbnail(GlideApp.with(this).load(viewModel.getCityUpdateBannerSmall().getImageUrl()).override(30))
                        .timeout(20000).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        cityUpdBannerSmallParent.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        cityUpdBannerSmallParent.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(imgCityUpdBannerSmall);
            } else {
                cityUpdBannerSmallParent.setVisibility(View.GONE);
            }

        });

        cityUpdateViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    // User has dragged
                    if (handler!=null){
                        handler.removeCallbacks(timeCounter);

                    }


                }
                if (state == ViewPager2.SCROLL_STATE_IDLE){
                    if (handler!=null){
                        handler.postDelayed(timeCounter,VIEW_TIME_LIMIT);

                    }


                }
            }
        });
        viewModel.getRecentlyViewedLocLiveResult().observe(requireActivity(), recentlyViewedLocations -> {
            if (recentlyViewedLocations != null && !recentlyViewedLocations.isEmpty()) {
                viewModel.getRecentlyViewedDetailedLive(recentlyViewedLocations).observe(requireActivity(), recentlyViewedDetailedModels -> {
                    if (recentlyViewedDetailedModels != null && !recentlyViewedDetailedModels.isEmpty()) {


                        recentlyViewedLocAdapter.submitList(recentlyViewedDetailedModels);
                        recyclerRecentlyViewedLoc.setAdapter(recentlyViewedLocAdapter);
                        recyclerRecentlyViewedLoc.setVisibility(View.VISIBLE);
                        txtRecentlyViewedHeader.setVisibility(View.VISIBLE);
                        btnRecentlyViewedViewAll.setVisibility(View.VISIBLE);
                        recentlyViewedTagParent.setVisibility(View.VISIBLE);
                    }
                });

            } else {
                recyclerRecentlyViewedLoc.setVisibility(View.GONE);
                txtRecentlyViewedHeader.setVisibility(View.GONE);
                btnRecentlyViewedViewAll.setVisibility(View.GONE);
                recentlyViewedTagParent.setVisibility(View.GONE);
            }
        });
        viewModel.getFeaturedLocLiveResult().observe(requireActivity(), featuredLocations -> {
            if (featuredLocations != null && !featuredLocations.isEmpty()) {
                featuredLocationAdapter.submitList(featuredLocations);
                recyclerFeaturedLoc.setAdapter(featuredLocationAdapter);
                featuredTagParent.setVisibility(View.VISIBLE);
            } else {
                featuredTagParent.setVisibility(View.GONE);
            }
        });


        viewModel.getMostSearchedLocLiveResult().observe(requireActivity(), mostSearchedLocations -> {
            if (mostSearchedLocations != null && !mostSearchedLocations.isEmpty()) {
                mostSearchedLocationAdapter.submitList(mostSearchedLocations);
                recyclerMostSearchedLoc.setAdapter(mostSearchedLocationAdapter);

                mostSearchedTagParent.setVisibility(View.VISIBLE);
            } else {
                mostSearchedTagParent.setVisibility(View.GONE);
            }
        });


        viewModel.getBestRatedLocLiveResult().observe(requireActivity(), bestRatedLocations -> {
            if (bestRatedLocations != null && !bestRatedLocations.isEmpty()) {
                bestRatedLocAdapter.submitList(bestRatedLocations);
                recyclerBestRatedLoc.setAdapter(bestRatedLocAdapter);
                bestRatedTagParent.setVisibility(View.VISIBLE);
            } else {
                bestRatedTagParent.setVisibility(View.GONE);
            }
        });

        viewModel.getCityProfessionalsLive().observe(requireActivity(), cityProfessionalsModels -> {
            if (cityProfessionalsModels != null && !cityProfessionalsModels.isEmpty()) {
                cityProfessionalsAdapter.submitList(cityProfessionalsModels);
                recyclerCityProfessionals.setAdapter(cityProfessionalsAdapter);
                cityProfessionalTagParent.setVisibility(View.VISIBLE);
            } else {
                cityProfessionalTagParent.setVisibility(View.GONE);
            }
        });





        homePageScrollView.setOnScrollChangeListener((view1, i, i1, i2, i3) -> {
            if (i1 > 60) {
                floatBtnNearMeSearch.shrink();
            } else{
                floatBtnNearMeSearch.extend();
            }
        });



        animateViewPager();

        floatBtnNearMeSearch.setOnClickListener(this);
        btnFeaturedLocViewAll.setOnClickListener(this);
        btnRecentlyViewedViewAll.setOnClickListener(this);
        btnBestRatedViewAll.setOnClickListener(this);
        btnCityProfessionalViewAll.setOnClickListener(this);
        imgCityUpdBannerLarge.setOnClickListener(this);
        imgCityUpdBannerSmall.setOnClickListener(this);
    }


    private void animateViewPager() {

        timeCounter  = () -> {
            int listCount = cityUpdatesAdapter.getItemCount();
            int currentItem = cityUpdateViewPager.getCurrentItem();
            if (listCount<=0){
                return;
            }
            if (currentItem<(listCount-1)){
                if (cityUpdateViewPager.getVisibility()==View.VISIBLE){
                    cityUpdateViewPager.setCurrentItem(currentItem +1);
                }
            }else if (currentItem==(listCount-1)){
                if (cityUpdateViewPager.getVisibility()==View.VISIBLE){
                    cityUpdateViewPager.setCurrentItem(0);
                }

            }

        };



    }


    private void initViewPagerDesign(int offScreenLimit) {
        cityUpdateViewPager.setClipToPadding(false);
        cityUpdateViewPager.setClipChildren(false);
        cityUpdateViewPager.setOffscreenPageLimit(offScreenLimit);
        cityUpdateViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        cityUpdateViewPager.setPageTransformer(compositePageTransformer);

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(timeCounter,VIEW_TIME_LIMIT);

        Log.i("FragmentCheck", "onResume: ");



    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(timeCounter);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.floatBtnNearMeSearch) {
            Intent nearMeSearchIntent = new Intent(requireActivity(), NearMeSearchActivity.class);
            startActivity(nearMeSearchIntent);
        }
        if (viewId == R.id.btn_featured_loc_view_all) {
            Intent featuredPageIntent = new Intent(requireActivity(), FeaturedLocationsDetailPage.class);
            startActivity(featuredPageIntent);
        }
        if (viewId == R.id.btn_recently_viewed_view_all) {
            Intent recentPageIntent = new Intent(requireActivity(), RecentlyViewedDetailPage.class);
            startActivity(recentPageIntent);
        }
        if (viewId == R.id.btn_best_rated_view_all) {
            Intent bestRatedPageIntent = new Intent(requireActivity(), BestRatedDetailPage.class);
            startActivity(bestRatedPageIntent);
        }
        if (viewId == R.id.btn_city_professional_view_all) {
            Intent cityProfIntent = new Intent(requireActivity(), ProfessionalsDetailPage.class);
            startActivity(cityProfIntent);
        }
        if (viewId == R.id.img_city_upd_banner_small){
            CityUpdatesModel model = viewModel.getCityUpdateBannerSmall();
            if (model!=null){
                String websiteSmall = model.getWebsite();
                if (websiteSmall!=null && !websiteSmall.equals("")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(websiteSmall));
                    startActivity(i);
                }
            }

        }
        if (viewId==R.id.img_city_upd_banner_large){
            CityUpdatesModel model = viewModel.getCityUpdateBannerLarge();
            if (model!=null){
                String websiteSmall = model.getWebsite();
                if (websiteSmall!=null && !websiteSmall.equals("")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(websiteSmall));
                    startActivity(i);
                }
            }
        }
    }
}