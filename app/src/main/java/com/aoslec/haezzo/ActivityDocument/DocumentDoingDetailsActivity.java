package com.aoslec.haezzo.ActivityDocument;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aoslec.haezzo.ActivityOnDealList.OnDealListActivity;
import com.aoslec.haezzo.Bean.DocumentBean;
import com.aoslec.haezzo.NetworkTask.DocumentNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DocumentDoingDetailsActivity extends AppCompatActivity {

    String urlAddr = null;
    String macIP = ShareVar.macIP;
    ArrayList<DocumentBean> documentBeans;
    Intent intent= null;
    String dnumber = null;
    String dstatus = null;


    //TextView 들
    TextView DdoingDetails_tvDgaga,DdoingDetails_tvDproducts,DdoingDetails_tvDtitle, DdoingDetails_tvDcontent, DdoingDetails_tvDdate, DdoingDetails_tvDtime,
            DdoingDetails_tvDplace, DdoingDetails_tvDmoney, DdoingDetails_tvDpay;

    Button DdoingDetails_btnDcomplete;
    ImageView DdoingDetails_ivDimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_doing_details);

        //button연결
        DdoingDetails_btnDcomplete = findViewById(R.id.DdoingDetails_btnDcomplete);
        DdoingDetails_btnDcomplete.setOnClickListener(onClickListener);

        //textView 연결
        DdoingDetails_tvDgaga = findViewById(R.id.DdoingDetails_tvDgaga);
        DdoingDetails_tvDproducts = findViewById(R.id.DdoingDetails_tvDproducts);
        DdoingDetails_tvDtitle = findViewById(R.id.DdoingDetails_tvDtitle);
        DdoingDetails_tvDcontent = findViewById(R.id.DdoingDetails_tvDcontent);
        DdoingDetails_tvDdate = findViewById(R.id.DdoingDetails_tvDdate);
        DdoingDetails_tvDtime = findViewById(R.id.DdoingDetails_tvDdate);
        DdoingDetails_tvDplace = findViewById(R.id.DdoingDetails_tvDplace);
        DdoingDetails_tvDmoney = findViewById(R.id.DdoingDetails_tvDmoney);
        DdoingDetails_tvDpay = findViewById(R.id.DdoingDetails_tvDpay);

        //이미지뷰 연결
        DdoingDetails_ivDimage = findViewById(R.id.DdoingDetails_ivDimage);

    }//onClick

    @Override//*********중요!!!
    protected void onResume() {
        super.onResume();
        showDetails();
    }

    private void connectGetData(){
        //검색하는 화면
        try {
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(DocumentDoingDetailsActivity.this, urlAddr,"select");
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

        DdoingDetails_tvDgaga.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDgaga());
        DdoingDetails_tvDproducts.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDproduct());
        DdoingDetails_tvDtitle.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDtitle());
        DdoingDetails_tvDcontent.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDcontent());
        DdoingDetails_tvDdate.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDdate());
        DdoingDetails_tvDtime.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDtime());
        DdoingDetails_tvDplace.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDplace());
        DdoingDetails_tvDmoney.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDmoney());
        DdoingDetails_tvDpay.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDpay());

        Glide.with(this)
                .load(ShareVar.urlAddr + documentBeans.get((Integer.parseInt(dnumber))-1).getDimage())
                .into(DdoingDetails_ivDimage);

    }//showDetails

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.DdoingDetails_btnDcomplete:
                    new AlertDialog.Builder(DocumentDoingDetailsActivity.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("거래완료")
                            .setMessage("거래완료를 누르시면 변경이 불가능합니다. \n정말로 누르실껀가요?")
                            .setNegativeButton("아니오", null )
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(DocumentDoingDetailsActivity.this, OnDealListActivity.class);
                                    dnumber = intent.getStringExtra("dnumber");
                                    dstatus = intent.getStringExtra("dstatus");
                                    intent.putExtra("dnumber",dnumber);
                                    //거래완료 누르면 dstatus가 거래완료! 상태로 변경 되어야 함
                                    ShareVar.Document_dstatus = "거래완료";
                                    startActivity(intent);
                                }
                            })
                            .show();
                    break;
            }

        } // onClick
    }; //onClickListener


}//------