package com.aoslec.haezzo.NetworkTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aoslec.haezzo.Bean.OnDealListBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OnDealListNetworkTask extends AsyncTask<Integer, String, Object> {


    Context context = null;
    String mAddr = null;
    ArrayList<OnDealListBean> onDealListBeans;

    String where = null;

    // Constructor
    public OnDealListNetworkTask(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.onDealListBeans = new ArrayList<OnDealListBean>();
        this.where = where;
    } // constructor

    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v("Message", " Myhaezzolist - doInBackgroud ... start");

        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String result =null; // networktask 잘 했는지 안했는지 받을 거임

        try{
            URL url = new URL(mAddr);//ip주소 -- 생성자 할때 받음
            Log.v("Message", "  - doIn mAddr : " + mAddr);


            Log.v("Message", "  - doIn Http start");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            //서버 받으려면 무조건 httpurl 필요하구나.
            Log.v("Message", "  - doIn Http check 1");
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Log.v("Message", "  - doIn Http check 2");
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                Log.v("Message", "  Myhaezzolist- doIn bufferedReader : " + bufferedReader);
                //--> string 인식 하려구!
                while (true){
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }
                //이제 JSON 을 만들어 줘야 하므로 구분하자
                //넌 무슨 기능이니? select, insert, delete
                if(where.equals("select")){

                    Log.v("Message", " Myhaezzolist - doIn select start");
                    //return 값이 없고
                    parserSelect(stringBuffer.toString());
                }else {
                    //return 값이 있다.
                    result = parserAction(stringBuffer.toString());
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

            Log.v("Message", "  - doIn return workbooks : " + onDealListBeans);
            return onDealListBeans; // select는 엄청 많은 값이 들어올거임
        }else{
            return result; //입력 수정 삭제는 잘했다 못했다만 넘어 올거고
        }

    } //  doInback

    private void parserSelect(String str){

        Log.v("Message", "  - Method parserSelect Start");
        try{
            JSONObject jsonObject = new JSONObject(str);
            Log.v("Message","jsonObject 진입");
            JSONArray jsonArray = new JSONArray(jsonObject.getString("myhaezzo_info"));
            Log.v("Message","jsonArray 진입");
            onDealListBeans.clear();
            Log.v("Message", "  - parserSelect : myhaezzobeans clear OK");

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String dnumber = Integer.toString(jsonObject1.getInt("dnumber"));
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
                String dstatus = jsonObject1.getString("dstatus");
                Log.v("Message", "  - parser json dnumber = "+ dnumber);
                //어레이에 있는거 뽑아와서 빈
                OnDealListBean bean = new OnDealListBean(dnumber, dgaga, dproduct, dtitle, dimage, dcontent, ddate, dtime, dplace, dmoney, dpay, dstatus);
                onDealListBeans.add(bean);
                //members는 어레이리스트, member는 빈
                //--->for문 돌면서 차곡차곡 쌓기
            }


        }catch (Exception e){
            Log.v("Message", "Fail to get DB");
            e.printStackTrace();
        }

    }// parserSelect

    // {"result" : "OK"} 받아오기 위해 만드는 METHOD
    private String parserAction(String str){
        String returnValue = null;
        try{
            JSONObject jsonObject = new JSONObject(str);
            jsonObject.getString("result");
            // 입력이 잘 됐다고 하면 {"result" : "1"} 이 뜰 것이고, 오류면 {"result" : "0"}이 넘어옴.
            returnValue = jsonObject.getString("null");

        }catch (Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }



}//------
