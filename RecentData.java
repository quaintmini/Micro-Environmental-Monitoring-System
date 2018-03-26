package com.zhangtory.hj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhangtory.draw.DrawChart;
import com.zhangtory.hj.R;
import com.zhangtory.http.Webconn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class RecentData extends BaseActivity {
	private int defaultDevId = 1;
	private TextView text;
	private Handler mHandler;
	private ArrayAdapter<String> arrAdp;
	private Spinner selectLocation;
	private DrawChart view_temp;
	private DrawChart view_humi;
	private DrawChart view_pm;
	private DrawChart view_lx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recentdata);

		mHandler = new Handler();
		mHandler.post(new TimerProcess());
		text = (TextView) this.findViewById(R.id.recentText);
		selectLocation = (Spinner) this.findViewById(R.id.spinner2);

		LinearLayout layout_temp = (LinearLayout) findViewById(R.id.temp_canvas);
		view_temp = new DrawChart(RecentData.this);
		view_temp.invalidate();
		layout_temp.addView(view_temp);
		LinearLayout layout_humi = (LinearLayout) findViewById(R.id.humi_canvas);
		view_humi = new DrawChart(RecentData.this);
		view_humi.invalidate();
		layout_humi.addView(view_humi);
		LinearLayout layout_pm = (LinearLayout) findViewById(R.id.pm_canvas);
		view_pm = new DrawChart(RecentData.this);
		view_pm.invalidate();
		layout_pm.addView(view_pm);
		LinearLayout layout_lx = (LinearLayout) findViewById(R.id.lx_canvas);
		view_lx = new DrawChart(RecentData.this);
		view_lx.invalidate();
		layout_lx.addView(view_lx);

		getDevInformation();
	}

	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String ret = msg.obj.toString();
			switch (msg.what) {
			case 1:
				try {
					JSONArray jsonArr = new JSONArray(ret);
					final String[] dev = new String[jsonArr.length()];
					final int[] devId = new int[jsonArr.length()];
					for (int i = 0; i < jsonArr.length(); i++) {
						JSONObject item = jsonArr.getJSONObject(i);
						dev[i] = item.getString("location");
						devId[i] = item.getInt("id");
					}
					arrAdp = new ArrayAdapter<String>(RecentData.this,
							android.R.layout.simple_spinner_item, dev);
					selectLocation.setAdapter(arrAdp);
					selectLocation
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {
									defaultDevId = devId[position];
									getRecentInformation();
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> parent) {
								}
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				String[] sTemp = new String[6];
				String[] sHumi = new String[6];
				String[] sPm = new String[6];
				String[] sLx = new String[6];
				String[] sTime = new String[6];
				try {
					JSONArray jsonArr = new JSONArray(ret);
					JSONObject item = jsonArr.getJSONObject(0);
					String stime = item.getString("time");
					String ti = "服务器数据更新时间: " + stime.substring(8, 10) + "时"
							+ stime.substring(10, 12) + "分"
							+ stime.substring(12, 14) + "秒";
					text.setText(ti);
					String hour = stime.substring(8, 10);
					String min = stime.substring(10, 12);
					for (int i = 1; i < jsonArr.length(); i++) {
						JSONObject item1 = jsonArr.getJSONObject(i);
						sTemp[jsonArr.length() -i-1] = item1.getInt("avg(temp)") + "";
						sHumi[jsonArr.length() -i-1] = item1.getInt("avg(humi)") + "";
						sPm[jsonArr.length() -i-1] = item1.getInt("avg(pm)") + "";
						sLx[jsonArr.length() -i-1] = item1.getInt("avg(lx)") + "";
					}
					int h = Integer.valueOf(hour);
					int cnt = 0;
					for (int i = 5; i >= 0; i--) {
						int tem = h - cnt;
						if (tem < 0) {
							tem += 24;
						}
						sTime[i] = tem + ":" + min;
						cnt++;
					}
					view_temp.SetInfo(sTime, // X轴刻度
							new String[] { "", "10", "20", "30", "40" }, // Y轴刻度
							sTemp, // 数据
							"℃");
					view_humi.SetInfo(sTime, // X轴刻度
							new String[] { "", "30", "40", "50", "60" }, // Y轴刻度
							sHumi, // 数据
							"%rh");
					view_pm.SetInfo(sTime, // X轴刻度
							new String[] { "", "30", "40", "50", "60" }, // Y轴刻度
							sPm, // 数据
							"ug/m3");
					view_lx.SetInfo(sTime, // X轴刻度
							new String[] { "", "100", "300", "500", "700" }, // Y轴刻度
							sLx, // 数据
							"lx");
					view_temp.invalidate();
					view_humi.invalidate();
					view_pm.invalidate();
					view_lx.invalidate();
				} catch (JSONException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}
	};

	// delay 1min
	private class TimerProcess implements Runnable {
		public void run() {
			mHandler.postDelayed(this, 60000);
			getRecentInformation();
		}
	}

	private void getDevInformation() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String ret = Webconn.getDev();
				System.out.println("recent dev:");
				System.out.println(ret);
				handler.sendMessage(handler.obtainMessage(1, ret));
			}
		}).start();
	}

	private void getRecentInformation() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String ret = Webconn.getRecent(defaultDevId);
				System.out.println("recent recent:");
				System.out.println(ret);
				handler.sendMessage(handler.obtainMessage(3, ret));
			}
		}).start();
	}

}
