package com.example.week7.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络请求帮助类
 * 
 * @author Administrator
 * 
 */
public class HttpUtils {

	/**
	 * 发送请求
	 * 
	 * @param address地址
	 * @param listener回调接口
	 */
	public static String sendHttpRequest(String address) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(address);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(8000);
			conn.setConnectTimeout(8000);
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream));
				String line = "";
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return null;
	}
}
