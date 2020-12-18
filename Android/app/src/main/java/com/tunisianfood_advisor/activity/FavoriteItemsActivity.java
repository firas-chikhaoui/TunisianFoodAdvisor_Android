package com.tunisianfood_advisor.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.tunisianfood_advisor.R;
import com.tunisianfood_advisor.adapter.ProductAdapter;
import com.tunisianfood_advisor.model.Product;

import java.util.HashMap;

public class FavoriteItemsActivity extends AppCompatActivity implements

        ProductAdapter.OnProductClickListener{

    private RelativeLayout mBack;
    private RecyclerView mRecyclerView;
    public String Username;
    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_items);


        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        Username = user.get(SessionManager.KEY_NAME);

        Log.d("TAG","session : "+Username);

        initialize();
        setupWidgets();
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
        GridLayoutManager llmProduct = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(llmProduct);
        ProductAdapter mProductAdapter = new ProductAdapter(this,Username);
        mRecyclerView.setAdapter(mProductAdapter);
    }

    @Override
    public void OnProductNoFavoriteClick(Product products) {

    }

    @Override
    public void OnProductFavoriteClick(Product products) {

    }
}
