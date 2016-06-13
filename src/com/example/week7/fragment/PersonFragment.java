package com.example.week7.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week7.R;
import com.example.week7.activity.ChooseLocationActivity;
import com.example.week7.activity.MoreActivity;
import com.example.week7.activity.MyParttimeActivity;
import com.example.week7.activity.OrderListActivity;
import com.example.week7.constant.Config;
import com.example.week7.dao.UserDao;
import com.example.week7.domain.Location;
import com.example.week7.domain.User;
import com.example.week7.utils.BitmapUtils;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.UploadUtils;
import com.example.week7.view.PullScrollView;
import com.example.week7.view.PullScrollView.OnTurnListener;

/**
 * 个人的Fragment
 * 
 * @author Administrator
 * 
 */
public class PersonFragment extends Fragment implements OnTurnListener,
		OnClickListener {
	private static final int QIANDAO_SUCCESS = 0;// 签到成功
	private static final int QIANDAO_FAIL = 1;// 签到失败
	private static final int QIANDAO_ERROR = 2;// 签到错误
	private static final int EDITNICKNAME_SUCCESS = 3;// 修改昵称成功
	private static final int EDITNICKNAME_FAIL = 4;// 修改昵称失败
	private static final int EDITNICKNAME_ERROR = 5;// 修改昵称错误
	private static final int EDITSEX_SUCCESS = 6;// 修改性别成功
	private static final int EDITNICKSEX_FAIL = 7;// 修改性别失败
	private static final int EDITNICKSEX_ERROR = 8;// 修改性别错误
	private static final int CHOOSE_PHOTO = 9;// 从相册选择图片
	private static final int TAKE_PHOTO = 10;// 启动拍照
	private static final int CROP_PHOTO = 11;// 裁剪图片
	protected static final int UPLOAD_IMAGE_ERROR = 12;// 上传图片失败
	private static final int WRITEIMG_SUCCESS = 13;// 图片上传成功
	private static final int WRITEIMG_FAIL = 14;// 图片上传失败
	private static final int WRITEIMG_ERROR = 15;// 图片上传错误
	private static final int REQUEST_LOCATION_CODE = 16;// 请求定位页面的标志
	private PullScrollView mScrollView;
	private ImageView mHeadImg;
	private ImageView ivPhoto;
	private TextView tvQiandao;
	private TextView tvScore;
	private TextView tvUsername;
	private LinearLayout llParttime;
	private LinearLayout llExpress;
	private LinearLayout llNickname;
	private LinearLayout llLocation;
	private LinearLayout llSex;
	private LinearLayout llMore;
	private TextView tvTel;
	private TextView tvNickname;
	private TextView tvSex;
	private User user;
	private Uri imageUri;
	private AlertDialog dialog;

	/**
	 * Handler
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 签到失败
			case QIANDAO_FAIL:
				// 签到错误
			case QIANDAO_ERROR:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// 签到成功
			case QIANDAO_SUCCESS:
				ToastUtils.showToast(getActivity(), "签到成功");
				tvScore.setText("我的积分：" + msg.obj);
				saveInfo("score", msg.obj);
				break;
			// 修改昵称失败
			case EDITNICKNAME_FAIL:
				// 修改昵称错误
			case EDITNICKNAME_ERROR:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// 修改昵称成功
			case EDITNICKNAME_SUCCESS:
				ToastUtils.showToast(getActivity(), "昵称修改成功");
				tvNickname.setText((String) msg.obj);
				tvUsername.setText((String) msg.obj);
				saveInfo("nickname", msg.obj);
				break;
			// 修改性别错误
			case EDITNICKSEX_ERROR:
				// 修改性别失败
			case EDITNICKSEX_FAIL:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// 修改性别成功
			case EDITSEX_SUCCESS:
				ToastUtils.showToast(getActivity(), "性别修改成功");
				tvSex.setText((String) msg.obj);
				saveInfo("sex", msg.obj);
				break;
			// 修改头像错误
			case WRITEIMG_ERROR:
				// 修改头像失败
			case WRITEIMG_FAIL:
				ToastUtils.showToast(getActivity(), (String) msg.obj);
				break;
			// 修改头像成功
			case WRITEIMG_SUCCESS:
				ToastUtils.showToast(getActivity(), "头像上传成功");
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				String path = (String) map.get("path");
				Bitmap bitmap = (Bitmap) map.get("bitmap");
				ivPhoto.setImageBitmap(bitmap);// 显示头像
				saveInfo("photo", path);
				break;
			default:
				ToastUtils.showToast(getActivity(), "请求错误");
				break;
			}
		}

	};
	private TextView tvLocation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		initData();
		initListener();
		return view;
	}

	/**
	 * 将修改后的信息保存到sp中
	 * 
	 * @param obj
	 */
	protected void saveInfo(String key, Object obj) {
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		sp.edit().putString(key, obj + "").commit();
	}

	/**
	 * 初始化数据并进行设置
	 */
	private void initData() {
		Bundle arguments = getArguments();
		user = (User) arguments.getSerializable("user");
		Glide.with(getActivity()).load(user.getPhoto()).into(ivPhoto);
		tvScore.setText("我的积分：" + user.getScore());
		tvUsername.setText(user.getNickname());
		tvTel.setText(user.getPhone());
		tvNickname.setText(user.getNickname());
		tvSex.setText(user.getSex());
		tvLocation.setText(user.getProvince() + user.getCity()
				+ user.getAddress());
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvQiandao.setOnClickListener(this);
		llParttime.setOnClickListener(this);
		llExpress.setOnClickListener(this);
		llNickname.setOnClickListener(this);
		llLocation.setOnClickListener(this);
		llSex.setOnClickListener(this);
		llMore.setOnClickListener(this);
		ivPhoto.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 * 
	 * @param inflater
	 * @return
	 */
	protected View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_person, null);
		mScrollView = (PullScrollView) view.findViewById(R.id.scroll_view);
		mHeadImg = (ImageView) view.findViewById(R.id.background_img);
		ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
		tvQiandao = (TextView) view.findViewById(R.id.tv_qiandao);
		tvScore = (TextView) view.findViewById(R.id.tv_score);
		tvUsername = (TextView) view.findViewById(R.id.user_name);
		llParttime = (LinearLayout) view.findViewById(R.id.ll_parttime);
		llExpress = (LinearLayout) view.findViewById(R.id.ll_express);
		llNickname = (LinearLayout) view.findViewById(R.id.ll_nickname);
		llLocation = (LinearLayout) view.findViewById(R.id.ll_location);
		tvLocation = (TextView) view.findViewById(R.id.tv_location);
		llSex = (LinearLayout) view.findViewById(R.id.ll_sex);
		llMore = (LinearLayout) view.findViewById(R.id.ll_more);
		tvTel = (TextView) view.findViewById(R.id.tv_tel);
		tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
		tvSex = (TextView) view.findViewById(R.id.tv_sex);

		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);
		return view;
	}

	@Override
	public void onTurn() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 签到按钮
		case R.id.tv_qiandao:
			new Thread() {
				public void run() {
					HashMap<String, Object> returnMap = new UserDao(
							getActivity()).qiaodao(user.getPhone());
					updateQiandaoUI(returnMap);
				};
			}.start();
			break;
		// 我的兼职
		case R.id.ll_parttime:
			Intent myparttimeIntent = new Intent(getActivity(),
					MyParttimeActivity.class);
			startActivity(myparttimeIntent);
			break;
		// 我的快递
		case R.id.ll_express:
			Intent intent = new Intent(getActivity(), OrderListActivity.class);
			startActivity(intent);
			break;
		// 昵称
		case R.id.ll_nickname:
			openEditNicknameDialog();
			break;
		// 性别
		case R.id.ll_sex:
			openEditSexDialog();
			break;
		// 地区
		case R.id.ll_location:
			// 打开定位选择
			Intent chooseLocationIntent = new Intent(getActivity(),
					ChooseLocationActivity.class);
			startActivityForResult(chooseLocationIntent, REQUEST_LOCATION_CODE);
			break;
		// 更多
		case R.id.ll_more:
			Intent moreIntent = new Intent(getActivity(), MoreActivity.class);
			startActivity(moreIntent);
			break;
		// 头像
		case R.id.iv_photo:
			showChoosePhotoDialog();
			break;
		// 从相册选择图片
		case R.id.btn_album:
			Intent i = new Intent("android.intent.action.GET_CONTENT");
			i.setType("image/*");
			startActivityForResult(i, CHOOSE_PHOTO);// 打开相册
			break;
		// 取消选择图片
		case R.id.btn_cancel:
			closeChoosePhotoDialog();
			break;
		// 进行拍照
		case R.id.btn_take:
			File file = new File(Environment.getExternalStorageDirectory(),
					"output_image.jpg");
			try {
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			imageUri = Uri.fromFile(file);
			Intent takePhotoIntent = new Intent();
			takePhotoIntent.setAction("android.media.action.IMAGE_CAPTURE");
			takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(takePhotoIntent, TAKE_PHOTO);
			break;
		}
	}

	/**
	 * 打开修改性别的对话框
	 */
	private void openEditSexDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("请选择性别");
		final String items[] = { "男", "女" };
		String sex = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("sex", "");
		int position = 0;
		for (int i = 0; i < items.length; i++) {
			if (sex.equals(items[i]))
				position = i;
		}
		builder.setSingleChoiceItems(items, position,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						final String sex = items[which];
						new Thread() {
							public void run() {
								HashMap<String, Object> returnMap = new UserDao(
										getActivity()).editSex(user.getPhone(),
										sex);
								updateWriteSexUI(returnMap, sex);
							};
						}.start();
						dialog.dismiss();
					}
				});
		builder.show();
	}

	/**
	 * 打开修改昵称的对话框
	 */
	private void openEditNicknameDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("修改昵称");
		View view = View.inflate(getActivity(), R.layout.dialog_edit_nickname,
				null);
		final EditText etNickname = (EditText) view
				.findViewById(R.id.et_nickname);
		etNickname.setText(getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("nickname", ""));
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 修改昵称
				new Thread() {
					public void run() {
						String nickname = etNickname.getText().toString()
								.trim();
						HashMap<String, Object> returnMap = new UserDao(
								getActivity()).editNickname(user.getPhone(),
								nickname);
						updateWriteNicknameUI(returnMap, nickname);
					}

				}.start();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 刷新写性别后的数据
	 * 
	 * @param returnMap
	 * @param nickname
	 */
	private void updateWriteSexUI(HashMap<String, Object> returnMap, String sex) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				message.obj = sex;
				message.what = EDITSEX_SUCCESS;
			} else {
				message.what = EDITNICKSEX_FAIL;
				message.obj = respMsg;
			}
		} else {
			message.what = EDITNICKSEX_ERROR;
			message.obj = "请求错误";
		}
		handler.sendMessage(message);
	}

	/**
	 * 刷新写昵称后的数据
	 * 
	 * @param returnMap
	 * @param nickname
	 */
	private void updateWriteNicknameUI(HashMap<String, Object> returnMap,
			String nickname) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				message.obj = nickname;
				message.what = EDITNICKNAME_SUCCESS;
			} else {
				message.what = EDITNICKNAME_FAIL;
				message.obj = respMsg;
			}
		} else {
			message.what = EDITNICKNAME_ERROR;
			message.obj = "请求错误";
		}
		handler.sendMessage(message);
	}

	/**
	 * 刷新签到后的界面
	 */
	protected void updateQiandaoUI(HashMap<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				int score = (Integer) returnMap.get("score");
				message.obj = score;
				message.what = QIANDAO_SUCCESS;
			} else {
				message.what = QIANDAO_FAIL;
				message.obj = respMsg;
			}
		} else {
			message.what = QIANDAO_ERROR;
			message.obj = "请求错误";
		}
		handler.sendMessage(message);
	}

	/**
	 * 刷新签到后的界面
	 * 
	 * @param bitmap
	 */
	protected void updateWriteImgUI(Map<String, Object> returnMap, String path,
			Bitmap bitmap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = WRITEIMG_SUCCESS;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("path", path);
				map.put("bitmap", bitmap);
				message.obj = map;
			} else {
				message.what = WRITEIMG_FAIL;
				message.obj = "头像上传失败";
			}
		} else {
			message.what = WRITEIMG_ERROR;
			message.obj = "请求错误";
		}
		handler.sendMessage(message);
	}

	/**
	 * 显示选择照片的对话框
	 */
	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		dialog = builder.create();
		View view = View.inflate(getActivity(), R.layout.dialog_choose_photo,
				null);
		Button btnAlbum = (Button) view.findViewById(R.id.btn_album);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		Button btnTake = (Button) view.findViewById(R.id.btn_take);
		btnAlbum.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnTake.setOnClickListener(this);
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 关闭选择照片的对话框
	 */
	private void closeChoosePhotoDialog() {
		dialog.dismiss();
	}

	// 从拍照Activity返回数据时回调
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 从定位页面获取传来的数据
		case REQUEST_LOCATION_CODE:
			if (resultCode == Activity.RESULT_OK) {
				final Location location = (Location) data
						.getSerializableExtra("location");
				// 显示定位信息
				tvLocation.setText(location.getProvince() + location.getCity()
						+ location.getAddress());
				// 把定位信息后的信息写到服务器中
				new Thread() {
					@Override
					public void run() {
						new UserDao(getActivity()).editAddress(user.getPhone(),
								location.getProvince(), location.getCity(),
								location.getAddress());
					}
				}.start();
				// 把定位信息写到本地
				saveInfo("province", location.getProvince());
				saveInfo("city", location.getCity());
				saveInfo("address", location.getAddress());
			}
			break;
		// 拍照
		case TAKE_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			closeChoosePhotoDialog();
			break;
		// 裁剪图片
		case CROP_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				// Bitmap bitmap = BitmapUtils
				// .load(getActivity(),);
				// uploadImage(bitmap);
			}
			closeChoosePhotoDialog();
			break;
		// 选择图片
		case CHOOSE_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				// 判断手机系统版本号
				if (Build.VERSION.SDK_INT >= 19) {
					handleImageOnKitKat(data);
				} else {
					handleImageBeforeKitKat(data);
				}
			}
			closeChoosePhotoDialog();
			break;
		}
	}

	private void handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		displayImage(imagePath);
	}

	@SuppressLint("NewApi")
	private void handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
			// 如果是document类型的Uri，则通过document id处理
			String docId = DocumentsContract.getDocumentId(uri);
			if ("com.android.providers.media.documents".equals(uri
					.getAuthority())) {
				String id = docId.split(":")[1];// 解析出数字格式的id
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
			} else if ("com.android.providers.downloads.documents".equals(uri
					.getAuthority())) {
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
			} else if ("content".equalsIgnoreCase(uri.getScheme())) {
				// 如果不是document类型的Uri，则使用普通方式处理
				imagePath = getImagePath(uri, null);
			}
		}
		displayImage(imagePath);// 根据图片路径显示图片
	}

	/**
	 * 显示图片
	 * 
	 * @param imagePath
	 */
	private void displayImage(String imagePath) {
		if (imagePath != null) {
			final Bitmap bitmap = BitmapUtils.load(getActivity(), imagePath);
			File file = new File(imagePath);
			uploadImage(bitmap, file);
		} else {
			Toast.makeText(getActivity(), "Failed to get image ", 0).show();
		}
	}

	private String getImagePath(Uri uri, String selection) {
		String path = null;
		// 通过该Uri和selection来获取真实的图片路径
		Cursor cursor = getActivity().getContentResolver().query(uri, null,
				selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
			cursor.close();
		}
		return path;
	}

	/**
	 * 上传头像
	 */
	public void uploadImage(final Bitmap bitmap, final File file) {
		// 上传头像
		new Thread() {
			public void run() {
				// 首先上传图片
				String requestUrl = Config.SERVER_URL + "?c=User&a=upload_img";
				String result = UploadUtils.uploadFile(file, requestUrl);
				// 然后修改用户头像数据
				if (!TextUtils.isEmpty(result)) {
					Map<String, Object> returnMap = new UserDao(getActivity())
							.writeImg(user.getPhone(), result);
					updateWriteImgUI(returnMap, result, bitmap);
				} else {
					handler.sendEmptyMessage(UPLOAD_IMAGE_ERROR);
				}
			};
		}.start();
	}
}
