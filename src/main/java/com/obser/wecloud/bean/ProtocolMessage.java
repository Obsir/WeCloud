package com.obser.wecloud.bean;

/**
 * Created by Obser on 2017/8/10.
 */

public class ProtocolMessage {
    private String fromUserName;
    private String fromUserIp;
    private String content;
    private String type;
    private String fromUserPicture;

    public String getFromUserPicture() {
        return fromUserPicture;
    }

    public void setFromUserPicture(String fromUserPicture) {
        this.fromUserPicture = fromUserPicture;
    }

    public ProtocolMessage(String fromUserName, String fromUserIp, String content, String type, String fromUserPicture) {
        this.fromUserName = fromUserName;
        this.fromUserIp = fromUserIp;
        this.content = content;
        this.type = type;
        this.fromUserPicture = fromUserPicture;

    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserIp() {
        return fromUserIp;
    }

    public void setFromUserIp(String fromUserIp) {
        this.fromUserIp = fromUserIp;
    }

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
