package com.obser.wecloud.bean;

import com.stfalcon.chatkit.commons.models.IUser;

/*
 * Created by troy379 on 04.04.17.
 */
public class ChatUser implements IUser {

    private String id;
    private String name;
    private String avatar;
    private boolean online;
    private String ip;
    private String account;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ChatUser(String id, String name, String avatar, String ip, String account, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
        this.ip = ip;
        this.account = account;

    }

    @Override
    public String toString() {
        return "{id:" + id + ", name:" + name + ", picture:" + avatar + ", ip:" + ip + "}";
    }

    public String getIp() {
        return ip;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
