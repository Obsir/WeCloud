/*
 * Copyright (C) 2017  即时通讯网(52im.net) & Jack Jiang.
 * The MobileIMSDK_X (MobileIMSDK v3.x) Project. 
 * All rights reserved.
 * 
 * > Github地址: https://github.com/JackJiang2011/MobileIMSDK
 * > 文档地址: http://www.52im.net/forum-89-1.html
 * > 即时通讯技术社区：http://www.52im.net/
 * > 即时通讯技术交流群：320837163 (http://www.52im.net/topic-qqgroup.html)
 *  
 * "即时通讯网(52im.net) - 即时通讯开发者社区!" 推荐开源工程。
 * 
 * LocalUDPDataSender.java at 2017-5-1 21:06:42, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.obser.wecloud.core;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.obser.wecloud.utils.UDPUtils;


import java.net.DatagramSocket;

public class LocalUDPDataSender
{
	private final static String TAG = LocalUDPDataSender.class.getSimpleName();
	private static LocalUDPDataSender instance = null;
	
	private Context context = null;

	/**
	 * 单例返回LocalUDPDataSender
	 * @param context
	 * @return
	 */
	public static LocalUDPDataSender getInstance(Context context)
	{
		if(instance == null)
			instance = new LocalUDPDataSender(context);
		return instance;
	}
	
	private LocalUDPDataSender(Context context)
	{
		this.context = context;
	}

	/**
	 * 暴露给外部调用的消息发送
	 * @param data
	 * @param to_user_ip
	 * @return
	 */
	public int sendCommonData(String data, String to_user_ip)
	{
		if(!TextUtils.isEmpty(data))
		{
			ConfigEntity.serverIP = to_user_ip;
			byte[] b = data.getBytes();
			Log.d(TAG, "正在发送");
			int code = send(b, b.length);
			return code;
		}
		else
			return -1;
	}

	/**
	 * 实际执行发送消息的方法
	 * @param fullBytes
	 * @param dataLen
	 * @return
	 */
	private int send(byte[] fullBytes, int dataLen)
	{
		
		DatagramSocket ds = LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
		if(ds != null) {
			Log.d(TAG, "正在发送");
			return UDPUtils.send(ds, fullBytes, dataLen) ? 0 : -1;
		} else{
			return -1;
		}
	}


	/**
	 * 异步发送消息的消息发送类
	 */
	public static abstract class SendCommonDataAsync extends AsyncTask<Object, Integer, Integer>
	{
		protected Context context = null;
		protected String data = null;
		protected String to_user_ip = null;
		
		public SendCommonDataAsync(Context context, String data, String to_user_ip)
		{
//			Log.d(TAG,"创建了SendCommonDataAsync");
			this.context = context;
			this.data = data;
			this.to_user_ip = to_user_ip;
		}


		@Override
		protected Integer doInBackground(Object... params)
		{
			if(data != null)
				return LocalUDPDataSender.getInstance(context).sendCommonData(data, to_user_ip);//dataContentWidthStr, to_user_id);
			return -1;
		}

		@Override
		protected abstract void onPostExecute(Integer code);
	}

}
