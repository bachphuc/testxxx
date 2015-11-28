package com.learn.mobile.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.customview.com.github.brnunes.swipeablerecyclerview.SwipeAbleRecyclerViewTouchListener;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        SwipeAbleRecyclerViewTouchListener swipeTouchListener =
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
                                    adapter.remove(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
        return view;
    }
}
