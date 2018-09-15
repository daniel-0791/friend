package com.example.friendcircle;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.example.friendcircle.bean.Comment;
import com.example.friendcircle.bean.Post;
import com.example.friendcircle.bean.User;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/**
 * ���۽���
 * @author Administrator
 *
 */
public class CommentActivity extends Activity {
    private EditText mCommentEt;//��������
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		mCommentEt = (EditText) findViewById(R.id.et_comment);
	}
	/**
	 * ���ۼ���
	 * @param v
	 */
    public void comment(View v){
    	String commentStr = mCommentEt.getText().toString().trim();
    	if(TextUtils.isEmpty(commentStr)){//�ǿ���֤
    		Toast.makeText(this, "�������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//�õ�������
    	User user = User.getCurrentUser(User.class);
    	//�õ������۵�����
    	Post post = (Post) getIntent().getSerializableExtra("post");
    	
    	//��������Ϣ�ύ��������
    	Comment comment = new Comment();
    	comment.setContent(commentStr);
    	comment.setPost(post);
    	comment.setUser(user);
    	comment.save(new SaveListener<String>() {
			
			@Override
			public void done(String objectId, BmobException ex) {
				if(ex==null){//���۳ɹ�
					finish();
					Toast.makeText(CommentActivity.this, "���۳ɹ�", Toast.LENGTH_SHORT).show();
				}else{//����ʧ��
					Toast.makeText(CommentActivity.this, "����ʧ�ܣ�"+ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
    }
	

}
