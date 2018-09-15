package com.example.friendcircle;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.QueryListener;

import com.example.friendcircle.utils.StringUtils;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 短信验证界面
 * @author Administrator
 *
 */
public class SmsActivity extends Activity implements OnClickListener {
	private EditText mPhoneEt, mCodeEt;
	private Button mGetCodeEt,mNextBtn;// 获取手机验证码
	private int count = 60;// 默认为60秒
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPhoneEt = (EditText) findViewById(R.id.et_phone);
		mCodeEt = (EditText) findViewById(R.id.et_code);
		mGetCodeEt = (Button) findViewById(R.id.btn_get_code);
		mGetCodeEt.setOnClickListener(this);
		mNextBtn = (Button) findViewById(R.id.btn_next);
		mNextBtn.setOnClickListener(this);

	}

	private void check() {
		String phone = mPhoneEt.getText().toString().trim();
		// 手机号验证
		if (StringUtils.isMobileNO(phone)) {// 手机号验证通过
			// 手机号不为空，发送短信获取验证码
			BmobSMS.requestSMSCode(this, phone, "mysms",
					new RequestSMSCodeListener() {

						@Override
						public void done(Integer smsId, BmobException ex) {
							if (ex == null) {// 验证码发送成功
								Log.i("smile", "短信id：" + smsId);// 用于查询本次短信发送详情
								Toast.makeText(SmsActivity.this, "短信已发送",
										Toast.LENGTH_LONG).show();
								mGetCodeEt.setEnabled(false);// 不可点击
								// Android自带的倒计时控件
								countDownTimer();
							}
						}

					});

		} else {
			Toast.makeText(this, "手机号格式不正确，请重新输入", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 倒计时
	 */
	private void countDownTimer() {
		new CountDownTimer(60000, 1000) {
			// 倒计一秒，执行一次
			@Override
			public void onTick(long arg0) {
				// TODO Auto-generated method stub
				if (count != 0) {
					count--;
					mGetCodeEt.setText(count+"s");
				}

			}

			// 倒计时完成
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				mGetCodeEt.setEnabled(true);// 不可点击
				mGetCodeEt.setText("获取验证码");
			}

		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_code:// 获取短信验证码
			check();
			break;
		case R.id.btn_next://下一步
			//验证手机号
			final String phone = mPhoneEt.getText().toString().trim();
			if (StringUtils.isMobileNO(phone)==false) {// 手机号验不通过
				Toast.makeText(this, "手机号格式不正确，请重新输入", Toast.LENGTH_LONG).show();
				return;
			}
			String code = mCodeEt.getText().toString().trim();
			if(TextUtils.isEmpty(code)){
				Toast.makeText(this, "手机验证码不能为空", Toast.LENGTH_LONG).show();
				return;
			}
			//验证验证码
			BmobSMS.verifySmsCode(this,phone, code, new VerifySMSCodeListener() {

			    @Override
			    public void done(BmobException ex) {
			        // TODO Auto-generated method stub
			        if(ex==null){//短信验证码已验证成功
			            Log.i("bmob", "验证通过");
			            Toast.makeText(SmsActivity.this, "手机验证码验证通过", Toast.LENGTH_LONG).show();
			            //跳到新頁面
//						Intent intent=new Intent(MainActivity.this, ZhuCeNextActivity.class);
//						intent.putExtra("phone", phone);
//						startActivity(intent);
			        }else{
			            Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
			        }
			    }
			});
			//临时测试入口，正式情况下不能再这里跳转
			Intent intent=new Intent(SmsActivity.this, RegisterNextActivity.class);
			intent.putExtra("phone", phone);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
