package com.learn.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.learn.mobile.DMobiApplication;
import com.learn.mobile.R;
import com.learn.mobile.customview.BlurringView;
import com.learn.mobile.customview.DMaterialProgressDrawable;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.ImageHelperLib.ImageAdapterBase;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cimi.com.easeinterpolator.EaseBackOutInterpolator;

/**
 * Created by 09520_000 on 9/23/2015.
 */
public class DActivityBasic extends DActivityBase implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = DActivityBasic.class.getSimpleName();
    protected AppBarLayout appBarLayout;
    protected int appBarOffsetTop = 0;

    // TODO: 5/9/2016 for custom image view
    protected View customViewerLayout;
    protected ImageView customImageViewer;
    protected View customViewerContent;
    protected DMaterialProgressDrawable imageViewerLoading;
    protected boolean bShowCustomViewer = false;
    protected BlurringView mBlurringView;
    protected ImageView customImageLoader;

    // TODO: 5/9/2016 for upload file activity
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

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        appBarOffsetTop = i;
        onAppBarLayoutOffsetChange(appBarLayout, i);
    }

    protected void onAppBarLayoutOffsetChange(AppBarLayout appBarLayout, int i) {

    }

    public int getAppBarOffsetTop() {
        return appBarOffsetTop;
    }

    public int getAppBarHeight() {
        if (appBarLayout == null) {
            return 0;
        }
        return appBarLayout.getHeight();
    }

    boolean bLoadingCompleted = false;

    public void showImagePreview(String imageUrl) {
        if (customViewerLayout != null && customImageViewer != null) {
            customViewerLayout.setVisibility(View.VISIBLE);

            customViewerLayout.setAlpha(0f);
            customViewerLayout.bringToFront();

            bShowCustomViewer = true;

            mBlurringView.invalidate();
            bLoadingCompleted = false;

            ImageHelper.display(customImageViewer, imageUrl);
            ImageHelper.getAdapter()
                    .with(this)
                    .load(imageUrl)
                    .into(customImageViewer)
                    .callback(new ImageAdapterBase.ImageBitmapLoadedListener() {
                        @Override
                        public void onCompleted(Bitmap bitmap) {
                            bLoadingCompleted = true;
                            imageViewerLoading.stop();
                            customImageLoader.setVisibility(View.GONE);
                        }
                    })
                    .display();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (customViewerContent.getAnimation() != null) {
                        customViewerContent.getAnimation().start();
                    } else {
                        float scaleFrom = 0.2f, scaleTo = 1;
                        ScaleAnimation scaleAnimation = new ScaleAnimation(scaleFrom, scaleTo, scaleFrom, scaleTo, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) {
                            protected void applyTransformation(float interpolatedTime, Transformation t) {
                                super.applyTransformation(interpolatedTime, t);
                                customViewerLayout.setAlpha(interpolatedTime);
                            }
                        };
                        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (!bLoadingCompleted) {
                                    customImageLoader.setVisibility(View.VISIBLE);
                                    imageViewerLoading.start();
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        Interpolator interpolator = new EaseBackOutInterpolator();
                        scaleAnimation.setInterpolator(interpolator);
                        scaleAnimation.setDuration(200);

                        customViewerContent.setAnimation(scaleAnimation);
                        customViewerContent.startAnimation(scaleAnimation);
                        findViewById(android.R.id.content).invalidate();
                    }
                }
            });
        }
    }

    public boolean isShowCustomViewer() {
        return bShowCustomViewer;
    }

    public void hideImagePreview() {
        if (customViewerLayout != null && customImageViewer != null) {
            customViewerLayout.setVisibility(View.GONE);
            imageViewerLoading.stop();
            bShowCustomViewer = false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP && bShowCustomViewer) {
            hideImagePreview();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void initCustomView() {
        customViewerLayout = findViewById(R.id.custom_viewer);
        customViewerLayout.bringToFront();

        customImageViewer = (ImageView) findViewById(R.id.custom_image_viewer);


        customImageLoader = (ImageView) findViewById(R.id.img_loader);
        imageViewerLoading = new DMaterialProgressDrawable(this, customImageViewer);
        imageViewerLoading.setStrokeWidth(1);
        imageViewerLoading.setBackgroundColor(0xFFFAFAFA);
        imageViewerLoading.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);
        imageViewerLoading.setHasBackground(false);
        customImageLoader.setImageDrawable(imageViewerLoading);

        View closeCustomViewer = findViewById(R.id.bt_close_custom_viewer);
        closeCustomViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImagePreview();
            }
        });

        customViewerContent = findViewById(R.id.custom_viewer_content);

        mBlurringView = (BlurringView) findViewById(R.id.blurring_view);
        View blurredView = findViewById(R.id.content);

        // Give the blurring view a reference to the blurred view.
        mBlurringView.setBlurredView(blurredView);
    }

    protected void captureImage() {
        if (!isDeviceSupportCamera(this)) {
            DMobi.showToast(this, "This device not support camera.");
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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
            }
        }

        onAfterActivityResult(requestCode, resultCode, data);
    }

    public void addPhotoAttachment(Uri uri) {
        fileUris.add(uri);
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
