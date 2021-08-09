package com.aoslec.haezzo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aoslec.haezzo.Bean.HelperListBean;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;

import java.util.ArrayList;

public class HelperListAdapter extends RecyclerView.Adapter<HelperListAdapter.ViewHolder>{

    private Context mContext = null; //어디서 불렀는지
    private int layout =0;
    private ArrayList<HelperListBean> data =null; //data
    private LayoutInflater inflater = null ;

    //IP
    String macIP = ShareVar.macIP;


    //생성자
    public HelperListAdapter(Context mContext, int layout, ArrayList<HelperListBean> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        //4.인플레이터 추가
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    //------------2.ViewHolder 만들기
    //class 안에 class , 제너릭으로 ViewHolder를 부름
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView list_tvdate, helperList_tvNickname,helperList_tvAge,helperList_tvFm,helperList_tvHgaga;
        public TextView helperList_tvAddress, helperList_tvHrating;
        public WebView helperList_wvImage;


        public ViewHolder(View itemView) {
            super(itemView);
            list_tvdate = itemView.findViewById(R.id.list_tvdate);

            helperList_tvNickname = itemView.findViewById(R.id.helperList_tvNickname);
            helperList_tvAge = itemView.findViewById(R.id.helperList_tvAge);
            helperList_tvFm = itemView.findViewById(R.id.helperList_tvFm);
            helperList_tvHgaga = itemView.findViewById(R.id.helperList_tvHgaga);
            helperList_tvAddress = itemView.findViewById(R.id.helperList_tvAddress);
            helperList_tvHrating = itemView.findViewById(R.id.helperList_tvHrating);
            helperList_wvImage = itemView.findViewById(R.id.helperList_wvImage);


////            아까 클리커블, 포커서블 해놨음
////            셀모양 눌렀을때? 인가봄
//            itemView.setOnClickListener(new View.OnClickListener() {
//                Intent intent = null;
//                @Override
//                public void onClick(View v) {
//                    //뭘 클릭했는지 (현재 클릭한 위치)
//                    int position = getAdapterPosition();
//                    intent.putExtra("date", data.get(position).getDate());
//                    intent.putExtra("title", data.get(position).getTitle());
//                    intent.putExtra("detail", data.get(position).getDetail());
//                    intent.putExtra("status", data.get(position).getStatus());
//                    intent = Intent(v.getContext(), DiaryUpdateActivity.class);
//
//                    //snackbar 띄울거임
//                    Snackbar.make(v,data.get(position).getTitle(), Snackbar.LENGTH_LONG).setAction("누르셨습니다.",null).show();
//                }
//            });
        }
    }

    //뷰홀더 만들기
    @Override
    public HelperListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_custom_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    //뷰홀더 사용
    @Override
    public void onBindViewHolder(HelperListAdapter.ViewHolder holder, int position) {


        //배열이므로
        // holder.wv_img.loadData(htmlData(data.get(position).getImg()),"text/html","UTF-8");
        holder.helperList_tvNickname.setText(data.get(position).getUnickname());
        holder.helperList_tvAge.setText(data.get(position).getUage());
        holder.helperList_tvFm.setText(data.get(position).getUfm());
        holder.helperList_tvHgaga.setText(data.get(position).getHgaga());
        holder.helperList_tvAddress.setText(data.get(position).getUaddress().substring(data.get(position).getUaddress().indexOf("시 ")+1, data.get(position).getUaddress().indexOf("동")+1));
        holder.helperList_tvHrating.setText(data.get(position).getHrating());
        holder.helperList_wvImage.loadData(htmlData(data.get(position).getUimage()),"text/html","UTF-8");


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(),DiaryUpdateActivity.class);
//                intent.putExtra("date", data.get(position).getDate());
//                intent.putExtra("title", data.get(position).getTitle());
//                intent.putExtra("detail", data.get(position).getDetail());
//                intent.putExtra("status", data.get(position).getStatus());
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//                v.getContext().startActivity(intent);
//            }
//        });



    }//viewholder

    @Override
    public int getItemCount() {
        return data.size();
    }

    //htmlData
    private String htmlData(String image){
        String content =
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"+
                        "<html><head>"+
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf8\"/>"+
                        "<head><body>"+
                        "<img src=\"http://" + macIP +":8080/test/Haezzo/";
        content += image + "\" alt=\"헬퍼\" width=\"100%\" height=\"100%\"></body></html>";
        return content;
    }


}
