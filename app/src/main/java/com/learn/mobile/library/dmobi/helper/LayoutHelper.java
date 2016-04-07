package com.learn.mobile.library.dmobi.helper;

import android.util.Log;

import com.learn.mobile.R;
import com.learn.mobile.library.dmobi.DMobi;
import com.learn.mobile.model.Comment;
import com.learn.mobile.model.DMobileModelBase;
import com.learn.mobile.model.Funny;
import com.learn.mobile.model.Link;
import com.learn.mobile.model.Photo;
import com.learn.mobile.model.Search;
import com.learn.mobile.model.User;

import java.util.HashMap;

/**
 * Created by 09520_000 on 9/26/2015.
 */
public class LayoutHelper {
    public static final String TAG = LayoutHelper.class.getSimpleName();
    private static HashMap<String, Object> layoutMaps = new HashMap<>();
    private static HashMap<String, Object> layoutTypes = new HashMap<>();

    public final static String FEED_LAYOUT = "FEED_LAYOUT";
    public final static String LIST_LAYOUT = "LIST_LAYOUT";
    public final static int FEED_DEFAULT_LAYOUT = 5;

    static {
        layoutTypes.put(FEED_DEFAULT_LAYOUT + "", R.layout.feed_basic_layout);
        registerLayout(DMobileModelBase.class, LayoutHelper.FEED_LAYOUT, R.layout.feed_basic_layout);

        registerLayout(Photo.class, LayoutHelper.FEED_LAYOUT, R.layout.feed_photo_layout);
        registerLayout(Photo.class, LayoutHelper.FEED_LAYOUT, R.layout.feed_photo_layout_2, "2");
        registerLayout(Photo.class, LayoutHelper.FEED_LAYOUT, R.layout.feed_photo_layout_3, "3");
        registerLayout(Photo.class, LayoutHelper.FEED_LAYOUT, R.layout.feed_photo_layout_4, "4");
        registerLayout(Photo.class, LayoutHelper.LIST_LAYOUT, R.layout.photo_item_layout);

        registerLayout(User.class, LayoutHelper.LIST_LAYOUT, R.layout.user_item_custom_layout);
        registerLayout(Link.class, LayoutHelper.FEED_LAYOUT, R.layout.feed_link_layout);

        registerLayout(Funny.class, LayoutHelper.LIST_LAYOUT, R.layout.funny_item_layout);

        registerLayout(Comment.class, LayoutHelper.LIST_LAYOUT, R.layout.comment_item_layout);

        registerLayout(Search.class, LayoutHelper.LIST_LAYOUT, R.layout.user_item_layout);
    }

    public static int registerLayout(Class c, String suffix, int layout) {
        return registerLayout(c, suffix, layout, "");
    }

    public static int registerLayout(Class c, String suffix, int layout, String type) {
        Log.i(TAG, c.getSimpleName());
        String layoutType = c.getSimpleName() + "_" + suffix;
        if (!type.equals("")) {
            layoutType += "_" + type;
        }
        if (!layoutMaps.containsKey(layoutType)) {
            int id = DMobi.getIdentityId();
            layoutTypes.put(id + "", layout);
            layoutMaps.put(layoutType, id);
            return id;
        }
        return (int) layoutMaps.get(layoutType);
    }

    public static int getLayout(int id) {
        if (layoutTypes.containsKey(id + "")) {
            return (int) layoutTypes.get(id + "");
        }
        return 0;
    }

    public static int getLayoutType(Class c, String suffix) {
        return getLayoutType(c, suffix, "");
    }

    public static int getLayoutType(Class c, String suffix, String type) {
        String layoutType = c.getSimpleName() + "_" + suffix;
        if (!type.equals("")) {
            layoutType += "_" + type;
        }
        return (layoutMaps.get(layoutType) != null ? (int) layoutMaps.get(layoutType) : 0);
    }
}
