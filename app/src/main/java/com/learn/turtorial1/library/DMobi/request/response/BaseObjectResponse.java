package com.learn.turtorial1.library.dmobi.request.response;

import java.util.ArrayList;

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
}
