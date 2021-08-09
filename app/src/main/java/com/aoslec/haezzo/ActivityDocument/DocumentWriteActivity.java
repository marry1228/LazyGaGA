package com.aoslec.haezzo.ActivityDocument;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aoslec.haezzo.MainActivity;
import com.aoslec.haezzo.NetworkTask.DocumentImageNetworkTask;
import com.aoslec.haezzo.NetworkTask.DocumentNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentWriteActivity extends AppCompatActivity {

    String urlAddr = null;
    String urlAddr1 = null; // 이미지 업로드
    String dgaga ; // 가전, 가구 버튼을 누를 때 값, 기본값은 가전
    String dproduct ;
    Button btnFurniture, btnElectronics, btnWrite;
    EditText etTitle, etContent, etMoney, etAddress;
    DatePicker dpDate;
    String resultdpDate ;
    TimePicker tpTime;
    String nHour, nMinute;
    String dtime;
    String dplace;
    String dpay;
    String dstatus = "대기 중"; //기본 값은 대기 중

    //작업용
    String dtitle, dcontent, dmoney;

    //spinner작업
    Spinner Dproducts;
    Spinner Dpays;
    ArrayAdapter<CharSequence> adapter = null;
    ArrayAdapter<CharSequence> adapter2 = null;

    //임시, usernumber
    String unumber = "15";

    //------image 업로드, 지급수단, product 제외하고 진행 -----//
    String dimage;

    private final static String TAG = "DocumentWriteActivity";
    ImageView imageView = null;
    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code
    private String img_path = null; // 최종 file name
    private String f_ext = null;    // 최종 file extension
    File tempSelectFile;

    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.aoslec.haezzo/";


    //IP받아오기
    String macIP = ShareVar.macIP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_write);

        urlAddr = ShareVar.urlAddr + "documentInsert.jsp?";

        ActivityCompat.requestPermissions(DocumentWriteActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        ActivityCompat.requestPermissions(DocumentWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);


        //연결
        etTitle = findViewById(R.id.wtire_etTitle);
        etContent = findViewById(R.id.write_etContent);
        etMoney = findViewById(R.id.write_etMoney);
        etAddress = findViewById(R.id.write_etLocation);
        imageView = findViewById(R.id.iv_dwriteimage);

        //입력 자릿수 제한
        etTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        etMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

        //버튼
        btnWrite = findViewById(R.id.write_btnWrite);
        btnWrite.setOnClickListener(onClickListener);
        btnFurniture = findViewById(R.id.write_btnFurniture);
        btnFurniture.setOnClickListener(onClickListener);
        btnElectronics = findViewById(R.id.write_btnElectronics);
        btnElectronics.setOnClickListener(onClickListener);

        //이미지뷰
        imageView.setOnClickListener(onClickListener);

        //datepicker
        dpDate = (DatePicker)findViewById(R.id.write_dpDate);
        resultdpDate = String.format("%d-%d-%d", dpDate.getYear(), dpDate.getMonth()+1, dpDate.getDayOfMonth()); // 월에 주의!

        //Timepicker
        tpTime = (TimePicker)findViewById(R.id.write_tpTime);
        tpTime.setIs24HourView(false);
        tpTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                nHour = Integer.toString(hourOfDay);
                nMinute = Integer.toString(minute);
                dtime = (nHour+nMinute);
            }
        });

        //spinner dproduct 연결
        adapter = ArrayAdapter.createFromResource(this, R.array.dproduct_category,
                android.R.layout.simple_spinner_dropdown_item);


        Dproducts = findViewById(R.id.write_sDproducts);
        Dproducts.setAdapter(adapter);

        //spinner dpay 연결
        adapter2 = ArrayAdapter.createFromResource(this, R.array.dpay_category,
                android.R.layout.simple_spinner_dropdown_item);

        Dpays = findViewById(R.id.write_sDpay);
        Dpays.setAdapter(adapter2);

        //dplace 주소값 받아오기
        dplace = ShareVar.strAddress;
        etAddress.setText(dplace);
    }//onCreate



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_dwriteimage:
                    Intent intent2 = new Intent(Intent.ACTION_PICK);
                    intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent2.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, REQ_CODE_SELECT_IMAGE);
                    break;
                case R.id.write_btnWrite:
                    dtitle = etTitle.getText().toString();
                    dcontent = etContent.getText().toString();
                    dmoney = etMoney.getText().toString();
                    dproduct = Dproducts.getSelectedItem().toString();

                    urlAddr1 = "http://"+ ShareVar.macIP + ":8080/test/Haezzo/multipartRequest.jsp";
                    DocumentImageNetworkTask networkTask = new DocumentImageNetworkTask(DocumentWriteActivity.this, urlAddr1, img_path, imageView);
                    try {
                        Integer result = networkTask.execute(100).get();
                        switch (result) {
                            case 1:
                                //get방식 url
                                urlAddr = urlAddr + "dgaga=" + dgaga + "&dproduct=" + dproduct + "&dtitle=" + dtitle +
                                        "&dimage=" + dimage + "&dcontent=" + dcontent + "&ddate=" + resultdpDate + "&dtime=" + dtime + "&dplace=" + dplace + "&dmoney=" +dmoney + "&dpay=" + dpay + "&dstatus=" + dstatus + "&unumber=" + ShareVar.strUnumber;
                                Log.v("urlAddr", urlAddr);
                                //urlAddr는 전역변수라 아무 메소드에서 쓸 수 있음
                                String strResult = connectInsertData();//여기에 return값 줄거임

//                    Toast.makeText(WriteDocumentActivity.this, "글이 입력되었습니다", Toast.LENGTH_SHORT).show();

                                Toast.makeText(DocumentWriteActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                                File file = new File(img_path);
                                file.delete();
                                break;
                            case 0:
                                Toast.makeText(DocumentWriteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(DocumentWriteActivity.this, MainActivity.class);
                    startActivity(intent);

                    // 0702 finish() 추가
                    DocumentWriteActivity.this.finish();
                    break;

                case R.id.write_btnFurniture:
                    btnFurniture.setBackgroundColor(Color.YELLOW);
                    btnElectronics.setBackgroundColor(Color.GRAY);
                    dgaga = "가구";
                    break;

                case R.id.write_btnElectronics:
                    btnFurniture.setBackgroundColor(Color.GRAY);
                    btnElectronics.setBackgroundColor(Color.YELLOW);
                    dgaga = "가전";
                    break;
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.v(TAG, "Data :" + String.valueOf(data));

        if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                //이미지의 URI를 얻어 경로값으로 반환.
                img_path = getImagePathToUri(data.getData());
                Log.v(TAG, "image path :" + img_path);
                Log.v(TAG, "Data :" +String.valueOf(data.getData()));

                //이미지를 비트맵형식으로 반환
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                int height = image_bitmap.getHeight();
                int width = image_bitmap.getWidth();

                Bitmap image_bitmap_copy = null;


                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
//                Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                while (width > 400){
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, (height*400)/width, true);
                    height = image_bitmap_copy.getHeight();
                    width = image_bitmap_copy.getWidth();
                }
                imageView.setImageBitmap(image_bitmap_copy);

                // 파일 이름 및 경로 바꾸기(임시 저장, 경로는 임의로 지정 가능)
                String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                dimage = date + "." + f_ext;
                tempSelectFile = new File(devicePath , dimage);
                OutputStream out = new FileOutputStream(tempSelectFile);
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                // 임시 파일 경로로 위의 img_path 재정의
                img_path = devicePath + dimage;
                Log.v(TAG,"fileName :" + img_path);
                Log.v(TAG,"imageName_activityResult :" + dimage);
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
        Log.v(TAG, "Image Path :" + imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        // 확장자 명 저장
        f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());

        return imgPath;
    }


    private String connectInsertData(){
        String result = null;
        try{
            //여기서 networktask
            //insertactivity에서 부른거야, 나는 ip주소 줄게 그리고     hb insert 할거야
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(DocumentWriteActivity.this, urlAddr,"insert");
            //jsp통해서 받아온 return 값 -> object
            Object obj = documentNetworkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;//잘끝났으면 1 아니면 다른값 넘길 거임

    }//connectInsertData




}//—DocumentWrite