package com.obser.wecloud.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.obser.wecloud.R;
import com.obser.wecloud.User;
import com.obser.wecloud.activity.MainActivity;
import com.obser.wecloud.activity.MessageActivity;
import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.bean.Message;
import com.obser.wecloud.bean.MessageForUser;
import com.obser.wecloud.core.ChatTransDataEventImpl;
import com.obser.wecloud.core.ClientCoreSDK;
import com.obser.wecloud.fixtures.Avatars;
import com.obser.wecloud.fixtures.DialogsFixtures;
import com.obser.wecloud.utils.NToast;
import com.obser.wecloud.utils.NetUtils;
import com.obser.wecloud.utils.UDPUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Obser on 2017/8/3.
 */

public class ConversationFragment extends Fragment implements DateFormatter.Formatter, DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    private DialogsList mDialogsList;
    private DialogsListAdapter<Dialog> dialogsListAdapter;
    //自定义
    private List<User> userList;
    private List<com.obser.wecloud.bean.User> userChatList;
    private Activity mContext;
    private String userAccount;
    private com.obser.wecloud.bean.User mUser;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        initView(view);
        initData();
        initAdapter();
        initClientSDK();
//        updateUI();
//        refreshUIListener();
        return view;
    }

    private void initClientSDK() {
        ClientCoreSDK.getInstance().init(this.getContext());
        ClientCoreSDK.getInstance().setChatTransDataEvent(new ChatTransDataEventImpl());
        ClientCoreSDK.getInstance().getChatTransDataEvent().setDialogsListAdapter(dialogsListAdapter);
    }



    private void initAdapter() {
        //If you using another library - write here your way to load image
        dialogsListAdapter = new DialogsListAdapter<>(R.layout.item_dialog_fragment, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //If you using another library - write here your way to load image
                Picasso.with(ConversationFragment.this.getActivity()).load(url).error(R.drawable.status_servant_1_94).into(imageView);
            }
        });

//        dialogsListAdapter.setItems(DialogsFixtures.getDialogs());
        dialogsListAdapter.setOnDialogClickListener(this);
        dialogsListAdapter.setOnDialogLongClickListener(this);
        dialogsListAdapter.setDatesFormatter(this);
        mDialogsList.setAdapter(dialogsListAdapter);
    }

    private void initData() {
        mContext = this.getActivity();
        userChatList = new ArrayList<>();
        userAccount = getActivity().getIntent().getStringExtra("account");
        Log.d("Conversation", userAccount);
//        Avatars.init(this.getContext());
        NetUtils.doGet(NetUtils.FIND_ALL_USER, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NToast.shortToast(mContext, "刷新失败，请检查网络！");
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String userListJson = response.body().string();
                        Log.d("正确得到所有用户", userListJson);
                        Gson gson = new Gson();
                        MessageForUser messageForUser = gson.fromJson(userListJson, MessageForUser.class);
                        userList = messageForUser.getBody().getONLINEUSER();
                        userList.addAll(messageForUser.getBody().getOFFLINEUSER());
                        for(User user : userList){
                            if(user.getUsername().equals(userAccount)){
                                mUser = new com.obser.wecloud.bean.User("0", user.getRealname(), user.getPicture(), UDPUtils.getIPAddress(mContext), user.getState() == 1);
                            }
                            Log.d("Conversation", user.getRealname() + ":" + user.getUsername());
                            com.obser.wecloud.bean.User newUser = new com.obser.wecloud.bean.User("1", user.getRealname(), "nnnn", user.getIp(), user.getState() == 1);
                            userChatList.add(newUser);
                        }
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(com.obser.wecloud.bean.User user : userChatList){
                                    Log.d("ConversationFragment", user.getName() + ":" + user.getIp());
                                    onNewMessage(user.getName() + ":" + user.getIp(), new Message(String.valueOf(System.currentTimeMillis()), user, ""), 0);
                                }
                            }
                        });
                    }
                }
        );
    }


    public void onNewMessage(String dialogId, IMessage message, int unRead) {
        if (!dialogsListAdapter.updateDialogWithMessage(dialogId, message)) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or reload all dialogs list
            String name = message.getUser().getName();
            String photo = message.getUser().getAvatar();
            ArrayList<com.obser.wecloud.bean.User> users = new ArrayList<>();
            users.add((com.obser.wecloud.bean.User) message.getUser());
            dialogsListAdapter.addItem(new Dialog(dialogId, name, photo, users, (Message) message, unRead));
        }
    }


    private void initView(View view) {
        mDialogsList = (DialogsList) view.findViewById(R.id.dialogsList);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        MessageActivity.open(this.getActivity(), dialog, mUser, dialogsListAdapter);
//        this.getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
//        Toast.makeText(this.getActivity(), "Hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else if (DateFormatter.isCurrentYear(date)) {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }
}
