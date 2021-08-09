package com.aoslec.haezzo.ActivityDocument;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aoslec.haezzo.ActivityOnDealList.OnDealListActivity;
import com.aoslec.haezzo.Bean.DocumentBean;
import com.aoslec.haezzo.MainActivity;
import com.aoslec.haezzo.NetworkTask.DocumentNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DocumentWaitingDetailsActivity extends AppCompatActivity {

    String urlAddr = null;
    String macIP = ShareVar.macIP;
    //수정 부분
    String subUrl_update = null;
    //String subUrl_delete = null;
    String dnumber = null;

    ArrayAdapter<CharSequence> adapter2 = null;


    ArrayList<DocumentBean> documentBeans;
    Intent intent= null;

    //TextView 들
    TextView DwaitingDetails_tvDgaga, DwaitingDetails_tvDplace;

    EditText DwaitingDetails_etDtitle, DwaitingDetails_etDcontent,
            DwaitingDetails_etDmoney;

    Button DwaitingDetails_btnFurniture,DwaitingDetails_btnElectronics,DwaitingDetails_btnDmodify;

    ImageView DwaitingDetails_ivDimage;

    //입력된 값을 받을 변수들
    String sDproduct, sDtitle, sDcontent, sDplace, sDmoney, sDpay, sDgaga;
    DatePicker dpDate;
    DatePicker DwaitingDetails_tvDdate;
    String resultdpDate ;
    TimePicker tpTime;
    TimePicker DwaitingDetails_tvDtime;
    String nHour, nMinute;
    String dtime;
    String dplace;

    //spinner작업
    Spinner Dproducts;
    Spinner DwaitingDetails_etDproducts;
    Spinner DwaitingDetails_tvDpay;
    ArrayAdapter<CharSequence> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_waiting_details);
        Log.v("Message", "DocumentWaitingDetails_onCreate");


        //button연결
        DwaitingDetails_btnDmodify = findViewById(R.id.DwaitingDetails_btnDmodify);
        DwaitingDetails_btnDmodify.setOnClickListener(onClickListener);

        DwaitingDetails_btnFurniture = findViewById(R.id.DwaitingDetails_btnFurniture);
        DwaitingDetails_btnFurniture.setOnClickListener(onClickListener);
        DwaitingDetails_btnElectronics = findViewById(R.id.DwaitingDetails_btnElectronics);
        DwaitingDetails_btnElectronics.setOnClickListener(onClickListener);

        //textView 연결
        DwaitingDetails_etDproducts = findViewById(R.id.DwaitingDetails_sDproducts);
        DwaitingDetails_etDtitle = findViewById(R.id.DwaitingDetails_etTitle);
        DwaitingDetails_etDcontent = findViewById(R.id.DwaitingDetails_etContent);
        DwaitingDetails_tvDdate = findViewById(R.id.DwaitingDetails_dpDate);
        DwaitingDetails_tvDtime = findViewById(R.id.DwaitingDetails_tpTime);
        DwaitingDetails_etDmoney = findViewById(R.id.DwaitingDetails_etMoney);
        DwaitingDetails_tvDpay = findViewById(R.id.DwaitingDetails_sDpay);

        //입력 자릿수 제한
        DwaitingDetails_etDtitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        DwaitingDetails_etDcontent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        DwaitingDetails_etDmoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});

        //이미지뷰 연결
        DwaitingDetails_ivDimage = findViewById(R.id.DwaitingDetails_image);

        //datepicker
        dpDate = (DatePicker)findViewById(R.id.DwaitingDetails_dpDate);
        resultdpDate = String.format("%d-%d-%d", dpDate.getYear(), dpDate.getMonth()+1, dpDate.getDayOfMonth()); // 월에 주의!

        //Timepicker
        tpTime = (TimePicker)findViewById(R.id.DwaitingDetails_tpTime);
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


        Dproducts = findViewById(R.id.DwaitingDetails_sDproducts);
        Dproducts.setAdapter(adapter);

        //spinner dpay 연결
        adapter2 = ArrayAdapter.createFromResource(this, R.array.dpay_category,
                android.R.layout.simple_spinner_dropdown_item);

        DwaitingDetails_tvDpay.setAdapter(adapter2);


        //dplace 주소값 받아오기
        dplace = ShareVar.strAddress;
        DwaitingDetails_tvDplace = findViewById(R.id.DwaitingDetails_etLocation);
        DwaitingDetails_tvDplace.setText(dplace);

        //수정 jsp 연결
        subUrl_update = "documentUpdate.jsp?";
        // subUrl_delete = "documentDelete.jsp?";

    }//onCreate

    @Override//*********중요!!!
    protected void onResume() {
        super.onResume();
        showDetails();
    }

    private void connectGetData(){
        //검색하는 화면
        try {
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(DocumentWaitingDetailsActivity.this, urlAddr,"select");
            //networktask 가 구동이 되서
            Object obj = documentNetworkTask.execute().get();//프로그레스바 돌고, 데이터 가지러가고
            documentBeans = (ArrayList<DocumentBean>) obj;

        }catch(Exception e){
            e.printStackTrace();
        }

    }//connectGetdata


    public void showDetails(){
        intent = getIntent();
        dnumber =intent.getStringExtra("dnumber");

        Log.v("Message", "Details_showDetail_dnumber = " + dnumber);

        //url불러와서 + select.jsp 넣기
        urlAddr = ShareVar.urlAddr + "documentSelect.jsp?";

        connectGetData();

        DwaitingDetails_tvDgaga.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDgaga());
