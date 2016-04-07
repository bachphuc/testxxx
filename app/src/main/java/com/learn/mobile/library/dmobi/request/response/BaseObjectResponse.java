package com.learn.mobile.library.dmobi.request.response;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;

import java.util.ArrayList;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class BaseObjectResponse<T> {
    public int complete;
    public int status;
    public String message;
    public ArrayList<String> errors;

    public BaseObjectResponse() {
    }

    public void setErrorRequest() {
        this.setErrorRequest(DMobi.translate("Can not fetch data from server."));
    }

    public void setErrorRequest(String error) {
        this.status = 0;
        this.complete = 0;
        this.errors = new ArrayList<>();
        this.errors.add(error);
    }

    public String getErrors() {
        String sError = "";
        if (errors != null) {
            for (int i = 0; i < errors.size(); i++) {
                sError = sError + " " + errors.get(i) + ",";
            }
        }
        if (sError.equals("")) {
            sError = DUtils.trimAll(sError, ',');
        }
        return sError;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessfully() {
        if (status != 0) {
            return true;
        }
        return false;
    }

    public boolean isFail() {
        if (status == 0) {
            return true;
        }
        return false;
    }

    public boolean hasData() {
        if (complete == 0) {
            return false;
        }
        return true;
    }
}
