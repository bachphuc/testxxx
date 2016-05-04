package com.learn.mobile.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.customview.ItemClickSupport;
import com.learn.mobile.customview.PromptTextDialog;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.DUtils.DUtils;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.DConfig;
import com.learn.mobile.library.dmobi.helper.DbHelper;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.request.DRequest;
import com.learn.mobile.library.dmobi.request.DResponse;
import com.learn.mobile.library.dmobi.request.response.BasicObjectResponse;
import com.learn.mobile.model.Link;
import com.learn.mobile.model.User;
import com.learn.mobile.service.SLink;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;

public class PostActivity extends UploadFileBase implements View.OnClickListener, PromptTextDialog.NoticeDialogListener {
    private static final String TAG = PostActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private ImageView imgPreview;
    LinearLayout photoAttachmentPanel;

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

    // Gallery Slider
    private SlidingUpPanelLayout slidingUpPanelLayout;
    RecyclerView recyclerView;
    int itemMaxWidth = 240;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO disable swipe to close activity
        enableSwipe = false;

        // setContentView(R.layout.activity_post);
        setContentView(R.layout.activity_post_v1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        photoAttachmentPanel = (LinearLayout) findViewById(R.id.panel_photo_attachment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setShadowBackgroundColor(R.color.transparent);
        // TODO set global application context config
        DConfig.setContext(getApplicationContext());

        // TODO Init request
        initRequest();

        // TODO Init view
        initView();

        // TODO try to get data from another application
        tryGetShareData();

        // TODO Init Gallery Slider Panel
        initSliderGallery();

        // TODO calculator size
        calculatorSize();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.img_preview);

        ImageButton imageButton = (ImageButton) findViewById(R.id.bt_capture_image);
        imageButton.setOnClickListener(this);

        imageButton = (ImageButton) findViewById(R.id.bt_open_library);
        imageButton.setOnClickListener(this);

        btRemoveImage = (ImageView) findViewById(R.id.bt_remove_attachment);
        btRemoveImage.setOnClickListener(this);

        ImageView imageView = (ImageView) findViewById(R.id.bt_post);
        imageView.setOnClickListener(this);

        imageButton = (ImageButton) findViewById(R.id.bt_share_link);
        imageButton.setOnClickListener(this);

        tbDescription = (EditText) findViewById(R.id.tv_description);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.link_preview_loader);
        progressBar.setIndeterminateDrawable(new IndeterminateProgressDrawable(this));

