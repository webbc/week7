package com.example.week7.dao;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.bumptech.glide.load.model.UrlLoader;
import com.example.week7.constant.Config;
import com.example.week7.domain.Parttime;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * 兼职信息的业务层
 * 
 * @author Administrator
 * 
 */
public class ParttimeDao {

	/**
	 * 获取兼职信息列表
	 * 
	 * @param currentStartId开始查询的角标
	 * @param count总的条目数
	 * @param type请求类型
	 * @return
	 */
	public List<Parttime> getParttimeList(int currentStartId, int count,
			String type) {
		String address = null;
		if (type.equals("parttime")) {
			address = Config.SERVER_URL + "?c=Parttime&a=get_parttime&start="
					+ currentStartId + "&count=" + count;
		} else if (type.equals("jd")) {
			address = Config.SERVER_URL
					+ "?c=Parttime&a=get_jd_parttime&start=" + currentStartId
					+ "&count=" + count;
		}
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			List<Parttime> parttimeLists = parseData(result);
			return parttimeLists;
		}
		return null;
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 * @return
	 */
	private List<Parttime> parseData(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			if (respCode == 1) {
				List<Parttime> parttimeLists = new ArrayList<Parttime>();
				JSONArray dataJson = jsonObject.getJSONArray("data");
				for (int i = 0; i < dataJson.length(); i++) {
					JSONObject jsonObject2 = dataJson.getJSONObject(i);
					int id = Integer.parseInt(jsonObject2.getString("id"));
					String title = jsonObject2.getString("title");
					int column = Integer.parseInt(jsonObject2
							.getString("column"));
					String img = jsonObject2.getString("img");
					int istop = Integer
							.parseInt(jsonObject2.getString("istop"));
					String type = jsonObject2.getString("type");
					String content = jsonObject2.getString("content");
					int state = Integer
							.parseInt(jsonObject2.getString("state"));
					String area = jsonObject2.getString("area");
					String company = jsonObject2.getString("company");
					String gender = jsonObject2.getString("gender");
					String treatment = jsonObject2.getString("treatment");
					int person_count = Integer.parseInt(jsonObject2
							.getString("person_count"));
					String stime = jsonObject2.getString("stime");
					String etime = jsonObject2.getString("etime");
					String worktime = jsonObject2.getString("worktime");
					String addtime = jsonObject2.getString("addtime");
					String phone = jsonObject2.getString("phone");
					String qq = jsonObject2.getString("qq");
					String remarks = jsonObject2.getString("remarks");
					String comment_count = jsonObject2
							.getString("comment_count");
					int apply_count = Integer.parseInt(jsonObject2
							.getString("apply_count"));
					Parttime parttime = new Parttime(id, title, column, img,
							istop, type, content, state, area, company, gender,
							treatment, person_count, stime, etime, worktime,
							addtime, phone, qq, remarks, comment_count,
							apply_count);
					parttimeLists.add(parttime);
				}
				return parttimeLists;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取报名信息
	 * 
	 * @param id数据库id
	 * @param phone电话
	 */
	public Map<String, Integer> getApplyInfo(int id, String phone) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String address = Config.SERVER_URL
					+ "?c=Parttime&a=get_status_by_id&pid=" + id + "&phone="
					+ URLEncoder.encode(crypt.encrypt(phone));
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				Map<String, Integer> returnMap = parseParttimeApplyData(result);
				return returnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析从兼职申请信息接口获取的数据
	 * 
	 * @param result
	 * @return
	 */
	private Map<String, Integer> parseParttimeApplyData(String result) {
		try {
			Map<String, Integer> returnMap = new HashMap<String, Integer>();
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			if (respCode == 1) {
				JSONObject dataJson = jsonObject.getJSONObject("data");
				int ispast = dataJson.getInt("ispast");
				int isfull = dataJson.getInt("isfull");
				int isapply = dataJson.getInt("isapply");
				int remainperson = dataJson.getInt("remainperson");
				returnMap.put("ispast", ispast);
				returnMap.put("isfull", isfull);
				returnMap.put("isapply", isapply);
				returnMap.put("remainperson", remainperson);
			}
			returnMap.put("respCode", respCode);
			return returnMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 写入报名信息
	 * 
	 * @param id兼职id
	 * @param phone用户电话
	 * @param tel报名人电话
	 * @param sid学号
	 * @param roomid寝室号
	 * @param name真实姓名
	 * @return
	 */
	public Map<String, Object> writeParttimeJob(int id, String phone,
			String tel, String sid, String roomid, String name) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String address = Config.SERVER_URL
					+ "?c=Parttime&a=write_ptjob&pid=" + id + "&sid="
					+ URLEncoder.encode(sid) + "&roomid="
					+ URLEncoder.encode(roomid) + "&realname="
					+ URLEncoder.encode(name) + "&realphone="
					+ URLEncoder.encode(crypt.encrypt(phone)) + "&applyphone="
					+ URLEncoder.encode(crypt.encrypt(tel));
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				Map<String, Object> returnMap = parseWriteParttimeJobData(result);
				return returnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析报名接口数据
	 * 
	 * @param result
	 * @return
	 */
	private Map<String, Object> parseWriteParttimeJobData(String result) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			if (respCode == 1) {
				JSONObject dataJson = jsonObject.getJSONObject("data");
				int isapply = dataJson.getInt("isapply");
				returnMap.put("isapply", isapply);
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
