package com.aoslec.haezzo.ActivityDocument;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aoslec.haezzo.Adapter.HaezulgaeListAdapter;
import com.aoslec.haezzo.Bean.HaezulgaeListBean;
import com.aoslec.haezzo.NetworkTask.HaezulgaeListNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;

import java.util.ArrayList;

public class HaezulgaeListActivity extends AppCompatActivity {

    String urlAddr = null;
    ArrayList<HaezulgaeListBean> haezulgaeListBeans;
    HaezulgaeListAdapter haezulgaeListAdapter;

    String macIP = ShareVar.macIP;

    RecyclerView recyclerView = null;
    RecyclerView.LayoutManager layoutManager = null;

    //지원하기 버튼
    Button Dapply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("Message","HaezulgaeListActivity_onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haezulgae_list);

        urlAddr = "http://"+ macIP + ":8080/test/Haezzo/haezulgaeDocumentSelectList.jsp?";

        recyclerView = findViewById(R.id.lv_haezulgaeList);
        layoutManager = new LinearLayoutManager(HaezulgaeListActivity.this);
        recyclerView.setLayoutManager(layoutManager);


    } // onCreate

    //화면이 뜰때: resume, 다시 와도 resume 부터 실행.
    @Override
    protected void onResume() {
        super.onResume();
        //수정된 정보를 부르기 위해
        connectGetData();
    }
    private void connectGetData(){
        try {
            HaezulgaeListNetworkTask haezulgaeListNetworkTask = new HaezulgaeListNetworkTask(HaezulgaeListActivity.this, urlAddr, "select");
            Object obj = haezulgaeListNetworkTask.execute().get();
            Log.v("Message","HelperListActivity_networkTask" + haezulgaeListNetworkTask);
            haezulgaeListBeans = (ArrayList<HaezulgaeListBean>) obj;
            Log.v("Message","HelperListActivity_helperListBeans" + haezulgaeListBeans);

            haezulgaeListAdapter = new HaezulgaeListAdapter(HaezulgaeListActivity.this, R.layout.haezulgae_custom_layout, haezulgaeListBeans);
            haezulgaeListAdapter.setOnItemClickListener(adapterClick);
            recyclerView.setAdapter(haezulgaeListAdapter);



        } catch (Exception e){
            e.printStackTrace();
        }
    }


    //adapter이용-> recyclerview 클릭시 (리스트에서 하나 클릭시)
    HaezulgaeListAdapter.OnItemClickListener adapterClick = new HaezulgaeListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(HaezulgaeListActivity.this, HaezulgaeDocumentDetailsActivity.class);
            String dnumber = haezulgaeListBeans.get(position).getDnumber();
            intent.putExtra("dnumber",dnumber);
            Log.v("Message","dnumber = " + dnumber);
            startActivity(intent);
        }
    };

}//-------