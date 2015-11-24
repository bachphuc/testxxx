package com.learn.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.activity.*;
import com.learn.mobile.activity.LoginActivity;
import com.learn.mobile.customview.dialog.SettingSiteDialog;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SUser;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnLeftFragmentInteractionListener}
 * interface.
 */
public class LeftMenuFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = LeftMenuFragment.class.getSimpleName();

    private View view;
    View headerView;
    private NavigationView navigationView;

    private OnLeftFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static LeftMenuFragment newInstance(String param1, String param2) {
        LeftMenuFragment fragment = new LeftMenuFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LeftMenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_main, container, false);

        navigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.menu_main_header);

        DMobi.registerEvent(Event.EVENT_UPDATE_PROFILE, new Event.Action() {
            @Override
            public void fireAction(String eventType, Object o) {
                updateProfile(o);
            }
        });

        // display user information  if user has already login
        setUser();

        return view;
    }

    public void setUser() {
        SUser sUser = (SUser) DMobi.getService(SUser.class);
        User user = sUser.getUser();
        if (user != null) {
            DMobi.log(TAG, "Update profile");
            updateProfile(user);
        }
        DMobi.log(TAG, "Update profile done");
    }

    public void updateProfile(Object o) {
        if (o instanceof User) {
            DMobi.log(TAG, "User is exist");
            User user = (User) o;
            TextView lbView = (TextView) headerView.findViewById(R.id.lb_email);
            if (lbView != null) {
                DMobi.log(TAG, "Update email done");
                lbView.setText(user.email);
                lbView = (TextView) headerView.findViewById(R.id.lb_fullName);
                lbView.setText(user.fullName);
            }

            ImageView imageView = (ImageView) headerView.findViewById(R.id.im_avatar);
            if (imageView != null) {
                ImageHelper.display(imageView, user.getImages().medium.url);
            }

            if (user.coverPhoto != null) {
                imageView = (ImageView) headerView.findViewById(R.id.img_cover);
                if (imageView != null) {
                    ImageHelper.display(imageView, user.coverPhoto.full.url);
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLeftFragmentInteractionListener) activity;
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Context context;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logout:
                SUser sUser = (SUser) DMobi.getService(SUser.class);
                sUser.logout();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.showLoginActivity();
                break;
            case R.id.bt_sample:
                context = getContext();
                intent = new Intent(context, Sample1Activity.class);
                context.startActivity(intent);
                break;

            case R.id.drawer_settings_site:
                showDialogSettingSite();
                break;
            case R.id.bt_login_new:
                context = getContext();
                intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                break;
        }
        return false;
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
    public interface OnLeftFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onLeftFragmentInteraction(String id);
    }

    // TODO: Show dialog setting site
    public void showDialogSettingSite(){
        FragmentManager fragmentManager = getFragmentManager();
        SettingSiteDialog settingSite = new SettingSiteDialog();
        settingSite.show(fragmentManager, "dialog");
    }

}
