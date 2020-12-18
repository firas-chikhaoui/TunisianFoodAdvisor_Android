package com.tunisianfood_advisor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.Utils.BottomNavigationViewHelper;
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

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout favoriteRestaurants;
    private TextView etiderprofil;

    private static final int ACTIVITY_NUM = 3;
    private Context mContext = ProfileActivity.this;
    private static final String TAG = "ProfileActivity";
    private TextView titreuser;
    private TextView teluser;
    public String UserR;
    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String, String> userR = session.getUserDetails();
        UserR = userR.get(SessionManager.KEY_NAME);


        OkHttpClient client = new OkHttpClient();

        String  url = "http://192.168.1.6:4000/getAllInfoUser/"+UserR;

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
                    Type userType = new TypeToken<ArrayList<User>>(){}.getType();
                    List<User> userList = gson.fromJson(response.body().string(), userType);

                    titreuser = (TextView) findViewById(R.id.search_text);
                    titreuser.setText(userList.get(0).getNickname());

                    teluser = (TextView) findViewById(R.id.profile_tel);
                    teluser.setText(userList.get(0).getPhoneNum());

                }
            }
        });





        setupWidgets();
        setupBottomNavigationView();

    }


    private void setupWidgets() {
        favoriteRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FavoritePlacesActivity.class);
                startActivity(i);
            }
        });

        etiderprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent i = new Intent(ProfileActivity.this, EditerProfilActivity.class);
        startActivity(i);
            }
        });
    }

    //    BottomNavigationView setup
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
