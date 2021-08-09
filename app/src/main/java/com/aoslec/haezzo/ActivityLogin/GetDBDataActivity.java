package com.aoslec.haezzo.ActivityLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.aoslec.haezzo.Bean.UserListBean;
import com.aoslec.haezzo.MainActivity;
import com.aoslec.haezzo.NetworkTask.UserNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.aoslec.haezzo.SplashActivity;

import java.util.ArrayList;

public class  GetDBDataActivity extends AppCompatActivity {

    //DB에서 값을 가져와서 ShareVar에 넣는 코드.

    private String strUnumber = "";
    private String strNickname = "";
    private String strProfileImg = "";
    private String strEmail = "";
    private String strGender = "";
    private String strAgeRange = "";
    private String strAddress = "";

    ArrayList<UserListBean> userListBeans;
    String urlAddr = ShareVar.urlAddr + "userSelect.jsp?" + "uemail=" + ShareVar.strEmail;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_db_data);

        Log.v("GetDBData","GetDBDataActivity");

        connectGetData();

        Log.v("GetDBData", "get 직전");
        // 값 Bean에서 받아오기
        strUnumber = userListBeans.get(0).getUnumber();
        strNickname = userListBeans.get(0).getUnickname();
        strProfileImg = userListBeans.get(0).getUimage();
        strEmail = userListBeans.get(0).getUemail();
        strGender = userListBeans.get(0).getUfm();
        strAgeRange = userListBeans.get(0).getUage();
        strAddress = userListBeans.get(0).getUaddress();

        // 받은 값들 다시 ShaerVar에 넣기
        ShareVar.strUnumber = strUnumber;
        ShareVar.strNick = strNickname;
        ShareVar.strProfileImg = strProfileImg;
        ShareVar.strEmail = strEmail;
        ShareVar.strGender = strGender;
        ShareVar.strAgeRange = strAgeRange;
        ShareVar.strAddress = strAddress;

        // 로딩창 객체 생성
        progressDialog = new ProgressDialog(this);
        // 로딩창을 투명하게
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Log.v("GetDBData","Handler 직전");
        Handler hand = new Handler();

        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
               // progressDialog.show();

                Intent intent = new Intent(GetDBDataActivity.this, MainActivity.class);
                startActivity(intent);
                GetDBDataActivity.this.finish();
            }
        }, 1000);  //postDelayed

    } // onCreate

    private void connectGetData(){
        //검색하는 화면
        try {
            UserNetworkTask userNetworkTask = new UserNetworkTask(GetDBDataActivity.this, urlAddr,"select");
            //networktask 가 구동이 되서

            Object obj = userNetworkTask.execute().get();//프로그레스바 돌고, 데이터 가지러가고

            userListBeans = (ArrayList<UserListBean>) obj;
            Log.v("GetDBData", "GetDBdata Bean 완성!");

        }catch(Exception e){
            e.printStackTrace();
        }

    }//connectGetdata


}