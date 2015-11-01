package com.learn.mobile.fragment;

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
import com.learn.mobile.customview.DFeedImageView;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.Photo;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PhotoViewFragment extends Fragment {
    protected Photo photo;
    protected View view;

    ViewPager viewPager;

    public void setViewPager(ViewPager v){
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
        view = inflater.inflate(R.layout.fragment_photo_view_v1, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.im_imageView);
        if (photo != null && imageView != null) {
            if (photo.images != null) {
                if (imageView instanceof DFeedImageView) {
                    float ratio = (float) photo.images.large.height / (float) photo.images.large.width;
                    DFeedImageView dFeedImageView = (DFeedImageView) imageView;
                    dFeedImageView.setScale(ratio);
                    ImageHelper.display(imageView, photo.images.large.url);
                } else if (imageView instanceof GestureImageView) {
                    GestureImageView gestureImageView = (GestureImageView) imageView;
                    gestureImageView.getController().getSettings()
                            .setMaxZoom(4f)
                            .setPanEnabled(true)
                            .setZoomEnabled(true)
                            .setDoubleTapEnabled(true)
                            .setRotationEnabled(false)
                            .setOverscrollDistance(0f, 0f)
                            .setOverzoomFactor(2f)
                            .setFillViewport(false)
                            .setFitMethod(Settings.Fit.INSIDE)
                            .setGravity(Gravity.CENTER);

                    ImageHelper.display(gestureImageView, photo.images.large.url);

                    if(viewPager != null){
                        gestureImageView.getController().enableScrollInViewPager(viewPager);
                    }
                }
            }
        }

        TextView textView = (TextView) view.findViewById(R.id.tvTitle);
        textView.setText(photo.user.getTitle());

        if (photo.user.images != null) {
            CircleImageView avatarImageView = (CircleImageView) view.findViewById(R.id.imageViewAvatar);
            if (avatarImageView != null) {
                ImageHelper.display(avatarImageView, photo.user.images.avatar.url);
            }
        }

        return view;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
