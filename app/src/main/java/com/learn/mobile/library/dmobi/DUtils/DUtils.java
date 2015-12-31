package com.learn.mobile.library.dmobi.DUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;

import com.learn.mobile.R;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;

/**
 * Created by 09520_000 on 8/22/2015.
 */
public class DUtils {
    /**
     * Trim specified charcater from front of string
     *
     * @param text      Text
     * @param character Character to remove
     * @return Trimmed text
     */
    public static String trimFront(String text, char character) {
        String normalizedText;
        int index;

        if (StringUtils.isEmpty(text)) {
            return text;
        }

        normalizedText = text.trim();
        index = 0;

        while (normalizedText.charAt(index) == character) {
            index++;
        }
        return normalizedText.substring(index).trim();
    }

    /**
     * Trim specified charcater from end of string
     *
     * @param text      Text
     * @param character Character to remove
     * @return Trimmed text
     */
    public static String trimEnd(String text, char character) {
        String normalizedText;
        int index;

        if (StringUtils.isEmpty(text)) {
            return text;
        }

        normalizedText = text.trim();
        index = normalizedText.length() - 1;

        while (normalizedText.charAt(index) == character) {
            if (--index < 0) {
                return "";
            }
        }
        return normalizedText.substring(0, index + 1).trim();
    }

    /**
     * Trim specified charcater from both ends of a String
     *
     * @param text      Text
     * @param character Character to remove
     * @return Trimmed text
     */
    public static String trimAll(String text, char character) {
        String normalizedText = trimFront(text, character);

        return trimEnd(normalizedText, character);
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
                return res;
            }
            cursor.close();
        }

        return contentUri.getPath();
    }

    public static String getString(String str) {
        if (str != null) {
            return str;
        }
        return "";
    }

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return (o.equals("") ? true : false);
        }
        if (o instanceof Integer) {
            return (o.equals(0) ? true : false);
        }
        return false;
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    public static String decodeUTF8(String str) {
        String name = "";
        try {
            name = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            name = str;
        }

        String decodedName = Html.fromHtml(name).toString();
        return decodedName;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

}
