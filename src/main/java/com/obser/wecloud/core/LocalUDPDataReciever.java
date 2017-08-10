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
 * LocalUDPDataReciever.java at 2017-5-1 21:06:42, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.obser.wecloud.core;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class LocalUDPDataReciever
{
	private final static String TAG = LocalUDPDataReciever.class.getSimpleName();



	
	private static LocalUDPDataReciever instance = null;
	private static MessageHandler messageHandler = null;
	
	private Thread thread = null;
	private Context context = null;
	
	public static LocalUDPDataReciever getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new LocalUDPDataReciever(context);
			messageHandler = new MessageHandler(context);
		}
		return instance;
	}


	
	private LocalUDPDataReciever(Context context)
	{
		this.context = context;
	}
	
	public void stop()
	{
		if(thread != null)
		{
			thread.interrupt();
			thread = null;
		}
	}
	
	public void startup()
	{
		stop();
		
		try
		{
			thread = new Thread(new Runnable()
			{
				public void run()
				{
					try
					{
						Log.d(TAG, "开始监听");
						p2pListeningImpl();
					}
					catch (Exception eee)
					{
						Log.w(TAG, "【IMCORE】本地UDP监听停止了(socket被关闭了?),"+eee.getMessage(), eee);
					}
				}
			});
			thread.start();
		}
		catch (Exception e)
		{
			Log.w(TAG, "【IMCORE】本地UDPSocket监听开启时发生异常,"+e.getMessage(), e);
		}
	}

	private void p2pListeningImpl() throws Exception
	{
		while (true)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			DatagramSocket localUDPSocket = LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
			if (localUDPSocket != null && !localUDPSocket.isClosed())
			{
				localUDPSocket.receive(packet);
				Message m = Message.obtain();
				m.obj = packet;
				messageHandler.sendMessage(m);
			}
		}
	}
	
	private static class MessageHandler extends Handler
	{
		private Context context = null;
		
		public MessageHandler(Context context)
		{
			this.context = context;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			DatagramPacket packet = (DatagramPacket)msg.obj;
			if(packet == null)
				return;
			
			try
			{
				ClientCoreSDK.getInstance().getChatTransDataEvent().onMessageReceive(new String(packet.getData(), 0, packet.getLength()));
			}
			catch (Exception e)
			{
				Log.w(TAG, "【IMCORE】处理消息的过程中发生了错误.", e);
			}
		}

	}
}
