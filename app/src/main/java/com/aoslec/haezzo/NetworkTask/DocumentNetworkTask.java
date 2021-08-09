package com.aoslec.haezzo.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aoslec.haezzo.Bean.DocumentBean;
import com.aoslec.haezzo.Bean.HelperListBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DocumentNetworkTask extends AsyncTask<Integer,String,Object> {

    Context context =null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ArrayList<DocumentBean> documentBeans;

    String where = null;

    //Constructor
    public DocumentNetworkTask(Context context, String mAddr, String where) {

        this.context = context;
        this.mAddr = mAddr;
        this.documentBeans = new ArrayList<DocumentBean>();
        this.where = where;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialog");
        progressDialog.setMessage("Get....");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
    }
    //--------------------------------progress 작업 끝

    //--------------------------------data 작업

    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v("Message","doInBackGround");
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String result =null; // networktask 잘 했는지 안했는지 받을 거임

        try{
            URL url = new URL(mAddr);//ip주소 -- 생성자 할때 받음
            Log.v("Message","doinBackGround_url" + url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            //서버 받으려면 무조건 httpurl 필요하구나.
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                Log.v("Message","bufferedReader :" + bufferedReader);

                //--> string 인식 하려구!
                while (true){
                    String strline = bufferedReader.readLine();
                    Log.v("Message","strline :" +strline);

                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }

                //이제 JSON 을 만들어 줘야 하므로 구분하자
                //넌 무슨 기능이니? select, insert, delete
                Log.v("message", "DocumentNetwork 구분 시작");
                if(where.equals("select")){
                    //return 값이 없고
                    parserSelect(stringBuffer.toString());

                }
                else {
                    //insert, update, delete
                    //return 값이 있다.
                    result = parserAction(stringBuffer.toString());
                    Log.v("Message","result :" + result);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //
        if(where.equals("select")){
            return documentBeans; // select는 엄청 많은 값이 들어올거임
        }else{
            return  result; //insert, update, delete는 잘했다 못했다만 넘어 올거고
        }
    }//doinback

    // insert, update, delete
    private  String parserAction(String str){
        Log.v("Message","parserAction");
        String returnValue = null;
        try {
            JSONObject jsonObject = new JSONObject(str);
            returnValue = jsonObject.getString("result");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  returnValue;
    }

    //parserSelect--> Select문을 사용할때 씀
    private void parserSelect(String str){
        Log.v("Message","parserSelect");
        try{
            JSONObject jsonObject = new JSONObject(str);
            Log.v("Message","jsonObject 진입");
            JSONArray jsonArray = new JSONArray(jsonObject.getString("document_info"));
            Log.v("Message","jsonArray 진입");
            documentBeans.clear();
            Log.v("Message", "  - parserSelect : helperLists clear OK");

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String dnumber = jsonObject1.getString("dnumber");
                String dgaga = jsonObject1.getString("dgaga");
                String dproduct = jsonObject1.getString("dproduct");
                String dtitle = jsonObject1.getString("dtitle");
                String dimage = jsonObject1.getString("dimage");
                String dcontent = jsonObject1.getString("dcontent");
                String ddate = jsonObject1.getString("ddate");
                String dtime = jsonObject1.getString("dtime");
                String dplace = jsonObject1.getString("dplace");
                String dmoney = jsonObject1.getString("dmoney");
                String dpay = jsonObject1.getString("dpay");
                String unumber = jsonObject1.getString("unumber");
                String hnumber = jsonObject1.getString("hnumber");
                Log.v("Message","dnumber:" + dnumber + "dgaga :" + dgaga);
                //어레이에 있는거 뽑아와서 빈
                DocumentBean documentBean = new DocumentBean(dnumber,dgaga,dproduct,dtitle,dimage,dcontent,ddate,dtime,dplace,dmoney,dpay,unumber,hnumber);
                documentBeans.add(documentBean);
                //members는 어레이리스트, member는 빈
                //--->for문 돌면서 차곡차곡 쌓기
            }

        }catch (Exception e){
            Log.v("Message", "Fail to get DB");
            e.printStackTrace();
        }
    }


}//----
