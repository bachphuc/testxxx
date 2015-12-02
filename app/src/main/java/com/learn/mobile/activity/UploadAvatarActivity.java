package com.learn.mobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.learn.mobile.R;

public class UploadAvatarActivity extends UploadFileBase implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);

        imgPreview = (ImageView) findViewById(R.id.im_preview);

        Button button = (Button) findViewById(R.id.bt_open_library);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_open_camera);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_open_library);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.bt_update_avatar);
        button.setOnClickListener(this);
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
                onUpdateAvatar();
                break;
        }
    }

    public void onUpdateAvatar(){

    }
}
