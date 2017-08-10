package com.obser.wecloud.protocol;

import com.google.gson.Gson;
import com.obser.wecloud.bean.ProtocolMessage;

/**
 * Created by Obser on 2017/8/10.
 */

public class Protocol {
    private static Gson gson = new Gson();
    /**
     * 封装消息
     * @param message
     * @return
     */
    public static String packMessage(ProtocolMessage message){
        gson = new Gson();
        return gson.toJson(message);
    }
    /**
     * 解析消息
     * @param json
     * @return
     */
    public static ProtocolMessage unPackMessage(String json){
        ProtocolMessage message = gson.fromJson(json, ProtocolMessage.class);
        return message;
    }
}
