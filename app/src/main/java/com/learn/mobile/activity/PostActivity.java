package com.learn.mobile.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.customview.PromptTextDialog;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.Utils.Utils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.BasicObjectResponse;
import com.learn.mobile.model.Link;
import com.learn.mobile.service.SLink;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;

public class PostActivity extends AppCompatActivity implements View.OnClickListener, PromptTextDialog.NoticeDialogListener {
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
    EditText tbDescription;
    boolean bLink = false;
    String module;

    SLink sLink;
    Link link;
    DRequest dRequest;
    String strLink;
    PromptTextDialog newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // TODO set global application context config
        DConfig.setContext(getApplicationContext());

        // TODO Init request
        initRequest();

        // TODO Init view
        initView();

        // TODO try to get data from another application
        tryGetShareData();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.photo_image_preview);

        ImageButton imageButton = (ImageButton) findViewById(R.id.bt_capture_image);
        imageButton.setOnClickListener(this);
        btRemoveImage = (ImageView) findViewById(R.id.bt_remove_attachment);
        btRemoveImage.setOnClickListener(this);

        ImageView imageView = (ImageView) findViewById(R.id.bt_post);
        imageView.setOnClickListener(this);

        imageButton = (ImageButton) findViewById(R.id.bt_share_link);
        imageButton.setOnClickListener(this);

        tbDescription = (EditText) findViewById(R.id.tb_description);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.link_preview_loader);
        progressBar.setIndeterminateDrawable(new IndeterminateProgressDrawable(this));
    }

    private void initRequest() {
        dRequest = DMobi.createRequest();
        dRequest.setApi("feed.add");
    }

    private void tryGetShareData() {
        Intent receivedIntent = getIntent();

        String receivedAction = receivedIntent.getAction();

        if (receivedAction != null && receivedAction == Intent.ACTION_SEND) {
            String receivedType = receivedIntent.getType();
            String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
            if (receivedType != null) {
                if (receivedType.startsWith("text/")) {
                    DMobi.log(TAG, receivedText);
                    getLinkPreview(receivedText);
                } else if (receivedType.startsWith("image/")) {
                    fileUri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    if (fileUri != null) {
                        module = "photo";
                        DMobi.log(TAG, fileUri.getPath());
                        previewImage();
                    }
                }
            }
        }
    }

    private void getLinkPreview(String url) {
        UrlValidator urlValidator = new UrlValidator();

        if (urlValidator.isValid(url)) {
            bLink = true;
            module = "link";
            strLink = url;
            sLink = (SLink) DMobi.getService(SLink.class);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.panel_photo_attachment);
            relativeLayout.setVisibility(View.GONE);
            relativeLayout = (RelativeLayout) findViewById(R.id.panel_link_attachment);
            relativeLayout.setVisibility(View.VISIBLE);

            sLink.preview(url, new DResponse.Complete() {
                @Override
                public void onComplete(Boolean status, Object o) {
                    if (status) {
                        previewLinkComplete(o);
                    } else {
                        getLinkPreviewFail();
                    }
                }
            });
        } else {
            DMobi.showToast("Link is invalid format. Please add another link.");
        }
    }

    private void getLinkPreviewFail() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.panel_link_attachment);
        relativeLayout.setVisibility(View.GONE);
    }

    private void previewLinkComplete(Object o) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.panel_link_attachment_content);
        linearLayout.setVisibility(View.VISIBLE);


        ProgressBar progressBar = (ProgressBar) findViewById(R.id.link_preview_loader);
        progressBar.setVisibility(View.GONE);

        ImageView imageView;
        TextView textView;
        link = (Link) o;
        if (link.images != null) {
            if (link.images.full != null) {
                imageView = (ImageView) findViewById(R.id.link_image_preview);
                ImageHelper.display(imageView, link.images.full.url);
            }
        }
        textView = (TextView) findViewById(R.id.link_title);
        textView.setText(link.getTitle());

        if (link.siteUrl != null) {
            textView = (TextView) findViewById(R.id.link_url);
            textView.setText(link.siteUrl);
        }

        textView = (TextView) findViewById(R.id.link_description);
        textView.setText(link.getDescription());
    }

    public boolean needUpload() {
        return bHasImage;
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
            case R.id.bt_remove_attachment:
                Log.i(TAG, "Remove image...");
                removeImage();
                break;
            case R.id.bt_post:
                Log.i(TAG, "Upload image...");
                uploadToServer();
                break;
            case R.id.bt_share_link:
                showLinkDialog();
                break;
        }
    }

    private void showLinkDialog() {
        Log.i(TAG, "chan vai");
        newFragment = new PromptTextDialog();
        newFragment.setTitle(DMobi.translate("Add your link"));
        newFragment.setHintText(DMobi.translate("Input your link to share..."));
        newFragment.show(getSupportFragmentManager(), "promTextDialog");
    }

    public void uploadToServer() {
        String description = tbDescription.getText().toString();
        dRequest.addPost("content", description);
        if (module != null) {
            dRequest.addParam("module", module);
        }

        final Context that = this;
        final ProgressDialog progressDialog = DMobi.showLoading(that, "", "Posting...");

        // TODO Share link
        if (module != null && module == "link") {
            if (link != null) {
                dRequest.addPost("title", link.getTitle());
                dRequest.addPost("description", link.getDescription());
                dRequest.addPost("image", link.images.full.url);
                dRequest.addPost("url", strLink);
            }
        }

        dRequest.setPreExecute(new DResponse.PreExecute() {
            @Override
            public void onPreExecute() {
                bPosting = true;
                progressDialog.show();
                if (needUpload()) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                }
            }
        });

        dRequest.setComplete(new DResponse.Complete() {
            @Override
            public void onComplete(Boolean status, Object o) {
                String response = (String) o;
                progressBar.setVisibility(View.GONE);
                progressDialog.hide();
                bPosting = false;
                DMobi.log(TAG, response);
                if (status) {
                    BasicObjectResponse responseObject = DbHelper.parseResponse(response);
                    if (responseObject.isSuccessfully()) {
                        DMobi.showToast("Post successfully.");
                        DMobi.fireEvent(Event.EVENT_REFRESH_FEED, null);
                        finish();
                    } else {
                        DMobi.alert(that, responseObject.getErrors());
                    }
                } else {
                    DMobi.alert(that, "Response from Servers", response);
                }
            }
        });

        if (needUpload()) {
            if (fileUri == null) {
                DMobi.showToast("No file to upload.");
                return;
            }

            String filePath = Utils.getRealPathFromURI(this, fileUri);
            dRequest.setFilePath(filePath);
            dRequest.setUpdateProcess(new DResponse.UpdateProcess() {
                @Override
                public void onUpdateProcess(int percent) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(percent);
                }
            });
            dRequest.upload();
        } else {
            dRequest.post();
        }
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

        String path = Utils.getRealPathFromURI(this, fileUri);
        final Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        imgPreview.setImageBitmap(bitmap);
        btRemoveImage.setVisibility(View.VISIBLE);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.panel_link_attachment);
        relativeLayout.setVisibility(View.GONE);
        relativeLayout = (RelativeLayout) findViewById(R.id.panel_photo_attachment);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    private void removeImage() {
        bHasImage = false;
        module = null;
        imgPreview.setVisibility(View.GONE);
        btRemoveImage.setVisibility(View.GONE);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.panel_link_attachment);
        relativeLayout.setVisibility(View.GONE);
        relativeLayout = (RelativeLayout) findViewById(R.id.panel_photo_attachment);
        relativeLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // TODO Process when capture success
                previewImage();
            } else if (resultCode == RESULT_CANCELED) {
                // TODO Process when user cancelled Image capture
                DMobi.showToast("User cancelled image capture.");

            } else {
                // TODO Process when user failed to capture image
                DMobi.showToast("Sorry! Failed to capture image.");
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {
                DMobi.showToast("User cancelled video recording.");

            } else {
                DMobi.showToast("Sorry! Failed to record video.");
            }
        }
    }

    @Override
    public void onDialogSuccessClick(DialogFragment dialog, String value) {
        getLinkPreview(value);
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {

    }
}
