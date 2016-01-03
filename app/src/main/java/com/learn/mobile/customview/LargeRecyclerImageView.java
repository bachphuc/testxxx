package com.learn.mobile.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.helper.ImageHelper;
import com.learn.mobile.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 09520_000 on 1/2/2016.
 */
public class LargeRecyclerImageView extends RecyclerView {
    public static final String TAG = LargeRecyclerImageView.class.getSimpleName();
    LargeImageAdapter largeImageAdapter;
    LinearLayoutManager linearLayoutManager;

    public LargeRecyclerImageView(Context context) {
        super(context);
    }

    public LargeRecyclerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LargeRecyclerImageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageList(List<ImageItem> list) {
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            return;
        }
        if (largeImageAdapter == null) {
            largeImageAdapter = new LargeImageAdapter();
            setAdapter(largeImageAdapter);
        }
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(getContext());
            setLayoutManager(linearLayoutManager);
        }
        largeImageAdapter.setData(list);
        largeImageAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){
        if(largeImageAdapter != null){
            largeImageAdapter.notifyDataSetChanged();
        }
    }

    class LargeImageAdapter extends Adapter {
        List<ImageItem> data = new ArrayList<ImageItem>();

        public void setData(List<ImageItem> list) {
            data = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.large_image_view_item, parent, false);

            LargeImageViewHolder imageViewHolder = new LargeImageViewHolder(v);
            return imageViewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            LargeImageViewHolder viewHolder = (LargeImageViewHolder) holder;
            DFeedImageView imageView = viewHolder.imageView;
            ImageItem imageItem = data.get(position);
            float ratio = (float) imageItem.height / (float) imageItem.width;
            imageView.setScale(ratio);
            Log.i(TAG, imageItem.url);
            ImageHelper.display(imageView, imageItem.url);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class LargeImageViewHolder extends ViewHolder {
        public DFeedImageView imageView;

        public LargeImageViewHolder(View itemView) {
            super(itemView);

            imageView = (DFeedImageView) itemView.findViewById(R.id.im_image_piece);
        }
    }
}
