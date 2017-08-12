package com.obser.wecloud.bean;

import java.util.List;

/**
 * Created by Obser on 2017/8/12.
 */

public class TaskInfo {

    private List<Task> task;
    private List<Task> groups;
    private List<Task> user;

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }

    public List<Task> getGroups() {
        return groups;
    }

    public void setGroups(List<Task> groups) {
        this.groups = groups;
    }

    public List<Task> getUser() {
        return user;
    }

    public void setUser(List<Task> user) {
        this.user = user;
    }

}
