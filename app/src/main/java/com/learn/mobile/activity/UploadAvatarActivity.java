package com.learn.mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.CrossProcessCursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.edmodo.cropper.CropImageView;
import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SUser;

public class UploadAvatarActivity extends UploadFileBase implements View.OnClickListener {
    public static final String USER_AVATAR = "USER_AVATAR";
    GestureImageView gestureImageView;

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

        /*imgPreview = (ImageView) findViewById(R.id.im_preview);

        Button button = (Button) findViewById(R.id.bt_open_library);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_open_camera);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_open_library);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_update_avatar);
        button.setOnClickListener(this);*/

        // CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        // cropImageView.setImageResource(R.drawable.background_red);

        int frameW = getResources().getDimensionPixelSize(R.dimen.image_frame_width);
        int frameH = getResources().getDimensionPixelSize(R.dimen.image_frame_height);

        gestureImageView = (GestureImageView) findViewById(R.id.cropping_image);
        gestureImageView.getController().getSettings()
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

        // mImageView.setImageResource(R.drawable.background_red);
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
            /*case R.id.bt_update_avatar:
                onUpdateAvatar();
                break;*/
        }
    }

    public void onUpdateAvatar() {
        if (fileUri == null) {
            return;
        }
        final ProgressDialog progressDialog = DMobi.showLoading(this, "", "Upload avatar...");
        progressDialog.show();

        SUser sUser = (SUser) DMobi.getService(SUser.class);
        String filePath = DUtils.getRealPathFromURI(this, fileUri);
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
                ImageHelper.display(gestureImageView, filePath);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_avatar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_crop:
                /*Bitmap cropped = gestureImageView.crop();
                if (cropped != null) {
                    finish();
                }*/
                finish();
                return true;
            case R.id.mn_choose_from_gallery:
                openGallery();
                break;
            case R.id.mn_take_from_camera:
                captureImage();
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
