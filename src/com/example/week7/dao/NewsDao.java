package com.example.week7.dao;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.text.TextUtils;

import com.example.week7.constant.Config;
import com.example.week7.domain.News;
import com.example.week7.http.HttpUtils;

/**
 * 新闻业务层
 * 
 * @author Administrator
 * 
 */
public class NewsDao {

	/**
	 * 获取教务新闻列表
	 * 
	 * @param page请求页数
	 * @param type请求页面类型
	 * @return
	 */
	public ArrayList<News> getEduNews(int page, int type) {
		String address = Config.SERVER_URL + "?c=News&a=get_inform&page="
				+ page + "&type=" + type;
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			ArrayList<News> newsList = parseData(result);
			return newsList;
		}
		return null;
	}

	/**
	 * 获取就业信息新闻列表
	 * 
	 * @param page
	 * @param type
	 * @return
	 */
	public ArrayList<News> getJobNews(int page, int type) {
		String address = Config.SERVER_URL + "?c=News&a=get_news&page=" + page
				+ "&type=" + type;
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			ArrayList<News> newsList = parseData(result);
			return newsList;
		}
		return null;
	}

	private ArrayList<News> parseData(String result) {
		try {
			ArrayList<News> newsList = new ArrayList<News>();
			JSONObject jsonObject = new JSONObject(result);
			JSONArray dataJson = jsonObject.getJSONArray("data");
			for (int i = 0; i < dataJson.length(); i++) {
				JSONObject jsonObject2 = dataJson.getJSONObject(i);
				String title = jsonObject2.getString("title");
				String url = jsonObject2.getString("url");
				String number = jsonObject2.getString("number");
				String date = jsonObject2.getString("date");
				News news = new News(title, url, number, date);
				newsList.add(news);
			}
			return newsList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
