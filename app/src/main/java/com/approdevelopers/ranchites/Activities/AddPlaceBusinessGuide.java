package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.approdevelopers.ranchites.R;

public class AddPlaceBusinessGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_business_guide);

        Toolbar toolbar = findViewById(R.id.toolbar_add_place_guide);
        toolbar.setTitle("Add Place/Business Guide");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }
}