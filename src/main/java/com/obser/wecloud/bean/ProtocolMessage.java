package com.obser.wecloud.bean;

/**
 * Created by Obser on 2017/8/10.
 */

public class ProtocolMessage {
//    private String fromUserName;
//    private String fromUserIp;
    private String content;
    private String type;
    private String dialogId;
    private boolean mode;

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }
    //    private String fromUserPicture;
//    private String toUserAccount;

//    public String getFromUserPicture() {
//        return fromUserPicture;
//    }
//
//    public void setFromUserPicture(String fromUserPicture) {
//        this.fromUserPicture = fromUserPicture;
//    }
//
//    public String getToUserAccount() {
//        return toUserAccount;
//    }
//
//    public void setToUserAccount(String toUserAccount) {
//        this.toUserAccount = toUserAccount;
//    }
    private User user;

    public User getChatUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public ProtocolMessage(User user, String dialogId, String content, String type, boolean mode) {
        this.user = user;
        this.content = content;
        this.type = type;
        this.dialogId = dialogId;
        this.mode = mode;

    }

//    public String getFromUserName() {
//        return fromUserName;
//    }
//
//    public void setFromUserName(String fromUserName) {
//        this.fromUserName = fromUserName;
//    }
//
//    public String getFromUserIp() {
//        return fromUserIp;
//    }
//
//    public void setFromUserIp(String fromUserIp) {
//        this.fromUserIp = fromUserIp;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
