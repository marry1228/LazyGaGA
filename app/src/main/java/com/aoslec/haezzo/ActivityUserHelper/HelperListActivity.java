package com.aoslec.haezzo.ActivityUserHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aoslec.haezzo.Adapter.HelperListAdapter;
import com.aoslec.haezzo.Bean.HelperListBean;
import com.aoslec.haezzo.ActivityDocument.DocumentWriteActivity;
import com.aoslec.haezzo.NetworkTask.HelperNetworkTask;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;

import java.util.ArrayList;

public class HelperListActivity extends AppCompatActivity {

    String urlAddr = null;//만들어줄 어드레스
    ArrayList<HelperListBean> helperListBeans;
    HelperListAdapter helperListAdapter;

    String macIP = ShareVar.macIP;

    //recyclerview
    RecyclerView recyclerView = null;
    RecyclerView.LayoutManager layoutManager= null;

    //요청하기 버튼
    Button helperList_btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("Message","HelperListActivity_onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_list);

        urlAddr = "http://"+ macIP + ":8080/test/Haezzo/helperSelectList.jsp?";
        //리스트 뷰에서 학번 1 -> 수정 -- finish 없어지고 다시 리스트뷰 화면으로 넘어와서 그 화면에서 바뀐 장면 보여줘야 함
        //아래에 있는 뷰에 정보를 줘야함
        //화면이 뜰때: resume , 다음화면에 있다가 다시 와도 resume 부터 한다.
        //data 갱신에는 resume에 있음 !!!

        //recyclerview 초기화
        recyclerView = findViewById(R.id.lv_helperList);
        //linearLayoutManager(context)
        layoutManager = new LinearLayoutManager(HelperListActivity.this);
        //layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        //recyclerview야 너는 리니어야! 알려주기
        recyclerView.setLayoutManager(layoutManager);

        //요청하기 버튼
        helperList_btnApply = findViewById(R.id.helperList_btnApply);
        helperList_btnApply.setOnClickListener(onClickListener);
        
    }//onCreate
        @Override//*********중요!!!
        protected void onResume() {
            super.onResume();
            //수정된 정보를 부르기 위해
            //맨처음에도 실행, 위에 뭐가 얹혀졌다가 사라져도 실행 되기 위하여
            connectGetData();
        }

        private void connectGetData(){
            //검색하는 화면
            try {
                HelperNetworkTask helperNetworkTask = new HelperNetworkTask(HelperListActivity.this, urlAddr,"select");
                //networktask 가 구동이 되서
                Object obj = helperNetworkTask.execute().get();//프로그레스바 돌고, 데이터 가지러가고
                Log.v("Message","HelperListActivity_networkTask" + helperNetworkTask);
                helperListBeans = (ArrayList<HelperListBean>) obj;
                Log.v("Message","HelperListActivity_helperListBeans" + helperListBeans);

                helperListAdapter = new HelperListAdapter(HelperListActivity.this, R.layout.helper_custom_layout, helperListBeans);
                recyclerView.setAdapter(helperListAdapter);

                //클릭할거냐 롱클릭할거냐는 listView에 속성이 들어가야 함
//                recyclerView.setOnItemClickListener(onItemClickListener);
//                recyclerView.setOnItemLongClickListener(onItemLongClickListener);

            }catch(Exception e){
                e.printStackTrace();
            }

        }//connectGetdata

//        //짧게 눌렀을 때
//        AdapterView.OnItemClickListener onItemClickListener =new AdapterView.OnItemClickListener() {
//            Intent intent = null;
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //click 했으니 intent를 타고서 클릭리스너 수행!?
//
//                intent = new Intent(HaezzoListActivity.this, UpdateActivity.class);
//                intent.putExtra("code", members.get(position).getCode());
//                intent.putExtra("name", members.get(position).getName());
//                intent.putExtra("dept", members.get(position).getDept());
//                intent.putExtra("phone", members.get(position).getPhone());
//                intent.putExtra("macIP", macIP);
//                startActivity(intent);
//
//            }
//        };
//        //길게 눌렀을 때
//        AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
//            Intent intent = null;
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                intent = new Intent(SelectAllActivity.this, DeleteActivity.class);
//                intent.putExtra("code", members.get(position).getCode());
//                intent.putExtra("name", members.get(position).getName());
//                intent.putExtra("dept", members.get(position).getDept());
//                intent.putExtra("phone", members.get(position).getPhone());
//                intent.putExtra("macIP", macIP);
//                startActivity(intent);
//                return true;
//            }
//        };


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HelperListActivity.this, DocumentWriteActivity.class);
            startActivity(intent);
        }
    };
    
}//-----