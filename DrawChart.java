
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawChart extends View {
	public int XPoint = 40; 
	public int YPoint = 160; 
	public int XScale = 70; 
	public int YScale = 30; 
	public int XLength = 380;
	public int YLength = 140; 
	public String[] XLabel; 
	public String[] YLabel; 
	public String[] Data; 
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
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(16); 

	
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); 
		for (int i = 0; i * YScale < YLength; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paint); 
			try {
				canvas.drawText(YLabel[i], XPoint - 22,
						YPoint - i * YScale + 5, paint);
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paint); 
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paint);
		canvas.drawText(danwei, XPoint - 22, YPoint - YLength, paint);
		
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);
		for (int i = 0; i * XScale < XLength; i++) {
			canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale,
					YPoint - 5, paint); 
			try {
				canvas.drawText(XLabel[i], XPoint + i * XScale - 10,
						YPoint + 20, paint); 
				
				canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 2,
						paint);
				canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]),
						XPoint + i * XScale, YCoord(Data[i]), paint);
				canvas.drawText(Data[i], XPoint + i * XScale - 10,
						YCoord(Data[i]) - 5, paint); 
			} catch (Exception e) {
			}
		}
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paint); 
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paint);
		canvas.drawText("时间", XPoint + XLength + 5, YPoint + 5, paint); 
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;
		reDraw();
	}

	private int YCoord(String y0) 
	{
		int y;
		try {
			y = Integer.parseInt(y0);
		} catch (Exception e) {
			return 0; 
		}
		try {
			return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e) {
		}
		return y;
	}
}
