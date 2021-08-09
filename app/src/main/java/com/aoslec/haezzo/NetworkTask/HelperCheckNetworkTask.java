package com.aoslec.haezzo.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aoslec.haezzo.Bean.HelperBean;
import com.aoslec.haezzo.Bean.HelperListBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HelperCheckNetworkTask extends AsyncTask<Integer,String,Object> {

    //어디로 넘겨주지 ? 필요하므로 컨택스트
    Context context =null;
    //우리 웹서버 어디에 있지? -> Ip주소
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ArrayList<HelperBean> helperBeans;//---> 아 bean 만들어야 겠다

    // Network Task를 검색 입력 수정 삭제 구분없이 하나로 사용키 위해 생성자 변수 추가.
    String where = null;

    //Constructor
    public HelperCheckNetworkTask(Context context, String mAddr, String where) {
        //누가 부른다, 주소는 뭐야 , 입력인지 수정인지 삭제인지
        //예 ) insertActivity가 불럿어, 주소는 뭐고 insert 할거야
        this.context = context;
        this.mAddr = mAddr;
        this.helperBeans = new ArrayList<HelperBean>();//입력할때도 어레이 쓰고 그래야 해서
        //어레이는 불러올때만 쓰면 되는데 그래서 매개변수에 안넣음
        this.where = where;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);//context : insert 등 다 쓰므로 얘가 어떤앤지 알기 위해
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
                if(where.equals("select")){
                    //return 값이 없고
                    parserSelect(stringBuffer.toString());
                }else {
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
            return helperBeans; // select는 엄청 많은 값이 들어올거임
        }else{
            return  result; //입력 수정 삭제는 잘했다 못했다만 넘어 올거고
        }
    }//doinback

    //입력 수정 삭제를 잘 했는지
    //{"result" : "ok"}
    //{} : JSONObject
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
            JSONArray jsonArray = new JSONArray(jsonObject.getString("helper_data"));
            Log.v("Message","jsonArray 진입");
            helperBeans.clear();
            Log.v("Message", "  - parserSelect : helperData clear OK");

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String hnumber = jsonObject1.getString("hnumber");
                String haccountimage = jsonObject1.getString("haccountimage");
                String hname = jsonObject1.getString("hname");
                String hbank = jsonObject1.getString("hbank");
                String haccount = jsonObject1.getString("haccount");
                String hidcardimage = jsonObject1.getString("hidcardimage");
                String hprofileimage = jsonObject1.getString("hprofileimage");
                String hself = jsonObject1.getString("hself");
                String hgaga = jsonObject1.getString("hgaga");
                String hrating = jsonObject1.getString("hrating");
                Log.v("Message","unumber:" + hnumber + "uimage :" + haccountimage);
                //어레이에 있는거 뽑아와서 빈
                HelperBean helperBean = new HelperBean(hnumber,haccountimage,hname,hbank,haccount,hidcardimage,hprofileimage,hself,hgaga,hrating);
                helperBeans.add(helperBean);
                //members는 어레이리스트, member는 빈
                //--->for문 돌면서 차곡차곡 쌓기
            }


        }catch (Exception e){
            Log.v("Message", "Fail to get DB");
            e.printStackTrace();
        }
    }

} // ----
