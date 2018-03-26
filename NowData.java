package com.zhangtory.hj;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhangtory.http.Webconn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

 public class NowData extends BaseActivity {
	 private int defaultDevId = 1;
	 private TextView text;
	 private TextView text_pm;
	 private TextView text_lx;
	 private TextView text_humi;
	 private TextView text_temp;
	 private Handler mHandler;
	 private ArrayAdapter<String> arrAdp;
	 private Spinner selectLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nowdata);

		mHandler = new Handler();
		mHandler.post(new TimerProcess());
		text =  (TextView) this.findViewById(R.id.nowtext);
		text_pm =  (TextView) this.findViewById(R.id.text_now_pm);
		text_lx =  (TextView) this.findViewById(R.id.text_now_lx);
		text_humi =  (TextView) this.findViewById(R.id.text_now_humi);
		text_temp =  (TextView) this.findViewById(R.id.text_now_temp);
		selectLocation = (Spinner) this.findViewById(R.id.spinner1);
		
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
					arrAdp = new ArrayAdapter<String>(NowData.this,
							android.R.layout.simple_spinner_item, dev);
					selectLocation.setAdapter(arrAdp);
					selectLocation
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {
									defaultDevId = devId[position];
									getNowInformation();
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
			case 2:
				try {
					JSONArray jsonArr = new JSONArray(ret);
					JSONObject item = jsonArr.getJSONObject(0);
					String t = item.getInt("temp")+"";
					String h = "湿度: " + item.getInt("humi") + " %rh";
					String p = item.getInt("pm") + "";
					String l = "光照: " + item.getInt("lx") + " lx";
					String stime = item.getString("time");
					String ti = "服务器数据更新时间: " + stime.substring(8, 10) + "时"
							+ stime.substring(10, 12) + "分"
							+ stime.substring(12, 14) + "秒";
					text.setText(ti);
					text_temp.setText(t);
					text_pm.setText(p);
					text_humi.setText(h);
					text_lx.setText(l);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}
	};
	
	private void getDevInformation() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String ret = Webconn.getDev();
				System.out.println("now dev:");
				System.out.println(ret);
				handler.sendMessage(handler.obtainMessage(1, ret));
			}
		}).start();
	}

	private void getNowInformation() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String ret = Webconn.getNow(defaultDevId);
				System.out.print("now ret: ");
				System.out.println(ret);
				handler.sendMessage(handler.obtainMessage(2, ret));
			}
		}).start();
	}
	
	//delay 1min
	private class TimerProcess implements Runnable {
		public void run() {
			mHandler.postDelayed(this, 60000);
			getNowInformation();
		}
	}

}
