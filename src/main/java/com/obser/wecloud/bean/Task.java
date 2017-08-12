package com.obser.wecloud.bean;

import java.io.Serializable;

/**
 * Created by New World on 2017/7/30.
 */
public class Task implements Serializable{
    private Long taskId; //任务编号

    private String taskName; //任务名称

    private Long taskType; //任务类型 //1：信息采集  2：环境部署  3：现场勘探  4：风险评估  5：路线巡查  6：随机应变

    private int taskUrgentLevel; //任务紧急程度 //1：非常紧急  //2：比较紧急  //3：不紧急

    private String taskContent; //任务内容

    private String taskPosition; //任务执行地点

    private String taskAimUser; //任务接收人  //G or P or T开头，标识人或者群组或者小队，例如G,GID

    private String taskPubUser; //任务发布人

    private String taskPubTime; //任务发布时间

    private String taskCompleteTime; //任务完成时间

    private int taskState; //任务状态 //0：未发送成功   1：未完成状态  2：已完成  3：撤销

    private String taskCompleteInfo;  //任务完成情况

    public Task() {
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskType() {
        return taskType;
    }

    public void setTaskType(Long taskType) {
        this.taskType = taskType;
    }

    public int getTaskUrgentLevel() {
        return taskUrgentLevel;
    }

    public void setTaskUrgentLevel(int taskUrgentLevel) {
        this.taskUrgentLevel = taskUrgentLevel;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskAimUser() {
        return taskAimUser;
    }

    public void setTaskAimUser(String taskAimUser) {
        this.taskAimUser = taskAimUser;
    }

    public String getTaskPubUser() {
        return taskPubUser;
    }

    public void setTaskPubUser(String taskPubUser) {
        this.taskPubUser = taskPubUser;
    }

    public String getTaskPubTime() {
        return taskPubTime;
    }

    public void setTaskPubTime(String taskPubTime) {
        this.taskPubTime = taskPubTime;
    }

    public String getTaskCompleteTime() {
        return taskCompleteTime;
    }

    public void setTaskCompleteTime(String taskCompleteTime) {
        this.taskCompleteTime = taskCompleteTime;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public String getTaskCompleteInfo() {
        return taskCompleteInfo;
    }

    public void setTaskCompleteInfo(String taskCompleteInfo) {
        this.taskCompleteInfo = taskCompleteInfo;
    }

    public String getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(String taskPosition) {
        this.taskPosition = taskPosition;
    }
}
