package com.aoslec.haezzo.ActivityDocument;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aoslec.haezzo.ActivityUserHelper.MypageFixActivity;
import com.aoslec.haezzo.Bean.DocumentBean;
import com.aoslec.haezzo.NetworkTask.DocumentNetworkTask;
import com.aoslec.haezzo.NetworkTask.UserNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HaezulgaeDocumentDetailsActivity extends AppCompatActivity {

    String urlAddr = null;
    ArrayList<DocumentBean> documentBeans;
    String macIP = ShareVar.macIP;

    Intent intent = null;

    String hnumber = null;
    String dnumber = null;
    int dnumberInt;

    //TextView 들
    TextView HDdetails_btnFurniture,HDdetails_btnElectronics,HDdetails_tvDproducts,HDdetails_tvDtitle, HDdetails_tvDcontent, HDdetails_tvDdate,HDdetails_tvDtime,
            HDdetails_tvDplace,HDdetails_tvDmoney,HDdetails_tvDpay;

    Button HDdetails_btnDapply;
    ImageView HDdetails_ivDimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haezulgae_document_details);
        Log.v("Message", "HaezulgaeDetails_onCreate");

        intent = getIntent();

        // dnumber를 인텐트로 받아옴.
        dnumber = intent.getStringExtra("dnumber");
        dnumberInt = Integer.parseInt(dnumber);

        // hnumber을 받아옴.
        hnumber = ShareVar.hnumber;

        //textView 연결
        HDdetails_tvDproducts = findViewById(R.id.HDdetails_tvDproducts);
        HDdetails_tvDtitle = findViewById(R.id.HDdetails_tvDtitle);
        HDdetails_tvDcontent = findViewById(R.id.HDdetails_tvDcontent);
        HDdetails_tvDdate = findViewById(R.id.HDdetails_tvDdate);
        HDdetails_tvDtime = findViewById(R.id.HDdetails_tvDtime);
        HDdetails_tvDplace = findViewById(R.id.HDdetails_tvDplace);
        HDdetails_tvDmoney = findViewById(R.id.HDdetails_tvDmoney);
        HDdetails_tvDpay = findViewById(R.id.HDdetails_tvDpay);

        //이미지뷰 연결
        HDdetails_ivDimage = findViewById(R.id.HDdetails_ivDimage);

        //button연결
        HDdetails_btnDapply = findViewById(R.id.HDdetails_btnDapply);
        HDdetails_btnDapply.setOnClickListener(onClickListener);


    }//onCreate

    @Override//*********중요!!!
    protected void onResume() {
        super.onResume();
        showDetails();
    }

    private void connectGetData(){
        //검색하는 화면
        try {
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(HaezulgaeDocumentDetailsActivity.this, urlAddr,"select");
            //networktask 가 구동이 되서
            Object obj = documentNetworkTask.execute().get();//프로그레스바 돌고, 데이터 가지러가고
            documentBeans = (ArrayList<DocumentBean>) obj;

        }catch(Exception e){
            e.printStackTrace();
        }

    }//connectGetdata

    //지원하기 버튼 눌렀을때 값 넘기기 ( 나중에 수정할 부분: 다른쪽이랑 연결 )
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            intent = new Intent(HaezulgaeDocumentDetailsActivity.this, HaezulgaeListActivity.class);

            // DB에 nickname 및 나머지 값을 update 한다. (get방식 url)
            urlAddr = ShareVar.urlAddr + "loginIdInsert.jsp?" + "dnumber=" + dnumber + "&hnumber=" + hnumber ;

            connectUpdateData();
            Toast.makeText(HaezulgaeDocumentDetailsActivity.this, "지원 되었습니다.", Toast.LENGTH_SHORT).show();
//            startActivity(intent);

            HaezulgaeDocumentDetailsActivity.this.finish(); // 다시 HaezulgaeList로 이동

        } // onClick
    }; //onClickListener

    public void showDetails(){

        Log.v("Message", "Details_showDetail_dnumber = " + dnumber);

        //url불러와서 + select.jsp 넣기
        urlAddr = ShareVar.urlAddr + "haezulgaeDocumentSelectList.jsp?";
        connectGetData();

        HDdetails_tvDproducts.setText(documentBeans.get(dnumberInt-1).getDproduct());
        HDdetails_tvDtitle.setText(documentBeans.get(dnumberInt-1).getDtitle());
        HDdetails_tvDcontent.setText(documentBeans.get(dnumberInt-1).getDcontent());
        HDdetails_tvDdate.setText(documentBeans.get(dnumberInt-1).getDdate());
        HDdetails_tvDtime.setText(documentBeans.get(dnumberInt-1).getDtime());
        HDdetails_tvDplace.setText(documentBeans.get(dnumberInt-1).getDplace());
        HDdetails_tvDmoney.setText(documentBeans.get(dnumberInt-1).getDmoney());
        HDdetails_tvDpay.setText(documentBeans.get(dnumberInt-1).getDpay());

        Glide.with(this)
                .load(ShareVar.urlAddr + documentBeans.get(dnumberInt-1).getDimage())
                .into(HDdetails_ivDimage);

    }//showDetails

    private String connectUpdateData(){
        String result = null;

        try{
            DocumentNetworkTask documentNetworkTask = new DocumentNetworkTask(HaezulgaeDocumentDetailsActivity.this, urlAddr,"update");
            //jsp통해서 받아온 return 값 -> object
            Object obj = documentNetworkTask.execute().get();
            result = (String) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result; //잘끝났으면 1 아니면 0
    }//connectUpdateData

}//------