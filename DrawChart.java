package com.zhangtory.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawChart extends View {
	public int XPoint = 40; // 鍘熺偣鐨刋鍧愭爣
	public int YPoint = 160; // 鍘熺偣鐨刌鍧愭爣
	public int XScale = 70; // X鐨勫埢搴﹂暱搴�
	public int YScale = 30; // Y鐨勫埢搴﹂暱搴�
	public int XLength = 380; // X杞寸殑闀垮害
	public int YLength = 140; // Y杞寸殑闀垮害
	public String[] XLabel; // X鐨勫埢搴�
	public String[] YLabel; // Y鐨勫埢搴�
	public String[] Data; // 鏁版嵁
	public String danwei="";
	private Canvas canvas;

	public DrawChart(Context context) {
		super(context);
	}

	public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData,
						String dw) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		danwei = dw;
	}

	public void reDraw(){
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 鍘婚敮榻�
		paint.setColor(Color.BLACK);// 棰滆壊
		paint.setTextSize(16); // 璁剧疆杞存枃瀛楀ぇ灏�

		// 璁剧疆Y杞�
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // 杞寸嚎
		for (int i = 0; i * YScale < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paint); // 鍒诲害
			try {
				canvas.drawText(YLabel[i], XPoint - 22,
						YPoint - i * YScale + 5, paint); // 鏂囧瓧
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paint); // 绠ご
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paint);
		canvas.drawText(danwei, XPoint - 22, YPoint - YLength, paint); // 鍗曚綅
		// 璁剧疆X杞�
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // 杞寸嚎
		for (int i = 0; i * XScale < XLength; i++) {
			canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale,
					YPoint - 5, paint); // 鍒诲害
			try {
				canvas.drawText(XLabel[i], XPoint + i * XScale - 10,
						YPoint + 20, paint); // 鏂囧瓧
				// 鏁版嵁鍊�
				canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 2,
						paint);
				canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]),
						XPoint + i * XScale, YCoord(Data[i]), paint);
				canvas.drawText(Data[i], XPoint + i * XScale - 10,
						YCoord(Data[i]) - 5, paint); // 鏂囧瓧
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paint); // 绠ご
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paint);
		canvas.drawText("时间", XPoint + XLength + 5, YPoint + 5, paint); // 鍗曚綅
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);// 閲嶅啓onDraw鏂规硶
		this.canvas = canvas;
		reDraw();
	}

	private int YCoord(String y0) // 璁＄畻缁樺埗鏃剁殑Y鍧愭爣锛屾棤鏁版嵁鏃惰繑鍥�0
	{
		int y;
		try {
			y = Integer.parseInt(y0);
		} catch (Exception e) {
			return 0; // 鍑洪敊鍒欒繑鍥�0
		}
		try {
			return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e) {
		}
		return y;
	}
}
