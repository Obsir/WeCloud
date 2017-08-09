package com.obser.wecloud.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obser.wecloud.R;
import com.obser.wecloud.view.SelectableRoundedImageView;


/**
 * Created by AMing on 16/6/21.
 * Company RongCloud
 */
public class MineFragment extends Fragment{
    private SelectableRoundedImageView imageView;
    private TextView mName;
    private ImageView mNewVersionView;
    private boolean isHasNewVersion;
    private String url;
    private boolean isDebug;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.seal_mine_fragment, container, false);
        initViews(mView);
        return mView;
    }



    private void initViews(View mView) {
        mNewVersionView = (ImageView) mView.findViewById(R.id.new_version_icon);
        imageView = (SelectableRoundedImageView) mView.findViewById(R.id.mine_header);
        mName = (TextView) mView.findViewById(R.id.mine_name);
        LinearLayout mUserProfile = (LinearLayout) mView.findViewById(R.id.start_user_profile);
        LinearLayout mMineSetting = (LinearLayout) mView.findViewById(R.id.mine_setting);
        LinearLayout mMineService = (LinearLayout) mView.findViewById(R.id.mine_service);
        LinearLayout mMineXN = (LinearLayout) mView.findViewById(R.id.mine_xiaoneng);
        LinearLayout mMineAbout = (LinearLayout) mView.findViewById(R.id.mine_about);
        if(isDebug){
            mMineXN.setVisibility(View.VISIBLE);
        }else{
            mMineXN.setVisibility(View.GONE);
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
