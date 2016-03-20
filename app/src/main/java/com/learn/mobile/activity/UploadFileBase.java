package com.learn.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.learn.mobile.customview.activity.SwipeBackActivity;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.global.DConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by 09520_000 on 12/2/2015.
 */
public class UploadFileBase extends SwipeBackActivity {
    public static final String TAG = UploadFileBase.class.getSimpleName();
    protected Uri fileUri;
    protected List<Uri> fileUris = new ArrayList<>();
    protected static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    protected static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    protected static final int GALLERY_IMAGE_REQUEST_CODE = 300;

    protected static final int MEDIA_TYPE_IMAGE = 1;
    protected static final int MEDIA_TYPE_VIDEO = 2;
    protected static final String IMAGE_DIRECTORY_NAME = DConfig.APP_NAME;

    private Uri tempUri;

    protected ImageView imgPreview;

    protected void captureImage() {
        if (!isDeviceSupportCamera(this)) {
            DMobi.showToast(this, "This device not support camera.");
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        tempUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);
    }

    public void onAfterActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                addPhotoAttachment(tempUri);
                tempUri = null;
                // previewImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {

            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

            } else if (resultCode == Activity.RESULT_CANCELED) {


            } else {

            }
        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = data.getData();
                addPhotoAttachment(resultUri);
                // fileUris.add(resultUri);
                //  previewImage();
            }
        }

        onAfterActivityResult(requestCode, resultCode, data);
    }

    public void addPhotoAttachment(Uri uri) {
        fileUris.add(uri);
    }

    public void previewImage() {
        if (imgPreview == null || fileUri == null) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        String path = DUtils.getRealPathFromURI(this, fileUri);
        final Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        imgPreview.setImageBitmap(bitmap);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    protected static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

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

    protected boolean isDeviceSupportCamera(Context context) {
        if (context.getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
}
