package com.obser.wecloud.bean;

import java.io.Serializable;
import java.util.List;
import com.obser.wecloud.User;
/**
 * Created by Administrator on 2017/8/4.
 */

public class MessageForUser implements Serializable {
    /**
     * head : SUCCESS
     * body : {"OFFLINEUSER":[{"username":"changxing","userpwd":"123","userpower":1,"realname":"常星","ip":"0","port":"0","state":0,"picture":"1","team":1},{"username":"panteng","userpwd":"123","userpower":1,"realname":"潘腾","ip":"0","port":"0","state":0,"picture":"1","team":2},{"username":"yankaikai","userpwd":"123","userpower":1,"realname":"严凯凯","ip":"0","port":"0","state":0,"picture":"1","team":2},{"username":"songyufei","userpwd":"123","userpower":1,"realname":"宋雨霏","ip":"0","port":"0","state":0,"picture":"1","team":3},{"username":"makexin","userpwd":"123","userpower":1,"realname":"麻珂欣","ip":"0","port":"0","state":0,"picture":"1","team":3}],"ONLINEUSER":[{"username":"liguoxin","userpwd":"123","userpower":1,"realname":"李国鑫","state":1,"picture":"1","team":1}]}
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
        private List<User> OFFLINEUSER;
        private List<User> ONLINEUSER;

        public List<User> getOFFLINEUSER() {
            return OFFLINEUSER;
        }

        public void setOFFLINEUSER(List<User> OFFLINEUSER) {
            this.OFFLINEUSER = OFFLINEUSER;
        }

        public List<User> getONLINEUSER() {
            return ONLINEUSER;
        }

        public void setONLINEUSER(List<User> ONLINEUSER) {
            this.ONLINEUSER = ONLINEUSER;
        }
    }
}
