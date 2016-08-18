package com.learn.mobile.fragment.visitor;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.learn.mobile.R;
import com.learn.mobile.customview.dialog.SettingSiteDialog;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.DEventType;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = LoginFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private View rootView;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_login, container, false);

        View view = rootView.findViewById(R.id.bt_signUp);
        view.setOnClickListener(this);

        view = rootView.findViewById(R.id.bt_login);
        view.setOnClickListener(this);

        view = rootView.findViewById(R.id.bt_setting_site);
        view.setOnClickListener(this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        DMobi.log(TAG, "onButtonPressed");
        if (mListener != null) {
            DMobi.log(TAG, "pass onButtonPressed to parent activity");
            mListener.onFragmentInteraction(view);
        }
    }

    public void setListener(Context context) {
        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DMobi.log(TAG, "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_signUp:
                onButtonPressed(v);
                break;
            case R.id.bt_login:
                onLogin();
                break;
            case R.id.bt_setting_site:
                SettingSiteDialog settingSite = new SettingSiteDialog();
                settingSite.show(getFragmentManager(), "dialog");
                break;
        }
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
        void onFragmentInteraction(View view);
    }

    private void onLogin() {
        SUser sUser = (SUser) DMobi.getService(SUser.class);
        EditText emailInput = (EditText) rootView.findViewById(R.id.tb_email);
        String email = emailInput.getText().toString();
        if (DUtils.isEmpty(email)) {
            DMobi.alert(getActivity(), "Email can not be null.");
            return;
        }
        EditText passwordInput = (EditText) rootView.findViewById(R.id.tb_password);
        String password = passwordInput.getText().toString();
        if (DUtils.isEmpty(password)) {
            DMobi.alert(getActivity(), "Password can not be null.");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Login....");
        progressDialog.setMessage("Wait some minute...");
        progressDialog.show();

        sUser.login(email, password, new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                progressDialog.dismiss();
                if (o != null) {
                    DMobi.fireEvent(DEventType.EVENT_UPDATE_PROFILE, o);
                    DMobi.fireEvent(DEventType.EVENT_LOADMORE_FEED, o);
                    DMobi.fireEvent(DEventType.EVENT_LOGIN_SUCCESS, o);
                    DMobi.showToast("Login successfully.");
                    getActivity().finish();
                }
            }
        });
    }
}
