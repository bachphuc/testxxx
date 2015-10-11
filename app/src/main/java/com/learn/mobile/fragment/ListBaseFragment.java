package com.learn.mobile.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.learn.mobile.service.SBase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DFragmentListener.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ListBaseFragment extends Fragment implements Event.Action {
    protected DFragmentListener.OnFragmentInteractionListener mListener;
    protected int layout = 0;
    protected View view;

    protected RecyclerView recyclerView;
    protected DSwipeRefreshLayout dSwipeRefreshLayout;

    protected SBase service;
    protected Class serviceClass;
    protected DResponse.Complete completeResponse;

    protected RecyclerViewBaseAdapter adapter;
    protected boolean bFirstLoaded = false;
    protected int fragmentIndex;

    protected boolean bGirdLayout = false;

    protected boolean bAutoLoadData = false;
    protected boolean bRefreshList = false;
    protected LinearLayoutManager linearLayoutManager;

    protected HashMap<String, Object> requestParams = new HashMap<String, Object>();

    public ListBaseFragment() {

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

    public void setService(SBase service){
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
        recyclerView = (RecyclerView) view.findViewById(R.id.base_recycler_view);
        if (bGirdLayout) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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

        if (requestParams != null && requestParams.size() > 0) {
            service.clearRequestParams();
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                service.setRequestParams(new DRequest.RequestParam(entry.getKey(), entry.getValue()));
            }
        }

        adapter = new RecyclerViewBaseAdapter(service.getData());
        recyclerView.setAdapter(adapter);

        completeResponse = new DResponse.Complete() {
            @Override
            public void onComplete(Boolean statis, Object o) {
                dSwipeRefreshLayout.setRefreshing(false);
                dSwipeRefreshLayout.stopLoadMore();
                if (o != null) {
                    ListObjectResponse<DMobileModelBase> response = (ListObjectResponse<DMobileModelBase>) o;
                    if (response.isSuccessfully() && response.hasData()) {
                        adapter.notifyDataSetChanged();
                    }
                    if (response.isSuccessfully() && !response.hasData()) {
                        dSwipeRefreshLayout.loadMoreFinish();
                    }
                }
            }
        };

        dSwipeRefreshLayout = (DSwipeRefreshLayout) view.findViewById(R.id.base_swipe_refresh_layout);
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
        service.getNews(this.completeResponse);
    }

    public void onLoadMoreData() {
        service.getMores(this.completeResponse);
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
        try {
            mListener = (DFragmentListener.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
}
