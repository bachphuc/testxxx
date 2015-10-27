package com.learn.mobile.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.mobile.R;
import com.learn.mobile.adapter.FeedAdapter;
import com.learn.mobile.adapter.ObservableScrollView;
import com.learn.mobile.customview.DSwipeRefreshLayout;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SFeed;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.smoothappbarlayout.utils.ResourceUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewFeedsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class NewFeedsFragment extends Fragment implements Event.Action, ObservableScrollView {
    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    DSwipeRefreshLayout dSwipeRefreshLayout;
    FeedAdapter feedAdapter;
    SFeed sFeed;

    View rootView;

    DResponse.Complete completeResponse;
    User user;

    public NewFeedsFragment() {
        // TODO Required empty public constructor
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void refreshFeed() {
        if (!dSwipeRefreshLayout.isRefreshing()) {
            dSwipeRefreshLayout.setRefreshing(true);
        }
        sFeed.getNews(this.completeResponse);
    }

    public void loadMoreFeed() {
        sFeed.getMores(this.completeResponse);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_feed, container, false);
        this.rootView = view;

        // TODO Initialize recycler view
        initRecycleView();

        // TODO Initialize event
        initEvents();

        return view;
    }

    private void initEvents() {
        // TODO Initialize events
        DMobi.registerEvent(Event.EVENT_LOADMORE_FEED, this);
        DMobi.registerEvent(Event.EVENT_REFRESH_FEED, this);

        DMobi.registerEvent(Event.EVENT_LOCK_REFRESH_RECYCLER_VIEW, this);
    }

    @Override
    public void fireAction(String eventType, Object o) {
        // TODO Process events
        switch (eventType) {
            case Event.EVENT_LOADMORE_FEED:
                dSwipeRefreshLayout.startLoad();
                break;
            case Event.EVENT_REFRESH_FEED:
                refreshFeed();
                break;
            case Event.EVENT_LOCK_REFRESH_RECYCLER_VIEW:
                boolean canRefresh = (boolean) o;
                setEnableRefresh(canRefresh);
                break;
        }
    }

    // TODO Enable or disable swipeRefreshLayout
    public void setEnableRefresh(boolean b) {
        dSwipeRefreshLayout.setEnabled(b);
    }

    private void initRecycleView() {
        dSwipeRefreshLayout = (DSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        // TODO set user for service Feed
        if (user != null && user.userId == DMobi.getUser().userId) {
            sFeed = (SFeed) DMobi.getService(SFeed.class, "PROFILE-" + user.userId);
        } else {
            sFeed = (SFeed) DMobi.getInstance(SFeed.class);
        }
        if (user != null) {
            sFeed.setProfileUser(user.userId);
        }

        feedAdapter = new FeedAdapter(sFeed.getData());

        if (user != null) {
            RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(feedAdapter) {
                @Override
                public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return null;
                }

                @Override
                public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return new HeaderHolder(layoutInflater, viewGroup, R.layout.item_header_spacing);
                }
            };
            recyclerView.setAdapter(adapter);

            int actionBarSize = ResourceUtils.getActionBarSize(getContext());
            int progressViewStart = getResources().getDimensionPixelSize(R.dimen.app_bar_height) - actionBarSize;
            int progressViewEnd = progressViewStart + (int) (actionBarSize * 1.5f);
            dSwipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);
            dSwipeRefreshLayout.setHasHeader(true);

        } else {
            recyclerView.setAdapter(feedAdapter);
        }

        completeResponse = new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                dSwipeRefreshLayout.setRefreshing(false);
                dSwipeRefreshLayout.stopLoadMore();
                if (o != null) {
                    ListObjectResponse<DMobileModelBase> response = (ListObjectResponse<DMobileModelBase>) o;
                    if (response.isSuccessfully() && response.hasData()) {
                        feedAdapter.notifyDataSetChanged();
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
                refreshFeed();
            }
        });

        dSwipeRefreshLayout.setOnLoadMoreListener(new DSwipeRefreshLayout.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreFeed();
            }
        });
        dSwipeRefreshLayout.startLoad();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View getScrollView() {
        if (isAdded()) {
            return recyclerView;
        }
        return null;
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
