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
 * ����Ȧ������
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
		// ��ѯ����Ȧ��Ϣ
		getData();
	}

	/**
	 * �ӷ�������ȡ������Ϣ
	 */
	private void getData() {
		BmobQuery<Post> query = new BmobQuery<Post>();
		query.order("-updatedAt");
		query.include("author");// ϣ���ڲ�ѯ������Ϣ��ͬʱҲ�ѷ����˵���Ϣ��ѯ����
		query.findObjects(new FindListener<Post>() {

			@Override
			public void done(List<Post> post, BmobException e) {
				if (e == null) {
					Log.v("fpf", "�ɹ�"+post.toString());
					adapter.setBeans(post);
					adapter.notifyDataSetChanged();
					Toast("��ѯ�ɹ�");
				} else {
					Log.i("fpf", "ʧ�ܣ�" + e.getMessage());
					Toast("��ѯʧ�ܣ�" + e.getMessage());
				}
			}

		
		});
	}

	/**
	 * ��ת���������ӽ���
	 */
	public void add(View v) {
		Intent intent = new Intent(this, AddPostActivity.class);
		startActivity(intent);
	}
	/**
	 * ��ʾ��
	 * @param string
	 */
	private void Toast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_LONG).show();
	}
	/**
	 * ����ʵʱ���¼���
	 */
	private void bmobRealTimeData(){
		rtd = new BmobRealTimeData();
		rtd.start(new ValueEventListener() {
		    @Override
		    public void onDataChange(JSONObject data) {
		        Log.d("fpf", "("+data.optString("action")+")"+"���ݣ�"+data);
		     // ��ѯ����Ȧ��Ϣ
				getData();
		    }

		    @Override
		    public void onConnectCompleted(Exception ex) {
		        Log.d("fpf", "���ӳɹ�:"+rtd.isConnected());
				if(rtd.isConnected()){
				    // ���������
				    rtd.subTableUpdate("Post");
				}
		    }
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ȡ�����������
		rtd.unsubTableUpdate("Post");
	}

}
