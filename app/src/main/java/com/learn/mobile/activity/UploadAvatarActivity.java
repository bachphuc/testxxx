package com.learn.mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alexvasilkov.gestures.Settings;
import com.learn.mobile.R;
import com.learn.mobile.customview.CustomGestureImageView;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SUser;

import java.io.FileOutputStream;
import java.io.IOException;

public class UploadAvatarActivity extends UploadFileBase {
    public static final String USER_AVATAR = "USER_AVATAR";
    private static final String IMAGE_UPLOAD_FILE = "IMAGE_UPLOAD_FILE.png";

    private String imageUploadFilePath;
    private CustomGestureImageView gestureImageView;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();
        String avatarUrl = intent.getStringExtra(USER_AVATAR);

        int frameW = getResources().getDimensionPixelSize(R.dimen.image_avatar_crop_width);
        int frameH = getResources().getDimensionPixelSize(R.dimen.image_avatar_crop_height);

        gestureImageView = (CustomGestureImageView) findViewById(R.id.cropping_image);
        gestureImageView.setMovementAreaSpecified(true)
                .getController().getSettings()
                .setFitMethod(Settings.Fit.OUTSIDE)
                .setFillViewport(true)
                .setMovementArea(frameW, frameH)
                .setRotationEnabled(true);

        if (avatarUrl != null) {
            ImageHelper.display(gestureImageView, avatarUrl);
        } else {
            User user = DMobi.getUser();
            if (user != null) {
                ImageHelper.display(gestureImageView, user.getImages().getFull().url);
            }
        }
    }

    public void onUpdateAvatar() {
        if (fileUri == null && imageUploadFilePath == null) {
            return;
        }

        SUser sUser = (SUser) DMobi.getService(SUser.class);
        String filePath = (imageUploadFilePath == null ? DUtils.getRealPathFromURI(this, fileUri) : imageUploadFilePath);
        sUser.updateAvatar(filePath, new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                if (status) {
                    progressDialog.hide();
                    DMobi.showToast(DMobi.translate("Update profile avatar successfully."));
                }
            }
        });
    }

    @Override
    public void onAfterActivityResult(int requestCode, int resultCode, Intent data) {
        if (fileUri != null) {
            String filePath = DUtils.getRealPathFromURI(this, fileUri);
            if (filePath != null) {
                ImageHelper.getAdapter().resize(1024, 1024).display(gestureImageView, filePath);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update_avatar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_crop:
                Bitmap cropped = gestureImageView.crop();
                if (cropped != null) {
                    gestureImageView.setImageBitmap(cropped);
                    progressDialog = DMobi.showLoading(this, "", "Upload avatar...");
                    progressDialog.show();

                    FileOutputStream out = null;
                    try {
                        imageUploadFilePath = getExternalCacheDir() + "/" + IMAGE_UPLOAD_FILE;
                        out = new FileOutputStream(imageUploadFilePath);

                        // bmp is your Bitmap instance
                        // PNG is a loss less format, the compression factor (100) is ignored
                        cropped.compress(Bitmap.CompressFormat.PNG, 80, out);

                        onUpdateAvatar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.mn_choose_from_gallery:
                openGallery();
                break;
            case R.id.mn_take_from_camera:
                captureImage();
                break;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
