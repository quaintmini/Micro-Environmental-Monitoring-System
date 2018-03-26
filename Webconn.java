package com.zhangtory.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
//import org.apache.http.legacy;

import android.util.Log;

public class Webconn {

	/**
	 * 锟斤拷取锟斤拷锟斤拷锟借备位锟斤拷锟斤拷息
	 * 
	 * @return
	 */
	public static String getDev() {
		String url = "http://pj.zhangtory.com/pj/getdev.php";
		try {
			HttpPost httppost = new HttpPost(url);
			try {
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httppost);
				Log.i("-----", ""+httpResponse);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					return strResult;
				}
			} catch (Exception e) {
				Log.e("log_tag", "Error parsing classname data" + e.toString());
			}
		} catch (Exception e) {
			Log.e("log_tag", "Request failed" + e.toString());
		}
		return "null0";
	}
	/**
	 * 锟斤拷取锟借备锟斤拷前锟斤拷锟斤拷锟�
	 * 
	 * 
	 * @param equipId
	 * @return
	 */
	public static String getNow(int equipId) {
		String url = "http://pj.zhangtory.com/pj/getnow.php";
		try {
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", String
					.valueOf(equipId)));
			try {
				// 锟斤拷锟斤拷HTTP request
				httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				// 取锟斤拷HTTP response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httppost);
				// 锟斤拷状态锟斤拷为200 ok
				// Log.i("getreturn3", "3");
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 取锟斤拷锟斤拷应锟街达拷
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					return strResult;
				}
			} catch (Exception e) {
				System.out.println("e1");
				Log.e("log_tag", "Error parsing classname data" + e.toString());
			}
		} catch (Exception e) {
			System.out.println("e2");
			Log.e("log_tag", "Request failed" + e.toString());
		}
		return "null0";
	}
	/**
	 * 锟斤拷取锟借备5小时锟斤拷锟�
	 * 
	 * 
	 * @param equipId
	 * @return
	 */
	public static String getRecent(int equipId) {
		String url = "http://pj.zhangtory.com/pj/getrecent.php";
		try {
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", String
					.valueOf(equipId)));
			try {
				// 锟斤拷锟斤拷HTTP request
				httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				// 取锟斤拷HTTP response
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httppost);
				// 锟斤拷状态锟斤拷为200 ok
				// Log.i("getreturn3", "3");
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 取锟斤拷锟斤拷应锟街达拷
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					return strResult;
				}
			} catch (Exception e) {
				Log.e("log_tag", "Error parsing classname data" + e.toString());
			}
		} catch (Exception e) {
			Log.e("log_tag", "Request failed" + e.toString());
		}
		return "null0";
	}
	
}
