package com.tunisianfood_advisor.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.adapter.PlaceAdapter;
import com.tunisianfood_advisor.model.Place;

import java.util.HashMap;

public class PlacesListActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener{


    private RelativeLayout mBack;
    private RecyclerView mRecyclerView;
    private PlaceAdapter mPlaceAdapter;
    private TextView mTitle;

    public static String ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME";
    private String categoryName;
    private String Username = null;
    public String categoryName1;
    public String UserFav;
    // Session Manager Class
    SessionManager session;
    public String Search=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);


        Intent i = getIntent();
        categoryName1 = i.getStringExtra(ARG_CATEGORY_NAME);


        session = new SessionManager(getApplicationContext());

        session.checkLogin();


        HashMap<String, String> user1 = session.getUserDetails();
        UserFav = user1.get(SessionManager.KEY_NAME);


        initialize();
        getArgs();
        setupWidgets();
    }


    private void initialize() {
        mBack = findViewById(R.id.back);
        mRecyclerView = findViewById(R.id.recycler_view);
        mTitle = findViewById(R.id.toolbar_title);
    }

    private void getArgs() {
        Intent i = getIntent();
        categoryName = i.getStringExtra(ARG_CATEGORY_NAME);
        if (categoryName.equals("")) {
            mTitle.setText(getResources().getString(R.string.places_list_toolbar));
        } else {
            mTitle.setText(categoryName.toUpperCase());
        }
    }

    private void setupWidgets() {
        //change status bar color to transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.headerColor));
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //setup product recycler view
//        GridLayoutManager llmProduct = new GridLayoutManager(this, 2);
        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llmPlace);
        mPlaceAdapter = new PlaceAdapter(this, categoryName1,Username,UserFav,Search);
        mRecyclerView.setAdapter(mPlaceAdapter);
    }

    @Override
    public void onPlaceClickListener(Place place) {
        Intent i = new Intent(PlacesListActivity.this, PlaceActivity.class);
        i.putExtra("ARG_PLACE_ID", Integer.toString(place.getPlaceId()));
        startActivity(i);
    }

    @Override
    public void onPlaceFavoriteClick(Place place) {
        mPlaceAdapter.setFavorite(place.getPlaceId());
        mPlaceAdapter.notifyDataSetChanged();
    }
}
