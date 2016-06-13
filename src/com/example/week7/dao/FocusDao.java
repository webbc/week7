package com.example.week7.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.week7.constant.Config;
import com.example.week7.domain.Focus;
import com.example.week7.http.HttpUtils;

/**
 * 焦点图的业务层
 * 
 * @author Administrator
 * 
 */
public class FocusDao {

	private Map<String, Object> returnMap;// 解析节点图接口返回的数据
	private String tag = "TAG";

	/**
	 * 从服务器请求焦点图数据
	 * 
	 * @param start开始处
	 * @param count图片的个数
	 */
	public Map<String, Object> getFocus(int start, int count) {
		// http://127.0.0.1/Week7/and10381.php?c=Focus&a=get_focus&start=1&count=10
		String address = Config.SERVER_URL + "?c=Focus&a=get_focus&start="
				+ start + "&count=" + count;
		String result = HttpUtils.sendHttpRequest(address);
		if (result == null) {
			return null;
		}
		parseData(result);
		return returnMap;
	}

	/**
	 * 解析Json字符串
	 * 
	 * @param result
	 */
	private void parseData(String result) {
		returnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			JSONArray dataJson = jsonObject.getJSONArray("data");
			List<Focus> focusLists = new ArrayList<Focus>();
			for (int i = 0; i < dataJson.length(); i++) {
				JSONObject jsonObject2 = dataJson.getJSONObject(i);
				int id = Integer.parseInt(jsonObject2.getString("id"));
				String title = jsonObject2.getString("title");
				String img = jsonObject2.getString("img");
				String addtime = jsonObject2.getString("addtime");
				Focus focus = new Focus(id, title, img, addtime);
				focusLists.add(focus);
			}
			returnMap.put("respCode", respCode);
			returnMap.put("respMsg", respMsg);
			returnMap.put("focusLists", focusLists);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
