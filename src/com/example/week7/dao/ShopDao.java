package com.example.week7.dao;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.example.week7.constant.Config;
import com.example.week7.domain.Shop;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * 校园小铺的业务层
 * 
 * @author Administrator
 * 
 */
public class ShopDao {

	/**
	 * 根据指定位置来获取校园小铺的数据
	 * 
	 * @param phone
	 * 
	 * @param start
	 * @param count
	 * @return
	 */
	public ArrayList<Shop> getShop(String phone, int start, int count) {
		DesTools desTools = new DesTools(Config.DES_KEY);
		String address;
		try {
			address = Config.SERVER_URL
					+ "?c=Goods&a=get_goods_by_limit&start=" + start
					+ "&count=" + count + "&phone=" + desTools.encrypt(phone);
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				ArrayList<Shop> shopList = parseData(result);
				return shopList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析从获取商品接口获取的数据
	 * 
	 * @param result
	 * @return
	 */
	private ArrayList<Shop> parseData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			if (respCode == 1) {
				ArrayList<Shop> shopList = new ArrayList<Shop>();
				JSONArray dataJson = jsonObject.getJSONArray("data");
				for (int i = 0; i < dataJson.length(); i++) {
					JSONObject jsonObject2 = dataJson.getJSONObject(i);
					String id = jsonObject2.getString("id");
					String logo = jsonObject2.getString("logo");
					String desc = jsonObject2.getString("desc");
					String price = jsonObject2.getString("price");
					String hot = jsonObject2.getString("hot");
					String name = jsonObject2.getString("name");
					String tel = jsonObject2.getString("tel");
					Shop shop = new Shop(id, logo, desc, price, hot, name, tel);
					shopList.add(shop);
				}
				return shopList;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
