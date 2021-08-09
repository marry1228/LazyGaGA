package com.aoslec.haezzo.ActivityOnDealList;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aoslec.haezzo.ActivityDocument.DocumentDoingDetailsActivity;
import com.aoslec.haezzo.ActivityDocument.DocumentWaitingDetailsActivity;
import com.aoslec.haezzo.Adapter.OnDealListIngAdapter;
import com.aoslec.haezzo.Bean.OnDealListBean;
import com.aoslec.haezzo.NetworkTask.OnDealListNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;

import java.util.ArrayList;



public class OnDealTabIng extends Fragment {

    private View view;

    String macIP = ShareVar.macIP;
    String urlAddr = "http://"+macIP+":8080/test/Haezzo/myhaezzoIngSelectList.jsp?";

    ArrayList<OnDealListBean> onDealListBeans;
    OnDealListIngAdapter onDealListIngAdapter;

    RecyclerView recyclerView = null;
    RecyclerView.LayoutManager layoutManager = null;

    Intent intent = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Message", "FragmentTabIng Start");
        // Inflate the layout for this fragment
        // 첫번째 프레그먼트 연결작업
        view =  inflater.inflate(R.layout.fragment_ing_tab, container, false);
        recyclerView = view.findViewById(R.id.myhaezzo_rvLists);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //method
        connectGetData();

    }


    private void connectGetData(){
        Log.v("Message", "METHOD : fragment1_connectGetData Start");

        try{
            Log.v("Message", " fragment1 - Before start NetworkTask");

            OnDealListNetworkTask networkTask = new OnDealListNetworkTask(getActivity(), urlAddr, "select");
            Object obj = networkTask.execute().get();
            onDealListBeans = (ArrayList<OnDealListBean>) obj;
            Log.v("Message", " fragment - myhaezzo(arraylist) : " + onDealListBeans);

            onDealListIngAdapter = new OnDealListIngAdapter(getActivity(), R.layout.ondeal_ing_custom_layout, onDealListBeans);
            //클릭시 연결
            onDealListIngAdapter.setOnItemClickListener(adapterClick);
            Log.v("Message", " fragment - adapter is... : " + onDealListIngAdapter);
            recyclerView.setAdapter(onDealListIngAdapter);

        }catch(Exception e){
            e.printStackTrace();
        }

    }//connectGetData


    // 어댑터 클릭시 값 넘기면서 다음 화면 넘어가기
    OnDealListIngAdapter.OnItemClickListener adapterClick = new OnDealListIngAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            String dnumber = onDealListBeans.get(position).getDnumber();
            String dstatus = onDealListBeans.get(position).getDstatus();
            ShareVar.Document_dstatus = dstatus;
            if(ShareVar.Document_dstatus.equals("대기중")){
                intent = new Intent(getActivity(), DocumentWaitingDetailsActivity.class);
                intent.putExtra("dnumber",dnumber);
                intent.putExtra("dstatus",ShareVar.Document_dstatus);
                Log.v("Message","onDealTabwaiting_dnumber = " + dnumber);
                startActivity(intent);

            }
            if(ShareVar.Document_dstatus.equals("진행중")){
                intent = new Intent(getActivity(), DocumentDoingDetailsActivity.class);
                intent.putExtra("dnumber",dnumber);
                intent.putExtra("dstatus",ShareVar.Document_dstatus);
                Log.v("Message","onDealTabdoing_dnumber = " + dnumber);
                startActivity(intent);
            }
        }
    };


}