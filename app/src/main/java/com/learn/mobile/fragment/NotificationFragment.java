package com.learn.mobile.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.customview.com.github.brnunes.swipeablerecyclerview.SwipeAbleRecyclerViewTouchListener;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.model.Notification;
import com.learn.mobile.service.SNotification;

/**
 * Created by BachPhuc on 5/28/2016.
 */
public class NotificationFragment extends ListBaseFragment {
    SwipeAbleRecyclerViewTouchListener swipeTouchListener;
    SNotification sNotification;

    public NotificationFragment() {
        setServiceClass(SNotification.class);
        setRefreshList(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        sNotification = (SNotification) service;
        swipeTouchListener =
                new SwipeAbleRecyclerViewTouchListener(recyclerView,
                        new SwipeAbleRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.remove(position);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Notification notification = (Notification) adapter.getItem(position);
                                    sNotification.markAsRead(notification);
                                    DMobi.log(TAG, "delete notification id: " + notification.getId() + ", position: " + position);
                                    adapter.remove(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
        return view;
    }

    @Override
    public void onDestroyEvent() {
        if (recyclerView != null && swipeTouchListener != null) {
            recyclerView.removeOnItemTouchListener(swipeTouchListener);
        }
        super.onDestroyEvent();
    }
}
