package com.obser.wecloud.core;

import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.bean.Message;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.fixtures.Avatars;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;

/**
 * Created by Obser on 2017/8/7.
 */

public class ChatTransDataEventImpl implements ChatTransDataEvent {

    private  DialogsListAdapter<Dialog> dialogsListAdapter;
    private MessagesListAdapter<Message> messagesAdapter;
    private ArrayList<Message> messages = new ArrayList<>();


    public void setMessagesAdapter(MessagesListAdapter<Message> messagesAdapter){
        this.messagesAdapter = messagesAdapter;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override

    public void setDialogsListAdapter(DialogsListAdapter<Dialog> dialogsListAdapter) {
        this.dialogsListAdapter = dialogsListAdapter;
    }

    @Override
    public void onMessageReceive(String message) {
        String[] split = message.split(":");
        String avatarUrl = Avatars.getAvatar();
        Message msg = new Message(split[0], new User("1", split[0], avatarUrl, true), split[1]);
//        msg.setImage(new Message.Image(avatarUrl));
        messages.add(msg);
        onNewMessage(split[0], msg);
        if(messagesAdapter != null)
            messagesAdapter.addToStart(msg, true);
    }

    @Override
    public void onErrorResponse(int errorCode, String errorMsg) {

    }


    private void onNewMessage(String dialogId, IMessage message) {
        if (!dialogsListAdapter.updateDialogWithMessage(dialogId, message)) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or reload all dialogs list
            String name = message.getUser().getName();
            String photo = message.getUser().getAvatar();
            ArrayList<User> users = new ArrayList<>();
            users.add((User) message.getUser());
            dialogsListAdapter.addItem(new Dialog(dialogId, name, photo, users, (Message) message, 1));
        }
    }
}
