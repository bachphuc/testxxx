package com.learn.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.learn.mobile.customview.com.faradaj.blurbehind.BlurBehind;
import com.learn.mobile.customview.com.faradaj.blurbehind.OnBlurCompleteListener;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.library.dmobi.event.Event;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.Photo;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PhotoViewFragment extends Fragment implements View.OnClickListener, Event.Action {
    public static final String TAG = PhotoViewFragment.class.getSimpleName();
    protected Photo photo;
    protected View view;

    ViewPager viewPager;

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
        ImageView imageView = (ImageView) view.findViewById(R.id.im_imageView);
        if (photo != null && imageView != null) {
            if (photo.images != null) {
                if (imageView instanceof DFeedImageView) {
                    float ratio = (float) photo.images.getLarge().height / (float) photo.images.getLarge().width;
                    DFeedImageView dFeedImageView = (DFeedImageView) imageView;
                    dFeedImageView.setScale(ratio);
                    ImageHelper.display(imageView, photo.images.getLarge().url);
                } else if (imageView instanceof GestureImageView) {
                    GestureImageView gestureImageView = (GestureImageView) imageView;
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

                    ImageHelper.display(gestureImageView, photo.images.getExtraLarge().url, photo.images.getLarge().url);

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

        imageView = (ImageView) view.findViewById(R.id.bt_comment);
        imageView.setOnClickListener(this);

        imageView = (ImageView) view.findViewById(R.id.bt_like);
        imageView.setOnClickListener(this);

        imageView = (ImageView) view.findViewById(R.id.bt_share);
        imageView.setOnClickListener(this);

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
        if (o instanceof Event.ModelAction) {
            DMobi.log(TAG, "Update view...");
            updateView();
        }
    }
}
