package com.aoslec.haezzo.ActivityUserHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoslec.haezzo.ActivityOnDealList.OnDealListActivity;
import com.aoslec.haezzo.ActivityLogin.KakaoLoginActivity;
import com.aoslec.haezzo.Bean.UserListBean;
import com.aoslec.haezzo.NetworkTask.UserNetworkTask;
import com.aoslec.haezzo.ActivityPayment.Pay1Activity;
import com.aoslec.haezzo.MainActivity;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

public class MypageActivity extends AppCompatActivity {
    Button button, button_fixmypage;
    BottomNavigationView main_bottomNavigationView;

    private String strNick = ShareVar.strNick;
    private String strProfileImg = ShareVar.strProfileImg;
    private String strEmail = ShareVar.strEmail;
    private String strGender = ShareVar.strGender;
    private String strAgeRange = ShareVar.strAgeRange;
    private String strAddress = ShareVar.strAddress;

    TextView tv_nickname, tv_gender, tv_agerange, tv_email, tv_address;
    ImageView iv_profileimg;


    String macIP = ShareVar.macIP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //네비게이션
        main_bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_bottom_navigation);
        main_bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tv_nickname = findViewById(R.id.tv_mypage_nickname);
        tv_gender = findViewById(R.id.tv_mypage_gender);
        tv_agerange = findViewById(R.id.tv_mypage_agerange);
        tv_email = findViewById(R.id.tv_mypage_email);
        tv_address = findViewById(R.id.tv_mypage_address);
        iv_profileimg = findViewById(R.id.iv_mypage_profileimg);

        // 마이페이지에 값 설정
        tv_nickname.setText(strNick);
        tv_email.setText(strEmail);
        tv_gender.setText(strGender);
        tv_agerange.setText(strAgeRange);
        tv_address.setText(strAddress);

        //Glide를 이용해 이미지 파일 불러와 프로필 사진으로 set
        Glide.with(this).load(strProfileImg).into(iv_profileimg);

        // 결제 버튼 (임시)
        button = findViewById(R.id.btn_payTest);
        button.setOnClickListener(onClickListener);

        // 내 정보 수정 버튼
        button_fixmypage = findViewById(R.id.btn_fixmypage);
        button_fixmypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, MypageFixActivity.class);
                startActivity(intent);
            }
        });


        //로그아웃 버튼
        findViewById(R.id.myPage_btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MypageActivity.this, KakaoLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        }); // logout

    } //onCreate

    @Override //*********중요!!!
    protected void onResume() {
        super.onResume();

    }



    //
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_payTest:
                    Intent intent2 = new Intent(MypageActivity.this, Pay1Activity.class);
                    startActivity(intent2);
                    break;
            }
        }
    };


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




} // MypageActivity