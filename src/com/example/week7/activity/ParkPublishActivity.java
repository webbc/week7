package com.example.week7.activity;

import java.io.File;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.week7.R;
import com.example.week7.constant.Config;
import com.example.week7.dao.ParkDao;
import com.example.week7.utils.ToastUtils;
import com.example.week7.utils.UploadUtils;

/**
 * 发布微博页面
 * 
 * @author Administrator
 * 
 */
public class ParkPublishActivity extends BaseActivity implements
		OnClickListener {
	private static final int PUBLISH_SUCCES = 0;
	private static final int PUBLISH_ERROR = 1;
	private static final int CHOOSE_PHOTO = 2;// 从相册选择图片
	protected static final int UPLOAD_FILE_ERROR = 3;// 上传图片失败
	private EditText etContent;
	private Button btnChoosePic;
	private Button btnOk;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PUBLISH_SUCCES:
				ToastUtils.showToast(ParkPublishActivity.this, "发布成功");
				Intent data = new Intent();
				setResult(RESULT_OK, data);
				finish();
				break;
			case PUBLISH_ERROR:
				ToastUtils.showToast(ParkPublishActivity.this, "发布失败");
				break;
			case UPLOAD_FILE_ERROR:
				ToastUtils.showToast(ParkPublishActivity.this, "图片上传失败");
				break;
			default:
				ToastUtils.showToast(ParkPublishActivity.this, "请求错误");
				break;
			}
		}

	};
	private AlertDialog dialog;
	private File file;// 选择的文件对象

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
		btnChoosePic.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	/**
	 * 初始化UI
	 */
	private void initView() {
		setContentView(R.layout.activity_park_publish);
		etContent = (EditText) findViewById(R.id.et_content);
		btnChoosePic = (Button) findViewById(R.id.btn_choose_pic);
		btnOk = (Button) findViewById(R.id.btn_ok);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 选择图片按钮
		case R.id.btn_choose_pic:
			showChoosePhotoDialog();
			break;
		// 确认按钮
		case R.id.btn_ok:
			final String content = etContent.getText().toString();
			if (TextUtils.isEmpty(content)) {
				ToastUtils.showToast(this, "内容不能为空");
				break;
			}
			if (content.length() > 144) {
				ToastUtils.showToast(this, "不能超过144个字符");
				break;
			}
			// 向服务器请求数据
			new Thread() {
				public void run() {
					// 先提交图片到服务器
					// 首先上传图片
					if (file != null) {
						String requestUrl = Config.SERVER_URL
								+ "?c=User&a=upload_img";
						String result = UploadUtils
								.uploadFile(file, requestUrl);
						if (!TextUtils.isEmpty(result)) {
							// 后把微博写到服务器
							Map<String, Object> returnMap = new ParkDao()
									.writePark(phone, content, result);
							updateUI(returnMap);
						} else {
							handler.sendEmptyMessage(UPLOAD_FILE_ERROR);
						}
					} else {
						// 后把微博写到服务器
						Map<String, Object> returnMap = new ParkDao()
								.writePark(phone, content, null);
						updateUI(returnMap);
					}

				};
			}.start();
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
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	/**
	 * 刷新UI
	 * 
	 * @param returnMap
	 */
	protected void updateUI(Map<String, Object> returnMap) {
		Message message = handler.obtainMessage();
		if (returnMap != null) {
			int respCode = (Integer) returnMap.get("respCode");
			if (respCode == 1) {
				message.what = PUBLISH_SUCCES;
			} else {
				message.what = PUBLISH_ERROR;
			}
		} else {
			message.what = PUBLISH_ERROR;
		}
		handler.sendMessage(message);
	}

	// 从拍照Activity返回数据时回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CHOOSE_PHOTO:
			if (resultCode == RESULT_OK) {
				// 判断手机系统版本号
				if (Build.VERSION.SDK_INT >= 19) {
					file = handleImageOnKitKat(data);
				} else {
					file = handleImageBeforeKitKat(data);
				}
				// 判断返回的文件对象
				if (file != null) {
					btnChoosePic.setText("图片已选择");
				} else {
					btnChoosePic.setText("点击选择图片");
				}
			}
			break;
		}
		closeChoosePhotoDialog();
	}

	private File handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		return returnImage(imagePath);
	}

	@SuppressLint("NewApi")
	private File handleImageOnKitKat(Intent data) {
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
		return returnImage(imagePath);// 根据图片路径显示图片
	}

	/**
	 * 显示图片
	 * 
	 * @param imagePath
	 */
	private File returnImage(String imagePath) {
		if (imagePath != null) {
			File file = new File(imagePath);
			return file;
		} else {
			Toast.makeText(this, "Failed to get image ", 0).show();
		}
		return null;
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
