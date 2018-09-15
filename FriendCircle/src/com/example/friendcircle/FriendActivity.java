package com.example.friendcircle;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ValueEventListener;

import com.example.friendcircle.adapter.FriendCircleAdapter;
import com.example.friendcircle.bean.Post;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 朋友圈主界面
 * 
 * @author Administrator
 * 
 */
public class FriendActivity extends Activity {
	private ListView mDataLv;
	//private List<Post> beans = new ArrayList<Post>();
    private FriendCircleAdapter adapter;
    private BmobRealTimeData rtd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend);
		mDataLv = (ListView) findViewById(R.id.lv_data);
		adapter = new FriendCircleAdapter(this);
		mDataLv.setAdapter(adapter);
		bmobRealTimeData();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 查询朋友圈信息
		getData();
	}

	/**
	 * 从服务器获取帖子信息
	 */
	private void getData() {
		BmobQuery<Post> query = new BmobQuery<Post>();
		query.order("-updatedAt");
		query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
		query.findObjects(new FindListener<Post>() {

			@Override
			public void done(List<Post> post, BmobException e) {
				if (e == null) {
					Log.v("fpf", "成功"+post.toString());
					adapter.setBeans(post);
					adapter.notifyDataSetChanged();
					Toast("查询成功");
				} else {
					Log.i("fpf", "失败：" + e.getMessage());
					Toast("查询失败：" + e.getMessage());
				}
			}

		
		});
	}

	/**
	 * 跳转到发布帖子界面
	 */
	public void add(View v) {
		Intent intent = new Intent(this, AddPostActivity.class);
		startActivity(intent);
	}
	/**
	 * 提示框
	 * @param string
	 */
	private void Toast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_LONG).show();
	}
	/**
	 * 数据实时更新监听
	 */
	private void bmobRealTimeData(){
		rtd = new BmobRealTimeData();
		rtd.start(new ValueEventListener() {
		    @Override
		    public void onDataChange(JSONObject data) {
		        Log.d("fpf", "("+data.optString("action")+")"+"数据："+data);
		     // 查询朋友圈信息
				getData();
		    }

		    @Override
		    public void onConnectCompleted(Exception ex) {
		        Log.d("fpf", "连接成功:"+rtd.isConnected());
				if(rtd.isConnected()){
				    // 监听表更新
				    rtd.subTableUpdate("Post");
				}
		    }
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 取消监听表更新
		rtd.unsubTableUpdate("Post");
	}

}
