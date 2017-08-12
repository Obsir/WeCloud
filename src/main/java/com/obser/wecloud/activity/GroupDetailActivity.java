package com.obser.wecloud.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.obser.wecloud.R;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.fragment.ConversationFragment;
import com.obser.wecloud.view.DemoGridView;
import com.obser.wecloud.view.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    private TextView tv_title;
    private List<User> users;
    private TextView mTextViewMemberSize;
    private DemoGridView mGridView;
    private boolean isCreated;
    private TextView mGroupDisplayNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        initView();
        initData();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        mTextViewMemberSize = (TextView) findViewById(R.id.group_member_size);
        mGridView = (DemoGridView) findViewById(R.id.gridview);
        mGroupDisplayNameText = (TextView) findViewById(R.id.group_displayname_text);
    }

    private void initData(){
        isCreated = true;
        users = (List<User>) getIntent().getSerializableExtra("users");
        initGroupMemberData();
    }


    private void initGroupMemberData() {
        if (users != null && users.size() > 0) {
            tv_title.setText(getString(R.string.group_info) + "(" + users.size() + ")");
            mTextViewMemberSize.setText(getString(R.string.group_member_size) + "(" + users.size() + ")");
            mGridView.setAdapter(new GridAdapter(this, users));
        } else {
            return;
        }

        for (User user : users) {
//            if (user.getUserId().equals(getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, ""))) {
//                if (!TextUtils.isEmpty(user.getDisplayName())) {
//                    mGroupDisplayNameText.setText(user.getDisplayName());
//                } else {
//                    mGroupDisplayNameText.setText("无");
//                }
//            }
            mGroupDisplayNameText.setText(user.getRealname());
        }
    }

    private class GridAdapter extends BaseAdapter {

        private List<User> list;
        Context context;


        public GridAdapter(Context context, List<User> list) {
            if (list.size() >= 31) {
                this.list = list.subList(0, 30);
            } else {
                this.list = list;
            }

            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.social_chatsetting_gridview_item, parent, false);
            }
            SelectableRoundedImageView iv_avatar = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_avatar);
            TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            ImageView badge_delete = (ImageView) convertView.findViewById(R.id.badge_delete);

            // 最后一个item，减人按钮
            if (position == getCount() - 1 && isCreated) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.icon_btn_deleteperson);

//                iv_avatar.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
//                        intent.putExtra("isDeleteGroupMember", true);
//                        intent.putExtra("GroupId", mGroup.getGroupsId());
//                        startActivityForResult(intent, 101);
//                    }
//
//                });
            } else if ((isCreated && position == getCount() - 2) || (!isCreated && position == getCount() - 1)) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.jy_drltsz_btn_addperson);

//                iv_avatar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
//                        intent.putExtra("isAddGroupMember", true);
//                        intent.putExtra("GroupId", mGroup.getGroupsId());
//                        startActivityForResult(intent, 100);
//
//                    }
//                });
            } else { // 普通成员
                final User bean = list.get(position);
                tv_username.setText(bean.getRealname());

//                Friend friend = SealUserInfoManager.getInstance().getFriendByID(bean.getUserId());
//                if (friend != null && !TextUtils.isEmpty(friend.getDisplayName())) {
//                    tv_username.setText(friend.getDisplayName());
//                } else {
//                    tv_username.setText(bean.getName());
//                }

                Picasso.with(GroupDetailActivity.this).load(bean.getPicture()).error(R.drawable.status_servant_1_94).into(iv_avatar);
//                String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(bean);
//                ImageLoader.getInstance().displayImage(portraitUri, iv_avatar, App.getOptions());
//                iv_avatar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        UserInfo userInfo = new UserInfo(bean.getUserId(), bean.getName(), TextUtils.isEmpty(bean.getPortraitUri().toString()) ? Uri.parse(RongGenerate.generateDefaultAvatar(bean.getName(), bean.getUserId())) : bean.getPortraitUri());
//                        Intent intent = new Intent(context, UserDetailActivity.class);
//                        Friend friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
//                        intent.putExtra("friend", friend);
//                        intent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
//                        //Groups not Serializable,just need group name
//                        intent.putExtra("groupName", mGroup.getName());
//                        intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
//                        context.startActivity(intent);
//                    }
//
//                });

            }

            return convertView;
        }

        @Override
        public int getCount() {
            if (isCreated) {
                return list.size() + 2;
            } else {
                return list.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<User> list) {
            this.list = list;
            notifyDataSetChanged();
        }

    }
}
