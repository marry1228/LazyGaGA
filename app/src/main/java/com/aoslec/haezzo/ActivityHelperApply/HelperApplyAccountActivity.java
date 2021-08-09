package com.aoslec.haezzo.ActivityHelperApply;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.aoslec.haezzo.NetworkTask.HelperApplyDataNetworkTask;
import com.aoslec.haezzo.NetworkTask.HelperApplyImageNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperApplyAccountActivity extends AppCompatActivity {

    EditText et_hname;
    Button buttonBack, buttonOK, buttonCancel;
    ArrayAdapter<CharSequence> adpater_banks = null;
    Spinner sp_banks = null;
    EditText et_haccount;
    ImageView iv_haccountimage;


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
        setContentView(R.layout.activity_helper_apply_account);

        ActivityCompat.requestPermissions(HelperApplyAccountActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        ActivityCompat.requestPermissions(HelperApplyAccountActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

        et_hname = findViewById(R.id.et_hname);
        et_haccount = findViewById(R.id.et_haccount);
        iv_haccountimage = findViewById(R.id.iv_haccountimage);

        buttonBack = findViewById(R.id.btn_helper_apply_account_back);
        buttonOK = findViewById(R.id.btn_helper_apply_account_ok);
        buttonCancel = findViewById(R.id.btn_helper_apply_account_cancel);

        adpater_banks = ArrayAdapter.createFromResource(this, R.array.banks,
                android.R.layout.simple_spinner_dropdown_item);
        sp_banks = findViewById(R.id.sp_banks);
        sp_banks.setAdapter(adpater_banks);

        iv_haccountimage.setOnClickListener(onClickListener);
        buttonBack.setOnClickListener(onClickListener);
        buttonOK.setOnClickListener(onClickListener);
        buttonCancel.setOnClickListener(onClickListener);

        Glide.with(HelperApplyAccountActivity.this)
                .load("http://" + ShareVar.macIP + ":8080/test/Haezzo/duck.jpg")
                .into(iv_haccountimage);

        urlAddr1 = "http://"+ShareVar.macIP+":8080/test/Haezzo/helperApplyAccountUpdateReturn.jsp?";

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_haccountimage:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    break;
                case R.id.btn_helper_apply_account_back:
                    finish();
                    break;
                case R.id.btn_helper_apply_account_ok:
                    ShareVar.hName = et_hname.getText().toString();
                    ShareVar.hBank = sp_banks.getSelectedItem().toString();
                    ShareVar.hAccount = et_haccount.getText().toString();

                    urlAddr = "http://"+ ShareVar.macIP + ":8080/test/Haezzo/multipartRequest.jsp";
                    HelperApplyImageNetworkTask networkTask = new HelperApplyImageNetworkTask(HelperApplyAccountActivity.this, urlAddr, img_path, iv_haccountimage);
                    try {
                        Integer result = networkTask.execute(100).get();
                        switch (result) {
                            case 1:
//                                connectInsertData(imageName);

                                Toast.makeText(HelperApplyAccountActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                                File file = new File(img_path);
                                file.delete();
                                break;
                            case 0:
                                Toast.makeText(HelperApplyAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    urlAddr1 = urlAddr1 + "hname="+ShareVar.hName+"&hbank="+ShareVar.hBank+"&haccount="+ShareVar.hAccount+"&haccountimage="+ShareVar.hAccountImage+"&uemail="+ShareVar.strEmail;
                    Log.v("Message",urlAddr1);
                    String result = connectUpdateData();
                    if(result.equals("1")) {
                        Toast.makeText(HelperApplyAccountActivity.this, ShareVar.strEmail+"가 수정 되었습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(HelperApplyAccountActivity.this, "수정 실패되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent2 = new Intent(HelperApplyAccountActivity.this, HelperApplyIdCardActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                case R.id.btn_helper_apply_account_cancel:
                    finish();
                    break;
            }

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                //이미지의 URI를 얻어 경로값으로 반환.
                img_path = getImagePathToUri(data.getData());

                //이미지를 비트맵형식으로 반환
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                int height = image_bitmap.getHeight();
                int width = image_bitmap.getWidth();

                Bitmap image_bitmap_copy = null;


                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                while (width > 400){
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, (height*400)/width, true);
                    height = image_bitmap_copy.getHeight();
                    width = image_bitmap_copy.getWidth();
                }
                iv_haccountimage.setImageBitmap(image_bitmap_copy);

                // 파일 이름 및 경로 바꾸기(임시 저장, 경로는 임의로 지정 가능)
                String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                ShareVar.hAccountImage = date + "." + f_ext;
                tempSelectFile = new File(devicePath , ShareVar.hAccountImage);
                OutputStream out = new FileOutputStream(tempSelectFile);
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                // 임시 파일 경로로 위의 img_path 재정의
                img_path = devicePath + ShareVar.hAccountImage;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImagePathToUri(Uri data) {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        // 확장자 명 저장
        f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());

        return imgPath;
    }

    private String connectUpdateData() {
        String result = null;
        try {
            HelperApplyDataNetworkTask networkTask = new HelperApplyDataNetworkTask(HelperApplyAccountActivity.this, urlAddr1, "update");
            Object obj = networkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

} // ----