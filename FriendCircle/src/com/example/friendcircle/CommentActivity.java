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
 * 评论界面
 * @author Administrator
 *
 */
public class CommentActivity extends Activity {
    private EditText mCommentEt;//评论内容
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		mCommentEt = (EditText) findViewById(R.id.et_comment);
	}
	/**
	 * 评论监听
	 * @param v
	 */
    public void comment(View v){
    	String commentStr = mCommentEt.getText().toString().trim();
    	if(TextUtils.isEmpty(commentStr)){//非空验证
    		Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//拿到评论人
    	User user = User.getCurrentUser(User.class);
    	//拿到被评论的帖子
    	Post post = (Post) getIntent().getSerializableExtra("post");
    	
    	//将评论信息提交到服务器
    	Comment comment = new Comment();
    	comment.setContent(commentStr);
    	comment.setPost(post);
    	comment.setUser(user);
    	comment.save(new SaveListener<String>() {
			
			@Override
			public void done(String objectId, BmobException ex) {
				if(ex==null){//评论成功
					finish();
					Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
				}else{//评论失败
					Toast.makeText(CommentActivity.this, "评论失败："+ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
    }
	

}
