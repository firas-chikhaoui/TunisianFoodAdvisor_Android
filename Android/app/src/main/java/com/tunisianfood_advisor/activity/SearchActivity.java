package com.tunisianfood_advisor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.Utils.BottomNavigationViewHelper;
import com.tunisianfood_advisor.adapter.PlaceAdapter;
import com.tunisianfood_advisor.model.Place;
import com.tunisianfood_advisor.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener {
    private PlaceAdapter mPlaceAdapter;
    private RecyclerView mPlaceRecycler;
    private TextView mPlaceList;
    private RelativeLayout btnsearch;
    private TextView text_search;
    private TextView edtEditText;
    public String categoryName1;
    public String Username = null;
    public String hisuser;
    public String UserFav;
    public String Search = "Search";

    // Session Manager Class
    SessionManager session;


    private static final int ACTIVITY_NUM = 1;
    private Context mContext = SearchActivity.this;

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        session = new SessionManager(getApplicationContext());

        session.checkLogin();


        HashMap<String, String> user = session.getUserDetails();
        UserFav = user.get(SessionManager.KEY_NAME);


        OkHttpClient client = new OkHttpClient();

        String url = "http://192.168.1.6:4000/gethisSearchAllRest/"+UserFav;

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

                    // Parse and get server response text data.
                    Gson gson = new Gson();
                    Type UserType = new TypeToken<ArrayList<User>>() {
                    }.getType();
                    List<User> UserList = gson.fromJson(response.body().string(), UserType);


                    String his = UserList.get(0).getHistorique();


                    Log.d("his", "val " +his);
                    if (his.equals("null")) {
                        text_search = (TextView) findViewById(R.id.search_title_text);
                        String his_ = "Résultats du recherche:";
                        text_search.setText(his_);
                    }else {
                        text_search = (TextView) findViewById(R.id.search_title_text);
                        String his_ = "Résultats du recherche: (" + his + ")";
                        text_search.setText(his_);
                    }

                }
            }
        });


        btnsearch = (RelativeLayout) findViewById(R.id.btn_search);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEditText = (TextView) findViewById(R.id.search_text);

                // update user historique
                hisuser = edtEditText.getText().toString();

                String url = "http://192.168.1.6:4000/sethisuser/" + hisuser + "/" + UserFav;

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


                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });

        initComponents();
        setupWidgets();
        setupBottomNavigationView();


    }


    private void initComponents() {
        mPlaceRecycler = findViewById(R.id.place_recycler_view);
    }


    private void setupWidgets() {

        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlaceRecycler.setLayoutManager(llmPlace);
        mPlaceAdapter = new PlaceAdapter(this, categoryName1, Username, UserFav, Search);
        mPlaceRecycler.setAdapter(mPlaceAdapter);


    }


    //    BottomNavigationView setup
    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onPlaceClickListener(Place place) {
        Intent i = new Intent(SearchActivity.this, PlaceActivity.class);
        i.putExtra("ARG_PLACE_ID", Integer.toString(place.getPlaceId()));
        startActivity(i);
    }

    @Override
    public void onPlaceFavoriteClick(Place place) {
        mPlaceAdapter.setFavorite(place.getPlaceId());
        mPlaceAdapter.notifyDataSetChanged();

    }
}
