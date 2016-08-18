package com.learn.mobile.fragment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.learn.mobile.R;
import com.learn.mobile.activity.CommentActivity;
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.customview.DMaterialProgressDrawable;
import com.learn.mobile.customview.com.faradaj.blurbehind.BlurBehind;
import com.learn.mobile.customview.com.faradaj.blurbehind.OnBlurCompleteListener;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.DEventType;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.global.Constant;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.library.dmobi.helper.ImageHelperLib.ImageAdapterBase;
import com.learn.mobile.model.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PhotoViewFragment extends DFragmentBase implements View.OnClickListener, Event.Action {
    public static final String TAG = PhotoViewFragment.class.getSimpleName();
    public static final String DOWNLOAD_IMAGE = "DOWNLOAD_IMAGE";
    protected Photo photo;
    protected View view;
    ImageView mainImageView;

    ViewPager viewPager;

    public ImageView getMainImageView() {
        return mainImageView;
    }

    public void setViewPager(ViewPager v) {
        viewPager = v;
    }

    public PhotoViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_view_detail, container, false);
        mainImageView = (ImageView) view.findViewById(R.id.im_imageView);

        final ImageView imageLoader = (ImageView) view.findViewById(R.id.img_loader);
        final DMaterialProgressDrawable imageViewerLoading = new DMaterialProgressDrawable(getContext(), imageLoader);
        imageViewerLoading.setStrokeWidth(1);
        imageViewerLoading.setBackgroundColor(0xFFFAFAFA);
        imageViewerLoading.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);
        imageLoader.setImageDrawable(imageViewerLoading);
        imageViewerLoading.setHasBackground(false);
        imageViewerLoading.start();

        if (photo != null && mainImageView != null) {
            if (photo.images != null) {
                if (mainImageView instanceof DFeedImageView) {
                    float ratio = (float) photo.images.getLarge().height / (float) photo.images.getLarge().width;
                    DFeedImageView dFeedImageView = (DFeedImageView) mainImageView;
                    dFeedImageView.setScale(ratio);
                    ImageHelper.display(mainImageView, photo.images.getLarge().url);
                } else if (mainImageView instanceof GestureImageView) {
                    GestureImageView gestureImageView = (GestureImageView) mainImageView;
                    gestureImageView.getController().getSettings()
                            .setMaxZoom(2f)
                            .setPanEnabled(true)
                            .setZoomEnabled(true)
                            .setDoubleTapEnabled(true)
                            .setRotationEnabled(false)
                            .setOverscrollDistance(0f, 0f)
                            .setOverzoomFactor(2f)
                            .setFillViewport(true)
                            .setFitMethod(Settings.Fit.INSIDE)
                            .setGravity(Gravity.CENTER);

                    ImageHelper.getAdapter()
                            .with(getContext())
                            .load(photo.images.getExtraLarge().url)
                            .into(gestureImageView)
                            .callback(new ImageAdapterBase.ImageBitmapLoadedListener() {
                                @Override
                                public void onCompleted(Bitmap bitmap) {
                                    imageViewerLoading.stop();
                                    imageLoader.setVisibility(View.GONE);
                                }
                            })
                            .display();

                    if (viewPager != null) {
                        gestureImageView.getController().enableScrollInViewPager(viewPager);
                    }
                }
            }
        }

        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        textView.setText(photo.user.getTitle());

        textView = (TextView) view.findViewById(R.id.tv_description);
        textView.setText(photo.getDescription());

        if (photo.user.images != null) {
            CircleImageView avatarImageView = (CircleImageView) view.findViewById(R.id.img_avatar);
            if (avatarImageView != null) {
                ImageHelper.display(avatarImageView, photo.user.images.getAvatar().url);
            }
        }

        View button = view.findViewById(R.id.bt_comment);
        button.setOnClickListener(this);

        button = view.findViewById(R.id.bt_like);
        button.setOnClickListener(this);

        button = view.findViewById(R.id.bt_share);
        button.setOnClickListener(this);

        initEvent();

        updateView();

        return view;
    }

    public void updateView() {
        TextView textView = (TextView) view.findViewById(R.id.tb_total_comment);
        textView.setText(photo.getTotalComment() + " " + (photo.getTotalComment() > 1 ? DMobi.translate("comments") : DMobi.translate("comment")));

        textView = (TextView) view.findViewById(R.id.tv_total_like);
        textView.setText(photo.getTotalLike() + " " + (photo.getTotalLike() > 1 ? DMobi.translate("likes") : DMobi.translate("like")));

        ImageView imageView = (ImageView) view.findViewById(R.id.bt_like);
        textView = (TextView) view.findViewById(R.id.tv_total_like);
        photo.updateLikeView(imageView, textView);
    }

    public void initEvent() {
        DMobi.registerEvent(photo.getEventType(), this);
        DMobi.registerEvent(DEventType.EVENT_PHOTO_VIEWER + "_" + photo.getId(), this, true);
    }

    @Override
    public void onDestroyEvent() {
        super.onDestroyEvent();
        DMobi.destroyEvent(photo.getEventType());
        DMobi.destroyEvent(DEventType.EVENT_PHOTO_VIEWER + "_" + photo.getId());
        if (photo != null) {
            photo = null;
        }
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_comment:
                showCommentDetail(v.getContext());
                break;
            case R.id.bt_like:
                TextView textView = (TextView) view.findViewById(R.id.tv_total_like);
                photo.like((ImageView) v, textView);
                break;
            case R.id.bt_share:
                photo.share(v.getContext());
                break;
        }
    }

    public void showCommentDetail(final Context context) {
        BlurBehind.getInstance().execute(getActivity(), new OnBlurCompleteListener() {
            @Override
            public void onBlurComplete() {
                DMobi.pushData(CommentActivity.COMMENT_DETAIL_OBJECT, photo);
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void fireAction(String eventType, Object o) {
        if (eventType.equals(photo.getEventType())) {
            if (o instanceof Event.ModelAction) {
                DMobi.log(TAG, "Update view...");
                updateView();
            }
        } else if (eventType.equals(DEventType.EVENT_PHOTO_VIEWER + "_" + photo.getId())) {
            String action = o.toString();
            processAction(action);
        }
    }

    public void processAction(String action) {
        switch (action) {
            case DOWNLOAD_IMAGE:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadImage();
                    }
                }).start();
                break;
        }
    }

    public void downloadImage() {
        ImageView currentImageView = getMainImageView();
        Drawable drawable = currentImageView.getDrawable();
        Bitmap bitmap;
        if (drawable == null) {
            return;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
        } else {
            currentImageView.buildDrawingCache();
            bitmap = currentImageView.getDrawingCache();
        }

        FileOutputStream out = null;
        try {
            String fileName;
            String directoryPath;
            boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

            if (isSDPresent) {
                directoryPath = Environment.getExternalStorageDirectory() + "/" + Constant.DOWNLOAD_FOLDER_NAME + "/";
                fileName = Environment.getExternalStorageDirectory() + "/" + Constant.DOWNLOAD_FOLDER_NAME + "/" + photo.getTitle() + ".png";
            } else {
                directoryPath = Environment.getDataDirectory() + "/" + Constant.DOWNLOAD_FOLDER_NAME + "/";
                fileName = Environment.getDataDirectory() + "/" + Constant.DOWNLOAD_FOLDER_NAME + "/" + photo.getTitle() + ".png";
            }
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                if (!directory.mkdir()) {
                    DMobi.showToast(getContext(), getContext().getResources().getString(R.string.can_not_create_folder_to_save_download_image));
                    return;
                }
            }
            out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a loss less format, the compression factor (100) is ignored
            String message = photo.getTitle() + ".png";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // intent.setType("image/png");

            Uri imageUri = Uri.parse(fileName);
            // Uri imageUri = Uri.fromFile(new File(fileName));

            Uri hacked_uri = Uri.parse("file://" + imageUri.getPath());
            intent.setDataAndType(hacked_uri, "image/*");

            intent.putExtra(Intent.EXTRA_STREAM, imageUri);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.drawable.ic_file_download_white_24dp)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

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
}

