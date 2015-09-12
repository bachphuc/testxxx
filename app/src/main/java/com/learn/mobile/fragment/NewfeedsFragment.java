package com.learn.mobile.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.FeedAdapter;
import com.learn.mobile.R;
import com.learn.mobile.customview.DSwipeRefreshLayout;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.Dresponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DmobileModelBase;
import com.learn.mobile.service.SFeed;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewFeedsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewFeedsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewFeedsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    DSwipeRefreshLayout dSwipeRefreshLayout;
    FeedAdapter feedAdapter;
    int iPage = 0;
    int iMaxFeedId = 0;
    SFeed sFeed;

    View rootView;

    Dresponse.Complete completeResponse;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewFeedsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewFeedsFragment newInstance(String param1, String param2) {
        NewFeedsFragment fragment = new NewFeedsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewFeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void refreshFeed() {
        SFeed feedService = (SFeed) DMobi.getService("SFeed");
        feedService.loadNewFeeds(this.completeResponse);
    }

    public void loadMoreFeed() {
        SFeed feedService = (SFeed) DMobi.getService("SFeed");
        feedService.loadMoreFeeds(this.completeResponse);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_feed, container, false);
        this.rootView = view;

        // Initialize recycler view
        initRecycleView();

        // Initialize event
        DMobi.registerEvent(Event.EVENT_LOADMORE_FEED, new Event.Action() {
            @Override
            public void fireAction(String eventType, Object o) {
                dSwipeRefreshLayout.startLoad();
            }
        });
        return view;
    }

    public void setEnableRefresh(boolean b){
        dSwipeRefreshLayout.setEnabled(b);
    }

    private void initRecycleView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        sFeed = (SFeed) DMobi.getService(SFeed.class);

        feedAdapter = new FeedAdapter(sFeed.getFeedData());
        recyclerView.setAdapter(feedAdapter);

        completeResponse = new com.learn.mobile.library.dmobi.request.Dresponse.Complete() {
            @Override
            public void onComplete(Object o) {
                dSwipeRefreshLayout.setRefreshing(false);
                dSwipeRefreshLayout.stopLoadMore();
                if (o != null) {
                    ListObjectResponse<DmobileModelBase> response = (ListObjectResponse<DmobileModelBase>) o;
                    if (response.isSuccessfully() && response.hasData()) {
                        feedAdapter.notifyDataSetChanged();
                    }
                    if (response.isSuccessfully() && !response.hasData()) {
                        dSwipeRefreshLayout.loadMoreFinish();
                    }
                }
            }
        };

        dSwipeRefreshLayout = (DSwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        dSwipeRefreshLayout.setOnRefreshListener(new DSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });

        dSwipeRefreshLayout.setOnLoadMoreListener(new DSwipeRefreshLayout.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreFeed();
            }
        });
        Log.i("Fragment New feed", "Load feed...");
        dSwipeRefreshLayout.startLoad();
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
            mListener = (OnFragmentInteractionListener) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
