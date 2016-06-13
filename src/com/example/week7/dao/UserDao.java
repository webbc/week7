package com.example.week7.dao;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.example.week7.constant.Config;
import com.example.week7.domain.User;
import com.example.week7.http.HttpUtils;
import com.example.week7.utils.DesTools;

/**
 * 用户业务层
 * 
 * @author Administrator
 * 
 */
public class UserDao {

	private SharedPreferences sp;// 保存用户信息的sp对象
	private Context context;// 上下文对象
	private HashMap<String, Object> loginReturnMap;// 请求登录返回的数据
	private HashMap<String, Object> regReturnMap;// 请求学校返回的数据

	/**
	 * 构造函数
	 * 
	 * @param user
	 */
	public UserDao(Context context) {
		this.context = context;
	}

	/**
	 * 根据手机号和密码进行登录操作
	 * 
	 * @param mPassword密码
	 * @param mPhone手机号
	 * 
	 * @return登录操作返回的结果
	 */
	public HashMap<String, Object> login(String mPhone, String mPassword) {
		String loginUrl = getLoginUrl(mPhone, mPassword);
		String result = HttpUtils.sendHttpRequest(loginUrl);
		if (result == null) {
			return null;
		}
		parseLoginData(result);
		return loginReturnMap;
	}

	/**
	 * 拼接并获取登录的请求接口
	 * 
	 * @param mPassword密码
	 * @param mPhone手机号
	 */
	private String getLoginUrl(String mPhone, String mPassword) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String phone = crypt.encrypt(mPhone);
			String password = crypt.encrypt(mPassword);
			String url = Config.SERVER_URL + "?c=Login&a=login&phone=" + phone
					+ "&password=" + password + "&version=1.3";
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析从登录接口获取的数据
	 */
	protected void parseLoginData(String result) {
		loginReturnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			// 解析回复数据
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			User newUser = new User();
			if (respCode == 1) {
				// 解析并封装用户信息
				JSONObject dataJson = jsonObject.getJSONObject("data");
				newUser.setId(dataJson.getInt("id"));
				newUser.setPhone(dataJson.getString("phone"));
				newUser.setPassword(dataJson.getString("password"));
				newUser.setEmail(dataJson.getString("email"));
				newUser.setNickname(dataJson.getString("nickname"));
				newUser.setPhoto(dataJson.getString("photo"));
				newUser.setScore(dataJson.getString("score"));
				newUser.setRtime(dataJson.getString("rtime"));
				newUser.setLtime(dataJson.getString("ltime"));
				if (dataJson.getString("state").equals("1")) {
					newUser.setUseful(true);
				} else {
					newUser.setUseful(false);
				}
				newUser.setSex(dataJson.getString("sex"));
				newUser.setRoom_id(dataJson.getString("room_id"));
				newUser.setProvince(dataJson.getString("province"));
				newUser.setCity(dataJson.getString("city"));
				newUser.setAddress(dataJson.getString("address"));

				if (dataJson.getString("isqiandao").equals("1")) {
					newUser.setQiandao(true);
				} else {
					newUser.setQiandao(false);
				}
				newUser.setVersion(dataJson.getString("version"));
				newUser.setSid(Integer.parseInt(dataJson.getString("sid")));
			}
			// 封装返回数据
			loginReturnMap.put("respCode", respCode);
			loginReturnMap.put("respMsg", respMsg);
			loginReturnMap.put("user", newUser);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存用户信息到sp中
	 */
	public void saveUserInfo(User newUser) {
		sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isLogin", true);
		editor.putString("nickname", newUser.getNickname());
		editor.putString("score", newUser.getScore());
		editor.putString("phone", newUser.getPhone());
		editor.putString("sex", newUser.getSex());
		editor.putString("province", newUser.getProvince());
		editor.putString("city", newUser.getCity());
		editor.putString("address", newUser.getAddress());
		editor.putString("photo", newUser.getPhoto());
		editor.commit();
	}

	/**
	 * 进行注册操作
	 * 
	 * @param mSid学校id
	 * @param mPassword密码
	 * @param mPhone电话号码
	 */
	public Map<String, Object> register(String mPhone, String mPassword,
			int mSid) {
		String address = getRegisterUrl(mPhone, mPassword, mSid);
		String result = HttpUtils.sendHttpRequest(address);
		if (result == null) {
			return null;
		}
		parseRegData(result);
		return regReturnMap;
	}

	/**
	 * 解析注册(找回密码)接口返回的数据
	 * 
	 * @param result
	 */
	private void parseRegData(String result) {
		regReturnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = Integer.parseInt(respJson.getString("respCode"));
			String respMsg = respJson.getString("respMsg");
			regReturnMap.put("respCode", respCode);
			regReturnMap.put("respMsg", respMsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拼接注册接口请求的地址
	 * 
	 * @param mPhone电话
	 * @param mPassword密码
	 * @param mSid用户所属学校的id
	 * @return
	 */
	private String getRegisterUrl(String mPhone, String mPassword, int mSid) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String phone = crypt.encrypt(mPhone);
			String password = crypt.encrypt(mPassword);
			String url = Config.SERVER_URL + "?c=Register&a=register&phone="
					+ phone + "&password=" + password + "&sid" + mSid;
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改密码
	 * 
	 * @param mPhone手机号
	 * @param mPassword新密码
	 * @return从接口中解析过后的数据
	 */
	public HashMap<String, Object> editPassword(String mPhone, String mPassword) {
		String editPasswordUrl = getEditPasswordUrl(mPhone, mPassword);
		String result = HttpUtils.sendHttpRequest(editPasswordUrl);
		// 如果加载网络数据为空，就直接返回null
		if (result == null) {
			return null;
		}
		parseRegData(result);
		return regReturnMap;
	}

	/**
	 * 拼接字符串，生成修改密码的接口地址
	 * 
	 * @param mPassword手机号
	 * @param mPhone新密码
	 * 
	 * @return修改密码接口地址
	 */
	private String getEditPasswordUrl(String mPhone, String mPassword) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			String phone = crypt.encrypt(mPhone);
			String url = Config.SERVER_URL + "?c=User&a=find_password&phone="
					+ phone + "&password=" + mPassword;
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 签到
	 * 
	 * @param phone
	 * @return
	 */
	public HashMap<String, Object> qiaodao(String phone) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL + "?c=User&a=qiandao&phone="
					+ phone;
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				parseQiandaoData(result);
				return regReturnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析签到接口数据
	 * 
	 * @param result
	 */
	private void parseQiandaoData(String result) {
		regReturnMap = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject respJson = jsonObject.getJSONObject("resp");
			int respCode = respJson.getInt("respCode");
			String respMsg = respJson.getString("respMsg");
			if (respCode == 1) {
				int score = jsonObject.getInt("data");
				regReturnMap.put("score", score);
			}
			regReturnMap.put("respCode", respCode);
			regReturnMap.put("respMsg", respMsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改昵称
	 * 
	 * @param phone
	 * @param nickname
	 * @return
	 */
	public HashMap<String, Object> editNickname(String phone, String nickname) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL
					+ "?c=User&a=write_nickname&phone=" + phone + "&nickname="
					+ URLEncoder.encode(nickname);
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				parseRegData(result);
				return regReturnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改性别
	 * 
	 * @param phone
	 * @param sex
	 * @return
	 */
	public HashMap<String, Object> editSex(String phone, String sex) {
		String address = Config.SERVER_URL + "?c=User&a=write_sex&sex="
				+ URLEncoder.encode(sex) + "&phone=" + URLEncoder.encode(phone);
		String result = HttpUtils.sendHttpRequest(address);
		if (!TextUtils.isEmpty(result)) {
			parseRegData(result);
			return regReturnMap;
		}
		return null;
	}

	/**
	 * 修改用户头像
	 * 
	 * @param phone
	 * @param path
	 * @return
	 */
	public Map<String, Object> writeImg(String phone, String path) {
		DesTools crypt = new DesTools(Config.DES_KEY);
		try {
			phone = crypt.encrypt(phone);
			String address = Config.SERVER_URL + "?c=User&a=write_img&phone="
					+ phone + "&path=" + URLEncoder.encode(path);
			String result = HttpUtils.sendHttpRequest(address);
			if (!TextUtils.isEmpty(result)) {
				parseRegData(result);
				return regReturnMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改位置信息
	 * 
	 * @param province
	 * @param city
	 * @param address
	 * @param string
	 */
	public void editAddress(String phone, String province, String city,
			String address) {
		String url = Config.SERVER_URL + "?c=User&a=write_adress&phone="
				+ phone + "&province=" + URLEncoder.encode(province) + "&city="
				+ URLEncoder.encode(city) + "&address="
				+ URLEncoder.encode(address);
		HttpUtils.sendHttpRequest(url);
	}
}
