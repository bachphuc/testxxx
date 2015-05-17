package com.learn.turtorial1;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 09520_000 on 5/11/2015.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private List<ContactInfo> contactInfoList;
    private DisplayImageOptions displayImageOptions;
    private AnimateImageListener animateImageListener;

    public ContactAdapter(List<ContactInfo> contactInfos){
        this.contactInfoList = contactInfos;
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();

        animateImageListener = new AnimateImageListener();

    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if(viewType == LayoutFeed.FEED_LAYOUT){
            Log.i("ContactAdapter","line-25:i=" + viewType);
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.feed_layout, viewGroup, false);
        }
        else if(viewType == LayoutFeed.META1_LAYOUT){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.material_basic_buttons_card, viewGroup, false);
        }
        else if(viewType == LayoutFeed.META2_LAYOUT){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.material_basic_image_buttons_card_layout, viewGroup, false);
        }
        else if(viewType == LayoutFeed.META3_LAYOUT){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.material_basic_list_card_layout, viewGroup, false);
        }
        else if(viewType == LayoutFeed.META4_LAYOUT){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.material_big_image_card_layout, viewGroup, false);
        }
        else if(viewType == LayoutFeed.META5_LAYOUT){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.material_image_with_buttons_card, viewGroup, false);
        }
        else if(viewType == LayoutFeed.META6_LAYOUT){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.material_small_image_card, viewGroup, false);
        }
        else if(viewType == LayoutFeed.META7_LAYOUT){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.material_welcome_card_layout, viewGroup, false);
        }
        else{
            Log.i("ContactAdapter","line-30:i=" + viewType);
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.card_layout, viewGroup, false);
        }

        return new ContactViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position){
        ContactInfo contactInfo = contactInfoList.get(position);
        return contactInfo.type;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int position) {
        ContactInfo contactInfo = contactInfoList.get(position);
        if(contactInfo.type == LayoutFeed.FEED_LAYOUT){
            contactViewHolder.vFeedTitle.setText(contactInfo.feedTitle);
        }
        else if(contactInfo.type == LayoutFeed.EVENT_LAYOUT){
            contactViewHolder.vName.setText(contactInfo.name);
            contactViewHolder.vSurname.setText(contactInfo.surname);
            contactViewHolder.vEmail.setText(contactInfo.email);
            contactViewHolder.vTitle.setText(contactInfo.name + " " + contactInfo.surname);
            ImageLoader.getInstance().displayImage(contactInfo.image,contactViewHolder.vImage,displayImageOptions, animateImageListener);
        }
    }

    @Override
    public int getItemCount() {
        return contactInfoList.size();
    }

    private static class AnimateImageListener implements ImageLoadingListener{
        static final List<String> displayImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(loadedImage != null){
                ImageView imageView = (ImageView)view;
                boolean firstDisplay = !displayImages.contains(imageUri);
                if(firstDisplay){
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
