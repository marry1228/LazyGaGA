package com.aoslec.haezzo.ActivityHelperApply;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.aoslec.haezzo.NetworkTask.HelperApplyDataNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.io.File;

public class HelperApplyProfileActivity extends AppCompatActivity {

    EditText et_hself;
    Button buttonBack, buttonOK, buttonCancel;
    ArrayAdapter<CharSequence> adpater_gaga = null;
    Spinner sp_gaga = null;
    ImageView iv_hprofileimage2;


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
        setContentView(R.layout.activity_helper_apply_profile);

        ActivityCompat.requestPermissions(HelperApplyProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        ActivityCompat.requestPermissions(HelperApplyProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

        et_hself = findViewById(R.id.et_hself);
        iv_hprofileimage2 = findViewById(R.id.iv_hprofileimage2);

        buttonBack = findViewById(R.id.btn_helper_apply_profile_back);
        buttonOK = findViewById(R.id.btn_helper_apply_profile_ok);
        buttonCancel = findViewById(R.id.btn_helper_apply_profile_cancel);

        adpater_gaga = ArrayAdapter.createFromResource(this, R.array.gaga,
                android.R.layout.simple_spinner_dropdown_item);
        sp_gaga = findViewById(R.id.sp_gaga);
        sp_gaga.setAdapter(adpater_gaga);

        buttonBack.setOnClickListener(onClickListener);
        buttonOK.setOnClickListener(onClickListener);
        buttonCancel.setOnClickListener(onClickListener);

        Glide.with(HelperApplyProfileActivity.this)
                .load("http://" + ShareVar.macIP + ":8080/test/Haezzo/duck.jpg")
                .into(iv_hprofileimage2);

        urlAddr1 = "http://"+ShareVar.macIP+":8080/test/Haezzo/helperApplyProfileUpdateReturn.jsp?";

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_helper_apply_profile_back:
                    Intent intent = new Intent(HelperApplyProfileActivity.this, HelperApplyProfileImageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_helper_apply_profile_ok:
                    ShareVar.hSelf = et_hself.getText().toString();
                    ShareVar.hGaGa = sp_gaga.getSelectedItem().toString();

                    urlAddr1 = urlAddr1 + "hself="+ShareVar.hSelf+"&hgaga="+ShareVar.hGaGa+"&uemail="+ShareVar.strEmail;
                    Log.v("Message",urlAddr1);
                    String result = connectUpdateData();
                    if(result.equals("1")) {
                        Toast.makeText(HelperApplyProfileActivity.this, ShareVar.strEmail+"가 수정 되었습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(HelperApplyProfileActivity.this, "수정 실패되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent2 = new Intent(HelperApplyProfileActivity.this, HelperApplyFinalActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                case R.id.btn_helper_apply_profile_cancel:
                    finish();
                    break;
            }

        }
    };

    private String connectUpdateData() {
        String result = null;
        try {
            HelperApplyDataNetworkTask networkTask = new HelperApplyDataNetworkTask(HelperApplyProfileActivity.this, urlAddr1, "update");
            Object obj = networkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

} // ----