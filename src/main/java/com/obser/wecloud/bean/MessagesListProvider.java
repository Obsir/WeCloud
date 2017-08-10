package com.obser.wecloud.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Obser on 2017/8/10.
 */

public class MessagesListProvider {
    private MessagesListProvider(){

    }

    private static Map<String, List<Message>> map;

    public static List<Message> getMessagesById(String id){
        if(map == null)
            map = new HashMap<>();
        List<Message> list = map.get(id);
        if(list == null){
            list = new ArrayList<>();
            map.put(id, list);
        }
        Collections.sort(list);
        return list;
    }

}
