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
 * UDPUtils.java at 2017-5-1 21:06:42, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.obser.wecloud.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.obser.wecloud.core.ConfigEntity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class UDPUtils
{
	private final static String TAG = UDPUtils.class.getSimpleName();
	
	public static boolean send(DatagramSocket skt, byte[] d, int dataLen)
	{
		if(skt != null && d != null)
		{
			try
			{
				Log.d(TAG, "正在发送");
				return send(skt, new DatagramPacket(d, dataLen, InetAddress.getByName(ConfigEntity.serverIP), ConfigEntity.serverUDPPort));
			}
			catch (Exception e)
			{
				Log.e(TAG, "【IMCORE】send方法中》》发送UDP数据报文时出错了：remoteIp="+skt.getInetAddress()
						+", remotePort="+skt.getPort()+".原因是："+e.getMessage(), e);
				return false;
			}
		}
		else
		{
			Log.e(TAG, "【IMCORE】send方法中》》无效的参数：skt="+skt);//
			return false;
		}
	}
	
	public synchronized static boolean send(DatagramSocket skt, DatagramPacket p)
	{
		boolean sendSucess = true;
		if(skt != null && p != null)
		{
			try
			{
				skt.send(p);
				Log.d(TAG, "已经成功发送");
			}
			catch (Exception e)
			{
				sendSucess = false;
				Log.e(TAG, "【IMCORE】send方法中》》发送UDP数据报文时出错了，原因是："+e.getMessage(), e);
			}
		}
		else
		{
			Log.w(TAG, "【IMCORE】在send()UDP数据报时没有成功执行，原因是：skt==null || p == null!");
		}
			
		return sendSucess;
	}
	public static String getIPAddress(Context context) {
		NetworkInfo info = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
				try {
					//Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
					for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
						NetworkInterface intf = en.nextElement();
						for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
							InetAddress inetAddress = enumIpAddr.nextElement();
							if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress();
							}
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}

			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
				return ipAddress;
			}
		} else {
			//当前无网络连接,请在设置中打开网络
		}
		return null;
	}

	/**
	 * 将得到的int类型的IP转换为String类型
	 *
	 * @param ip
	 * @return
	 */
	public static String intIP2StringIP(int ip) {
		return (ip & 0xFF) + "." +
				((ip >> 8) & 0xFF) + "." +
				((ip >> 16) & 0xFF) + "." +
				(ip >> 24 & 0xFF);
	}
}
