package com.learn.mobile.library.dmobi.helper.DbHelperLib;

import android.provider.BaseColumns;

/**
 * Created by 09520_000 on 1/10/2016.
 */

public final class SearchReader {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    public SearchReader() {
    }

    /* Inner class that defines the table contents */
    public static abstract class SearchEntry implements BaseColumns {
        public static final String TABLE_NAME = "search";
        public static final String COLUMN_ITEM_TYPE = "item_type";
        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}