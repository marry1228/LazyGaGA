package com.aoslec.haezzo.ActivityOnDealList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aoslec.haezzo.ActivityDocument.DocumentDoneDetailsActivity;
import com.aoslec.haezzo.Adapter.OnDealListDoneAdapter;
import com.aoslec.haezzo.Bean.OnDealListBean;
import com.aoslec.haezzo.NetworkTask.OnDealListNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;

import java.util.ArrayList;


public class OnDealTabDone extends Fragment {


    private View view;

    String macIP = ShareVar.macIP;
    String urlAddr = "http://"+macIP+":8080/test/Haezzo/myhaezzoDoneSelectList.jsp?";

    ArrayList<OnDealListBean> onDealListBeans;
    OnDealListDoneAdapter onDealListDoneAdapter;

    RecyclerView recyclerView = null;
    RecyclerView.LayoutManager layoutManager = null;

    Intent intent = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Message", "FragmentTabDone Start");
        // Inflate the layout for this fragment
        // 첫번째 프레그먼트 연결작업
        view =  inflater.inflate(R.layout.fragment_done_tab, container, false);
        recyclerView = view.findViewById(R.id.myhaezzoListDone_rvLists);
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
        Log.v("Message", "METHOD : fragment2_connectGetData Start");

        try{
            Log.v("Message", " fragment2 - Before start NetworkTask");

            OnDealListNetworkTask networkTask = new OnDealListNetworkTask(getActivity(), urlAddr, "select");
            Object obj = networkTask.execute().get();
            onDealListBeans = (ArrayList<OnDealListBean>) obj;
            String test = onDealListBeans.get(1).getDnumber();
            Log.v("Message", " fragment2 - myhaezzo(arraylist) : " + test);

            onDealListDoneAdapter = new OnDealListDoneAdapter(getActivity(), R.layout.ondeal_done_custom_layout, onDealListBeans);
            //클릭시 연결
            onDealListDoneAdapter.setOnItemClickListener(adapterClick);
            Log.v("Message", " fragment2 - adapter is... : " + onDealListDoneAdapter);
            recyclerView.setAdapter(onDealListDoneAdapter);

        }catch(Exception e){
            e.printStackTrace();
        }

    }//connectGetData

    // 어댑터 클릭시 값 넘기면서 다음 화면 넘어가기
    OnDealListDoneAdapter.OnItemClickListener adapterClick = new OnDealListDoneAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            intent = new Intent(getActivity(), DocumentDoneDetailsActivity.class);
            String dnumber = onDealListBeans.get(position).getDnumber();
            intent.putExtra("dnumber",dnumber);
            String dstatus = onDealListBeans.get(position).getDstatus();
            ShareVar.Document_dstatus = dstatus;
            Log.v("Message","dnumber = " + dnumber);
            startActivity(intent);
        }
    };



}//--------