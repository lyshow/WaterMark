package com.lyshow.watermark.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.lyshow.watermark.R;
import com.lyshow.watermark.view.HandWrite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
/**
 * 
 * @author lyshow
 *	编辑图片的activity
 * 2016年5月17日
 */
public class EditActivity extends Activity implements OnClickListener {
	private Button btn_edit;
	private Button btn_delete;
	private Button btn_take;
	private Button btn_sure;
	private HandWrite iv_edit;
	private File temp;
	private static final int VIDEO_CAPTURE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit);
		initView();
		initData();
	}

	public void initData() {
		temp = new File(getIntent().getStringExtra("path"));
		iv_edit.setBitmap(FitTheScreenSizeImage(BitmapFactory.decodeFile(temp.getAbsolutePath())));
	}

	public void initView() {
		btn_edit = (Button) findViewById(R.id.btn_edit);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_take = (Button) findViewById(R.id.btn_take);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		iv_edit = (HandWrite) findViewById(R.id.iv_edit);

		btn_delete.setOnClickListener(this);
		btn_edit.setOnClickListener(this);
		btn_take.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_delete:
			if (temp.exists()) {
				temp.delete();
			}
			finish();
			break;
		case R.id.btn_edit:
			Log.e("b", "vvv");
			iv_edit.clear();
			break;
		case R.id.btn_sure:
			addWaterMarking(iv_edit.final_bitmap, temp.getAbsolutePath());
			Intent intent1=new Intent(this,ImageActivity.class);
			intent1.putExtra("path",temp.getAbsolutePath());
			startActivity(intent1);
			break;
		case R.id.btn_take:
			if (temp.exists()) {
				temp.delete();
			}
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
			// intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, VIDEO_CAPTURE);
			break;
		default:
			break;
		}
	}

	public Bitmap FitTheScreenSizeImage(Bitmap m) {
		int ScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int ScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
		float width = (float) ScreenWidth / m.getWidth();
		float height = (float) ScreenHeight / m.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(width, height);
		return Bitmap.createBitmap(m, 0, 0, m.getWidth(), m.getHeight(), matrix, true);
	}

	public void addWaterMarking(Bitmap bitmap, String path) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = sDateFormat.format(new java.util.Date());
		int width = bitmap.getWidth(), hight = bitmap.getHeight();
		Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
		Canvas canvas = new Canvas(icon);// 初始化画布绘制的图像到icon上
		Paint photoPaint = new Paint(); // 建立画笔
		photoPaint.setDither(true); // 获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);// 过滤一些
		Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, width, hight);// 创建一个指定的新矩形的坐标
		canvas.drawBitmap(bitmap, src, dst, photoPaint);// 将photo 缩放或则扩大到
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		textPaint.setTextSize(40.0f);// 字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
		textPaint.setColor(Color.RED);// 采用的颜色
		// textPaint.setShadowLayer(3f, 1,
		// 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置
		int ScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int ScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
		canvas.drawText(str, ScreenWidth - 400, ScreenHeight - 20, textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		// image.setImageBitmap(icon);
		saveFile(icon, path);
	}

	public void saveFile(Bitmap bitmap, String fileName) {
		File dirFile = new File(fileName);
		// 检测图片是否存在
		if (dirFile.exists()) {
			dirFile.delete(); // 删除原图片
		}
		File myCaptureFile = new File(fileName);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			// 100表示不进行压缩，70表示压缩率为30%
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CAPTURE) {
			initData();
		}

	}

	public Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		// 得到图片的宽度、高度；
		float imgWidth = opts.outWidth;
		float imgHeight = opts.outHeight;
		// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
		int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
		int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
		opts.inSampleSize = 1;
		if (widthRatio > 1 || widthRatio > 1) {
			if (widthRatio > heightRatio) {
				opts.inSampleSize = widthRatio;
			} else {
				opts.inSampleSize = heightRatio;
			}
		}
		// 设置好缩放比例后，加载图片进内容；
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, opts);
		return bitmap;
	}
}
