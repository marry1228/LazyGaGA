package com.aoslec.haezzo.ActivityLogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.aoslec.haezzo.NetworkTask.UserNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;

public class KakaoLoginSubActivity extends AppCompatActivity {

    private String strNick;
    EditText et_nickname ;

    private Button btn_Ok;

    private String strProfileImg = ShareVar.strProfileImg;
    private String strEmail = ShareVar.strEmail;
    private String strGender = ShareVar.strGender;
    private String strAgeRange = ShareVar.strAgeRange;
    private String strAddress = ShareVar.strAddress;

    String urlAddr = ShareVar.urlAddr + "kakaoLoginInsert.jsp?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login_sub);

        Log.v("LoginSub","LoginSubActivity");

        et_nickname = findViewById(R.id.et_nickname);

        btn_Ok = findViewById(R.id.signup_btnOk);
        btn_Ok.setOnClickListener(onClickListener);

    }//onCreate

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            strNick = et_nickname.getText().toString();
            ShareVar.strNick = strNick;

            // DB에 nickname 및 나머지 값을 넣는다. (get방식 url)
            urlAddr = urlAddr + "uimage=" + strProfileImg + "&uage=" + strAgeRange +
                    "&ufm=" + strGender + "&unickname=" + strNick + "&uaddress=" + strAddress + "&uemail=" + strEmail;

            String result = connectInsertData();

                Intent intent = new Intent(KakaoLoginSubActivity.this, GetDBDataActivity.class);
                startActivity(intent);
                KakaoLoginSubActivity.this.finish();
        }
    };

    private String connectInsertData(){
        String result = null;

        try{
            UserNetworkTask userNetworkTask = new UserNetworkTask(KakaoLoginSubActivity.this, urlAddr,"insert");
            //jsp통해서 받아온 return 값 -> object
            Object obj = userNetworkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result; //잘끝났으면 1 아니면 0


    }//connectInsertData

} //KakaoLoginSubActivity