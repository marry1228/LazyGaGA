package com.aoslec.haezzo.ActivityLogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aoslec.haezzo.Bean.UserListBean;
import com.aoslec.haezzo.MainActivity;
import com.aoslec.haezzo.NetworkTask.UserNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;


public class KakaoLoginActivity extends AppCompatActivity {

    // 돌려쓰기 위해 public static으로 IP값을 잡는다.
    private String macIP = ShareVar.macIP;
    private String urlAddr = ShareVar.urlAddr;
    private String profileImg = "";
    private String email = "";
    private String gender = "";
    private String agerange = "";
    private String emailFromDB = "";

    private ArrayList<UserListBean> userListBeans;

    private ISessionCallback mSessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);

        mSessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                // 로그인 요청
                UserManagement.getInstance().me(new MeV2ResponseCallback() {

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        // 로그인 실패
                        Toast.makeText(KakaoLoginActivity.this, "로그인 도중에 오류가 발생 했습니다. ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        // 세선이 닫혔을 때
                        Toast.makeText(KakaoLoginActivity.this, "세션이 닫혔습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(MeV2Response result) { // 로그인 성공 시, 정보를 result에 담아서 줌

                        email = result.getKakaoAccount().getEmail(); // 이메일
                        agerange = result.getKakaoAccount().getAgeRange().getValue(); // 나이대
                        gender = result.getKakaoAccount().getGender().getValue(); // 성별

                        // 로그인 후, result에서 필요한 정보를 얻어 옴
                        profileImg = result.getKakaoAccount().getProfile().getProfileImageUrl(); // 프로필 이미지

                        ShareVar.strEmail = email;
                        ShareVar.strGender = gender;
                        ShareVar.strAgeRange = agerange;
                        ShareVar.strProfileImg = profileImg;

                        // 로그인 체크를 위해 GET 방식으로 작성
                        urlAddr = urlAddr + "kakaoLoginSelect.jsp?" + "uemail=" + email;
                        connectLoginData();

                        emailFromDB = userListBeans.get(0).getUemail();

                        Log.v("LoginActivityMessage", "email : " + email);
                        if (email.equals(emailFromDB)){ // 이미 DB에 이메일이 있을 때
                            Log.v("LoginActivityMessage", "DB에 이메일 있을 때 : " + userListBeans.get(0).getUemail());
                            Intent intent = new Intent(KakaoLoginActivity.this, GetDBDataActivity.class);
                            startActivity(intent);
                            KakaoLoginActivity.this.finish();
                        } else { // 처음 로그인 할 때
                            Log.v("LoginActivityMessage", "DB에 이메일 있을 때 : " + userListBeans.get(0).getUemail());
                            Intent intent = new Intent(KakaoLoginActivity.this, KakaoLoginMapInsertActivity.class);
                            startActivity(intent);
                            KakaoLoginActivity.this.finish();
                        }

                    } // onSuccess
                });
            } // onSessionOpened

            @Override
            public void onSessionOpenFailed(KakaoException e) {
                Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        Session.getCurrentSession().addCallback(mSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
//      getAppKeyHash();
    }//onCreate

    private void connectLoginData() {
        try {
            UserNetworkTask usernetworkTask = new UserNetworkTask(KakaoLoginActivity.this, urlAddr,"login");
            //jsp통해서 받아온 return 값 -> object
            Object obj = usernetworkTask.execute().get();
            userListBeans = (ArrayList<UserListBean>) obj;
            Log.v("LoginActivityMessage", "KakaoLogin bean완성!");

        }catch (Exception e){
            e.printStackTrace();
        }
    }


//    카카오 로그인 시 키(해시)를 얻는 메소드 이다.
//    private void getAppKeyHash(){
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures){
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                Log.e("Hash key", something); // 오류 로그 형식으로 해쉬키를 찍어낸다.
//            }
//        }catch (Exception e){
//            Log.e("name not found", e.toString());
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }


    public String rtIP(){
        return macIP;
    }
}//Main