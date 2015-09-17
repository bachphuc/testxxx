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
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.service.SBase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DFragmentListener.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListBaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DFragmentListener.OnFragmentInteractionListener mListener;

    // fragment layout
    private int layout = 0;
    private View view;

    private RecyclerView recyclerView;
    private DSwipeRefreshLayout dSwipeRefreshLayout;

    private SBase service;
    Class serviceClass;
    DResponse.Complete completeResponse;

    private RecyclerViewBaseAdapter adapter;
    private boolean bStartLoad = false;
    private int fragmentIndex;

    private boolean bGirdLayout = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListBaseFragment newInstance(String param1, String param2) {
        ListBaseFragment fragment = new ListBaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListBaseFragment() {
        // Required empty public constructor
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
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

    public void setbGirdLayout(boolean bGirdLayout) {
        this.bGirdLayout = bGirdLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        return view;
    }

    private void initializeView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.base_recycler_view);
        if (bGirdLayout) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        service = DMobi.getService(serviceClass);

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
    }

    public void startLoad() {
        if (bStartLoad) {
            return;
        }
        bStartLoad = true;
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
}
