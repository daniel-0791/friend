package com.example.friendcircle;

import cn.bmob.v3.BmobUser;

import com.example.friendcircle.bean.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 设置界面
 * @author Administrator
 *
 */
public class SettingActivity extends Activity implements OnClickListener{
        private TextView mnameTV;
        private EditText mSignatureET;
        private Button mxiugaiBT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mnameTV=(TextView) findViewById(R.id.tv_name);
		mSignatureET=(EditText) findViewById(R.id.et_gexingqian);
          
		mxiugaiBT=(Button) findViewById(R.id.btn_xiugai);
		mxiugaiBT.setOnClickListener(this);
 	
		 BmobUser.getCurrentUser(User.class);//获取自定义用户信息
//		 User user = BmobUser.getCurrentUser(User.class); 
//		  user.setNickname("啊啊");
//		  user.setSignature("ssss");
//		 mnameTV.setText(user.getNickname());
//		 mSignatureET.setText(user.getSignature());
	}
    /**
     * 跳转到登录
     * @param v
     */
	public void login(View v){
		Intent intent = new Intent(this,LoginActivity.class);
		startActivity(intent);
	}
	 /**
     * 跳转到注册
     * @param v
     */
	public void register(View v){
		Intent intent = new Intent();
		intent.setClass(this, SmsActivity.class);
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_xiugai:
			     
			
			break;

		default:
			break;
		}
	}
}
