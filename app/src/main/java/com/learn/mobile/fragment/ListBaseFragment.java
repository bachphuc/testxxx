package com.learn.mobile.fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.R;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.DSwipeRefreshLayout;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.ResourceUtils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SBase;

import java.util.HashMap;
import java.util.List;

import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.recyclerview.holder.HeaderHolder;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DFragmentListener.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ListBaseFragment extends DFragmentBase implements Event.Action, ObservableFragment {
    public static final String TAG = ListBaseFragment.class.getSimpleName();
    protected DFragmentListener.OnFragmentInteractionListener mListener;
    protected int layout = 0;
    protected View view;

    protected RecyclerView recyclerView;
    protected DSwipeRefreshLayout dSwipeRefreshLayout;

    protected SBase service;
    protected Class serviceClass;
    protected DResponse.Complete refreshCompleteResponse;
    protected DResponse.Complete loadMoreCompleteResponse;

    protected RecyclerViewBaseAdapter adapter;
    protected boolean bFirstLoaded = false;
    protected int fragmentIndex;

    protected boolean bGirdLayout = false;
    protected int spanSize = 2;
    protected int spaceDivide = 8;

    protected boolean bAutoLoadData = false;
    protected boolean bRefreshList = false;
    protected List<DMobileModelBase> data;

    public void setData(List<DMobileModelBase> list) {
        data = list;
    }

    protected LinearLayoutManager linearLayoutManager;
    protected GridLayoutManager gridLayoutManager;
    protected SpacesItemDecoration spacesItemDecoration;

    // allow pull to refresh list
    protected boolean bAllowPullToRefresh = true;
    // allow load more list when scroll to bottom
    protected boolean bAllowLoadMoreList = true;

    protected User user;

    protected HashMap<String, Object> requestParams = new HashMap<>();

    protected boolean bHasAppBar = false;

    protected boolean bScrollBottomTopWhenLoadMoreFinish = false;

    public void setScrollToBottomWhenLoadMoreFinish(boolean b) {
        bScrollBottomTopWhenLoadMoreFinish = b;
    }

    protected boolean bScrollBottomAnimate = false;

    public void setScrollBottomAnimate(boolean b) {
        bScrollBottomAnimate = b;
    }

    public void setHasAppBar(boolean b) {
        bHasAppBar = b;
    }

    public ListBaseFragment() {

    }

    public void setSpanSize(int size) {
        spanSize = size;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAutoLoadData(boolean b) {
        bAutoLoadData = b;
    }

    public void setRefreshList(boolean b) {
        bRefreshList = b;
    }

    public void setParam(String key, Object o) {
        requestParams.put(key, o);
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public void setService(SBase service) {
        this.service = service;
    }

    public void setFragmentIndex(int index) {
        fragmentIndex = index;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public int getLayout() {
        return layout;
    }

    public void scrollToBottom(boolean animate) {
        if (recyclerView != null) {
            if (recyclerView.getAdapter().getItemCount() > 0) {
                if (!animate) {
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                } else {
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                }
            }
        }
    }

    public void setGirdLayout(boolean bGirdLayout) {
        this.bGirdLayout = bGirdLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // TODO Disable or enable swipeRefreshLayout
    public void setEnableRefresh(boolean b) {
        dSwipeRefreshLayout.setEnabled(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (layout != 0) {
            view = inflater.inflate(layout, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_list_base, container, false);
        }

        initializeView();

        initializeEvent();

        if (bAutoLoadData) {
            startLoad();
        }
        return view;
    }

    private void initializeView() {
        dSwipeRefreshLayout = (DSwipeRefreshLayout) view.findViewById(R.id.base_swipe_refresh_layout);

        recyclerView = (RecyclerView) view.findViewById(R.id.base_recycler_view);
        if (spacesItemDecoration == null) {
            spacesItemDecoration = new SpacesItemDecoration(spaceDivide);
        }
        if (bGirdLayout) {
            gridLayoutManager = new GridLayoutManager(getContext(), spanSize);
            if (bHasAppBar) {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position == 0) {
                            return spanSize;
                        }
                        return 1;
                    }
                });
                spacesItemDecoration.setHasHeader(true);
            }
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(spacesItemDecoration);
        } else {
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        if (service == null) {
            if (bRefreshList) {
                service = DMobi.getInstance(serviceClass);
            } else {
                service = DMobi.getService(serviceClass);
            }
        }

        if (user != null) {
            assert service != null;
            service.setUser(user);
        }

        if (requestParams != null && requestParams.size() > 0) {
            service.clearRequestParams();
            service.addRequestParams(requestParams);
        }
        if (data == null) {
            adapter = new RecyclerViewBaseAdapter(service.getData());
        } else {
            adapter = new RecyclerViewBaseAdapter(data);
        }
        adapter.setService(service);
        if (bHasAppBar) {
            int layoutHeader = R.layout.item_header_main_spacing;
            if (user != null) {
                layoutHeader = R.layout.item_header_spacing;
            }

            final int finalLayoutHeader = layoutHeader;
            RecyclerView.Adapter recyclerAdapter = new SimpleRecyclerViewAdapter(adapter) {
                @Override
                public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return null;
                }

                @Override
                public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return new HeaderHolder(layoutInflater, viewGroup, finalLayoutHeader);
                }
            };
            recyclerView.setAdapter(recyclerAdapter);

            int actionBarSize = ResourceUtils.getActionBarSize(getContext());
            int progressViewStart = getResources().getDimensionPixelSize(R.dimen.app_bar_height) - actionBarSize;
            int progressViewEnd = progressViewStart + (int) (actionBarSize * 1.5f);
            dSwipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);
            dSwipeRefreshLayout.setHasHeader(true);

        } else {
            recyclerView.setAdapter(adapter);
        }
    }

    public void initializeEvent() {
        if (bAllowPullToRefresh) {
            refreshCompleteResponse = new DResponse.Complete() {
                @Override
                public void onComplete(Boolean status, Object o) {
                    dSwipeRefreshLayout.setRefreshing(false);
                    dSwipeRefreshLayout.stopLoadMore();
                    if (o != null) {
                        ListObjectResponse<DMobileModelBase> response = (ListObjectResponse<DMobileModelBase>) o;
                        if (response.isSuccessfully() && response.hasData()) {
                            // adapter.notifyDataSetChanged();
                            adapter.notifyItemRangeInserted(0, response.data.size());
                            adapter.fireNotifyDataSetChanged();
                        }
                        if (response.isSuccessfully() && !response.hasData()) {
                            dSwipeRefreshLayout.loadMoreFinish();
                        }
                    }
                }
            };

            dSwipeRefreshLayout.setOnRefreshListener(new DSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onRefreshData();
                }
            });
        } else {
            dSwipeRefreshLayout.setEnabled(false);
        }

        if (bAllowLoadMoreList) {
            loadMoreCompleteResponse = new DResponse.Complete() {
                @Override
                public void onComplete(Boolean status, Object o) {
                    dSwipeRefreshLayout.setRefreshing(false);
                    dSwipeRefreshLayout.stopLoadMore();
                    if (o != null) {
                        ListObjectResponse<DMobileModelBase> response = (ListObjectResponse<DMobileModelBase>) o;
                        if (response.isSuccessfully() && response.hasData()) {
                            adapter.notifyItemRangeInserted(adapter.getItemCount() - response.data.size() - 1, response.data.size());
                            // adapter.notifyDataSetChanged();
                            adapter.fireNotifyDataSetChanged();
                            if (bScrollBottomTopWhenLoadMoreFinish) {
                                scrollToBottom(bScrollBottomAnimate);
                            }
                        }
                        if (response.isSuccessfully() && !response.hasData()) {
                            dSwipeRefreshLayout.loadMoreFinish();
                        }
                    }
                }
            };

            dSwipeRefreshLayout.setOnLoadMoreListener(new DSwipeRefreshLayout.LoadMoreListener() {
                @Override
                public void loadMore() {
                    onLoadMoreData();
                }
            });
        } else {
            dSwipeRefreshLayout.setEnableLoadMore(false);
        }

        DMobi.registerEvent(Event.EVENT_LIST_BASE_FRAGMENT_LOADED + "_" + fragmentIndex, new Event.Action() {
            @Override
            public void fireAction(String eventType, Object o) {
                adapter.notifyDataSetChanged();
                startLoad();
            }
        });

        DMobi.registerEvent(Event.EVENT_LOCK_REFRESH_RECYCLER_VIEW, this);
    }

    @Override
    public void onDestroyEvent() {
        if (dSwipeRefreshLayout != null) {
            dSwipeRefreshLayout.setOnRefreshListener(null);
            dSwipeRefreshLayout.setOnLoadMoreListener(null);
        }
        DMobi.destroyEvent(Event.EVENT_LIST_BASE_FRAGMENT_LOADED + "_" + fragmentIndex);
        DMobi.destroyEvent(Event.EVENT_LOCK_REFRESH_RECYCLER_VIEW);
        super.onDestroyEvent();
    }

    @Override
    public void fireAction(String eventType, Object o) {
        // TODO Process events
        switch (eventType) {
            case Event.EVENT_LOCK_REFRESH_RECYCLER_VIEW:
                boolean canRefresh = (boolean) o;
                setEnableRefresh(canRefresh);
                break;
        }
    }

    public void startLoad() {
        if (bFirstLoaded) {
            return;
        }
        bFirstLoaded = true;
        dSwipeRefreshLayout.startLoad();
    }

    public void onRefreshData() {
        service.getNews(this.refreshCompleteResponse);
    }

    public void onLoadMoreData() {
        service.getMores(this.loadMoreCompleteResponse);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void append(DMobileModelBase item) {
        service.append(item);
        adapter.notifyDataSetChanged();
    }

    public void prepend(DMobileModelBase item) {
        service.prepend(item);
        adapter.notifyDataSetChanged();
    }

    public SBase getService() {
        return service;
    }

    public RecyclerViewBaseAdapter getAdapter() {
        return adapter;
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public View getScrollTarget() {
        if (isAdded()) {
            return recyclerView;
        }
        return null;
    }

    @Override
    public boolean onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset) {
        return Utils.syncOffset(smoothAppBarLayout, target, verticalOffset, getScrollTarget());
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        protected int spacing;

        protected float[] points;

        protected boolean bHasHeader = false;
        protected int totalSpan = 0;

        public void reset() {
            totalSpan = 0;
            points = null;
        }

        public void setTotalSpan(int i) {
            totalSpan = i;
            getPoints();
        }

        public void setHasHeader(boolean b) {
            bHasHeader = b;
        }

        public SpacesItemDecoration(int space) {
            this.spacing = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            int halfSpacing = spacing / 2;

            int childCount = parent.getAdapter().getItemCount();

            int childIndex = parent.getChildAdapterPosition(view);

            int spanCount = getTotalSpan(parent);
            int spanIndex = childIndex % spanCount;

            processOffset(childCount, childIndex, outRect, spanCount, spanIndex, halfSpacing);
        }

        protected void processOffset(int childCount, int childIndex, Rect outRect, int spanCount, int spanIndex, int halfSpacing) {
            if (bHasHeader && childIndex == 0) {
                outRect.top = spacing;
                outRect.bottom = 0;
                outRect.left = spacing;
                outRect.right = spacing;
                return;
            }

            if (bHasHeader) {
                childCount = childCount - 1;
                childIndex = childIndex - 1;
            }

            if (spanCount < 1) return;

            outRect.top = halfSpacing;
            outRect.bottom = halfSpacing;
            outRect.left = halfSpacing;
            outRect.right = halfSpacing;

            if (isTopEdge(childIndex, spanCount)) {
                outRect.top = spacing;
            }

            if (isBottomEdge(childIndex, childCount, spanCount)) {
                outRect.bottom = spacing;
            }

            if (points != null && points.length > spanIndex * 2 + 1) {
                outRect.left = Math.round(points[spanIndex * 2]);
                outRect.right = Math.round(points[spanIndex * 2 + 1]);
            }
        }

        protected boolean isLeftEdge(int spanIndex, int spanCount) {

            return spanIndex == 0;
        }

        protected boolean isRightEdge(int spanIndex, int spanCount) {

            return spanIndex == spanCount - 1;
        }

        protected boolean isTopEdge(int childIndex, int spanCount) {
            return childIndex < spanCount;
        }

        protected boolean isBottomEdge(int childIndex, int childCount, int spanCount) {
            int row = childCount % spanCount;
            int mod = (row > 0 ? row : spanCount);

            return childIndex >= childCount - mod;
        }

        protected int getTotalSpan(RecyclerView parent) {
            if (totalSpan != 0) {
                return totalSpan;
            }
            RecyclerView.LayoutManager mgr = parent.getLayoutManager();
            if (mgr instanceof GridLayoutManager) {
                totalSpan = ((GridLayoutManager) mgr).getSpanCount();
            } else if (mgr instanceof StaggeredGridLayoutManager) {
                totalSpan = ((StaggeredGridLayoutManager) mgr).getSpanCount();
            }
            getPoints();
            return totalSpan;
        }

        protected void getPoints() {
            if (totalSpan == 0) {
                return;
            }

            points = new float[totalSpan * 2];
            points[0] = spacing;
            float n = totalSpan, s = spacing, W = 300, w = (W - (n + 1) * s) / n, aw = W / n;
            for (int i = 1; i < 2 * n; i++) {
                int col = i % 2;
                float val = s - points[i - 1];
                if (col == 1) {
                    val = aw - points[i - 1] - w;
                }
                points[i] = val;
            }
        }
    }
}
