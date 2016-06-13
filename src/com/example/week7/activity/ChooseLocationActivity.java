package com.example.week7.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.example.week7.R;
import com.example.week7.adapter.LocationListViewAdapter;
import com.example.week7.domain.Location;
import com.example.week7.utils.ToastUtils;

/**
 * 选择区域位置的页面
 * 
 * @author Administrator
 * 
 */
public class ChooseLocationActivity extends BaseActivity implements
		BDLocationListener {
	private static final int LOCATION_SUCCESS = 0;// 定位成功
	private static final int LOCATION_FAIL = 1;// 定位失败
	private ListView lvLocation;
	private LocationListViewAdapter adapter;
	private ArrayList<Location> locationList;
	private LinearLayout llProgress;

	/**
	 * 定位所用对象
	 */
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = this;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOCATION_FAIL:
				llProgress.setVisibility(View.GONE);
				ToastUtils.showToast(ChooseLocationActivity.this,
						(String) msg.obj);
				break;
			case LOCATION_SUCCESS:
				llProgress.setVisibility(View.GONE);
				adapter = new LocationListViewAdapter(
						ChooseLocationActivity.this, locationList);
				lvLocation.setAdapter(adapter);
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 初始化位置信息
		 */
		mLocationClient = new LocationClient(this); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		initLocation();
		// 开启定位服务
		mLocationClient.start();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		lvLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent data = new Intent();
				data.putExtra("location", locationList.get(position));
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_choose_location);
		lvLocation = (ListView) findViewById(R.id.lv_location);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
	}

	/**
	 * 初始化定位SDK参数
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 0;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	/**
	 * 接受到定位结果的回调
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {
		String province = null, city = null;
		String exception = null;
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
			province = location.getProvince();
			city = location.getCity();
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
			province = location.getProvince();
			city = location.getCity();
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			province = location.getProvince();
			exception = "请确保开启GPS或网络";
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			exception = "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因";
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			exception = "网络不同导致定位失败，请检查网络是否通畅";
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			exception = "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机";
		}
		List<Poi> list = location.getPoiList();
		locationList = new ArrayList<Location>();
		for (Poi poi : list) {
			Location l = new Location(province, city, poi.getName());
			locationList.add(l);
		}
		Message message = handler.obtainMessage();
		if (exception == null) {
			message.what = LOCATION_SUCCESS;
		} else {
			message.obj = exception;
			message.what = LOCATION_FAIL;
		}
		handler.sendMessage(message);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 停止定位服务
		mLocationClient.stop();
	}
}
