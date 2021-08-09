package com.aoslec.haezzo.ActivityOnDealList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.aoslec.haezzo.ActivityUserHelper.MypageActivity;
import com.aoslec.haezzo.Adapter.TabPagerAdapter;
import com.aoslec.haezzo.MainActivity;
import com.aoslec.haezzo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class OnDealListActivity extends AppCompatActivity {

    BottomNavigationView main_bottomNavigationView;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    TabPagerAdapter adapter;
    ImageView imageView;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("Message", "onCreate_onDealList");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_deal_list);

        //레이아웃, 뷰페이저 연결
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("진행중"));
        tabLayout.addTab(tabLayout.newTab().setText("거래완료"));

        viewPager = findViewById(R.id.pager);
        //페이지 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new TabPagerAdapter(fragmentManager,getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("Message", "onTabSelected");
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.v("Message", "onTabUnselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.v("Message", "onTabReselected");
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

//        viewPager.registerOnPageChangeCallback(new ViewPager.OnPageChangeCallback() {
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                tabLayout.selectTab(tabLayout.getTabAt(position));
//            }
//        });

        main_bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_bottom_navigation);
        main_bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_list:
                    startActivity(new Intent(getApplicationContext(), OnDealListActivity.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_mypage:
                    startActivity(new Intent(getApplicationContext(), MypageActivity.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        }
    };

}//-----