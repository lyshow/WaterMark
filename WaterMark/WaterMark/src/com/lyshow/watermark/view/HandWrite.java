package com.lyshow.watermark.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author lyshow
 *	涂鸦的自定义view
 * 2016年5月17日
 */
public class HandWrite extends View
{
    private Paint paint = null;
    private Bitmap originalBitmap = null;
    private Bitmap new1Bitmap = null;
    private Bitmap new2Bitmap = null;
    private float clickX = 0,clickY = 0;
    private float startX = 0,startY = 0;
    private boolean isMove = true;
    private Context context;
    private boolean isClear = false;
    private int color = Color.RED;
    private float strokeWidth = 3.0f;
    public Bitmap final_bitmap;
	public HandWrite(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context=context;
	}
	public void setBitmap(Bitmap bitmap){
		originalBitmap = bitmap;
		new1Bitmap = Bitmap.createBitmap(originalBitmap);
		
	}
    public void clear(){
    	isClear = true;
    	new2Bitmap = Bitmap.createBitmap(originalBitmap);
    	invalidate();
    }
    public void setstyle(float strokeWidth){
    	this.strokeWidth = strokeWidth;
    }
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawBitmap(HandWriting(new1Bitmap), 0, 0,null);
		final_bitmap=HandWriting(new1Bitmap);
	}

	public Bitmap HandWriting(Bitmap originalBitmap)
	{
		Canvas canvas = null;
		
		if(isClear){
			canvas = new Canvas(new2Bitmap);
		}
		else{
			canvas = new Canvas(originalBitmap);
		}
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setStrokeWidth(strokeWidth);
		if(isMove){
			canvas.drawLine(startX, startY, clickX, clickY, paint);
		}
		
		startX = clickX;
		startY = clickY;
		
		if(isClear){
			return new2Bitmap;
		}
		return originalBitmap;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		clickX = event.getX();
		clickY = event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			startX = event.getX();
			startY = event.getY();
			isMove = false;
			invalidate();
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			
			isMove = true;
			invalidate();
			return true;
		}		
		return super.onTouchEvent(event);
	}
}

