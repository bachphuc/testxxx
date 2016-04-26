package com.learn.mobile;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.*;

/**
 * Created by BachPhuc on 4/9/2016.
 */
public class DMobiApplication extends Application {

    public static RefWatcher getRefWatcher(Context context) {
        DMobiApplication application = (DMobiApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }
}
