package com.aoslec.haezzo.ActivityUserHelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aoslec.haezzo.ActivityLogin.KakaoLoginActivity;
import com.aoslec.haezzo.MainActivity;
import com.aoslec.haezzo.NetworkTask.UserNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;


public class MypageFixActivity extends AppCompatActivity {
    String urlAddr = ShareVar.urlAddr;

    Button btnSignout;
    Button btnFixOk;

    TextView tv_nickname, tv_gender, tv_agerange, tv_email, tv_address;
    ImageView iv_profileimg;

    // 값들 ShareVar에서 받아오기
    private String strNick = ShareVar.strNick;
    private String strProfileImg = ShareVar.strProfileImg;
    private String strEmail = ShareVar.strEmail;
    private String strGender = ShareVar.strGender;
    private String strAgeRange = ShareVar.strAgeRange;
    private String strAddress = ShareVar.strAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_fix);

        urlAddr =  urlAddr + "loginIdInsert.jsp?";
        btnSignout = findViewById(R.id.myPagefix_btnSignout); // 회원 탈퇴 버튼
        btnFixOk = findViewById(R.id.btn_mypagefix_fixok); // 수정 완료 버튼

        // id와 연결
        tv_nickname = findViewById(R.id.tv_mypagefix_nickname);
        tv_gender = findViewById(R.id.tv_mypagefix_gender);
        tv_agerange = findViewById(R.id.tv_mypagefix_agerange);
        tv_email = findViewById(R.id.tv_mypagefix_email);
        tv_address = findViewById(R.id.tv_mypagefix_address);
        iv_profileimg = findViewById(R.id.iv_mypagefix_profileimg);

        // MypageFix에 값 설정
        tv_nickname.setText(strNick);
        tv_email.setText(strEmail);
        tv_gender.setText(strGender);
        tv_agerange.setText(strAgeRange);
        tv_address.setText(strAddress);

        //Glide를 이용해 이미지 파일 불러와 프로필 사진으로 set
        Glide.with(this).load(strProfileImg).into(iv_profileimg);

        // 수정 완료 버튼 클릭 시
        btnFixOk.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 확인 여부를 묻는 팝업 창
                new AlertDialog.Builder(MypageFixActivity.this)
                        .setMessage("이대로 수정하시겠어요?")
                        .setPositiveButton("그래요!", new DialogInterface.OnClickListener() { // 네 버튼 클릭 시

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 수정된 값으로 값 업데이트
                                strNick = tv_nickname.getText().toString();
                                strEmail = tv_email.getText().toString();
                                strGender = tv_gender.getText().toString();
                                strAgeRange = tv_agerange.getText().toString();
                                strAddress = tv_address.getText().toString();
                                //strProfileImg = (이미지 불러오기 아직 미개발 단계);

                                Intent intent = new Intent(MypageFixActivity.this, MypageActivity.class);

                                // DB에 nickname 및 나머지 값을 update 한다. (get방식 url)
                                urlAddr = urlAddr + "uimage=" + strProfileImg + "&uage=" + strAgeRange +
                                        "&ufm=" + strGender + "&unickname=" + strNick + "&uaddress=" + strAddress + "&uemail=" + strEmail;

                                connectUpdateData();
                                Toast.makeText(MypageFixActivity.this, "수정 되었습니다", Toast.LENGTH_SHORT).show();

                                // update된 값들을 sharevar에 보내 줌
                                ShareVar.strNick = strNick;
                                ShareVar.strProfileImg = strProfileImg;
                                ShareVar.strEmail = strEmail;
                                ShareVar.strGender = strGender;
                                ShareVar.strAgeRange = strAgeRange;
                                ShareVar.strAddress = strAddress;

                                // MainActivity로 이동
                                startActivity(intent);
                                MypageFixActivity.this.finish();
                            }
                        })
                        .setNegativeButton("잠시만요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //팝업 창을 닫음
                            }
                        }).show();

            }
        });

        //회원탈퇴 버튼 클릭 시
        btnSignout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //탈퇴 여부를 묻는 팝업창 실행
                new AlertDialog.Builder(MypageFixActivity.this)
                        .setMessage("정말 탈퇴하시는 거에요ㅠㅠ") //팝업창의 메세지 설정
                        .setPositiveButton("네(단호)", new DialogInterface.OnClickListener() { //"네" 버튼 클릭 시 -> 회원탈퇴 수행

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() { //회원탈퇴 실행
                                    @Override
                                    public void onFailure(ErrorResult errorResult) { // 회원탈퇴 실패 시
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) { // 로그인 세션이 닫혀있을 시
                                        //다시 로그인해달라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MypageFixActivity.this, KakaoLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() { // 가입되지 않은 계정에서 회원탈퇴 요구 시
                                        //가입되지 않은 계정이라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MypageFixActivity.this, KakaoLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) { // 회원탈퇴에 성공 시
                                        //"회원탈퇴에 성공했습니다."라는 Toast 메세지를 띄우고 로그인 창으로 이동함
                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MypageFixActivity.this, KakaoLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss(); //팝업 창을 닫음
                            }
                        })
                        .setNegativeButton("아니요(망설임)", new DialogInterface.OnClickListener() { //"아니요" 버튼 클릭 시 -> 팝업 창을 닫음
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss(); //팝업 창을 닫음
                            }
                        }).show();
            }
        });

    } // OnCreate

    private String connectUpdateData(){
        String result = null;

        try{
            UserNetworkTask userNetworkTask = new UserNetworkTask(MypageFixActivity.this, urlAddr,"update");
            //jsp통해서 받아온 return 값 -> object
            Object obj = userNetworkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result; //잘끝났으면 1 아니면 0
    }//connectUpdateData

}
