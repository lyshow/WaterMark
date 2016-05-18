package com.lyshow.watermark.activity;

import java.io.File;

import com.lyshow.watermark.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
/**
 * 
 * @author lyshow
 *	显示添加水印和涂鸦过后的view
 * 2016年5月17日
 */
public class ImageActivity extends Activity {
	private ImageView iv;
	File temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		iv = (ImageView) findViewById(R.id.iv);
		/*boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			temp= new File(getIntent().getStringExtra("path"));
		}*/
		temp= new File(getIntent().getStringExtra("path"));
		Bitmap bitmap = BitmapFactory.decodeFile(temp.getAbsolutePath());
		iv.setImageBitmap(bitmap);
	}
}
