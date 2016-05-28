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
import com.learn.mobile.customview.DSwipeRefreshLayout;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.ResourceUtils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.ListObjectResponse;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SFeed;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.recyclerview.holder.HeaderHolder;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewFeedsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class NewFeedsFragment extends DFragmentBase implements Event.Action, ObservableFragment {
    public static final String TAG = NewFeedsFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    DSwipeRefreshLayout dSwipeRefreshLayout;
    FeedAdapter feedAdapter;
    SFeed sFeed;

    View rootView;
    boolean bHasAppBar = true;

    public void setHasAppBar(boolean b) {
        bHasAppBar = b;
    }

    DResponse.Complete completeResponse;
    User user;
    public int _id;

    public NewFeedsFragment() {
        _id = DMobi.getIdentityId();
        DMobi.log(TAG, "NewFeedsFragment fragment id " + _id);
        // TODO Required empty public constructor
    }

    public void setUser(User user) {
        DMobi.log(TAG, "setUser fragment id " + _id);
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void refreshFeed() {
        if (!DMobi.isUser()) {
            return;
        }
        if (!dSwipeRefreshLayout.isRefreshing()) {
            dSwipeRefreshLayout.setRefreshing(true);
        }
        sFeed.getNews(this.completeResponse);
    }

    public void loadMoreFeed() {
        if (!DMobi.isUser()) {
            return;
        }
        DMobi.log(TAG, "LoadMoreFeed");
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
        DMobi.log(TAG, "initEvents");
        // TODO Initialize events
        DMobi.registerEvent(Event.EVENT_LOADMORE_FEED, this, true);
        DMobi.registerEvent(Event.EVENT_REFRESH_FEED, this, true);
        DMobi.registerEvent(Event.EVENT_LOCK_REFRESH_RECYCLER_VIEW, this, true);

        // TODO Remove data when logout
        DMobi.registerEvent(Event.EVENT_LOGOUT_SUCCESS, this);
        DMobi.registerEvent(Event.EVENT_FEED_UPDATE_VIEW, this);
    }

    @Override
    public void onDestroyEvent() {
        DMobi.destroyEvent(Event.EVENT_LOADMORE_FEED);
        DMobi.destroyEvent(Event.EVENT_REFRESH_FEED);
        DMobi.destroyEvent(Event.EVENT_LOCK_REFRESH_RECYCLER_VIEW);
        DMobi.destroyEvent(Event.EVENT_LOGOUT_SUCCESS);
        DMobi.destroyEvent(Event.EVENT_FEED_UPDATE_VIEW);
        dSwipeRefreshLayout.setOnRefreshListener(null);
        dSwipeRefreshLayout.setOnLoadMoreListener(null);
        super.onDestroyEvent();
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
            case Event.EVENT_LOGOUT_SUCCESS:
                sFeed.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
            case Event.EVENT_FEED_UPDATE_VIEW:
                updateRecyclerView();
                break;
        }
    }

    // TODO Enable or disable swipeRefreshLayout
    public void setEnableRefresh(boolean b) {
        dSwipeRefreshLayout.setEnabled(b);
    }

    private void initRecycleView() {
        DMobi.log(TAG, getActivity().getClass().getSimpleName() + " initRecycleView fragment id " + _id);
        dSwipeRefreshLayout = (DSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        // TODO set user for service Feed
        if (user != null && user.getId() == DMobi.getUser().getId()) {
            sFeed = (SFeed) DMobi.getService(SFeed.class, "PROFILE-" + user.getId());
        } else {
            sFeed = (SFeed) DMobi.getInstance(SFeed.class);
        }
        if (user != null) {
            DMobi.log(TAG, user.getId());
        } else {
            DMobi.log(TAG, "user is null.");
        }
        if (user != null) {
            sFeed.setProfileUser(user.getId());
        }

        feedAdapter = new FeedAdapter(sFeed.getData());
        if (bHasAppBar) {
            int layoutHeader = R.layout.item_header_main_spacing;
            if (user != null) {
                layoutHeader = R.layout.item_header_spacing;
            }

            final int finalLayoutHeader = layoutHeader;
            RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(feedAdapter) {
                @Override
                public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return null;
                }

                @Override
                public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                    return new HeaderHolder(layoutInflater, viewGroup, finalLayoutHeader);
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
                DMobi.log(TAG, "setOnLoadMoreListener , load more 237");
            }
        });
        if (DMobi.isUser()) {
            dSwipeRefreshLayout.startLoad();
        }
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

    // @Override
    public View getScrollView() {
        if (isAdded()) {
            return recyclerView;
        }
        return null;
    }

    public void updateRecyclerView() {
        recyclerView.getAdapter().notifyDataSetChanged();
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
