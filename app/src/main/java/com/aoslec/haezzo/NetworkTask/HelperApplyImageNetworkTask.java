package com.aoslec.haezzo.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HelperApplyImageNetworkTask extends AsyncTask<Integer, String, Integer> {

    private final static String TAG = "NetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    String devicePath;
    ImageView imageView;
    String where;

    public HelperApplyImageNetworkTask(Context context, String mAddr, String devicePath, ImageView imageView) {
        this.context = context;
        this.mAddr = mAddr;
        this.devicePath = devicePath;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Status");
        progressDialog.setMessage("Uploading ....");
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        Log.v(TAG, "onPostExecute()");
        progressDialog.dismiss();

    }

    @Override
    protected void onCancelled() {
        Log.v(TAG,"onCancelled()");
        super.onCancelled();
    }


    @Override
    protected Integer doInBackground(Integer... integers) {
        File file = new File(devicePath);
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url(mAddr)
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.v(TAG, "Success");
            return 1;

        }catch (Exception e){
            Log.v(TAG, "Error");
            e.printStackTrace();
            return 0;
        }
    }
}
