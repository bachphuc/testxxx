package com.learn.mobile.activity;

import android.app.ProgressDialog;
import android.database.CrossProcessCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.edmodo.cropper.CropImageView;
import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.service.SUser;

public class UploadAvatarActivity extends UploadFileBase implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);

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

        GestureImageView  mImageView = (GestureImageView) findViewById( R.id.cropping_image);
        mImageView.getController().getSettings()
                .setFitMethod(Settings.Fit.OUTSIDE)
                .setFillViewport(true)
                .setMovementArea(frameW, frameH)
                .setRotationEnabled(true);

        mImageView.setImageResource(R.drawable.background_red);
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
}
