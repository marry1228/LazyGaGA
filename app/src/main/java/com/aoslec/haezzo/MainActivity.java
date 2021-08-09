package com.aoslec.haezzo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aoslec.haezzo.ActivityDocument.DocumentWriteActivity;
import com.aoslec.haezzo.ActivityDocument.HaezulgaeListActivity;
import com.aoslec.haezzo.ActivityHelperApply.HelperApplyIntroActivity;
import com.aoslec.haezzo.ActivityUserHelper.HelperListActivity;
import com.aoslec.haezzo.ActivityOnDealList.OnDealListActivity;
import com.aoslec.haezzo.ActivityUserHelper.MypageActivity;
import com.aoslec.haezzo.Adapter.HelperListAdapter;
import com.aoslec.haezzo.Bean.HelperBean;
import com.aoslec.haezzo.Bean.HelperListBean;
import com.aoslec.haezzo.Bean.UserListBean;
import com.aoslec.haezzo.NetworkTask.HelperCheckNetworkTask;
import com.aoslec.haezzo.NetworkTask.HelperNetworkTask;
import com.aoslec.haezzo.NetworkTask.UserNetworkTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String strNick = ShareVar.strNick;
    private String strProfileImg = ShareVar.strProfileImg;
    private String strEmail = ShareVar.strEmail;
    private String strGender = ShareVar.strGender;
    private String strAgeRange = ShareVar.strAgeRange;
    private String strAddress = ShareVar.strAddress;
    private String tmpAddress = ""; // substring을 이용해 생략된 주소

    private String addressNull = "";
    private TextView tv_main_address;

    // helper check
    private String unumber = ShareVar.strUnumber;
    private String urlAddr = ShareVar.urlAddr;
    private String tmpurlAddr ;
    private ArrayList<UserListBean> userListBeans;

    // --

    long first_time;
    long second_time;

    Button main_writeDocument;
    ImageButton main_btnHaezzo, main_btnHaezulgae;

    BottomNavigationView main_bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 메인 에서 주소 출력되는 부분
        tv_main_address = findViewById(R.id.tvbtn_main_address);
        tmpAddress = strAddress.substring(strAddress.indexOf("시 ")+1, strAddress.indexOf("동")+1);
        tv_main_address.setText(tmpAddress);
        // 버튼 클릭 이벤트 (아래에서 case로 나뉘어짐)
        tv_main_address.setOnClickListener(onClickListener);

        //해쭤 버튼 누르면 헬퍼보이는 리스트로 넘어가기
        main_btnHaezzo = findViewById(R.id.main_btnHaezzo);
        main_btnHaezzo.setOnClickListener(onClickListener);

        //해줄게 버튼 누르면 얼러트 뜨기
        main_btnHaezulgae = findViewById(R.id.main_btnHaezulgae);
        main_btnHaezulgae.setOnClickListener(onClickListener);

        //요청하기 버튼 눌렀을 때
        main_writeDocument = findViewById(R.id.main_writeDocument);
        main_writeDocument.setOnClickListener(onClickListener);

        //바틈 네비게이션 뷰 눌렀을때
        main_bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_bottom_navigation);
        main_bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);





    }//onCreate

    @Override
    public void onBackPressed() { // 뒤로가기 버튼 누를 때 이벤트

        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("앱 종료")
                .setMessage("앱을 종료하시겠어요?")
                .setNegativeButton("아니오", null )
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .show();
    }


    // 바텀 네비게이션
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
                    Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0,0);
                    return true;

            }
            return false;
        }
    };

    //해쭤 버튼 클릭 시
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_btnHaezzo:
                    Intent intent = new Intent(MainActivity.this, HelperListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.main_btnHaezulgae:

                    // 로그인 체크를 위해 GET 방식으로 작성
                    tmpurlAddr = urlAddr + "helperCheckSelect.jsp?" + "unumber=" + ShareVar.strUnumber;
                    connectLoginData();
                    Log.v("testmessage : unumber ", ShareVar.strUnumber);
                    Log.v("testmessage : get ", userListBeans.get(0).getUemail());
//                    if (userListBeans.get(0).getUemail().length()==0) { // 받은 hnumber가 비어 있을 때
//                        new AlertDialog.Builder(MainActivity.this)
//                                .setIcon(R.mipmap.ic_launcher)
//                                .setTitle("헬퍼 등록")
//                                .setMessage("'해줄게' 이용하시려면 \n 헬퍼가 되야 해요. \n 등록하시겠어요?")
//                                .setNegativeButton("아니오", null )
//                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent intent3 = new Intent(MainActivity.this, HelperApplyIntroActivity.class);
//                                        startActivity(intent3);
//
//                                    }
//                                })
//                                .show();
//
//                    } else{
                        Intent intent2 = new Intent(MainActivity.this, HaezulgaeListActivity.class);
                        startActivity(intent2);
//                    }
                    break;

                case  R.id.main_writeDocument:
                    Intent intent3 = new Intent(MainActivity.this, DocumentWriteActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.tvbtn_main_address:
                    Intent intent4 = new Intent(getApplicationContext(), MypageActivity.class);
                    startActivity(intent4);
                    break;
            }
        }
    };

    private void connectLoginData() {
        try {
            UserNetworkTask usernetworkTask = new UserNetworkTask(MainActivity.this, tmpurlAddr,"helpercheck");
            //jsp통해서 받아온 return 값 -> object
            Object obj = usernetworkTask.execute().get();
            userListBeans = (ArrayList<UserListBean>) obj;
            Log.v("message", "helpercheck bean완성!");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}//