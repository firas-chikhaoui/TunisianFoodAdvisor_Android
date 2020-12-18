package com.tunisianfood_advisor.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tunisianfood_advisor.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RelativeLayout  rellay1, rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1 = findViewById(R.id.rellay1);
            rellay2 = findViewById(R.id.rellay2);
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        initElements();
        initTabLayoutDatas();
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
    }

    private void initElements(){
        mTabLayout = (TabLayout)findViewById(R.id.activity_main_tab_layout);
        mViewPager = (ViewPager)findViewById(R.id.activity_main_view_pager);
    }

    private void initTabLayoutDatas(){
        mTitleList.add(getString(R.string.s_activity_main_login));
        mTitleList.add(getString(R.string.s_activity_main_register));

        mFragmentList.add(LoginFragment.newInstance());
        mFragmentList.add(RegisterFragment.newInstance());

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(fragmentManager,mFragmentList,mTitleList);
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"S'enregistrer d'abord !",Toast.LENGTH_SHORT).show();
    }
}
