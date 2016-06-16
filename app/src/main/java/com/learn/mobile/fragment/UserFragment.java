package com.learn.mobile.fragment;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.learn.mobile.service.SUser;

/**
 * Created by 09520_000 on 9/26/2015.
 */
public class UserFragment extends ListBaseFragment {
    public UserFragment() {
        setServiceClass(SUser.class);
        setGirdLayout(true);

        spacesItemDecoration = new UserItemDecoration(4);
    }

    public class UserItemDecoration extends SpacesItemDecoration {
        public UserItemDecoration(int space) {
            super(space);
        }

        @Override
        protected void processOffset(int childCount, int childIndex, Rect outRect, int spanCount,
                                     int spanIndex, int halfSpacing) {
            outRect.left = halfSpacing;
            outRect.top = halfSpacing;
            outRect.right = halfSpacing;
            outRect.bottom = halfSpacing;
            if (isTopEdge(childIndex, spanCount)) {
                outRect.top = spacing;
            }
            if (isBottomEdge(childIndex, childCount, spanCount)) {
                outRect.top = spacing;
            }
            if (isLeftEdge(spanIndex, spanCount)) {
                outRect.left = 0;
            }
            if (isRightEdge(spanIndex, spanCount)) {
                outRect.right = 0;
            }
        }
    }
}
