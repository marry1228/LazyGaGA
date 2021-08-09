package com.aoslec.haezzo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aoslec.haezzo.Bean.OnDealListBean;
import com.aoslec.haezzo.R;
import com.aoslec.haezzo.ShareVar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OnDealListDoneAdapter extends RecyclerView.Adapter<OnDealListDoneAdapter.ViewHolder> {
    private Context mContext = null;
    private int layout =0;
    private ArrayList<OnDealListBean> data =null;
    private LayoutInflater inflater = null ;

    //IP
    private String macIP = ShareVar.macIP;

    //Integer strdimage = null;

    //생성자
    public OnDealListDoneAdapter(Context mContext, int layout, ArrayList<OnDealListBean> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
    }


    //------------2.ViewHolder 만들기
    //class 안에 class , 제너릭으로 ViewHolder를 부름
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView myhaezzoListDone_tvDproduct,myhaezzoListDone_tvDdate,myhaezzoListDone_tvDplace,
                myhaezzoListDone_tvDstatus,myhaezzoListDone_tvDmoney;
        public ImageView myhaezzoListDone_ivDimg;


        public ViewHolder(View itemView) {
            super(itemView);
            myhaezzoListDone_ivDimg = itemView.findViewById(R.id.myhaezzoListDone_ivDimg);
            myhaezzoListDone_tvDproduct = itemView.findViewById(R.id.myhaezzoListDone_tvDproduct);
            myhaezzoListDone_tvDdate = itemView.findViewById(R.id.myhaezzoListDone_tvDdate);
            myhaezzoListDone_tvDplace = itemView.findViewById(R.id.myhaezzoListDone_tvDplace);
            myhaezzoListDone_tvDstatus = itemView.findViewById(R.id.myhaezzoListDone_tvDstatus);
            myhaezzoListDone_tvDmoney = itemView.findViewById(R.id.myhaezzoListDone_tvDmoney);

            // ----> 화면 클릭시 넘어가기
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
    }

    private OnDealListDoneAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnDealListDoneAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }


    @Override
    public OnDealListDoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ondeal_done_custom_layout,parent,false);
        OnDealListDoneAdapter.ViewHolder viewHolder = new OnDealListDoneAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OnDealListDoneAdapter.ViewHolder holder, int position) {
        holder.myhaezzoListDone_tvDproduct.setText(data.get(position).getDproduct());
        Log.v("Message",data.get(position).getDproduct() );
        holder.myhaezzoListDone_tvDdate.setText(data.get(position).getDdate());
        holder.myhaezzoListDone_tvDplace.setText(data.get(position).getDplace());
        holder.myhaezzoListDone_tvDstatus.setText(data.get(position).getDstatus());
        holder.myhaezzoListDone_tvDmoney.setText(data.get(position).getDmoney());


        Glide.with(mContext)
                .load(ShareVar.urlAddr + data.get(position).getDimage())
                .into(holder.myhaezzoListDone_ivDimg);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }



}//------
