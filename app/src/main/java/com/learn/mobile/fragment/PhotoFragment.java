package com.learn.mobile.fragment;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.learn.mobile.R;
import com.learn.mobile.adapter.ObservableScrollView;
import com.learn.mobile.service.SPhoto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PhotoFragment extends ListBaseFragment implements ObservableScrollView {
    protected int itemMaxWidth = 240;

    public PhotoFragment() {
        setServiceClass(SPhoto.class);
        setGirdLayout(true);
        setSpanSize(3);
        setRefreshList(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View superView = super.onCreateView(inflater, container, savedInstanceState);
        calculatorSize();
        return superView;
    }

    @Override
    public View getScrollView() {
        if (isAdded()) {
            return recyclerView;
        }
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (spacesItemDecoration != null) {
            spacesItemDecoration.reset();
        }

        calculatorSize();
    }

    public void calculatorSize() {
        Log.i("PhotoFragment", "calculatorSize");
        if (view == null) {
            return;
        }
        final FrameLayout layout = (FrameLayout) view.findViewById(R.id.fragment_list_base);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                // layout size
                int width = layout.getMeasuredWidth();
                Log.i("PhotoFragment", "calculatorSize:width: " + width);
                int count = 3;
                int paddingBottom = width / count;
                if (paddingBottom > itemMaxWidth) {
                    count = width / itemMaxWidth;
                    float mod = width % 200;
                    if (mod == 0) {
                    } else if (mod > itemMaxWidth / 2) {
                        count++;
                    }
                }
                spanSize = count;
                gridLayoutManager.setSpanCount(count);
                spacesItemDecoration.setTotalSpan(spanSize);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }
}
