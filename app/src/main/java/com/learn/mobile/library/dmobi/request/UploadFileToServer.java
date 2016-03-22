package com.learn.mobile.library.dmobi.request;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 09520_000 on 9/17/2015.
 */
public class UploadFileToServer extends AsyncTask<Void, Integer, String> {
    HttpClient httpclient;
    long totalSize = 0;
    DMultiPartEntity entity;
    String filePath;
    String responseString;

    private DResponse.ErrorListener errorListener = null;
    private DResponse.Listener listener = null;
    private DResponse.Complete complete = null;
    private DResponse.PreExecute preExecute = null;
    private DResponse.UpdateProcess updateProcess = null;
    private List<String> files = new ArrayList<>();

    public UploadFileToServer() {
        entity = new DMultiPartEntity(
                new DMultiPartEntity.ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
    }

    public void setCallBack(DResponse.PreExecute preExecute, DResponse.UpdateProcess updateProcess, DResponse.Listener listener, DResponse.ErrorListener errorListener, DResponse.Complete complete) {
        this.preExecute = preExecute;
        this.updateProcess = updateProcess;
        this.listener = listener;
        this.errorListener = errorListener;
        this.complete = complete;
    }

    public void setParams(Map<String, String> params) {
        if (params != null) {
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
                }
            } catch (IOException e) {
                responseString = e.toString();
            }
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.addFile(filePath);
    }

    public void addFile(String filePath) {
        this.files.add(filePath);
    }

    @Override
    protected void onPreExecute() {
        if (preExecute != null) {
            preExecute.onPreExecute();
        }
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (updateProcess != null) {
            updateProcess.onUpdateProcess(progress[0]);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    String url;
    boolean bSuccess = false;

    public void setUrl(String url) {
        this.url = url;
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString;

        httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(url);

        try {
            for (int i = 0; i < this.files.size(); i++) {
                File sourceFile = new File(this.files.get(i));
                entity.addPart("image" + i, new FileBody(sourceFile));
            }

            httppost.setEntity(entity);
            totalSize = entity.getContentLength();

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                bSuccess = true;
                responseString = EntityUtils.toString(r_entity);
            } else {
                bSuccess = false;
                responseString = "Error occurred! Http Status Code: " + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
        if (bSuccess && listener != null) {
            listener.onResponse(result);
        }
        if (!bSuccess && errorListener != null) {
            errorListener.onErrorResponse(result);
        }
        if (complete != null) {
            complete.onComplete(bSuccess, result);
        }
        super.onPostExecute(result);
    }
}
