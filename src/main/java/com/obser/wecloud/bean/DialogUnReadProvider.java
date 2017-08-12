package com.obser.wecloud.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Obser on 2017/8/12.
 */

public class DialogUnReadProvider {
    private DialogUnReadProvider(){

    }

    private static Map<String, Integer> map;

    public static void setUnReadByList(List<Dialog> dialogList){
        if(map == null)
            map = new HashMap<>();
        for(Dialog dialog : dialogList){
            map.put(dialog.getId(), dialog.getUnreadCount());
        }
    }

    public static int getUnReadById(String id){
        if(map == null || map.isEmpty())
            return 0;
        else
            return map.get(id) == null ? 0 : map.get(id);
    }
}
