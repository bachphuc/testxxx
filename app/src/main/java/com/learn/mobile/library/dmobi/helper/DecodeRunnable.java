package com.learn.mobile.library.dmobi.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import com.learn.mobile.model.ImageItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by 09520_000 on 1/2/2016.
 */
public class DecodeRunnable implements Runnable {
    public static final String TAG = DecodeRunnable.class.getSimpleName();
    private Bitmap bitmap;
    private ImageItem imageItem;
    private List<ImageItem> imageLists = new ArrayList<ImageItem>();
    private DecodeRunnableListener decodeRunnableListener;
    private String externalCacheDir;
    private static Hashtable<String, Object> globalLargeCaches = new Hashtable<String, Object>();

    public static void pushLargeImageToCache(String key, Object o) {
        globalLargeCaches.put(key, o);
    }

    private static Object getData(String key) {
        return globalLargeCaches.get(key);
    }

    public static List<ImageItem> getImageListFromCache(String fileName) {
        Object o = DecodeRunnable.getData(fileName);
        if (o == null) {
            return null;
        }
        return (List<ImageItem>) o;
    }

    @Override
    public void run() {
        if (imageItem == null) {
            return;
        }
        if (DecodeRunnable.getImageListFromCache(imageItem.name) != null) {
            imageLists = DecodeRunnable.getImageListFromCache(imageItem.name);
            if (decodeRunnableListener != null) {
                decodeRunnableListener.onComplete(imageLists);
                return;
            }
        }

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int splitHeight = 1000;
        float count = (float) bitmap.getHeight() / splitHeight;
        int quality = 100;

        Bitmap newBitmap;
        InputStream is = null;
        BitmapRegionDecoder decoder = null;

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            is = new ByteArrayInputStream(stream.toByteArray());
            decoder = BitmapRegionDecoder.newInstance(is, false);
            Rect rect;
            ImageItem imgItem;
            FileOutputStream out = null;

            for (float i = 0; i < count; i++) {
                String imageCacheFileDir = externalCacheDir + "/" + imageItem.name + "_" + (int) i + ".jpg";
                File file = new File(imageCacheFileDir);
                if (file.exists()) {
                    ImageHelper.Size size = ImageHelper.getImageSize(imageCacheFileDir);
                    imgItem = new ImageItem();
                    imgItem.width = size.getWidth();
                    imgItem.height = size.getHeight();
                    imgItem.url = imageCacheFileDir;
                    imageLists.add(imgItem);
                    continue;
                }
                rect = new Rect();
                int bottom = (int) i * splitHeight + splitHeight;
                int newHeight;
                if (bottom > height) {
                    bottom = height;
                }
                newHeight = bottom - (int) i * splitHeight;
                rect.set(0, (int) i * splitHeight, width, bottom);

                newBitmap = decoder.decodeRegion(rect, null);

                try {
                    out = new FileOutputStream(imageCacheFileDir, false);

                    // bmp is your Bitmap instance
                    // PNG is a loss less format, the compression factor (100) is ignored
                    newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                    imgItem = new ImageItem();
                    imgItem.width = width;
                    imgItem.height = newHeight;
                    imgItem.url = imageCacheFileDir;
                    imageLists.add(imgItem);
                    if (decodeRunnableListener != null) {
                        decodeRunnableListener.onUpdate(imageLists);
                    }

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
            if (imageLists.size() > 0) {
                DecodeRunnable.pushLargeImageToCache(imageItem.name, imageLists);
            }
            if (decodeRunnableListener != null) {
                decodeRunnableListener.onComplete(imageLists);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImageLists(List<ImageItem> list) {
        imageLists = list;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setImageItem(ImageItem imageItem) {
        this.imageItem = imageItem;
    }

    public void addDecodeListener(DecodeRunnableListener listener) {
        decodeRunnableListener = listener;
    }


    public void setExternalCacheDir(String path) {
        externalCacheDir = path;
    }

    public interface DecodeRunnableListener {
        public void onComplete(List<ImageItem> list);

        public void onUpdate(List<ImageItem> list);
    }
}