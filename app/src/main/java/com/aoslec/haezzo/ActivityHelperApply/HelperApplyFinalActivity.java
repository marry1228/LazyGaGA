package com.aoslec.haezzo.ActivityHelperApply;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.aoslec.haezzo.NetworkTask.HelperApplyDataNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.io.File;

public class HelperApplyFinalActivity extends AppCompatActivity {

    TextView tv_hself, tv_hgaga;
    Button buttonBack, buttonOK, buttonCancel;
    ImageView iv_hprofileimage3;


    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code
    private String img_path = null; // 최종 file name
    private String f_ext = null;    // 최종 file extension
    File tempSelectFile;
    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.aoslec.haezzo/";
    String urlAddr = null;
    String urlAddr1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_apply_final);

        ActivityCompat.requestPermissions(HelperApplyFinalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        ActivityCompat.requestPermissions(HelperApplyFinalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

        tv_hself = findViewById(R.id.tv_hself);
        tv_hgaga = findViewById(R.id.tv_hgaga);
        iv_hprofileimage3 = findViewById(R.id.iv_hprofileimage3);

        tv_hself.setText(ShareVar.hSelf);
        tv_hgaga.setText(ShareVar.hGaGa);

        buttonBack = findViewById(R.id.btn_helper_apply_final_back);
        buttonOK = findViewById(R.id.btn_helper_apply_final_ok);
        buttonCancel = findViewById(R.id.btn_helper_apply_final_cancel);

        buttonBack.setOnClickListener(onClickListener);
        buttonOK.setOnClickListener(onClickListener);
        buttonCancel.setOnClickListener(onClickListener);

        Glide.with(HelperApplyFinalActivity.this)
                .load("http://" + ShareVar.macIP + ":8080/test/Haezzo/duck.jpg")
                .into(iv_hprofileimage3);

        urlAddr1 = "http://"+ShareVar.macIP+":8080/test/Haezzo/helperApplyFinalUpdateReturn.jsp?";

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_helper_apply_final_back:
                    Intent intent = new Intent(HelperApplyFinalActivity.this, HelperApplyProfileActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_helper_apply_final_ok:

                    urlAddr1 = urlAddr1 + "hnumber="+"3"+"&uemail="+ShareVar.strEmail;
                    Log.v("Message",urlAddr1);
                    String result = connectUpdateData();
                    if(result.equals("1")) {
                        Toast.makeText(HelperApplyFinalActivity.this, ShareVar.strEmail+"가 수정 되었습니다.", Toast.LENGTH_SHORT).show();
                    }else {
//                        Toast.makeText(HelperApplyFinalActivity.this, "수정 실패되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;
                case R.id.btn_helper_apply_final_cancel:
                    finish();
                    break;
            }

        }
    };

    private String connectUpdateData() {
        String result = null;
        try {
            HelperApplyDataNetworkTask networkTask = new HelperApplyDataNetworkTask(HelperApplyFinalActivity.this, urlAddr1, "update");
            Object obj = networkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

} // ----