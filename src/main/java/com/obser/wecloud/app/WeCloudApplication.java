package com.obser.wecloud.app;

import android.app.Application;

import com.obser.wecloud.bean.ChatUser;
import com.obser.wecloud.bean.User;

/**
 * Created by Obser on 2017/8/11.
 */

public class WeCloudApplication extends Application {

    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
