package com.example.week7.dao;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.example.week7.constant.Config;
import com.example.week7.domain.Deliveryman;
import com.example.week7.domain.Express;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * 快递业务层
 * 
 * @author Administrator
 * 
 */
public class ExpressDao {

	/**
	 * 拿快递
	 * 
	 * @param mRemark
	 * @param mRoomId
	 * 
	 * @param mPhone用户名
	 * @param mName学生姓名
	 * @param mTel学生电话
	 * @param mExpressNo快递取货号
	 * @param mArriveTime到件时间
	 * @param mCompany快递公司
	 * @param mAid区域id
	 * @param mPayStyle支付方式
	 * @param mLarge快件大小
	 * @param mMoney金额
	 */
	public Map<String, Object> getExpress(String mPhone, String mName,
			String mTel, String mExpressNo, String mArriveTime,
			String mCompany, int mAid, int mPayStyle, int mLarge, int mMoney,
			String mRemark, String mRoomId) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			mPhone = URLEncoder.encode(crypt.encrypt(mPhone));
			mName = URLEncoder.encode(mName);
			mTel = URLEncoder.encode(mTel);
			mExpressNo = URLEncoder.encode(mExpressNo);
			mArriveTime = URLEncoder.encode(mArriveTime);
			mCompany = URLEncoder.encode(mCompany);
			mRemark = URLEncoder.encode(mRemark);
			mRoomId = URLEncoder.encode(mRoomId);
			String address = Config.SERVER_URL
					+ "?c=Express&a=get_express_02&phone=" + mPhone + "&name="
					+ mName + "&tel=" + mTel + "&getnum=" + mExpressNo
					+ "&arrivetime=" + mArriveTime + "&company=" + mCompany
					+ "&aid=" + mAid + "&paystyle=" + mPayStyle + "&large="
					+ mLarge + "&money=" + mMoney + "&desc=" + mRemark
					+ "&address=" + mRoomId;
			String result = HttpUtils.sendHttpRequest(address);
			if (TextUtils.isEmpty(result)) {
				return null;
			}
			Map<String, Object> reurnMap = parseTakeExpressData(result);
			return reurnMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析拿快递下单返回的数据
	 * 
	 * @param result
	 */
	private Map<String, Object> parseTakeExpressData(String result) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = Integer.parseInt(respJson.getString("respCode"));
			String respMsg = respJson.getString("respMsg");
			JSONObject dataJson = jsonObject.getJSONObject("data");
			int uid = Integer.parseInt(dataJson.getString("uid"));
			int sid = Integer.parseInt(dataJson.getString("sid"));
			String name = dataJson.getString("name");
			String phone = dataJson.getString("phone");
			String address = dataJson.getString("address");
			String order_code = dataJson.getString("order_code");
			String stime = dataJson.getString("stime");
			String getnum = dataJson.getString("getnum");
			String arrivetime = dataJson.getString("arrivetime");
			String company = dataJson.getString("company");
			int aid = Integer.parseInt(dataJson.getString("aid"));
			int paystyle = Integer.parseInt(dataJson.getString("paystyle"));
			int large = Integer.parseInt(dataJson.getString("large"));
			double money = Double.parseDouble(dataJson.getString("money"));
			String desc = dataJson.getString("desc");
			Express express = new Express();
			express.setUid(uid);
			express.setSid(sid);
			express.setName(name);
			express.setPhone(phone);
			express.setAddress(address);
			express.setOrder_code(order_code);
			express.setStime(stime);
			express.setGetnum(getnum);
			express.setArrivetime(arrivetime);
			express.setCompany(company);
			express.setAid(aid);
			express.setPaystyle(paystyle);
			express.setLarge(large);
			express.setMoney(money);
			express.setDesc(desc);
			returnMap.put("respCode", respCode);
			returnMap.put("respMsg", respMsg);
			returnMap.put("data", express);
			return returnMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有拿快递的订单信息
	 * 
	 * @param phone
	 * @param position
	 * @return
	 */
	public Map<String, Object> getAllOrder(String phone, int state) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL
					+ "?c=Express&a=get_order&phone=" + phone + "&state="
					+ state;
			String result = HttpUtils.sendHttpRequest(address);
			if (TextUtils.isEmpty(result)) {
				return null;
			}
			Map<String, Object> returnMap = parseOrderList(result);
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析所有订单接口返回的信息
	 * 
	 * @param result
	 * 
	 * @return
	 */
	private Map<String, Object> parseOrderList(String result) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = Integer.parseInt(respJson.getString("respCode"));
			String respMsg = respJson.getString("respMsg");
			JSONArray dataJson = jsonObject.getJSONArray("data");
			ArrayList<Express> expressLists = new ArrayList<Express>();
			for (int i = 0; i < dataJson.length(); i++) {
				JSONObject jsonObject2 = dataJson.getJSONObject(i);
				int id = Integer.parseInt(jsonObject2.getString("id"));
				int uid = Integer.parseInt(jsonObject2.getString("uid"));
				String order_code = jsonObject2.getString("order_code");
				String name = jsonObject2.getString("name");
				String phone = jsonObject2.getString("phone");
				String address = jsonObject2.getString("address");
				int aid = Integer.parseInt(jsonObject2.getString("aid"));
				double money = Double.parseDouble(jsonObject2
						.getString("money"));
				String stime = jsonObject2.getString("stime");
				String etime = jsonObject2.getString("etime");
				int state = Integer.parseInt(jsonObject2.getString("state"));
				String company = jsonObject2.getString("company");
				String getnum = jsonObject2.getString("getnum");
				String arrivetime = jsonObject2.getString("arrivetime");
				int paystyle = Integer.parseInt(jsonObject2
						.getString("paystyle"));
				int sid = Integer.parseInt(jsonObject2.getString("sid"));
				String desc = jsonObject2.getString("desc");
				String esttime = jsonObject2.getString("esttime");
				int large;
				try {
					large = Integer.parseInt(jsonObject2.getString("large"));
				} catch (Exception e) {
					large = 1;
				}
				String sendman = jsonObject2.getString("sendman");
				String sendmanphone = jsonObject2.getString("sendmanphone");
				String photo = jsonObject2.getString("photo");
				Express express = new Express();
				express.setId(id);
				express.setUid(uid);
				express.setOrder_code(order_code);
				express.setName(name);
				express.setPhone(phone);
				express.setAddress(address);
				express.setAid(aid);
				express.setMoney(money);
				express.setStime(stime);
				express.setEtime(etime);
				express.setState(state);
				express.setCompany(company);
				express.setGetnum(getnum);
				express.setArrivetime(arrivetime);
				express.setPaystyle(paystyle);
				express.setSid(sid);
				express.setDesc(desc);
				express.setEsttime(esttime);
				express.setLarge(large);
				Deliveryman deliveryman = null;
				if (!sendman.equals("null")) {
					deliveryman = new Deliveryman();
					deliveryman.setName(sendman);
					deliveryman.setPhone(sendmanphone);
					deliveryman.setPhoto(photo);
				}
				express.setDeliveryman(deliveryman);
				expressLists.add(express);
			}
			returnMap.put("respCode", respCode);
			returnMap.put("respMsg", respMsg);
			returnMap.put("data", expressLists);
			return returnMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取消订单
	 * 
	 * @param id订单id
	 * @param phone用户
	 */
	public Map<String, Object> cancelOrder(int id, String phone) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL
					+ "?c=Express&a=cancel_order_01&phone=" + phone
					+ "&orderid=" + id;
			String result = HttpUtils.sendHttpRequest(address);
			if (TextUtils.isEmpty(result)) {
				return null;
			}
			Map<String, Object> returnMap = parseCancelOrderData(result);
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析从取消订单接口获取的数据
	 * 
	 * @param resultjson数据
	 * @return
	 */
	private Map<String, Object> parseCancelOrderData(String result) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			returnMap.put("respCode", respCode);
			returnMap.put("respMsg", respMsg);
			return returnMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 寄快递
	 * 
	 * @param userPhone
	 * @param mCompany
	 * @param mName
	 * @param mPhone
	 * @param mRoomid
	 * @param mTime
	 */
	public Map<String, Object> sendExpress(String userPhone, String mCompany,
			String mName, String mPhone, String mRoomid, String mTime) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			userPhone = crypt.encrypt(userPhone);
			String address = Config.SERVER_URL
					+ "?c=Express&a=write_sendexpress&phone=" + userPhone
					+ "&company=" + URLEncoder.encode(mCompany) + "&name="
					+ URLEncoder.encode(mName) + "&tel="
					+ URLEncoder.encode(mPhone) + "&roomid="
					+ URLEncoder.encode(mRoomid) + "&receivetime="
					+ URLEncoder.encode(mTime);
			String result = HttpUtils.sendHttpRequest(address);
			if (TextUtils.isEmpty(result)) {
				return null;
			}
			Map<String, Object> returnMap = parseCancelOrderData(result);
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
