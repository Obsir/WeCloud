package com.obser.wecloud.core;

import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.bean.Message;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;

/**
 * Created by Obser on 2017/8/7.
 */

public interface ChatTransDataEvent {
    void onMessageReceive(String message);
    void onErrorResponse(int errorCode, String errorMsg);
    //自定义
    void setMessagesAdapter(MessagesListAdapter<Message> messagesAdapter);
    void setDialogsListAdapter(DialogsListAdapter<Dialog> dialogsListAdapter);
    ArrayList<Message> getMessages();
}
