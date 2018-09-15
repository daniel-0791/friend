package com.example.friendcircle;

import com.example.friendcircle.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * ��¼����
 * @author Administrator
 *
 */
public class LoginActivity extends Activity implements OnClickListener {
	private Button mLoginBtn;
	private EditText mUnameEt, mPwdEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mLoginBtn = (Button) findViewById(R.id.btn_ulogin);
		mLoginBtn.setOnClickListener(this);
		mPwdEt = (EditText) findViewById(R.id.et_upwd);
		mUnameEt = (EditText) findViewById(R.id.et_uname);
	}
    

	public void query() {
		final String name = mUnameEt.getText().toString().trim();
		String pwd = mPwdEt.getText().toString().trim();

		User bu2 = new User();
		
		bu2.setUsername(name);
		bu2.setPassword(pwd);
		Log.v("fpf",name+"-"+pwd);
		bu2.login(new SaveListener<User>() {

			@Override
			public void done(User User, BmobException e) {
				if (e == null) {
					Toast.makeText(LoginActivity.this, "��½�ɹ�",
							Toast.LENGTH_LONG).show();
					// ͨ��BmobUser user = BmobUser.getCurrentUser()��ȡ��¼�ɹ���ı����û���Ϣ
					// ������Զ����û�����MyUser����ͨ��MyUser user =
					// BmobUser.getCurrentUser(MyUser.class)��ȡ�Զ����û���Ϣ
					// User user = BmobUser.getCurrentUser(User.class);
					 
					Intent intent = new Intent(LoginActivity.this,
							FriendActivity.class);
				 
					 
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "��½ʧ��",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ulogin:
			if (TextUtils.isEmpty(mUnameEt.toString())) {// �û�������Ϊ��
				Toast.makeText(this, "�û�������Ϊ��", Toast.LENGTH_SHORT).show();
			} else if (TextUtils.isEmpty(mPwdEt.toString())) {// ���벻��Ϊ��
				Toast.makeText(this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
			}
			query();

			break;

		default:
			break;
		}

	}

}
