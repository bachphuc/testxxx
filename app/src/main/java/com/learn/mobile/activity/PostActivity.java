package com.learn.mobile.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.learn.mobile.R;
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
import com.learn.mobile.service.SLink;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.IndeterminateProgressDrawable;

public class PostActivity extends UploadFileBase implements View.OnClickListener, PromptTextDialog.NoticeDialogListener {
    private static final String TAG = PostActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private ImageView imgPreview;

    boolean bHasImage = false;

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
    private SlidingUpPanelLayout mLayout;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO disable swipe to close activity
        enableSwipe = false;

        // setContentView(R.layout.activity_post);
        setContentView(R.layout.activity_post_v1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
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
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.photo_image_preview);

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
                    getLinkPreview(receivedText);
                } else if (receivedType.startsWith("image/")) {
                    fileUri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    if (fileUri != null) {
                        module = "photo";
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
                dRequest.addPost("image", DUtils.getString(link.images.full.url));
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
                return;
            }

            String filePath = DUtils.getRealPathFromURI(this, fileUri);
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
    public void previewImage() {
        module = "photo";
        bHasImage = true;
        imgPreview.setVisibility(View.VISIBLE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        String path = DUtils.getRealPathFromURI(this, fileUri);
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
        GalleryAdapter galleryAdapter = new GalleryAdapter();
        galleryAdapter.setData(imageData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
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

        galleryAdapter.notifyDataSetChanged();

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
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

        TextView t = (TextView) findViewById(R.id.name);
        t.setText(Html.fromHtml(getString(R.string.hello)));
        Button f = (Button) findViewById(R.id.follow);
        f.setText(Html.fromHtml(getString(R.string.follow)));
        f.setMovementMethod(LinkMovementMethod.getInstance());
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
                startActivity(i);
            }
        });
    }

    class GalleryAdapter extends RecyclerView.Adapter {
        protected List<String> data;

        public void setData(List<String> list) {
            data = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_item, parent, false);
            // set the view's size, margins, padding and layout parameters
            GalleryViewHolder vh = new GalleryViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String img = data.get(position);
            GalleryViewHolder galleryViewHolder = (GalleryViewHolder) holder;
            int width = dpToPx(galleryViewHolder.imageView.getContext(), 120);
            Glide.with(galleryViewHolder.imageView.getContext())
                    .load(img)
                    .override(width, width)
                    .into(galleryViewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public int dpToPx(Context context, int dp) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

        public int pxToDp(Context context, int px) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return dp;
        }
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.im_view);
        }
    }
}
