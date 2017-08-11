package com.obser.wecloud.bean;

/**
 * Created by ${LGX} on 2017/8/11.
 */
public class LoginInfo {

    /**
     * head : SUCCESS
     * body : {"USER":{"username":"liguoxin","userpwd":"123","userpower":1,"realname":"李国鑫","ip":"0","port":"0","state":0,"picture":"1","team":1}}
     */

    private String head;
    private BodyBean body;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * USER : {"username":"liguoxin","userpwd":"123","userpower":1,"realname":"李国鑫","ip":"0","port":"0","state":0,"picture":"1","team":1}
         */

        private User USER;

        public User getUSER() {
            return USER;
        }

        public void setUSER(User USER) {
            this.USER = this.USER;
        }

    }
}
