package com.learn.mobile.fragment;

import com.learn.mobile.service.SUser;

/**
 * Created by 09520_000 on 9/26/2015.
 */
public class UserFragment extends ListBaseFragment{
    public UserFragment(){
        setServiceClass(SUser.class);
    }
}
