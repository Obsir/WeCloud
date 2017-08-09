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
 * LocalUDPSocketProvider.java at 2017-5-1 21:06:42, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.obser.wecloud.core;

import android.util.Log;

import java.net.DatagramSocket;

public class LocalUDPSocketProvider
{
	private final static String TAG = LocalUDPSocketProvider.class.getSimpleName();
	
	private static LocalUDPSocketProvider instance = null;
	
	private DatagramSocket localUDPSocket = null;
	
	public static LocalUDPSocketProvider getInstance()
	{
		if(instance == null)
			instance = new LocalUDPSocketProvider();
		return instance;
	}
	
	private LocalUDPSocketProvider()
	{
		//
	}

	public DatagramSocket resetLocalUDPSocket()
	{
		try
		{
			closeLocalUDPSocket();

			localUDPSocket = (ConfigEntity.serverUDPPort == 0?
					new DatagramSocket():new DatagramSocket(ConfigEntity.serverUDPPort));//_Utils.LOCAL_UDP_SEND$LISTENING_PORT);
			return localUDPSocket;
		}
		catch (Exception e)
		{
			Log.w(TAG, "【IMCORE】localUDPSocket创建时出错，原因是："+e.getMessage(), e);
			closeLocalUDPSocket();
			return null;
		}
	}
	
	private boolean isLocalUDPSocketReady()
	{
		return localUDPSocket != null && !localUDPSocket.isClosed();
	}
	
	public DatagramSocket getLocalUDPSocket()
	{
		if(isLocalUDPSocketReady())
		{
			Log.d(TAG, "【IMCORE】isLocalUDPSocketReady()==true，直接返回本地socket引用哦。");
			return localUDPSocket;
		}
		else
		{
			Log.d(TAG, "【IMCORE】isLocalUDPSocketReady()==false，需要先resetLocalUDPSocket()...");
			return resetLocalUDPSocket();
		}
	}

	public void closeLocalUDPSocket()
	{
		try
		{
			if(localUDPSocket != null)
			{
				localUDPSocket.close();
				localUDPSocket = null;
			}
			else
			{
				Log.d(TAG, "【IMCORE】Socket处于未初化状态（可能是您还未登陆），无需关闭。");
			}	
		}
		catch (Exception e)
		{
			Log.w(TAG, "【IMCORE】lcloseLocalUDPSocket时出错，原因是："+e.getMessage(), e);
		}
	}
}
