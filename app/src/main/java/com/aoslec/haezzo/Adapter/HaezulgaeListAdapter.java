package com.aoslec.haezzo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aoslec.haezzo.Bean.HaezulgaeListBean;
import com.aoslec.haezzo.Bean.HelperListBean;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HaezulgaeListAdapter extends RecyclerView.Adapter<HaezulgaeListAdapter.ViewHolder>{

    String macIP = ShareVar.macIP;
    private Context mContext = null; //어디서 불렀는지
    private int layout =0;
    private ArrayList<HaezulgaeListBean> data =null; //data
    private LayoutInflater inflater = null ;

    //생성자
    public HaezulgaeListAdapter(Context mContext, int layout, ArrayList<HaezulgaeListBean> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        //4.인플레이터 추가
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    //------------2.ViewHolder 만들기
    //class 안에 class , 제너릭으로 ViewHolder를 부름
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView list_tvdate, HaezulgaeList_tvDgaga, HaezulgaeList_tvDnumber,HaezulgaeList_tvDproduct, HaezulgaeList_tvDtitle,
                HaezulgaeList_tvDdate, HaezulgaeList_tvDplace, HaezulgaeList_tvDmoney;
        public WebView HaezulgaeList_wvImage;

        public Button haezulgaeList_btnDapply;


        public ViewHolder(View itemView) {
            super(itemView);
            //문서번호 추가
            HaezulgaeList_tvDnumber = itemView.findViewById(R.id.haezulgaeList_tvDnumber);
            HaezulgaeList_tvDproduct = itemView.findViewById(R.id.haezulgaeList_tvDproduct);
            HaezulgaeList_tvDtitle = itemView.findViewById(R.id.haezulgaeList_tvDtitle);
            HaezulgaeList_tvDdate = itemView.findViewById(R.id.haezulgaeList_tvDdate);
            HaezulgaeList_tvDplace = itemView.findViewById(R.id.haezulgaeList_tvDplace);
            HaezulgaeList_tvDmoney = itemView.findViewById(R.id.haezulgaeList_tvDmoney);
            HaezulgaeList_wvImage = itemView.findViewById(R.id.haezulgaeList_wvImage);
            //버튼 추가
           //haezulgaeList_btnDapply = itemView.findViewById(R.id.haezulgaeList_btnDapply);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickposition = getAdapterPosition();
                    if (clickposition != RecyclerView.NO_POSITION){
                        if (mListener !=null){
                            mListener.onItemClick(v, clickposition);
                        }
                    }
                }
            }); // setOnClickListener
        }
    }//---viewHolder

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }



    //뷰홀더 만들기
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.haezulgae_custom_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    //뷰홀더 사용
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //배열이므로
        // holder.wv_img.loadData(htmlData(data.get(position).getImg()),"text/html","UTF-8");
        holder.HaezulgaeList_tvDproduct.setText(data.get(position).getDproduct());
        holder.HaezulgaeList_tvDtitle.setText(data.get(position).getDtitle());
        holder.HaezulgaeList_tvDdate.setText(data.get(position).getDdate());
        holder.HaezulgaeList_tvDplace.setText(data.get(position).getDplace());
        holder.HaezulgaeList_tvDmoney.setText(data.get(position).getDmoney());
        //문서번호 추가
        holder.HaezulgaeList_tvDnumber.setText(data.get(position).getDnumber());
        holder.HaezulgaeList_wvImage.loadData(htmlData(data.get(position).getDimage()),"text/html","UTF-8");



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
                        "<img src=\"http://" + macIP + ":8080/test/Haezzo/";
        content += image + "\" alt=\"게시물 사진\" width=\"100%\" height=\"100%\"></body></html>";
        return content;
    }


}
