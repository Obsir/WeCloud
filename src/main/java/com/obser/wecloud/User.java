package com.obser.wecloud;
import java.io.Serializable;

/**
 * @class User
 * @brief 系统实体类-用户类
 *
 * 包含用户的各种参数。
 * @author 常星
 * @date 2017年7月21日
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1947226543936059439L;


    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String username; //用户名
    private String userpwd; //用户密码
    private int userpower; //用户真实姓名
    private String realname; //用户真实姓名
    private String ip; //用户当前ip
    private String port; //用户当前接收信息端口
    private int state; //用户当前上线状态
    private String picture; //用户头像照片
    private int team;

    public User() {
    }

    public User(String username, String picture) {
        this.username = username;
        this.picture = picture;
    }

    public User(String username, String userpwd, int userpower, String realname, String ip, String port, int state, String picture) {
        this.username = username;
        this.userpwd = userpwd;
        this.userpower = userpower;
        this.realname = realname;
        this.ip = ip;
        this.port = port;
        this.state = state;
        this.picture = picture;
    }

    /**
     * @fn 用于填充用户对象数据或者获取用户对象数据
     * @brief 用户访问箭扣
     * @author 常星
     * @date 2017年7月21日
     * */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getUserpower() {
        return userpower;
    }

    public void setUserpower(int userpower) {
        this.userpower = userpower;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
