package com.learn.mobile.library.dmobi.helper.ImageHelperLib;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.davemorrissey.labs.subscaleview.decoder.ImageDecoder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by gokhanbarisaker on 8/30/15.
 */
public class PicassoDecoder implements ImageDecoder {
    private Picasso picasso;

    public PicassoDecoder(){

    }

    @Override
    public Bitmap decode(Context context, Uri uri) throws Exception {
        return Picasso.with(context)
                .load(uri)
                .config(Bitmap.Config.RGB_565)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .get();
    }
}
