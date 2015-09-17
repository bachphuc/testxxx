package com.learn.mobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = PostActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private ImageView imgPreview;

    boolean bHasImage = false;

    private Uri fileUri;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    static final String IMAGE_DIRECTORY_NAME = "test_upload";

    ImageView btRemoveImage;
    boolean bPosting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // TODO Init view
        initView();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        ImageButton imageButton = (ImageButton) findViewById(R.id.bt_capture_image);
        imageButton.setOnClickListener(this);
        btRemoveImage = (ImageView) findViewById(R.id.bt_remove_image);
        btRemoveImage.setOnClickListener(this);

        ImageView imageView = (ImageView) findViewById(R.id.bt_post);
        imageView.setOnClickListener(this);
    }

    // TODO Process view event click
    @Override
    public void onClick(View v) {
        if (bPosting) {
            return;
        }
        switch (v.getId()) {
            case R.id.bt_capture_image:
                captureImage();
                break;
            case R.id.bt_remove_image:
                Log.i(TAG, "Remove image...");
                removeImage();
                break;
            case R.id.bt_post:
                Log.i(TAG, "Upload image...");
                uploadToServer();
                break;
        }
    }

    public void uploadToServer() {
        DRequest dRequest = DMobi.createRequest();
        dRequest.setApi("photo.upload");
        dRequest.setFileUri(fileUri);
        dRequest.setPreExecute(new DResponse.PreExecute() {
            @Override
            public void onPreExecute() {
                bPosting = true;
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }
        });
        dRequest.setUpdateProcess(new DResponse.UpdateProcess() {
            @Override
            public void onUpdateProcess(int percent) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(percent);
            }
        });
        dRequest.setComplete(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                String response = (String) o;
                progressBar.setVisibility(View.GONE);
                bPosting = false;
                if (status) {
                    showAlert(response);
                } else {

                    showAlert(response);
                }
            }
        });
        dRequest.upload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_activity, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable = ImageHelper.getIconDrawable(drawable, Color.parseColor("#333333"));
                menuItem.setIcon(drawable);
            }
        }

        return true;
    }

    // TODO Open camera to capture photo
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
                Log.d("Upload", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
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
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // TODO this device has a camera
            return true;
        } else {
            // TODO no camera on this device
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO save file url in bundle as it will be null on screen orientation changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // TODO get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private void previewImage() {
        bHasImage = true;
        imgPreview.setVisibility(View.VISIBLE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

        imgPreview.setImageBitmap(bitmap);
        btRemoveImage.setVisibility(View.VISIBLE);
    }

    private void removeImage() {
        bHasImage = false;
        imgPreview.setVisibility(View.GONE);
        btRemoveImage.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // TODO Process when capture success
                previewImage();
            } else if (resultCode == RESULT_CANCELED) {
                // TODO Process when user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // TODO Process when user failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