        if (DMobi.isUser()) {
            TextView textView = (TextView) findViewById(R.id.tv_title);
            User user = (User) DMobi.getUser();
            textView.setText(user.getTitle());
            ImageView avatarView = (ImageView) findViewById(R.id.img_avatar);
            ImageHelper.display(avatarView, user.getImages().getAvatar());
        }
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
                    getLinkPreview(receivedText);
                } else if (receivedType.startsWith("image/")) {
                    //fileUri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    Uri uri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    if (uri != null) {
                        module = "photo";
                        // previewImage();
                        addPhotoAttachment(uri);
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
            View relativeLayout = findViewById(R.id.panel_photo_attachment);
            relativeLayout.setVisibility(View.GONE);
            relativeLayout = findViewById(R.id.panel_link_attachment);
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
        View relativeLayout = findViewById(R.id.panel_link_attachment);
        relativeLayout.setVisibility(View.GONE);
    }

    private void previewLinkComplete(Object o) {
        View linearLayout = findViewById(R.id.panel_link_attachment_content);
        linearLayout.setVisibility(View.VISIBLE);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.link_preview_loader);
        progressBar.setVisibility(View.GONE);

        ImageView imageView;
        TextView textView;
        link = (Link) o;
        if (link.images != null) {
            if (link.images.full != null) {
                imageView = (ImageView) findViewById(R.id.img_link_preview);
                ImageHelper.display(imageView, link.images.full.url);
            }
        }
        textView = (TextView) findViewById(R.id.link_title);
        textView.setText(link.getTitle());

        if (link.siteUrl != null) {
            textView = (TextView) findViewById(R.id.tv_link_url);
            textView.setText(link.siteUrl);
        }

        textView = (TextView) findViewById(R.id.tv_link_description);
        textView.setText(link.getDescription());
    }

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
                removeImage();
                break;
            case R.id.bt_post:
                uploadToServer();
                break;
            case R.id.bt_share_link:
                showLinkDialog();
                break;
            case R.id.bt_open_library:
                openGallery();
                break;
        }
    }

    private void showLinkDialog() {
        newFragment = new PromptTextDialog();
        newFragment.setTitle(DMobi.translate("Add your link"));
        newFragment.setHintText(DMobi.translate("Input your link to share..."));
        newFragment.show(getSupportFragmentManager(), "promTextDialog");
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void uploadToServer() {
        String description = tbDescription.getText().toString();
        dRequest.addPost("content", description);
        if (module != null) {
            dRequest.addParam("module", module);
        }

        final Context that = this;
        hideKeyboard();
        final ProgressDialog progressDialog = DMobi.showLoading(that, "", "Posting...");

        // TODO Share link
        if (module != null && module == "link") {
            if (link != null) {
                dRequest.addPost("title", link.getTitle());
                dRequest.addPost("description", link.getDescription());
                dRequest.addPost("image", DUtils.getString(link.images.full.url));
                dRequest.addPost("url", strLink);
            }
        }

        dRequest.setPreExecute(new DResponse.PreExecute() {
            @Override
            public void onPreExecute() {
                bPosting = true;
                progressDialog.show();
                if (fileUris.size() > 0) {
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
                progressDialog.dismiss();
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

        if (fileUris.size() > 0) {
            if (fileUris.size() == 0) {
                return;
            }

            for (int i = 0; i < fileUris.size(); i++) {
                Uri uri = fileUris.get(i);
                String filePath = DUtils.getRealPathFromURI(this, uri);
                if (filePath == null) {
                    filePath = DUtils.createNewImageFileFromUrl(this, uri);
                }
                dRequest.setFilePath(filePath);
            }

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void addPhotoAttachment(Uri uri) {
        super.addPhotoAttachment(uri);
        module = "photo";

        photoAttachmentPanel.setVisibility(View.VISIBLE);
        final RelativeLayout photoItemView = new RelativeLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = DUtils.dpToPx(this, 4);

        photoItemView.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DUtils.dpToPx(this, 240)));
        photoItemView.addView(imageView);

        ImageView removeView = new ImageView(this);
        removeView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_remove_circle_outline_white_24dp));
        RelativeLayout.LayoutParams closeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = DUtils.dpToPx(this, 16);
        closeLayoutParams.setMargins(margin, margin, margin, margin);

        removeView.setLayoutParams(closeLayoutParams);
        removeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewParent parent = v.getParent();
                if (parent != null) {
                    int position = photoAttachmentPanel.indexOfChild((View) parent);
                    fileUris.remove(position);
                    photoAttachmentPanel.removeView((View) parent);
                }
            }
        });
        photoItemView.addView(removeView);
        photoItemView.requestLayout();

        photoAttachmentPanel.addView(photoItemView);
        String path = DUtils.getRealPathFromURI(this, uri);
        if (path != null) {
            ImageHelper.display(imageView, path);
        } else {
            imageView.setImageURI(uri);
        }

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.panel_link_attachment);
        relativeLayout.setVisibility(View.GONE);

        View removeButton = findViewById(R.id.bt_remove_attachment);
        removeButton.setVisibility(View.GONE);
    }

    @Override
    public void previewImage() {
        module = "photo";
        imgPreview.setVisibility(View.VISIBLE);

        String path = DUtils.getRealPathFromURI(this, fileUri);
        if (path != null) {
            ImageHelper.display(imgPreview, path);
        } else {
            imgPreview.setImageURI(fileUri);
        }

        btRemoveImage.setVisibility(View.VISIBLE);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.panel_link_attachment);
        relativeLayout.setVisibility(View.GONE);
        relativeLayout = (RelativeLayout) findViewById(R.id.panel_photo_attachment);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    private void removeImage() {
        module = null;
        imgPreview.setVisibility(View.GONE);
        btRemoveImage.setVisibility(View.GONE);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.panel_link_attachment);
        relativeLayout.setVisibility(View.GONE);
        relativeLayout = (RelativeLayout) findViewById(R.id.panel_photo_attachment);
        relativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDialogSuccessClick(DialogFragment dialog, String value) {
        getLinkPreview(value);
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {

    }

    // TODO setup slider gallery panel
    protected void initSliderGallery() {
        recyclerView = (RecyclerView) findViewById(R.id.list);

        List<String> imageData = new ArrayList<String>();
        imageData.add(getResources().getString(R.string.take_from_camera));
        imageData.add(getResources().getString(R.string.choose_from_gallery));

        // Find the last picture
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };

        final Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        // Put it in the image view
        if (cursor.moveToFirst()) {
            // final ImageView imageView = (ImageView) findViewById(R.id.pictureView);
            // String imageLocation = cursor.getString(1);
            // File imageFile = new File(imageLocation);
            // if (imageFile.exists()) {
            // TODO: is there a better way to do this?
            // Bitmap bm = BitmapFactory.decodeFile(imageLocation);
            // imageView.setImageBitmap(bm);
            // }
            int i = 0;
            while (!cursor.isAfterLast() && i < 48) {
                imageData.add(cursor.getString(1));
                cursor.moveToNext();
                i++;
            }
            cursor.close();
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        final GalleryAdapter galleryAdapter = new GalleryAdapter();
        galleryAdapter.setData(imageData);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(galleryAdapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                int space = 4;
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                switch (position) {
                    case 0:
                        captureImage();
                        break;
                    case 1:
                        openGallery();
                        break;
                    default:
                        String imagePath = galleryAdapter.getItem(position);
                        // fileUri = Uri.fromFile(new File(imagePath));
                        Uri uri = Uri.fromFile(new File(imagePath));
                        addPhotoAttachment(uri);
                        // previewImage();
                        break;
                }
            }
        });

        galleryAdapter.notifyDataSetChanged();

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
    }

    class GalleryAdapter extends RecyclerView.Adapter {
        protected List<String> data;

        public void setData(List<String> list) {
            data = list;
        }

        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v;
            if (viewType > 1) {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.gallery_item, parent, false);
            } else {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.gallery_item_camera, parent, false);
            }
            // set the view's size, margins, padding and layout parameters
            GalleryViewHolder vh = new GalleryViewHolder(v);
            return vh;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            GalleryViewHolder galleryViewHolder = (GalleryViewHolder) holder;
            ImageView imageView = galleryViewHolder.imageView;
            Context context = imageView.getContext();
            switch (position) {
                case 0:
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_camera_white_36dp));
                    break;
                case 1:
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_photo_white_36dp));
                    break;
                default:
                    String img = data.get(position);
                    int width = DUtils.dpToPx(context, 120);

                    ImageHelper.getAdapter().resize(width, width).display(imageView, img);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.im_view);
        }
    }

    public void calculatorSize() {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.post_main_content);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                // layout size
                int width = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                Log.i(TAG, "Layout: width : " + width + ", height: " + height);

                // screen size
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                height = displaymetrics.heightPixels;
                width = displaymetrics.widthPixels;

                Log.i(TAG, "Screen: width : " + width + ", height: " + height);

                int count = 3;
                int paddingBottom = width / count;
                if (paddingBottom > itemMaxWidth) {
                    count = width / itemMaxWidth;
                    float mod = width % 200;
                    if (mod == 0) {
                        paddingBottom = itemMaxWidth;
                    } else if (mod > itemMaxWidth / 2) {
                        count++;
                    }
                    paddingBottom = width / count;
                }
                gridLayoutManager.setSpanCount(count);
                recyclerView.getAdapter().notifyDataSetChanged();

                layout.setPadding(layout.getPaddingLeft(), layout.getPaddingTop(), layout.getPaddingRight(), paddingBottom + 20);
                slidingUpPanelLayout.setPanelHeight(paddingBottom);
            }
        });
    }
}
