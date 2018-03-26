package com.zhangtory.hj;

import com.zhangtory.hj.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	private TabHost tabHost;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        
        intent=new Intent().setClass(this,NowData.class);
        spec=tabHost.newTabSpec("1").setIndicator("1").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, RecentData.class);
        spec=tabHost.newTabSpec("2").setIndicator("2").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, SettingActivity.class);
        spec=tabHost.newTabSpec("3").setIndicator("3").setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);
        
        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.main_tab_myExam:
					tabHost.setCurrentTabByTag("1");
					break;
				case R.id.main_tab_message:
					tabHost.setCurrentTabByTag("2");
					break;
				case R.id.main_tab_settings:
					tabHost.setCurrentTabByTag("3");
					break;
				default:
					break;
				}
			}
		});
    }
   
}