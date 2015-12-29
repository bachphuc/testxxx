package com.learn.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.learn.mobile.R;
import com.learn.mobile.fragment.visitor.LoginFragment;
import com.learn.mobile.fragment.visitor.RegisterFragment;
import com.learn.mobile.fragment.visitor.UploadAvatarFragment;
import com.learn.mobile.library.dmobi.DMobi;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, UploadAvatarFragment.OnFragmentInteractionListener {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private ViewPager viewPager;
    private LoginViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        viewPager = (ViewPager) findViewById(R.id.login_view_pager);
        adapter = new LoginViewPagerAdapter(getFragmentManager());
        adapter.setContext(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onFragmentInteraction(View view) {
        switch (view.getId()) {
            case R.id.bt_signUp:
                viewPager.setCurrentItem(1);
                break;
            case R.id.bt_show_login:
                viewPager.setCurrentItem(0);
                break;
            case R.id.bt_end_step_info:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!DMobi.isUser()) {
            exitApp();
        }
    }

    public void exitApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static class LoginViewPagerAdapter extends FragmentPagerAdapter {
        public LoginViewPagerAdapter(android.app.FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        private Context context;

        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public android.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    LoginFragment loginFragment = new LoginFragment();
                    loginFragment.setLister(context);
                    return loginFragment;
                case 1:
                    RegisterFragment registerFragment = new RegisterFragment();
                    registerFragment.setLister(context);
                    return registerFragment;
                case 2:
                    UploadAvatarFragment uploadAvatarFragment = new UploadAvatarFragment();
                    uploadAvatarFragment.setLister(context);
                    return uploadAvatarFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
