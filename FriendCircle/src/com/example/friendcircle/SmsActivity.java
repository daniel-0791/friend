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
 * ������֤����
 * @author Administrator
 *
 */
public class SmsActivity extends Activity implements OnClickListener {
	private EditText mPhoneEt, mCodeEt;
	private Button mGetCodeEt,mNextBtn;// ��ȡ�ֻ���֤��
	private int count = 60;// Ĭ��Ϊ60��
	

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
		// �ֻ�����֤
		if (StringUtils.isMobileNO(phone)) {// �ֻ�����֤ͨ��
			// �ֻ��Ų�Ϊ�գ����Ͷ��Ż�ȡ��֤��
			BmobSMS.requestSMSCode(this, phone, "mysms",
					new RequestSMSCodeListener() {

						@Override
						public void done(Integer smsId, BmobException ex) {
							if (ex == null) {// ��֤�뷢�ͳɹ�
								Log.i("smile", "����id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
								Toast.makeText(SmsActivity.this, "�����ѷ���",
										Toast.LENGTH_LONG).show();
								mGetCodeEt.setEnabled(false);// ���ɵ��
								// Android�Դ��ĵ���ʱ�ؼ�
								countDownTimer();
							}
						}

					});

		} else {
			Toast.makeText(this, "�ֻ��Ÿ�ʽ����ȷ������������", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * ����ʱ
	 */
	private void countDownTimer() {
		new CountDownTimer(60000, 1000) {
			// ����һ�룬ִ��һ��
			@Override
			public void onTick(long arg0) {
				// TODO Auto-generated method stub
				if (count != 0) {
					count--;
					mGetCodeEt.setText(count+"s");
				}

			}

			// ����ʱ���
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				mGetCodeEt.setEnabled(true);// ���ɵ��
				mGetCodeEt.setText("��ȡ��֤��");
			}

		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_code:// ��ȡ������֤��
			check();
			break;
		case R.id.btn_next://��һ��
			//��֤�ֻ���
			final String phone = mPhoneEt.getText().toString().trim();
			if (StringUtils.isMobileNO(phone)==false) {// �ֻ����鲻ͨ��
				Toast.makeText(this, "�ֻ��Ÿ�ʽ����ȷ������������", Toast.LENGTH_LONG).show();
				return;
			}
			String code = mCodeEt.getText().toString().trim();
			if(TextUtils.isEmpty(code)){
				Toast.makeText(this, "�ֻ���֤�벻��Ϊ��", Toast.LENGTH_LONG).show();
				return;
			}
			//��֤��֤��
			BmobSMS.verifySmsCode(this,phone, code, new VerifySMSCodeListener() {

			    @Override
			    public void done(BmobException ex) {
			        // TODO Auto-generated method stub
			        if(ex==null){//������֤������֤�ɹ�
			            Log.i("bmob", "��֤ͨ��");
			            Toast.makeText(SmsActivity.this, "�ֻ���֤����֤ͨ��", Toast.LENGTH_LONG).show();
			            //���������
//						Intent intent=new Intent(MainActivity.this, ZhuCeNextActivity.class);
//						intent.putExtra("phone", phone);
//						startActivity(intent);
			        }else{
			            Log.i("bmob", "��֤ʧ�ܣ�code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
			        }
			    }
			});
			//��ʱ������ڣ���ʽ����²�����������ת
			Intent intent=new Intent(SmsActivity.this, RegisterNextActivity.class);
			intent.putExtra("phone", phone);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
