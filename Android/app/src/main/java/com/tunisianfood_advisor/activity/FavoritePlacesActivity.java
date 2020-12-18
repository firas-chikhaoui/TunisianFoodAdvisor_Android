package com.tunisianfood_advisor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.Utils.BottomNavigationViewHelper;
import com.tunisianfood_advisor.adapter.PlaceAdapter;
import com.tunisianfood_advisor.model.Place;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;

public class FavoritePlacesActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener{

    private RelativeLayout mBack;
    private RecyclerView mRecyclerView;
    private PlaceAdapter mPlaceAdapter;
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = FavoritePlacesActivity.this;
    public String categoryName;
    public String UserFav;
    public String Username;
    // Session Manager Class
    SessionManager session;
    public String Search=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);


        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        Username = user.get(SessionManager.KEY_NAME);

        HashMap<String, String> user1 = session.getUserDetails();
        UserFav = user1.get(SessionManager.KEY_NAME);

        initialize();
        setupWidgets();
        setupBottomNavigationView();
    }

    private void initialize() {
        mBack = findViewById(R.id.back);
        mRecyclerView = findViewById(R.id.recycler_view);
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
        mPlaceAdapter = new PlaceAdapter(this,categoryName,Username,UserFav,Search);
        mRecyclerView.setAdapter(mPlaceAdapter);
    }

    //    BottomNavigationView setup
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onPlaceClickListener(Place place) {
        Intent i = new Intent(FavoritePlacesActivity.this, PlaceActivity.class);
        i.putExtra("ARG_PLACE_ID", Integer.toString(place.getPlaceId()));
        startActivity(i);
    }

    @Override
    public void onPlaceFavoriteClick(Place place) {
        mPlaceAdapter.setFavorite(place.getPlaceId());
        mPlaceAdapter.notifyDataSetChanged();
    }
}
