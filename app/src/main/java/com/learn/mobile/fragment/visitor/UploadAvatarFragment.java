package com.learn.mobile.fragment.visitor;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.customview.dialog.TutsPlusBottomSheetDialogFragment;
import com.learn.mobile.fragment.UploadFileBase;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.DEventType;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadAvatarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadAvatarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadAvatarFragment extends UploadFileBase implements View.OnClickListener {
    public static final String TAG = UploadAvatarFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private View rootView;

    private ImageView imgPreview;
    TutsPlusBottomSheetDialogFragment bottomSheetDialogFragment;

    public UploadAvatarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UploadAvatarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadAvatarFragment newInstance() {
        UploadAvatarFragment fragment = new UploadAvatarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upload_avatar, container, false);

        Button button = (Button) rootView.findViewById(R.id.bt_signup_process);
        button.setOnClickListener(this);

        ImageView backButton = (ImageView) rootView.findViewById(R.id.bt_back);
        backButton.setOnClickListener(this);

        imgPreview = (ImageView) rootView.findViewById(R.id.im_preview);
        imgPreview.setOnClickListener(this);

        bottomSheetDialogFragment = new TutsPlusBottomSheetDialogFragment();
        bottomSheetDialogFragment.setMenuResId(R.menu.menu_choose_photo);
        bottomSheetDialogFragment.setMenuTitle(DMobi.translate("Choose your Avatar"));
        bottomSheetDialogFragment.setOnMenuBottomSheetListener(new TutsPlusBottomSheetDialogFragment.OnMenuBottomSheetInteractionListener() {
            @Override
            public void onMenuBottomSheetInteraction(int id, String title) {
                switch (id) {
                    case R.id.menu_take_a_photo:
                        captureImage();
                        break;
                    case R.id.menu_open_gallery:
                        openGallery();
                        break;
                }
            }
        });

        return rootView;
    }

    public void showBottomMenu() {
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            bottomSheetDialogFragment.show(appCompatActivity.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
        }
    }

    public void setLister(Context context) {
        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
            case R.id.bt_open_camera:
                captureImage();
                break;
            case R.id.bt_open_library:
                openGallery();
                break;
            case R.id.bt_signup_process:
                onSignup();
                break;
            case R.id.im_preview:
                showBottomMenu();
                break;
            case R.id.bt_back:
                onButtonPressed(v);
                break;
        }
    }

    public void onSignup() {
        if (fileUri == null) {
            DMobi.alert(getActivity(), "Please capture or choose a file to make your avatar.");
            return;
        }

        SUser sUser = (SUser) DMobi.getService(SUser.class, SUser.SIGNUP_TAG);
        String avatarPath = DUtils.getRealPathFromURI(getActivity(), fileUri);
        sUser.setAvatarPath(avatarPath);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Register....");
        progressDialog.setMessage("Wait some minute...");
        progressDialog.show();

        sUser.register(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                progressDialog.hide();
                if (o != null) {
                    DMobi.fireEvent(DEventType.EVENT_UPDATE_PROFILE, o);
                    DMobi.fireEvent(DEventType.EVENT_LOADMORE_FEED, o);
                    DMobi.fireEvent(DEventType.EVENT_LOGIN_SUCCESS, o);
                    DMobi.showToast("Register successfully.");
                    getActivity().finish();
                }
            }
        });
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

    @Override
    public void previewImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        String path = DUtils.getRealPathFromURI(getActivity(), fileUri);
        final Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        imgPreview.setImageBitmap(bitmap);
    }
}
