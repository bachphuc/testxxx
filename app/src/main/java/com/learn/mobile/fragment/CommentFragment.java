package com.learn.mobile.fragment;

import com.learn.mobile.service.SComment;

/**
 * Created by 09520_000 on 10/4/2015.
 */
public class CommentFragment extends ListBaseFragment {
    public CommentFragment() {
        setServiceClass(SComment.class);
        setAutoLoadData(true);
        setRefreshList(true);
    }
}
