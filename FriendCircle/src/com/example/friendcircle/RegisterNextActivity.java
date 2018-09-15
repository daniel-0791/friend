package com.example.friendcircle;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.example.friendcircle.bean.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 设置密码界面
 * @author Administrator
 *
 */
public class RegisterNextActivity extends Activity {
    private EditText mPwdEt,mRePwdEt;
    private Button mSubmitBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhu_ce_next);
		//接收註冊頁面傳過來的電話號碼
		Intent intent=getIntent();
		String phone=intent.getStringExtra("phone");
		initView(phone);
	
	}
	private void initView(final String phone) {
		// TODO Auto-generated method stub
		mPwdEt = (EditText) findViewById(R.id.et_pwd);
		mRePwdEt = (EditText) findViewById(R.id.et_re_pwd);
		mSubmitBtn = (Button) findViewById(R.id.btn_submit);
		mSubmitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String pwd = mPwdEt.getText().toString().trim();
				String repwd = mRePwdEt.getText().toString().trim();
				//密码非空
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(RegisterNextActivity.this, "用户密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				//两次输入不一致
				if(!pwd.equals(repwd)){
					toast("两次密码输入不一致");
					mPwdEt.setText("");
					mRePwdEt.setText("");
					return;
				}
				// 注册
				signUp(phone, pwd);
				
			}


			
		});
		
	}
	private void toast(String mess) {
		Toast.makeText(RegisterNextActivity.this, mess, Toast.LENGTH_LONG).show();
	}
	/**
	 * 用户注册
	 * @param phone
	 * @param pwd
	 */
	private void signUp(final String phone, String pwd) {
		User user = new User();
		user.setUsername(phone);//用户名
		user.setPassword(pwd);//密码
		user.setNickname("用户"+phone);
		user.signUp(new SaveListener<User>() {

			@Override
			public void done(User arg0, BmobException ex) {
				if(ex==null){//注册成功
					toast("用户注册成功");
				}else{//注册失败
					toast("用户注册失败："+ex.toString());
				}
			}
		});
	}

}
