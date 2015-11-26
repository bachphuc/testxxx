package com.learn.mobile.library.dmobi.request.response;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class BaseObjectResponse<T> {
    public int complete;
    public int status;
    public String message;
    public ArrayList<String> errors;

    public BaseObjectResponse(){
    }

    public void setErrorRequest(){
        this.status = 0;
        this.complete = 0;
        this.errors = new ArrayList<String>();
        this.errors.add(DMobi.translate("Can not fetch data from server."));
    }

    public String getErrors(){
        String sError = "";
        if(errors != null){
            for(int i = 0; i< errors.size() ; i++){
                sError = sError + " " + errors.get(i) + ",";
            }
        }
        if(sError != ""){
            sError = DUtils.trimAll(sError, ',');
        }
        return  sError;
    }

    public String getMessage(){
        return message;
    }

    public boolean isSuccessfully(){
        if (status != 0){
            return true;
        }
        return false;
    }

    public boolean isFail(){
        if(status == 0){
            return true;
        }
        return false;
    }

    public boolean hasData(){
        if(complete == 0){
            return false;
        }
        return true;
    }
}
