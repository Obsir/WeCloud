package com.obser.wecloud.bean;

import com.stfalcon.chatkit.commons.models.IUser;

/*
 * Created by troy379 on 04.04.17.
 */
public class User implements IUser {

    private String id;
    private String name;
    private String avatar;
    private boolean online;
    private String ip;



    public User(String id, String name, String avatar, String ip, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
        this.ip = ip;

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
