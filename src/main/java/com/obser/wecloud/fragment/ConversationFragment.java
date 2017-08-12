package com.obser.wecloud.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.obser.wecloud.R;
import com.obser.wecloud.bean.DialogUnReadProvider;
import com.obser.wecloud.bean.MessagesListProvider;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.activity.MessageActivity;
import com.obser.wecloud.app.WeCloudApplication;
import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.bean.Message;
import com.obser.wecloud.bean.UserListInfo;
import com.obser.wecloud.core.ChatTransDataEventImpl;
import com.obser.wecloud.core.ClientCoreSDK;
import com.obser.wecloud.utils.NToast;
import com.obser.wecloud.utils.NetUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
    private Activity mContext;
    private Gson gson;
    private List<UserListInfo.BodyBean.GroupsBean> groups;
    private User mUser;
    private RefreshLayout refreshLayout;
    private List<Dialog> dialogList;
    private Date loginDate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        initView(view);
        initAdapter();
        initData();
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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshData();
            }
        });
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
        dialogList = new ArrayList<>();
        userList = new ArrayList<>();
        WeCloudApplication application = (WeCloudApplication) getActivity().getApplication();
        mUser = application.getUser();
        loginDate = new Date();
        gson = new Gson();
        refreshData();
    }


    /**
     * 向服务器请求数据
     */
    private void refreshData(){
        NetUtils.doGet(NetUtils.FIND_ALL_USER + "/" + mUser.getUsername(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NToast.shortToast(mContext, "刷新失败，请检查网络！");
                                refreshLayout.finishRefresh(false);
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String userListJson = response.body().string();
                        Log.d("正确得到所有用户", userListJson);
                        UserListInfo userListInfo = gson.fromJson(userListJson, UserListInfo.class);
                        DialogUnReadProvider.setUnReadByList(dialogList);
                        /* 清空旧数据 start */
                        userList.clear();
                        dialogList.clear();
                        /* 清空旧数据 end */
                        userList.addAll(userListInfo.getBody().getONLINEUSER());
                        userList.addAll(userListInfo.getBody().getOFFLINEUSER());
                        userList.remove(mUser);
                        groups = userListInfo.getBody().getGROUPS();
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogsListAdapter.clear();
                                //刷新私聊Dialog
                                for(User user : userList){
                                    if(user.getUsername().equals(mUser.getUsername()))
                                        continue;
                                    Log.d("ConversationFragment", user.getName() + ":" + user.getIp());
                                    ArrayList<User> users = new ArrayList<>();
                                    users.add(user);
                                    Message message = getDialogLastMessage(user);
                                    Dialog dialog = new Dialog(user.getUsername(), user.getRealname(), user.getPicture(), users, message, 0, true);
                                    dialogList.add(dialog);
//                                    onNewDialog(user.getUsername(), new Message(String.valueOf(System.currentTimeMillis()), user, ""), 0);
                                }
                                //刷新群聊Dialog
                                for(UserListInfo.BodyBean.GroupsBean groupsBean : groups){
                                    ArrayList<User> users = (ArrayList<User>) groupsBean.getGROUPSUSER();
                                    UserListInfo.BodyBean.GroupsBean.GroupsInfoBean groupsInfo = groupsBean.getGROUPSINFO();
//                                    Message message = new Message(String.valueOf(System.currentTimeMillis()), mUser, "");
                                    Message message = getDialogLastMessage(String.valueOf(groupsInfo.getGroupsId()));
                                    Dialog dialog = new Dialog(String.valueOf(groupsInfo.getGroupsId()), groupsInfo.getGroupsName(), groupsInfo.getGroupsPicture(), users, message, 0, false);
                                    dialogList.add(dialog);
//                                    onNewDialog(dialog, message);
                                }
                                //刷新Dialog列表
                                for(Dialog dialog : dialogList)
                                    dialog.setUnreadCount(DialogUnReadProvider.getUnReadById(dialog.getId()));
                                dialogsListAdapter.sortByLastMessageDate();
                                dialogsListAdapter.setItems(dialogList);
                                ClientCoreSDK.getInstance().getChatTransDataEvent().setDialogList(dialogList);
                                refreshLayout.finishRefresh(true);
                            }
                        });
                    }
                }
        );
    }


    /**
     * 更新私聊最后一条消息
     * @param user
     * @return Message
     */
    private Message getDialogLastMessage(User user){
        List<Message> messages = MessagesListProvider.getMessagesById(user.getUsername());
        Message message = null;
        if(!messages.isEmpty())
            message = messages.get(0);
        if(message == null){
            message = new Message(String.valueOf(System.currentTimeMillis()), user, "", loginDate);
        }
        return message;
    }

    /**
     * 更新群聊最后一条消息
     * @param dialogId
     * @return Message
     */
    private Message getDialogLastMessage(String dialogId){
        List<Message> messages = MessagesListProvider.getMessagesById(dialogId);
        Message message = null;
        if(!messages.isEmpty())
            message = messages.get(0);
        if(message == null)
            message = new Message(String.valueOf(System.currentTimeMillis()), mUser, "", loginDate);
        return message;
    }



    /**
     * 初始化私聊（已废弃）
     * @param dialog
     * @param message
     */
    public void onNewDialog(Dialog dialog, IMessage message){
        if (!dialogsListAdapter.updateDialogWithMessage(dialog.getId(), message)) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or reload all dialogs list
            dialogsListAdapter.addItem(dialog);
        }
    }

    /**
     * 初始化群组（已废弃）
     * @param dialogId
     * @param message
     * @param unRead
     */
    public void onNewDialog(String dialogId, IMessage message, int unRead) {
        if (!dialogsListAdapter.updateDialogWithMessage(dialogId, message)) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or reload all dialogs list
            User user = (User) message.getUser();
            String name = user.getRealname();
            String photo = user.getAvatar();
            ArrayList<User> users = new ArrayList<>();
            users.add((User) message.getUser());
            Log.d("ConversationFragment", dialogId);
            dialogsListAdapter.addItem(new Dialog(dialogId, name, photo, users, (Message) message, unRead, true));
        }
    }


    private void initView(View view) {
        mDialogsList = (DialogsList) view.findViewById(R.id.dialogsList);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        MessageActivity.open(this.getActivity(), dialog, dialogsListAdapter);
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
