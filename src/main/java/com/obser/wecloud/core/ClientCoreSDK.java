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
 * ClientCoreSDK.java at 2017-5-1 21:06:41, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.obser.wecloud.core;

import android.app.Application;
import android.content.Context;


public class ClientCoreSDK
{
	private final static String TAG = ClientCoreSDK.class.getSimpleName();
	

	private static ClientCoreSDK instance = null;
	
	private boolean _init = false;
	private boolean localDeviceNetworkOk = true;

	private ChatTransDataEvent chatTransDataEvent = null;

	private Context context = null;
	
	public static ClientCoreSDK getInstance()
	{
		if(instance == null)
			instance = new ClientCoreSDK();
		return instance;
	}
	
	private ClientCoreSDK()
	{
	}
	
	public void init(Context _context)
	{
		if(!_init)
		{
			if(_context == null)
				throw new IllegalArgumentException("context can't be null!");
			
			if(_context instanceof Application)
				this.context = _context;
			else
			{
				this.context = _context.getApplicationContext();
			}
			LocalUDPDataReciever.getInstance(context).startup();
		
//			IntentFilter intentFilter = new IntentFilter();
//			intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//			this.context.registerReceiver(networkConnectionStatusBroadcastReceiver, intentFilter);
		
			_init = true;
		}
	}
	
	public void release()
	{
		LocalUDPSocketProvider.getInstance().closeLocalUDPSocket();
		LocalUDPDataReciever.getInstance(context).stop();

//		try
//		{
//			context.unregisterReceiver(networkConnectionStatusBroadcastReceiver);
//		}
//		catch (Exception e)
//		{
//			Log.w(TAG, e.getMessage(), e);
//		}
		
		_init = false;
		
	}
	

	




	public boolean isInitialed()
	{
		return this._init;
	}
	
	public boolean isLocalDeviceNetworkOk()
	{
		return localDeviceNetworkOk;
	}


	public void setChatTransDataEvent(ChatTransDataEvent chatTransDataEvent)
	{
		this.chatTransDataEvent = chatTransDataEvent;
	}
	public ChatTransDataEvent getChatTransDataEvent()
	{
		return chatTransDataEvent;
	}
	

//	private final BroadcastReceiver networkConnectionStatusBroadcastReceiver = new BroadcastReceiver()
//	{
//		@Override
//		public void onReceive(Context context, Intent intent)
//		{
//			ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
//			NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//			NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			NetworkInfo ethernetInfo = connectMgr.getNetworkInfo(9);
//			if (!(mobNetInfo != null && mobNetInfo.isConnected())
//					&& !(wifiNetInfo != null && wifiNetInfo.isConnected())
//					&& !(ethernetInfo != null && ethernetInfo.isConnected()))
//			{
//				Log.e(TAG, "【IMCORE】【本地网络通知】检测本地网络连接断开了!");
//				localDeviceNetworkOk = false;
//				LocalUDPSocketProvider.getInstance().closeLocalUDPSocket();
//			}
//			else
//			{
//				if(net.openmob.mobileimsdk.android.ClientCoreSDK.DEBUG)
//					Log.e(TAG, "【IMCORE】【本地网络通知】检测本地网络已连接上了!");
//
//				localDeviceNetworkOk = true;
//				LocalUDPSocketProvider.getInstance().closeLocalUDPSocket();
//			}
//		}
//	};
}
