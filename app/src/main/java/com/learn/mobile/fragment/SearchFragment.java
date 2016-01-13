package com.learn.mobile.fragment;

import android.view.View;

import com.learn.mobile.adapter.ObservableScrollView;
import com.learn.mobile.service.SBase;
import com.learn.mobile.service.SSearch;

/**
 * Created by 09520_000 on 1/13/2016.
 */
public class SearchFragment extends ListBaseFragment implements ObservableScrollView {

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

    public void search(String query) {
        if (service != null) {
            service.clear();
            notifyDataSetChanged();
            service.addRequestParam(SSearch.SEARCH_QUERY, query);
            if(dSwipeRefreshLayout != null){
                dSwipeRefreshLayout.startLoad();
            }
        } else {
            setParam(SSearch.SEARCH_QUERY, query);
        }
    }
}
