package com.learn.turtorial1.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 5/17/2015.
 */
public class RequestListObjectResponse<T> {
    public ArrayList<T> data;
    public int complete;
    public int status;
    public String message;

    public RequestListObjectResponse(){

    }
}
