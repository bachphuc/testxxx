package com.learn.mobile.fragment.visitor;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadAvatarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadAvatarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadAvatarFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = UploadAvatarFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private View rootView;

    private Uri fileUri;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 300;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    static final String IMAGE_DIRECTORY_NAME = "test_upload";

    private ImageView imgPreview;

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

        Button button = (Button) rootView.findViewById(R.id.bt_open_library);
        button.setOnClickListener(this);

        button = (Button) rootView.findViewById(R.id.bt_open_camera);
        button.setOnClickListener(this);

        button = (Button) rootView.findViewById(R.id.bt_open_library);
        button.setOnClickListener(this);

        button = (Button) rootView.findViewById(R.id.bt_signup_process);
        button.setOnClickListener(this);

        imgPreview = (ImageView) rootView.findViewById(R.id.im_preview);
        return rootView;
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
                    DMobi.fireEvent(Event.EVENT_UPDATE_PROFILE, o);
                    DMobi.fireEvent(Event.EVENT_LOADMORE_FEED, o);
                    DMobi.fireEvent(Event.EVENT_LOGIN_SUCCESS, o);
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

    // TODO Open camera to capture photo
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO Process when capture success
                previewImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // TODO Process when user cancelled Image capture
                // DMobi.showToast("User cancelled image capture.");

            } else {
                // TODO Process when user failed to capture image
                // DMobi.showToast("Sorry! Failed to capture image.");
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // DMobi.showToast("User cancelled video recording.");

            } else {
                // DMobi.showToast("Sorry! Failed to record video.");
            }
        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                fileUri = data.getData();
                previewImage();
            }
        }
    }

    private void previewImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        String path = DUtils.getRealPathFromURI(getActivity(), fileUri);
        final Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        imgPreview.setImageBitmap(bitmap);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // TODO Create an place to External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // TODO Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                DMobi.log(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // TODO Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // TODO this device has a camera
            return true;
        } else {
            // TODO no camera on this device
            return false;
        }
    }
}
