package com.aoslec.haezzo.ActivityDocument;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aoslec.haezzo.Bean.DocumentBean;
import com.aoslec.haezzo.NetworkTask.DocumentNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DocumentDoneDetailsActivity extends AppCompatActivity {
    String urlAddr = null;
    String macIP = ShareVar.macIP;
    ArrayList<DocumentBean> documentBeans;
    Intent intent= null;
    String dnumber = null;
    String dstatus = null;

    //TextView 들
    TextView DdoneDetails_tvDgaga,DdoneDetails_tvDproducts,DdoneDetails_tvDtitle, DdoneDetails_tvDcontent, DdoneDetails_tvDdate, DdoneDetails_tvDtime,
            DdoneDetails_tvDplace, DdoneDetails_tvDmoney, DdoneDetails_tvDpay;

    ImageView DdoneDetails_ivDimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_done_details);

        //textView 연결
        DdoneDetails_tvDgaga = findViewById(R.id.DdoneDetails_tvDgaga);
        DdoneDetails_tvDproducts = findViewById(R.id.DdoneDetails_tvDproducts);
        DdoneDetails_tvDtitle = findViewById(R.id.DdoneDetails_tvDtitle);
        DdoneDetails_tvDcontent = findViewById(R.id.DdoneDetails_tvDcontent);
        DdoneDetails_tvDdate = findViewById(R.id.DdoneDetails_tvDdate);
        DdoneDetails_tvDtime = findViewById(R.id.DdoneDetails_tvDdate);
        DdoneDetails_tvDplace = findViewById(R.id.DdoneDetails_tvDplace);
        DdoneDetails_tvDmoney = findViewById(R.id.DdoneDetails_tvDmoney);
        DdoneDetails_tvDpay = findViewById(R.id.DdoneDetails_tvDpay);

        //이미지뷰 연결
        DdoneDetails_ivDimage = findViewById(R.id.DdoneDetails_ivDimage);
    }//onCreate

    @Override//*********중요!!!
    protected void onResume() {
        super.onResume();
        showDetails();
    }

    private void connectGetData(){
        //검색하는 화면
        try {
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(DocumentDoneDetailsActivity.this, urlAddr,"select");
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

        DdoneDetails_tvDgaga.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDgaga());
        DdoneDetails_tvDproducts.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDproduct());
        DdoneDetails_tvDtitle.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDtitle());
        DdoneDetails_tvDcontent.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDcontent());
        DdoneDetails_tvDdate.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDdate());
        DdoneDetails_tvDtime.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDtime());
        DdoneDetails_tvDplace.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDplace());
        DdoneDetails_tvDmoney.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDmoney());
        DdoneDetails_tvDpay.setText(documentBeans.get((Integer.parseInt(dnumber))-1).getDpay());

        Glide.with(this)
                .load(ShareVar.urlAddr + documentBeans.get((Integer.parseInt(dnumber))-1).getDimage())
                .into(DdoneDetails_ivDimage);

    }//showDetails
}//-----