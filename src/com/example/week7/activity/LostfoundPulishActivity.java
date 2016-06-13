package com.example.week7.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Intent;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.week7.R;
import com.example.week7.constant.Config;
import com.example.week7.dao.LostFoundDao;
import com.example.week7.domain.Lostfound;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.UploadUtils;
import com.example.week7.utils.VerificationFormat;

/**
 * 发布失物招领页面
 * 
 * @author Administrator
 * 
 */
public class LostfoundPulishActivity extends BaseActivity implements
		OnClickListener {
	private static final int REQUEST_SUCCESS = 0;// 请求成功
	private static final int REQUEST_FAIL = 1;// 请求失败
	private static final int REQUEST_ERROR = 2;// 请求错误
	private static final int CHOOSE_PHOTO = 3;// 从相册选择图片
	private static final int TAKE_PHOTO = 4;// 启动拍照
	private static final int CROP_PHOTO = 5;// 裁剪图片
	protected static final int UPLOAD_IMAGE_ERROR = 6;// 上传图片失败
	private ImageView ivBack;
	private EditText etName;
	private EditText etTel;
	private EditText etDesc;
	private Button btnChoosePic;
	private Button btnOk;
	private String name;
	private String tel;
	private String desc;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REQUEST_SUCCESS:
				ToastUtils.showToast(LostfoundPulishActivity.this, "发布成功");
				Lostfound lostfound = (Lostfound) msg.obj;
				Intent data = new Intent();
				data.putExtra("data", lostfound);
				setResult(RESULT_OK, data);
				finish();
				break;
			case REQUEST_FAIL:
			case REQUEST_ERROR:
				ToastUtils.showToast(LostfoundPulishActivity.this,
						(String) msg.obj);
				break;
			case UPLOAD_IMAGE_ERROR:
				ToastUtils.showToast(LostfoundPulishActivity.this, "图片上传失败");
				break;
			default:
				ToastUtils.showToast(LostfoundPulishActivity.this, "请求错误");
				break;
			}
		}

	};
	private AlertDialog dialog;
	private ImageView ivPic;
	private Uri imageUri;
	private File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
		btnChoosePic.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		ivPic.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_lostfound_publish);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		etName = (EditText) findViewById(R.id.et_name);
		etTel = (EditText) findViewById(R.id.et_tel);
		etDesc = (EditText) findViewById(R.id.et_desc);
		btnChoosePic = (Button) findViewById(R.id.btn_choose_pic);
		btnOk = (Button) findViewById(R.id.btn_ok);
		ivPic = (ImageView) findViewById(R.id.iv_pic);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 标题栏的退出按钮
		case R.id.iv_back:
			finish();
			break;
		// 选择图片按钮
		case R.id.btn_choose_pic:
			showChoosePhotoDialog();
			break;
		// 点击确认按钮
		case R.id.btn_ok:
			getInputData();
			if (checkData()) {
				new Thread() {
					public void run() {
						// 首先上传图片
						String requestUrl = Config.SERVER_URL
								+ "?c=User&a=upload_img";
						String result = UploadUtils
								.uploadFile(file, requestUrl);
						if (!TextUtils.isEmpty(result)) {
							Map<String, Object> returnMap = new LostFoundDao()
									.write(name, tel, result, desc, phone);
							updateUI(returnMap);
						} else {
							handler.sendEmptyMessage(UPLOAD_IMAGE_ERROR);
						}
					};
				}.start();
			}
			break;
		// 从相册选择图片
		case R.id.btn_album:
			Intent intent = new Intent("android.intent.action.GET_CONTENT");
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);// 打开相册
			break;
		// 取消选择图片
		case R.id.btn_cancel:
			closeChoosePhotoDialog();
			break;
		// 进行拍照
		case R.id.btn_take:
			file = new File(Environment.getExternalStorageDirectory(),
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
		// 点击图片，切换照片
		case R.id.iv_pic:
			showChoosePhotoDialog();
			break;
		}
	}

	/**
	 * 显示选择照片的对话框
	 */
	private void showChoosePhotoDialog() {
		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_choose_photo, null);
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

	/**
	 * 从服务器返回数据后，进行刷新界面操作
	 * 
	 * @param returnMap
	 */
	protected void updateUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			String respMsg = (String) returnMap.get("respMsg");
			if (respCode == 1) {
				message.obj = returnMap.get("data");
				message.what = REQUEST_SUCCESS;
			} else {
				message.obj = respMsg;
				message.what = REQUEST_FAIL;
			}
			handler.sendMessage(message);
		} else {
			message.what = REQUEST_ERROR;
			message.obj = "请求错误";
			handler.sendMessage(message);
		}
	}

	/**
	 * 检测输入的数据是否合法
	 */
	private boolean checkData() {
		if (TextUtils.isEmpty(name)) {
			ToastUtils.showToast(this, "姓名不能为空");
			return false;
		}
		if (TextUtils.isEmpty(tel)) {
			ToastUtils.showToast(this, "电话不能为空");
			return false;
		}
		if (!VerificationFormat.isMobile(tel)) {
			ToastUtils.showToast(this, "电话格式不正确");
			return false;
		}
		if (TextUtils.isEmpty(desc)) {
			ToastUtils.showToast(this, "描述不能为空");
			return false;
		}
		return true;
	}

	/**
	 * 获取输入的数据
	 */
	private void getInputData() {
		name = etName.getText().toString().trim();
		tel = etTel.getText().toString().trim();
		desc = etDesc.getText().toString().trim();
	}

	// 从拍照Activity返回数据时回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Bitmap bitmap = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									imageUri));
					ivPic.setVisibility(View.VISIBLE);
					btnChoosePic.setVisibility(View.GONE);
					ivPic.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		case CHOOSE_PHOTO:
			if (resultCode == RESULT_OK) {
				// 判断手机系统版本号
				if (Build.VERSION.SDK_INT >= 19) {
					handleImageOnKitKat(data);
				} else {
					handleImageBeforeKitKat(data);
				}
			}
			break;
		}
		closeChoosePhotoDialog();
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
		if (DocumentsContract.isDocumentUri(this, uri)) {
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
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			file = new File(imagePath);
			ivPic.setVisibility(View.VISIBLE);
			btnChoosePic.setVisibility(View.GONE);
			ivPic.setImageBitmap(bitmap);
		} else {
			ivPic.setVisibility(View.GONE);
			btnChoosePic.setVisibility(View.VISIBLE);
			Toast.makeText(this, "Failed to get image ", 0).show();
		}
	}

	private String getImagePath(Uri uri, String selection) {
		String path = null;
		// 通过该Uri和selection来获取真实的图片路径
		Cursor cursor = getContentResolver().query(uri, null, selection, null,
				null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
			cursor.close();
		}
		return path;
	}

}
