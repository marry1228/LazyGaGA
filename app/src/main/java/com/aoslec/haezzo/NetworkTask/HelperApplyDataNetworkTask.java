package com.aoslec.haezzo.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelperApplyDataNetworkTask extends AsyncTask<Integer, String, Object> {
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
//    ArrayList<Students> members;
    //NetworkTask를 검색, 입력, 수정, 삭제 구분 없이 하나로 사용하기 위해 생성자 변수 추가
    String where = null;

    public HelperApplyDataNetworkTask(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.where = where;
//        this.members =  new ArrayList<Students>();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialog");
        progressDialog.setMessage("Get.....");
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



    @Override
    protected Object doInBackground(Integer... integers) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String result = null; //DB 연결이 잘 되었는지 받는 용도

        try {
            URL url = new URL(mAddr);
            Log.v("MessageHi", url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000); //10초 동안 연결 시도하다가 실패시 에러

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }
                if (where.equals("select")) {
//                    parserSelect(stringBuffer.toString());
                }else{
                    Log.v("MessageHi", stringBuffer.toString());
                    result = parserAction(stringBuffer.toString());
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
//        if (where.equals("select")){
//            return members;
//        }else {
//            return result;
//        }
        return result;
    }

//    private void parserSelect(String str) {
//        try{
//            JSONObject jsonObject = new JSONObject(str);
//            JSONArray jsonArray = new JSONArray(jsonObject.getString("students_info"));
//            members.clear(); //기존에 쌓일 수 있는 데이터를 삭제함
//
//            for (int i=0; i<jsonArray.length(); i++){
//                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//                String name = jsonObject1.getString("name");
//                Log.v("Status", "name : "+name);
//                String dept = jsonObject1.getString("dept");
//                Log.v("Status", "dept : "+dept);
//                String code = jsonObject1.getString("code");
//                Log.v("Status", "code : "+code);
//                String phone = jsonObject1.getString("phone");
//                Log.v("Status", "phone : "+phone);
//
//
//                Students student = new Students(code,name,dept,phone);
//                members.add(student);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    private String parserAction(String str) {
        String returnValue = null;
        try{
            JSONObject jsonObject = new JSONObject(str);
            returnValue = jsonObject.getString("result");
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }

}
