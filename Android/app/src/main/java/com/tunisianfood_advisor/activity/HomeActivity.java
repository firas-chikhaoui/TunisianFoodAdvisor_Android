package com.tunisianfood_advisor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.Utils.BottomNavigationViewHelper;
import com.tunisianfood_advisor.adapter.HomeCategoriesAdapter;
import com.tunisianfood_advisor.adapter.PlaceAdapter;
import com.tunisianfood_advisor.model.Category;
import com.tunisianfood_advisor.model.Place;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.tunisianfood_advisor.activity.PlacesListActivity.ARG_CATEGORY_NAME;

public class HomeActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener,
        HomeCategoriesAdapter.OnCategoryClickListener {

    private RecyclerView mCategoryRecycler, mPlaceRecycler;
    private HomeCategoriesAdapter mCategoriesAdapter;
    private PlaceAdapter mPlaceAdapter;
    private TextView mPlaceList, mCategoriesList,bjr;
    private ImageView quitter;
    private LinearLayout searchblock;
    public String categoryName1;
    public String Username=null;
    public String account;
    public String User;
    public String UserFav;
    // Session Manager Class
    SessionManager session;
    SessionManager sessionQ;
    public String Search=null;
    public String hisuser;

    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity.this;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        User = user.get(SessionManager.KEY_NAME);

        HashMap<String, String> user1 = session.getUserDetails();
        UserFav = user1.get(SessionManager.KEY_NAME);


        bjr = (TextView) findViewById(R.id.browse);
        bjr.setText("Bienvenue, "+User);


        quitter = (ImageView) findViewById(R.id.quitter);

        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpClient client = new OkHttpClient();

                // update user historique
                hisuser = null;

                String  url = "http://192.168.1.6:4000/sethisuser/"+hisuser+"/"+UserFav;

                Request request_ = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request_).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {



                        }
                    }
                });


                sessionQ = new SessionManager(getApplicationContext());
                sessionQ.logoutUser();
            }
        });

        searchblock = (LinearLayout) findViewById(R.id.search_block);
        searchblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });


        initComponents();
        setupWidgets();
        setupBottomNavigationView();
    }

    private void initComponents() {
        mCategoryRecycler = findViewById(R.id.trending_recycler_view);
        mPlaceRecycler = findViewById(R.id.place_recycler_view);
        mCategoriesList = findViewById(R.id.categories_list);
    }

    private void setupWidgets() {

        LinearLayoutManager llmTrending = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCategoryRecycler.setLayoutManager(llmTrending);
        mCategoriesAdapter = new HomeCategoriesAdapter(this);
        mCategoryRecycler.setAdapter(mCategoriesAdapter);

        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlaceRecycler.setLayoutManager(llmPlace);
        mPlaceAdapter = new PlaceAdapter(this,categoryName1,Username,UserFav,Search);
        mPlaceRecycler.setAdapter(mPlaceAdapter);


        mCategoriesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CategoriesListActivity.class));
            }
        });
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
    public void onCategoryClickListener(Category category) {
        Intent i = new Intent(HomeActivity.this, PlacesListActivity.class);
        i.putExtra(ARG_CATEGORY_NAME, category.getCategoryName());
        startActivity(i);
    }

    @Override
    public void onPlaceClickListener(Place place) {
        Intent i = new Intent(HomeActivity.this, PlaceActivity.class);
        i.putExtra("ARG_PLACE_ID", Integer.toString(place.getPlaceId()));
        startActivity(i);
    }

    @Override
    public void onPlaceFavoriteClick(Place place) {
        mPlaceAdapter.setFavorite(place.getPlaceId());
        mPlaceAdapter.notifyDataSetChanged();

    }
}
