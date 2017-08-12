package com.obser.wecloud.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.obser.wecloud.R;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.view.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PrivateChatDetailActivity extends AppCompatActivity {

    private TextView tv_title;
    private SelectableRoundedImageView mImageView;
    private TextView friendName;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_friend_detail);
        initView();
        initData();
    }

    private void initData() {
        tv_title.setText(R.string.user_details);
        List<User> users = (List<User>) getIntent().getSerializableExtra("users");
        if(!users.isEmpty())
            user = users.get(0);
        friendName.setText(user.getRealname());
        Picasso.with(this).load(user.getPicture()).error(R.drawable.status_servant_1_94).into(mImageView);
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        mImageView = (SelectableRoundedImageView) findViewById(R.id.friend_header);
        friendName = (TextView) findViewById(R.id.friend_name);
    }
}
