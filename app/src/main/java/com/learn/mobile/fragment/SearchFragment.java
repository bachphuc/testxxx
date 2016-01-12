package com.learn.mobile.fragment;

import android.view.View;

import com.learn.mobile.adapter.ObservableScrollView;
import com.learn.mobile.service.SSearch;

/**
 * Created by 09520_000 on 1/13/2016.
 */
public class SearchFragment extends ListBaseFragment implements ObservableScrollView {
    public static final String SEARCH_KEY = "search";

    public SearchFragment() {
        setServiceClass(SSearch.class);
    }

    @Override
    public View getScrollView() {
        if (isAdded()) {
            return recyclerView;
        }
        return null;
    }
}
