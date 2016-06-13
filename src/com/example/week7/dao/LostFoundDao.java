package com.example.week7.dao;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.example.week7.constant.Config;
import com.example.week7.domain.Lostfound;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * 失物招领业务层
 * 
 * @author Administrator
 * 
 */
public class LostFoundDao {

	/**
	 * 根据条件获取失物招领数据
	 * 
	 * @param start开始处
	 * @param count总数
	 * @return
	 */
	public ArrayList<Lostfound> getLostfound(int start, int count) {
		String address = Config.SERVER_URL
				+ "?c=Lostfound&a=get_lostfound_by_limit&start=" + start
				+ "&count=" + count;
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			ArrayList<Lostfound> lostfoundList = parseData(result);
			return lostfoundList;
		}
		return null;
	}

	/**
	 * 解析从失物招领接口获取的数据
	 * 
	 * @param result
	 * @return
	 */
	private ArrayList<Lostfound> parseData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			if (respCode == 1) {
				ArrayList<Lostfound> lostfoundList = new ArrayList<Lostfound>();
				JSONArray dataJson = jsonObject.getJSONArray("data");
				for (int i = 0; i < dataJson.length(); i++) {
					JSONObject jsonObject2 = dataJson.getJSONObject(i);
					String id = jsonObject2.getString("id");
					String uid = jsonObject2.getString("uid");
					String content = jsonObject2.getString("content");
					String time = jsonObject2.getString("time");
					String img = jsonObject2.getString("img");
					String tel = jsonObject2.getString("tel");
					String name = jsonObject2.getString("name");
					String sid = jsonObject2.getString("sid");
					String photo = jsonObject2.getString("photo");
					String phone = jsonObject2.getString("phone");
					String nickname = jsonObject2.getString("nickname");
					Lostfound lostfound = new Lostfound(id, uid, content, time,
							img, tel, name, sid, photo, phone, nickname);
					lostfoundList.add(lostfound);
				}
				return lostfoundList;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 写入失物招领信息
	 * 
	 * @param name
	 * @param tel
	 * @param string
	 * @param desc
	 * @param phone
	 * @return
	 */
	public Map<String, Object> write(String name, String tel, String img,
			String desc, String phone) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String address = Config.SERVER_URL
					+ "?c=Lostfound&a=write_lostfound&name="
					+ URLEncoder.encode(name) + "&tel="
					+ URLEncoder.encode(tel) + "&img=" + URLEncoder.encode(img)
					+ "&content=" + URLEncoder.encode(desc) + "&phone="
					+ crypt.encrypt(URLEncoder.encode(phone));
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				Map<String, Object> returnMap = parseWriteData(result);
				return returnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析向服务器写失物招领信息接口返回的数据
	 * 
	 * @param result
	 * @return
	 */
	private Map<String, Object> parseWriteData(String result) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			if (respCode == 1) {
				JSONObject dataJson = respJson.getJSONObject("data");
				String id = dataJson.getString("id");
				String uid = dataJson.getString("uid");
				String content = dataJson.getString("content");
				String time = dataJson.getString("time");
				String img = dataJson.getString("img");
				String tel = dataJson.getString("tel");
				String name = dataJson.getString("name");
				String sid = dataJson.getString("sid");
				String photo = dataJson.getString("photo");
				String phone = dataJson.getString("phone");
				String nickname = dataJson.getString("nickname");
				Lostfound lostfound = new Lostfound(id, uid, content, time,
						img, tel, name, sid, photo, phone, nickname);
				returnMap.put("data", lostfound);
			}
			returnMap.put("respCode", respCode);
			returnMap.put("respMsg", respMsg);
			return returnMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
