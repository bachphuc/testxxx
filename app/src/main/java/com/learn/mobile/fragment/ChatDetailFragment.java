package com.learn.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.model.ChatMessage;
import com.learn.mobile.model.ChatUser;
import com.learn.mobile.service.SChat;

/**
 * Created by BachPhuc on 4/19/2016.
 */
public class ChatDetailFragment extends ListBaseFragment {
    public static final String TAG = ChatDetailFragment.class.getSimpleName();

    private ChatUser chatUser;
    private SChat socketService;
    SChat.SocketChatEventListener newMessageListener;
    SChat.SocketChatEventListener readyListener;

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }

    public ChatDetailFragment() {
        setServiceClass(SChat.class);
        setAutoLoadData(false);
        setRefreshList(false);
        bAllowLoadMoreList = false;
        bAllowPullToRefresh = false;
        setHasAppBar(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // todo process chat socket service listener
        socketService = (SChat) service;
        newMessageListener = new SChat.SocketChatEventListener() {
            @Override
            public void onListener(final Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DMobi.log(TAG, "newMessageListener");
                        if (data instanceof ChatMessage) {
                            DMobi.log(TAG, "newMessageListener chat message");
                            ChatMessage chatMessage = (ChatMessage) data;
                            if (chatUser.getTitle().equals(chatMessage.getTitle())
                                    || (socketService.getUsername().equals(chatMessage.getTitle()))) {
                                DMobi.log(TAG, "update new message in list");
                                notifyDataSetChanged();
                                scrollToBottom(true);
                            }
                        }
                    }
                });
            }
        };
        socketService.onNewMessageListener(newMessageListener);

        readyListener = new SChat.SocketChatEventListener() {
            @Override
            public void onListener(Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DMobi.log(TAG, "start chat with " + chatUser.getTitle());
                        socketService.startChat(chatUser.getTitle());
                    }
                });
            }
        };
        socketService.onReady(readyListener);

        socketService.init();
        return view;
    }

    @Override
    public void onDestroyEvent() {
        socketService.removeOnNewMessageListener(newMessageListener);
        socketService.removeOnReadyListener(readyListener);
        readyListener = null;
        newMessageListener = null;
        socketService = null;
        super.onDestroyEvent();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
