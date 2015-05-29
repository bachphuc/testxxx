package com.learn.turtorial1;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.turtorial1.model.Feed;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 09520_000 on 5/11/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private List<Feed> feeds;
    private DisplayImageOptions displayImageOptions;
    private AnimateImageListener animateImageListener;

    public ContactAdapter(List<Feed> feeedList) {
        this.feeds = feeedList;
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(64)).build();

        animateImageListener = new AnimateImageListener();

    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.material_basic_buttons_card, viewGroup, false);

        return new FeedViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder feedViewHolder, int position) {
        Feed feed = feeds.get(position);
        ImageView imageView;
        if (feed.user.images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.imageViewAvatar);
            ImageLoader.getInstance().displayImage(feed.user.images.full, imageView, displayImageOptions);
        }

        if (feed.item.images != null) {
            imageView = (ImageView) feedViewHolder.findView(R.id.main_image);
            DisplayImageOptions displayImageOp = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageOnFail(R.drawable.ic_error)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(0)).build();

            ImageLoader.getInstance().displayImage(feed.item.images.full, imageView, displayImageOp);
        }

        TextView textView = (TextView) feedViewHolder.findView(R.id.tvTitle);
        textView.setText(feed.user.getTitle());
        textView = (TextView) feedViewHolder.findView(R.id.tvDescription);
        textView.setText(feed.content);
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    private static class AnimateImageListener implements ImageLoadingListener {
        static final List<String> displayImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayImages.contains(imageUri);
                if (firstDisplay) {
                    displayImages.add(imageUri);
                    FadeInBitmapDisplayer.animate(imageView, 500);
                }
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }
}
