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
 * �����������
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
		//�����]�������^����Ԓ̖�a
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
				//����ǿ�
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(RegisterNextActivity.this, "�û����벻��Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				//�������벻һ��
				if(!pwd.equals(repwd)){
					toast("�����������벻һ��");
					mPwdEt.setText("");
					mRePwdEt.setText("");
					return;
				}
				// ע��
				signUp(phone, pwd);
				
			}


			
		});
		
	}
	private void toast(String mess) {
		Toast.makeText(RegisterNextActivity.this, mess, Toast.LENGTH_LONG).show();
	}
	/**
	 * �û�ע��
	 * @param phone
	 * @param pwd
	 */
	private void signUp(final String phone, String pwd) {
		User user = new User();
		user.setUsername(phone);//�û���
		user.setPassword(pwd);//����
		user.setNickname("�û�"+phone);
		user.signUp(new SaveListener<User>() {

			@Override
			public void done(User arg0, BmobException ex) {
				if(ex==null){//ע��ɹ�
					toast("�û�ע��ɹ�");
				}else{//ע��ʧ��
					toast("�û�ע��ʧ�ܣ�"+ex.toString());
				}
			}
		});
	}

}
