package com.learn.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.service.SChat;
import com.learn.mobile.service.SDChat;

/**
 * Created by BachPhuc on 4/25/2016.
 */

public class DChatListFragment extends ListBaseFragment {
    public static final String TAG = ChatFragment.class.getSimpleName();
    SChat chatService;

    public DChatListFragment() {
        setServiceClass(SDChat.class);
        setAutoLoadData(false);
        setRefreshList(false);
        bAllowLoadMoreList = false;
        bAllowPullToRefresh = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initChatService();

        return view;
    }

    public void initChatService() {
        chatService = (SChat) DMobi.getService(SDChat.class);

        chatService.onReady(new SChat.SocketChatEventListener() {
            @Override
            public void onListener(Object data) {
                DMobi.log(TAG, "yeah login success");
            }
        });

        chatService.addUserJoinCallbacks(new SChat.SocketChatEventListener() {
            @Override
            public void onListener(Object data) {
                DMobi.log(TAG, "user join");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        });
        chatService.init();
    }
}
