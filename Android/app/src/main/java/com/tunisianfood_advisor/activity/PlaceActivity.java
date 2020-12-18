package com.tunisianfood_advisor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.Utils.AndroidUtil;
import com.tunisianfood_advisor.adapter.ItemAdapter;
import com.tunisianfood_advisor.adapter.PlaceFeatureAdapter;
import com.tunisianfood_advisor.model.Avis;
import com.tunisianfood_advisor.model.Item;
import com.tunisianfood_advisor.model.Place;
import com.tunisianfood_advisor.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PlaceActivity extends AppCompatActivity implements PlaceFeatureAdapter.OnPlaceClickListener,
        ItemAdapter.onClickListener {

    private Context mContext = PlaceActivity.this;
    private static final String TAG = "PlaceActivity";
    private String placeName;

    private RelativeLayout mBack;

    private RecyclerView mProductRecycler, recyclerView1;
    private ItemAdapter mPlaceAdapter;
    private TextView titreplace;
    private TextView adressplace;
    private TextView deliveryplace;
    private ImageView icFavorite;
    private TextView avis;
    private RatingBar place_rating;
    private Button btnR1,btnR2,btnR3,btnR4,btnR5;
    public String UserR;
    // Session Manager Class
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Intent i = getIntent();
        placeName = i.getStringExtra("ARG_PLACE_ID");


        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String, String> userR = session.getUserDetails();
        UserR = userR.get(SessionManager.KEY_NAME);


     OkHttpClient client = new OkHttpClient();

        String  url = "http://192.168.1.6:4000/getRestwithId/"+placeName+"/"+UserR;

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse and get server response text data.
                    Gson gson = new Gson();
                    Type restType = new TypeToken<ArrayList<Place>>(){}.getType();
                    List<Place> restList = gson.fromJson(response.body().string(), restType);

                    titreplace = (TextView) findViewById(R.id.titreplace);
                    titreplace.setText(restList.get(0).getPlaceName());

                    adressplace = (TextView) findViewById(R.id.adressplace);
                    adressplace.setText(restList.get(0).getLocation());

                    deliveryplace = (TextView) findViewById(R.id.deliveryplace);
                    deliveryplace.setText(restList.get(0).getDelivery());


                    place_rating = (RatingBar) findViewById(R.id.place_rating);
                    place_rating.setRating(Float.parseFloat(restList.get(0).getRating()));


                    icFavorite = (ImageView) findViewById(R.id.ic_favorite);


                    if (restList.get(0).isFavorite() == 1) {
                        icFavorite.setImageResource(R.drawable.star);
                    } else {
                        icFavorite.setImageResource(R.drawable.star2);
                    }

                }
            }
        });


        String  url_ = "http://192.168.1.6:4000/getCountAvisWithId/"+placeName;

        Request request_ = new Request.Builder()
                .url(url_)
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
                    Type avisType = new TypeToken<ArrayList<Avis>>(){}.getType();
                    List<Avis> avisList = gson.fromJson(response.body().string(), avisType);


                    avis = (TextView) findViewById(R.id.avis);
                    String avis_ ="( "+avisList.get(0).getAvis()+" avis )";
                    avis.setText(avis_);

                }
            }
        });


        /// Rating 1 2 3 4 5

        btnR1 = findViewById(R.id.btn_R1);
        btnR2 = findViewById(R.id.btn_R2);
        btnR3 = findViewById(R.id.btn_R3);
        btnR4 = findViewById(R.id.btn_R4);
        btnR5 = findViewById(R.id.btn_R5);

        btnR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_R = "http://192.168.1.6:4000/setRatingUserRestau/";
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("user", UserR)
                        .add("restau",placeName)
                        .add("note","1")
                        .build();
                Request requestR = new Request.Builder()
                        .url(url_R)
                        .post(requestBody)
                        .build();


                okHttpClient.newCall(requestR).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONArray jsonArray = null;
                        try {
                            String state = "";
                            jsonArray = new JSONArray(s);
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("STATE");
                            }
                            if (state.equals("success")){

                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Vous avez vote 1 ",Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition( 0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition( 0, 0);
                                            }
                                        });
                                    }
                                };
                                thread.start();

                            }else {
                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"échec Rating",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponseRating: " + s);
                    }
                });

            }
        });
        btnR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_R = "http://192.168.1.6:4000/setRatingUserRestau/";
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("user", UserR)
                        .add("restau",placeName)
                        .add("note","2")
                        .build();
                Request requestR = new Request.Builder()
                        .url(url_R)
                        .post(requestBody)
                        .build();


                okHttpClient.newCall(requestR).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONArray jsonArray = null;
                        try {
                            String state = "";
                            jsonArray = new JSONArray(s);
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("STATE");
                            }
                            if (state.equals("success")){

                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Vous avez vote 2 ",Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition( 0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition( 0, 0);
                                            }
                                        });
                                    }
                                };
                                thread.start();

                            }else {
                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"échec Rating",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponseRating: " + s);
                    }
                });

            }
        });
        btnR3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_R = "http://192.168.1.6:4000/setRatingUserRestau/";
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("user", UserR)
                        .add("restau",placeName)
                        .add("note","3")
                        .build();
                Request requestR = new Request.Builder()
                        .url(url_R)
                        .post(requestBody)
                        .build();


                okHttpClient.newCall(requestR).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONArray jsonArray = null;
                        try {
                            String state = "";
                            jsonArray = new JSONArray(s);
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("STATE");
                            }
                            if (state.equals("success")){

                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Vous avez vote 3 ",Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition( 0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition( 0, 0);
                                            }
                                        });
                                    }
                                };
                                thread.start();

                            }else {
                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"échec Rating",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponseRating: " + s);
                    }
                });

            }
        });
        btnR4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_R = "http://192.168.1.6:4000/setRatingUserRestau/";
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("user", UserR)
                        .add("restau",placeName)
                        .add("note","4")
                        .build();
                Request requestR = new Request.Builder()
                        .url(url_R)
                        .post(requestBody)
                        .build();


                okHttpClient.newCall(requestR).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONArray jsonArray = null;
                        try {
                            String state = "";
                            jsonArray = new JSONArray(s);
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("STATE");
                            }
                            if (state.equals("success")){

                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Vous avez vote 4 ",Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition( 0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition( 0, 0);
                                            }
                                        });
                                    }
                                };
                                thread.start();

                            }else {
                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"échec Rating",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponseRating: " + s);
                    }
                });

            }
        });
        btnR5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_R = "http://192.168.1.6:4000/setRatingUserRestau/";
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("user", UserR)
                        .add("restau",placeName)
                        .add("note","5")
                        .build();
                Request requestR = new Request.Builder()
                        .url(url_R)
                        .post(requestBody)
                        .build();


                okHttpClient.newCall(requestR).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        JSONArray jsonArray = null;
                        try {
                            String state = "";
                            jsonArray = new JSONArray(s);
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("STATE");
                            }
                            if (state.equals("success")){

                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Vous avez vote 5 ",Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition( 0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition( 0, 0);
                                            }
                                        });
                                    }
                                };
                                thread.start();

                            }else {
                                Thread thread = new Thread(){
                                    public void run(){
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"échec Rating",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                };
                                thread.start();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponseRating: " + s);
                    }
                });

            }
        });


        initComponents();
        setupWidgets();
    }

    private void initComponents() {
        mBack = findViewById(R.id.back);
        mProductRecycler = findViewById(R.id.feature_recycler);
        recyclerView1 = findViewById(R.id.recycler);
        recyclerView1.setVisibility(View.VISIBLE);

    }

    private void setupWidgets() {
        Window window = getWindow();
        AndroidUtil.statusBarColorTransparent(window);

        //setup product recycler view
        LinearLayoutManager llmProduct = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mProductRecycler.setLayoutManager(llmProduct);
        PlaceFeatureAdapter mProductAdapter1 = new PlaceFeatureAdapter(this,placeName);
        mProductRecycler.setAdapter(mProductAdapter1);

        LinearLayoutManager lnl1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(lnl1);
        mPlaceAdapter = new ItemAdapter(this,placeName);
        recyclerView1.setAdapter(mPlaceAdapter);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void OnPlaceNoFavoriteClick(Product products) {

    }

    @Override
    public void OnPlaceFavoriteClick(Product products) {

    }

    @Override
    public void onTrendingClickListener(Product product) {
        Intent i = new Intent(PlaceActivity.this, ItemDetailsActivity.class);
        i.putExtra("ARG_PROD_NAME", product.getmProductName());
        i.putExtra("ARG_PROD_DESC", product.getmProductDescription());
        i.putExtra("ARG_PROD_PRIX", product.getmProductValue());
        startActivity(i);
    }

    @Override
    public void onClickListener(Item item) {
        Intent i = new Intent(PlaceActivity.this, ItemDetailsActivity.class);
        i.putExtra("ARG_PROD_NAME", item.getItemName());
        i.putExtra("ARG_PROD_DESC", item.getItemDesc());
        i.putExtra("ARG_PROD_PRIX", item.getItemPrice());
        startActivity(i);
    }
}
