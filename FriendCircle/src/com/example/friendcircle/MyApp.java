package com.example.friendcircle;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import android.app.Application;

public class MyApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		 //第一：默认初始化
        Bmob.initialize(this, "31bc1e6e4cc9cd004d8f33117586ecc2");
        BmobSMS.initialize(this,"31bc1e6e4cc9cd004d8f33117586ecc2");
	}
}
