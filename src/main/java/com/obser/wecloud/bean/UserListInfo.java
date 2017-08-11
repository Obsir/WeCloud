package com.obser.wecloud.bean;

import java.util.List;

/**
 * Created by ${LGX} on 2017/8/11.
 */
public class UserListInfo {


    /**
     * head : SUCCESS
     * body : {"OFFLINEUSER":[{"username":"changxing","userpwd":"123","userpower":1,"realname":"常星","ip":"0","port":"0","state":0,"picture":"1","team":1},{"username":"panteng","userpwd":"123","userpower":1,"realname":"潘腾","ip":"0","port":"0","state":0,"picture":"1","team":2},{"username":"yankaikai","userpwd":"123","userpower":1,"realname":"严凯凯","ip":"0","port":"0","state":0,"picture":"1","team":2},{"username":"songyufei","userpwd":"123","userpower":1,"realname":"宋雨霏","ip":"0","port":"0","state":0,"picture":"1","team":3},{"username":"makexin","userpwd":"123","userpower":1,"realname":"麻珂欣","ip":"0","port":"0","state":0,"picture":"1","team":3}],"GROUPS":[{"GROUPSINFO":{"groupsId":1,"groupsName":"test1","groupsUsername":"changxing","groupsTime":"2017-7-31","groupsPicture":"1"},"GROUPSUSER":[{"username":"changxing","userpwd":"123","userpower":1,"realname":"常星","ip":"0","port":"0","state":0,"picture":"1","team":1},{"username":"liguoxin","userpwd":"123","userpower":1,"realname":"李国鑫","state":1,"picture":"1","team":1},{"username":"yankaikai","userpwd":"123","userpower":1,"realname":"严凯凯","ip":"0","port":"0","state":0,"picture":"1","team":2},{"username":"makexin","userpwd":"123","userpower":1,"realname":"麻珂欣","ip":"0","port":"0","state":0,"picture":"1","team":3},{"username":"songyufei","userpwd":"123","userpower":1,"realname":"宋雨霏","ip":"0","port":"0","state":0,"picture":"1","team":3}]}],"ONLINEUSER":[{"username":"liguoxin","userpwd":"123","userpower":1,"realname":"李国鑫","state":1,"picture":"1","team":1}]}
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
        private List<GroupsBean> GROUPS;
        private List<User> ONLINEUSER;

        public List<User> getOFFLINEUSER() {
            return OFFLINEUSER;
        }

        public void setOFFLINEUSER(List<User> OFFLINEUSER) {
            this.OFFLINEUSER = OFFLINEUSER;
        }

        public List<GroupsBean> getGROUPS() {
            return GROUPS;
        }

        public void setGROUPS(List<GroupsBean> GROUPS) {
            this.GROUPS = GROUPS;
        }

        public List<User> getONLINEUSER() {
            return ONLINEUSER;
        }

        public void setONLINEUSER(List<User> ONLINEUSER) {
            this.ONLINEUSER = ONLINEUSER;
        }


        public static class GroupsBean {
            /**
             * GROUPSINFO : {"groupsId":1,"groupsName":"test1","groupsUsername":"changxing","groupsTime":"2017-7-31","groupsPicture":"1"}
             * GROUPSUSER : [{"username":"changxing","userpwd":"123","userpower":1,"realname":"常星","ip":"0","port":"0","state":0,"picture":"1","team":1},{"username":"liguoxin","userpwd":"123","userpower":1,"realname":"李国鑫","state":1,"picture":"1","team":1},{"username":"yankaikai","userpwd":"123","userpower":1,"realname":"严凯凯","ip":"0","port":"0","state":0,"picture":"1","team":2},{"username":"makexin","userpwd":"123","userpower":1,"realname":"麻珂欣","ip":"0","port":"0","state":0,"picture":"1","team":3},{"username":"songyufei","userpwd":"123","userpower":1,"realname":"宋雨霏","ip":"0","port":"0","state":0,"picture":"1","team":3}]
             */

            private GroupsInfoBean GROUPSINFO;
            private List<User> GROUPSUSER;

            public GroupsInfoBean getGROUPSINFO() {
                return GROUPSINFO;
            }

            public void setGROUPSINFO(GroupsInfoBean GROUPSINFO) {
                this.GROUPSINFO = GROUPSINFO;
            }

            public List<User> getGROUPSUSER() {
                return GROUPSUSER;
            }

            public void setGROUPSUSER(List<User> GROUPSUSER) {
                this.GROUPSUSER = GROUPSUSER;
            }

            public static class GroupsInfoBean {
                /**
                 * groupsId : 1
                 * groupsName : test1
                 * groupsUsername : changxing
                 * groupsTime : 2017-7-31
                 * groupsPicture : 1
                 */

                private int groupsId;
                private String groupsName;
                private String groupsUsername;
                private String groupsTime;
                private String groupsPicture;

                public int getGroupsId() {
                    return groupsId;
                }

                public void setGroupsId(int groupsId) {
                    this.groupsId = groupsId;
                }

                public String getGroupsName() {
                    return groupsName;
                }

                public void setGroupsName(String groupsName) {
                    this.groupsName = groupsName;
                }

                public String getGroupsUsername() {
                    return groupsUsername;
                }

                public void setGroupsUsername(String groupsUsername) {
                    this.groupsUsername = groupsUsername;
                }

                public String getGroupsTime() {
                    return groupsTime;
                }

                public void setGroupsTime(String groupsTime) {
                    this.groupsTime = groupsTime;
                }

                public String getGroupsPicture() {
                    return groupsPicture;
                }

                public void setGroupsPicture(String groupsPicture) {
                    this.groupsPicture = groupsPicture;
                }
            }


        }


    }
}
