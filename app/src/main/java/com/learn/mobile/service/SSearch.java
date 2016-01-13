package com.learn.mobile.service;

import com.learn.mobile.model.Search;

/**
 * Created by phuclb on 1/12/2016.
 */
public class SSearch extends SBase {
    public static final String SEARCH_QUERY = "search";

    public SSearch() {
        itemClass = Search.class;
        getDataByPage = true;
    }
}
