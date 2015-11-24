package com.learn.mobile.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.learn.mobile.R;
import com.learn.mobile.fragment.visitor.LoginFragment;
import com.learn.mobile.fragment.visitor.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener{
    public static final String TAG = LoginActivity.class.getSimpleName();
    private ViewPager viewPager;
    private LoginViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        viewPager = (ViewPager) findViewById(R.id.login_view_pager);
        adapter = new LoginViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(View view) {
        switch (view.getId()){
            case R.id.bt_signUp:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    public static class LoginViewPagerAdapter extends FragmentStatePagerAdapter {
        public LoginViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LoginFragment();
                case 1:
                    return new RegisterFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
