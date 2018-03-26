import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {
	private EditText editTempMin;
	private EditText editTempMax;
	private EditText editPmMax;
	private Button btnOk;
	private int defaultDevId = 1;
	private Handler mHandler;
	private ArrayAdapter<String> arrAdp;
	private Spinner selectLocation;
	private int tempMax;
	private int tempMin;
	private int pmMax;
	private int setFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		editTempMax = (EditText) findViewById(R.id.edit_temp_max);
		editTempMin = (EditText) findViewById(R.id.edit_temp_min);
		editPmMax = (EditText) findViewById(R.id.edit_pm_max);
		btnOk = (Button) findViewById(R.id.set_ok);
		mHandler = new Handler();
		mHandler.post(new TimerProcess());
		selectLocation = (Spinner) this.findViewById(R.id.spinner3);
		
		getDevInformation();

		btnOk.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				String tmax;
				String tmin;
				String pmax;
				if(editTempMax.getText().toString().isEmpty()){
					tmax="999";
				}else{
					tmax=editTempMax.getText().toString();
				}
				if(editTempMin.getText().toString().isEmpty()){
					tmin="0";
				}else{
					tmin=editTempMin.getText().toString();
				}
				if(editPmMax.getText().toString().isEmpty()){
					pmax="999";
				}else{
					pmax=editPmMax.getText().toString();
				}
//				System.out.println(tmax+" "+tmin+" "+pmax);
				tempMax = Integer.valueOf(tmax);
				tempMin = Integer.valueOf(tmin);
				pmMax = Integer.valueOf(pmax);
				setFlag = 1;
//				System.out.println(tempMax+" "+tempMin+" "+pmMax);
				Toast.makeText(SettingActivity.this, "设置成功！",
						Toast.LENGTH_LONG).show();
			}
		});
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
					arrAdp = new ArrayAdapter<String>(SettingActivity.this,
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
				if (setFlag == 1) {
					try {
						JSONArray jsonArr = new JSONArray(ret);
						JSONObject item = jsonArr.getJSONObject(0);
						int t = item.getInt("temp");
						int p = item.getInt("pm");
						// System.out.println(t+" "+p);
//						System.out.println(tempMax + " " + tempMin + " "
//								+ pmMax);
						if(t>=tempMax){
							System.out.println("temp Max");
							String ns = Context.NOTIFICATION_SERVICE;
							NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
							int icon = R.drawable.ic_launcher;
							CharSequence tickerText = "温度报警";
							long when = System.currentTimeMillis();
//							Notification notification = new Notification(icon, tickerText,
//									when);
//					        Notification.Builder builder = new Notification.Builder(getBaseContext());
//				            Notification notification = builder.getNotification();
							Context context = getApplicationContext();
							CharSequence contentTitle = "温度报警";
							CharSequence contentText = "温度超过预定值！请注意！";
							Intent notificationIntent = new Intent(SettingActivity.this, MainActivity.class);
							PendingIntent contentIntent = PendingIntent.getActivity(
									SettingActivity.this, 0, notificationIntent, 0);
							Notification notification = new Notification.Builder(context) 
							 .setContentTitle(contentTitle) 
							 .setContentText(contentText) 
							 .setContentIntent(contentIntent)
							 .build();
//							notification.setLatestEventInfo(context, contentTitle,
//									contentText, contentIntent);
							mNotificationManager.notify(1, notification);
						}
						if(t<=tempMin){
							System.out.println("temp min");
							String ns = Context.NOTIFICATION_SERVICE;
							NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
							int icon = R.drawable.ic_launcher;
							CharSequence tickerText = "温度报警";
							long when = System.currentTimeMillis();
//							Notification notification = new Notification(icon, tickerText,
//									when);
							Context context = getApplicationContext();
							CharSequence contentTitle = "温度报警";
							CharSequence contentText = "温度小于预定值！请注意！";
							Intent notificationIntent = new Intent(SettingActivity.this, MainActivity.class);
							PendingIntent contentIntent = PendingIntent.getActivity(
									SettingActivity.this, 0, notificationIntent, 0);
							Notification notification = new Notification.Builder(context) 
							 .setContentTitle(contentTitle) 
							 .setContentText(contentText) 
							 .setContentIntent(contentIntent)
							 .build();
//							notification.setLatestEventInfo(context, contentTitle,
//									contentText, contentIntent);
							mNotificationManager.notify(2, notification);
						}
						if(p>=pmMax){
							System.out.println("pm max");
							String ns = Context.NOTIFICATION_SERVICE;
							NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
							int icon = R.drawable.ic_launcher;
							CharSequence tickerText = "PM2.5浓度报警";
							long when = System.currentTimeMillis();
//							Notification notification = new Notification(icon, tickerText,
//									when);
							Context context = getApplicationContext();
							CharSequence contentTitle = "PM2.5浓度报警";
							CharSequence contentText = "PM2.5浓度超过预定值！请注意！";
							Intent notificationIntent = new Intent(SettingActivity.this, MainActivity.class);
							PendingIntent contentIntent = PendingIntent.getActivity(
									SettingActivity.this, 0, notificationIntent, 0);
							Notification notification = new Notification.Builder(context) 
							 .setContentTitle(contentTitle) 
							 .setContentText(contentText) 
							 .setContentIntent(contentIntent)
							 .build();
//							notification.setLatestEventInfo(context, contentTitle,
//									contentText, contentIntent);
							mNotificationManager.notify(3, notification);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
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
				System.out.println("set dev:");
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
				System.out.print("set ret: ");
				System.out.println(ret);
				handler.sendMessage(handler.obtainMessage(2, ret));
			}
		}).start();
	}

	// delay 1min
	private class TimerProcess implements Runnable {
		public void run() {
			mHandler.postDelayed(this, 60000);
			getNowInformation();
		}
	}

}
