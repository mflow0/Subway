package com.e.moon.subway;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * xml 파일이 아닌 코드로 버튼의 normal 버튼과 click 버튼을 생성자로 받아서 변경해주는 작업
 * @normalId = R.drawable.confirm_btn_normal
 * @clickedId = R.drawable.confirm_btn_clicked
 */

public class BitmapButton extends Button {

	int normalBitmapId;
	int clickedBitmapId;
	
	public BitmapButton(Context context) {
		super(context);
	}

	public BitmapButton(Context context, AttributeSet atts) {
		super(context, atts);

	}

	public void setBitmapId(int normalId, int clickedId) {
		normalBitmapId = normalId;
		clickedBitmapId = clickedId;
		
		setBackgroundResource(normalBitmapId);
	}
	

	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_UP:
				setBackgroundResource(normalBitmapId);
				
				break;
	
			case MotionEvent.ACTION_DOWN:
				setBackgroundResource(clickedBitmapId);
				
				break;
		}
		invalidate();
		return true;
	}	
	
}
