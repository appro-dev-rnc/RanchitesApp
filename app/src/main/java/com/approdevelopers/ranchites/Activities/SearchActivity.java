package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.approdevelopers.ranchites.Adapters.DiffUtilsRecentlyViewedClass;
import com.approdevelopers.ranchites.Adapters.DiffUtilsSearchClass;
import com.approdevelopers.ranchites.Adapters.RecentSearchesKeywordAdapter;
import com.approdevelopers.ranchites.Adapters.RecentlyViewedLocAdapter;
import com.approdevelopers.ranchites.Adapters.SearchResultsAdapter;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Interfaces.RecentSearchItemClickInterface;
import com.approdevelopers.ranchites.Interfaces.RecentSearchItemOnLongClickInterface;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.SearchActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements RecentSearchItemClickInterface, RecentSearchItemOnLongClickInterface, NetworkConnection.ReceiverListener {

    RecyclerView recyclerSearchResults,recyclerRecentlyViewed,recyclerRecentSearches;
    TextInputLayout textLayoutSearchKeyword;

    SearchResultsAdapter searchResultsAdapter;
    SearchActivityViewModel searchActivityViewModel;

    RecentlyViewedLocAdapter recentlyViewedLocAdapter;

    TextView textRecentSearches,txtRecentlyViewedTag;
    AppCompatAutoCompleteTextView autoCOmpleteSearchTV;
    RecentSearchesKeywordAdapter recentSearchesAdapter;
    String intentKeyword;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout searchActivityParentView;
    TextView txtSearchActNoResultFound;
    TextView txtSearchActSearchGuide;

    ArrayAdapter<String> categoriesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        networkConnection = new NetworkConnection();

        Intent intent = getIntent();
        if (intent!=null){
            intentKeyword = intent.getStringExtra("searchKeyword");
        }
        searchActivityViewModel = new ViewModelProvider(this).get(SearchActivityViewModel.class);
        //hooks
        recyclerSearchResults = findViewById(R.id.recyclerSearchResult);
        recyclerRecentlyViewed = findViewById(R.id.recycler_recently_viewed_search_page);
        recyclerRecentSearches = findViewById(R.id.recycler_recent_searches_keyword);
        textLayoutSearchKeyword = findViewById(R.id.textLayoutSearchKeyword);
        textRecentSearches = findViewById(R.id.textrecentsearches);
        txtRecentlyViewedTag = findViewById(R.id.textrecentlyviewedTag);
        searchActivityParentView = findViewById(R.id.search_page_parent_view);
        txtSearchActNoResultFound = findViewById(R.id.txt_search_act_no_item_found);
        txtSearchActSearchGuide = findViewById(R.id.txt_search_act_search_guide);
        autoCOmpleteSearchTV = findViewById(R.id.autoSearchKeyword);


        if (intentKeyword!=null){
            autoCOmpleteSearchTV.setText(intentKeyword);
            searchActivityViewModel.searchForResults(intentKeyword);
        }
        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerRecentlyViewed.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRecentSearches.setLayoutManager(new GridLayoutManager(this,3));


        Toolbar toolbar = findViewById(R.id.search_det_page_toolbar);
        setSupportActionBar(toolbar);


        searchResultsAdapter = new SearchResultsAdapter(new DiffUtilsSearchClass());
        recentlyViewedLocAdapter = new RecentlyViewedLocAdapter(new DiffUtilsRecentlyViewedClass());
        if (autoCOmpleteSearchTV.getText().toString().equals("")){
            txtSearchActSearchGuide.setVisibility(View.VISIBLE);
        }

        searchActivityViewModel.getSearchQueryKeyword().observe(this, s -> {

            if (!s.equals("")) {
                searchActivityViewModel.getSearchResultLiveData(s).observe(SearchActivity.this, searchResultModels -> {


                    if (searchResultModels != null && !searchResultModels.isEmpty()) {
                        searchResultsAdapter.submitList(searchResultModels);
                        recyclerSearchResults.setAdapter(searchResultsAdapter);
                        recyclerSearchResults.setVisibility(View.VISIBLE);
                        txtSearchActNoResultFound.setVisibility(View.GONE);
                        txtSearchActSearchGuide.setVisibility(View.GONE);

                    } else {
                        recyclerSearchResults.setVisibility(View.GONE);
                        if (autoCOmpleteSearchTV.getText().toString().equals("")) {
                            txtSearchActSearchGuide.setVisibility(View.VISIBLE);
                            txtSearchActNoResultFound.setVisibility(View.GONE);


                        } else {
                            txtSearchActNoResultFound.setVisibility(View.VISIBLE);
                            txtSearchActSearchGuide.setVisibility(View.GONE);


                        }
                    }

                });
            }else {
                recyclerSearchResults.setVisibility(View.GONE);
                txtSearchActSearchGuide.setVisibility(View.VISIBLE);
                txtSearchActNoResultFound.setVisibility(View.GONE);
            }

        });






        searchActivityViewModel.getRecentSearchesLive().observe(this, strings -> {
            if (strings!=null&& !strings.isEmpty()){
                Collections.reverse(strings);

                List<String> searchKeywords;
                if (strings.size()>10){
                    searchKeywords = strings.subList(0,10);
                }else {
                    searchKeywords = strings.subList(0,strings.size());
                }

                recentSearchesAdapter = new RecentSearchesKeywordAdapter(searchKeywords,SearchActivity.this,SearchActivity.this);
                recyclerRecentSearches.setAdapter(recentSearchesAdapter);
                recyclerRecentSearches.setVisibility(View.VISIBLE);
                textRecentSearches.setVisibility(View.VISIBLE);

            }else {
                recyclerRecentSearches.setVisibility(View.GONE);
                textRecentSearches.setVisibility(View.GONE);
            }
        });


        searchActivityViewModel.getSearchGuideKeywords().observe(this, strings -> {
            if (strings!=null && !strings.isEmpty()){
                categoriesAdapter = new ArrayAdapter<>(SearchActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, strings);
                autoCOmpleteSearchTV.setAdapter(categoriesAdapter);
            }
        });


        textLayoutSearchKeyword.setEndIconOnClickListener(view -> {

            String queryKeyword = autoCOmpleteSearchTV.getText().toString().trim();
            if (!queryKeyword.equals("")){
                searchActivityViewModel.searchForResults(queryKeyword);
                autoCOmpleteSearchTV.clearFocus();
                searchActivityViewModel.addToRecentSearches(queryKeyword);

            }else {
                autoCOmpleteSearchTV.setError("Empty Query");
            }
        });

        autoCOmpleteSearchTV.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String searchKeyword = textView.getText().toString().trim();

                if (!searchKeyword.equals("")){
                    searchActivityViewModel.searchForResults(searchKeyword);

                   autoCOmpleteSearchTV.clearFocus();
                    searchActivityViewModel.addToRecentSearches(searchKeyword);

                }else {
                    autoCOmpleteSearchTV.setError("Empty Query");
                }
                return true;
            }
            return false;
        });
        autoCOmpleteSearchTV.addTextChangedListener(myWatcher);

        autoCOmpleteSearchTV.setOnFocusChangeListener((view, b) -> {

            if (b){
                
                autoCOmpleteSearchTV.showDropDown();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        autoCOmpleteSearchTV.setOnItemClickListener((adapterView, view, i, l) -> {

            searchActivityViewModel.searchForResults((String) adapterView.getItemAtPosition(i));
            searchActivityViewModel.addToRecentSearches((String) adapterView.getItemAtPosition(i));
            Objects.requireNonNull(autoCOmpleteSearchTV).clearFocus();


        });


    }



    TextWatcher myWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          textLayoutSearchKeyword.setErrorEnabled(false);
          String query = charSequence.toString().trim();
          if (query.equals("")){
              txtSearchActSearchGuide.setVisibility(View.VISIBLE);
              txtSearchActNoResultFound.setVisibility(View.GONE);
              recyclerSearchResults.setVisibility(View.GONE);
          }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onRecentSearchClick(String keyword) {
        if (keyword!=null){
            autoCOmpleteSearchTV.setText(keyword);
            autoCOmpleteSearchTV.clearFocus();
            searchActivityViewModel.searchForResults(keyword);
            searchActivityViewModel.addToRecentSearches(keyword);
        }
    }

    @Override
    public void onRecentSearchLongClick(String keyword) {
        if (keyword!=null){

            new AlertDialog.Builder(this)
                    .setTitle("Delete ")
                    .setMessage("Are you sure you want to remove "+keyword+"from recent search keywords?" )

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Continue with delete operation
                        searchActivityViewModel.deleteFromRecentSearch(keyword);
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(SearchActivity.this,networkConnection,searchActivityParentView);
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

        if (isConnected){
            searchActivityViewModel.getRecentlyViewedLocLiveResult().observe(this, recentlyViewedLocations -> searchActivityViewModel.getRecentlyViewedDetailLive(recentlyViewedLocations).observe(SearchActivity.this, recentlyViewedDetailedModels -> {
                if (recentlyViewedDetailedModels!=null && !recentlyViewedDetailedModels.isEmpty()){
                    recentlyViewedLocAdapter.submitList(recentlyViewedDetailedModels);
                    recyclerRecentlyViewed.setAdapter(recentlyViewedLocAdapter);
                    recyclerRecentlyViewed.setVisibility(View.VISIBLE);
                    txtRecentlyViewedTag.setVisibility(View.VISIBLE);
                }else {
                    txtRecentlyViewedTag.setVisibility(View.GONE);
                    recyclerRecentlyViewed.setVisibility(View.GONE);
                }
            }));
        }else {
            txtRecentlyViewedTag.setVisibility(View.GONE);
            recyclerRecentlyViewed.setVisibility(View.GONE);
        }
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