//        DwaitingDetails_etDproducts.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDproduct());
        DwaitingDetails_etDtitle.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDtitle());
        DwaitingDetails_etDcontent.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDcontent());
//        DwaitingDetails_tvDdate.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDdate());
//        DwaitingDetails_tvDtime.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDtime());
        DwaitingDetails_tvDplace.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDplace());
        DwaitingDetails_etDmoney.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDmoney());
//        DwaitingDetails_tvDpay.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDpay());
        Log.e("Message","여기 에러뜨나");
        Glide.with(this)
                .load(ShareVar.urlAddr + documentBeans.get((Integer.parseInt(dnumber))-1).getDimage())
                .override(300,200)
                .centerCrop()
                .into(DwaitingDetails_ivDimage);

    }//showDetails

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.DwaitingDetails_btnDmodify:
                    intent = getIntent();
                    dnumber =intent.getStringExtra("dnumber");
                    Log.v("Message","dnumber는 어떻게 들어옵니까"+dnumber);

                    //화면에 입력된 값 가져오기
                    sDgaga = DwaitingDetails_tvDgaga.getText().toString();
                    sDproduct = Dproducts.getSelectedItem().toString();
                    sDtitle = DwaitingDetails_etDtitle.getText().toString();
                    sDcontent = DwaitingDetails_etDcontent.getText().toString();
                    resultdpDate = String.format("%d-%d-%d", dpDate.getYear(), dpDate.getMonth()+1, dpDate.getDayOfMonth()); // 월에 주의!

                    sDplace = DwaitingDetails_tvDplace.getText().toString();
                    sDmoney = DwaitingDetails_etDmoney.getText().toString();


                    urlAddr = ShareVar.urlAddr + subUrl_update +"dgaga="+ sDgaga + "&dproduct=" + sDproduct + "&dtitle=" + sDtitle + "&dcontent=" + sDcontent
                            + "&ddate=" + resultdpDate + "&dtime=" + tpTime + "&dplace=" + sDplace + "&dmoney=" + sDmoney + "&dpay=" + sDpay
                            + "&dnumber=" + dnumber;

                    String result_up = connectUpdateData();

                    //토스트 띄우기
                    Toast.makeText(DocumentWaitingDetailsActivity.this, "글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DocumentWaitingDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;

                case R.id.DwaitingDetails_btnFurniture:
                    DwaitingDetails_btnFurniture.setBackgroundColor(Color.YELLOW);
                    DwaitingDetails_btnElectronics.setBackgroundColor(Color.GRAY);
                    sDgaga = "가구";
                    break;

                case R.id.DwaitingDetails_btnElectronics:
                    DwaitingDetails_btnFurniture.setBackgroundColor(Color.GRAY);
                    DwaitingDetails_btnElectronics.setBackgroundColor(Color.YELLOW);
                    sDgaga = "가전";
                    break;

            }

        } // onClick
    }; //onClickListener

    private String connectUpdateData(){
        Log.v("Message", "METHOD : connectUpdateData Start");
        String result_up = null;
        try {
            // NetworkTask 가져와서 일을 시킬 거다 (어디에?, 어느 주소받아서?, 어느역할이야?)
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(DocumentWaitingDetailsActivity.this, urlAddr, "update");
            Object obj = documentNetworkTask.execute().get();
            result_up = (String)obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v("Message", "End of METHOD // result : " + result_up);
        return result_up;
    }//update

    private String connectDeleteData(){
        Log.v("Message", "METHOD : connectUpdateData Start");
        String result_del = null;
        try {
            // NetworkTask 가져와서 일을 시킬 거다 (어디에?, 어느 주소받아서?, 어느역할이야?)
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(DocumentWaitingDetailsActivity.this, urlAddr, "delete");
            Object obj = documentNetworkTask.execute().get();
            result_del = (String)obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v("Message", "End of METHOD // result : " + result_del);
        return result_del;
    }//delete

}//————