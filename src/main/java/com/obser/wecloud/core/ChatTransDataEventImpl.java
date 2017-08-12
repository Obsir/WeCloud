package com.obser.wecloud.core;

import android.util.Log;

import com.obser.wecloud.bean.ChatUser;
import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.bean.Message;
import com.obser.wecloud.bean.MessagesListProvider;
import com.obser.wecloud.bean.ProtocolMessage;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.protocol.Protocol;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Obser on 2017/8/7.
 */

public class ChatTransDataEventImpl implements ChatTransDataEvent {

    private  DialogsListAdapter<Dialog> dialogsListAdapter;
    private MessagesListAdapter<Message> messagesAdapter;
    private List<Dialog> dialogList;
    public void setMessagesAdapter(MessagesListAdapter<Message> messagesAdapter){
        this.messagesAdapter = messagesAdapter;
    }


    @Override

    public void setDialogsListAdapter(DialogsListAdapter<Dialog> dialogsListAdapter) {
        this.dialogsListAdapter = dialogsListAdapter;
    }

    @Override
    public void setDialogList(List<Dialog> dialogList) {
        this.dialogList = dialogList;
    }

    @Override
    public void onMessageReceive(String message) {
        ProtocolMessage protocolMessage = Protocol.unPackMessage(message);
        User user = protocolMessage.getChatUser();
        user.setId("1");
        Log.d("Conversation", message);
        Message msg = new Message(String.valueOf(System.currentTimeMillis()), user, protocolMessage.getContent());
//        msg.setImage(new Message.Image(avatarUrl));
        String dialogId;
        if(!protocolMessage.getMode())
            dialogId = protocolMessage.getDialogId();
        else
            dialogId = user.getUsername();

        List<Message> list = MessagesListProvider.getMessagesById(dialogId);
        list.add(msg);

        onNewMessage(dialogId, msg, protocolMessage.getMode());
        if(messagesAdapter != null)
            messagesAdapter.addToStart(msg, true);
    }

    @Override
    public void onErrorResponse(int errorCode, String errorMsg) {

    }


    public void onNewMessage(String dialogId, IMessage message, boolean mode) {
        if (!dialogsListAdapter.updateDialogWithMessage(dialogId, message)) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or reload all dialogs list
            Log.d("ConversationDialogId", dialogId);
            User user = (User) message.getUser();
            String name = user.getName();
            String photo = user.getAvatar();
            ArrayList<User> users = new ArrayList<>();
            users.add((User) message.getUser());
            Log.d("ChatTransDataEventImpl", dialogId);
            dialogsListAdapter.addItem(new Dialog(dialogId, name, photo, users, (Message) message, 1, mode));
        } else {
            for(Dialog dialog : dialogList){
                if(dialog.getId().equals(dialogId))
                    dialog.setUnreadCount(dialog.getUnreadCount() + 1);
            }
        }
    }
}
