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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.R;
import com.learn.mobile.adapter.RecyclerViewBaseAdapter;
import com.learn.mobile.customview.DSwipeRefreshLayout;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SBase;

import java.util.HashMap;
import java.util.Map;

import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.smoothappbarlayout.utils.ResourceUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DFragmentListener.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ListBaseFragment extends Fragment implements Event.Action {
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

    protected boolean bAutoLoadData = false;
    protected boolean bRefreshList = false;
    protected LinearLayoutManager linearLayoutManager;

    protected User user;

    protected HashMap<String, Object> requestParams = new HashMap<String, Object>();

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
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(8);
        if (bGirdLayout) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanSize);
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
            service.setUser(user);
        }

        if (requestParams != null && requestParams.size() > 0) {
            service.clearRequestParams();
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                service.setRequestParams(new DRequest.RequestParam(entry.getKey(), entry.getValue()));
            }
        }
        adapter = new RecyclerViewBaseAdapter(service.getData());

        if (bHasAppBar) {
            RecyclerView.Adapter recyclerAdapter = new SimpleRecyclerViewAdapter(adapter) {
                @Override
                public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return null;
                }

                @Override
                public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return new HeaderHolder(layoutInflater, viewGroup, R.layout.item_header_spacing);
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

        refreshCompleteResponse = new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                dSwipeRefreshLayout.setRefreshing(false);
                dSwipeRefreshLayout.stopLoadMore();
                if (o != null) {
                    ListObjectResponse<DMobileModelBase> response = (ListObjectResponse<DMobileModelBase>) o;
                    if (response.isSuccessfully() && response.hasData()) {
                        adapter.notifyDataSetChanged();
                        adapter.fireNotifyDataSetChanged();
                    }
                    if (response.isSuccessfully() && !response.hasData()) {
                        dSwipeRefreshLayout.loadMoreFinish();
                    }
                }
            }
        };

        loadMoreCompleteResponse = new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                dSwipeRefreshLayout.setRefreshing(false);
                dSwipeRefreshLayout.stopLoadMore();
                if (o != null) {
                    ListObjectResponse<DMobileModelBase> response = (ListObjectResponse<DMobileModelBase>) o;
                    if (response.isSuccessfully() && response.hasData()) {
                        adapter.notifyDataSetChanged();
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

        dSwipeRefreshLayout.setOnRefreshListener(new DSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });

        dSwipeRefreshLayout.setOnLoadMoreListener(new DSwipeRefreshLayout.LoadMoreListener() {
            @Override
            public void loadMore() {
                onLoadMoreData();
            }
        });
    }

    public void initializeEvent() {
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

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        private boolean bHasHeader = false;

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

            int spanCount = getTotalSpan(view, parent);

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
            int spanIndex = childIndex % spanCount;

            if (spanCount < 1) return;

            outRect.top = halfSpacing;
            outRect.bottom = halfSpacing;
            outRect.left = halfSpacing;
            outRect.right = halfSpacing;

            if (isTopEdge(childIndex, spanCount)) {
                outRect.top = spacing;
            }

            if (isLeftEdge(spanIndex, spanCount)) {
                outRect.left = spacing;
            }

            if (isRightEdge(spanIndex, spanCount)) {
                outRect.right = spacing;
            }

            if (isBottomEdge(childIndex, childCount, spanCount)) {
                outRect.bottom = spacing;
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
            return childIndex >= childCount - row;
        }

        protected int getTotalSpan(View view, RecyclerView parent) {

            RecyclerView.LayoutManager mgr = parent.getLayoutManager();
            if (mgr instanceof GridLayoutManager) {
                return ((GridLayoutManager) mgr).getSpanCount();
            } else if (mgr instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) mgr).getSpanCount();
            }

            return -1;
        }
    }
}
