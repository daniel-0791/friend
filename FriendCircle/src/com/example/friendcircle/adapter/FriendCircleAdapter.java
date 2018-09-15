package com.example.friendcircle.adapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import com.example.friendcircle.CommentActivity;
import com.example.friendcircle.R;
import com.example.friendcircle.bean.AddPostImageBean;
import com.example.friendcircle.bean.Comment;
import com.example.friendcircle.bean.Post;
import com.example.friendcircle.view.MyGridView;
import com.example.friendcircle.view.MyListView;
import com.sanlingyi.android.photo.lib.PhotoSelectFilePathActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ����Ȧ����������
 * 
 * @author Administrator
 * 
 */
public class FriendCircleAdapter extends BaseAdapter {
	private List<Post> beans;
	private LayoutInflater inflater;
	private Activity mContext;

	public void setBeans(List<Post> beans) {
		this.beans = beans;
	}

	public FriendCircleAdapter(Activity context) {
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans != null ? beans.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		ViewHolder holder = null;
		FriendCircleImageAdapter adapter = null;
		CommentInfoAdapter adapter2 = null;
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.friend_circle_item, null);
			holder.ivPic = (ImageView) v.findViewById(R.id.iv_pic);
			holder.tv_username = (TextView) v.findViewById(R.id.tv_username);
			holder.tv_date = (TextView) v.findViewById(R.id.tv_date);
			holder.tv_content = (TextView) v.findViewById(R.id.tv_content);
			holder.gv_img_list = (MyGridView) v.findViewById(R.id.gv_img_list);
			holder.btn_pinglun = (Button) v.findViewById(R.id.btn_pinglun);
			holder.btn_dianzan = (Button) v.findViewById(R.id.btn_dianzan);
			holder.lv_comment_list = (MyListView) v
					.findViewById(R.id.lv_comment_list);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		if (adapter == null) {
			adapter = new FriendCircleImageAdapter(mContext);
			holder.gv_img_list.setAdapter(adapter);
		}
		if(adapter2==null){
			adapter2 = new CommentInfoAdapter(mContext);
			holder.lv_comment_list.setAdapter(adapter2);
		}
		holder.btn_pinglun.setOnClickListener(new MyOnClick(holder, position));
		holder.btn_dianzan.setOnClickListener(new MyOnClick(holder, position));
		holder.tv_username.setText(beans.get(position).getAuthor()
				.getUsername());
		holder.tv_date.setText(beans.get(position).getCreatedAt());
		holder.tv_content.setText(beans.get(position).getContent());
		List<BmobFile> bmobFiles = beans.get(position).getBmobFiles();
		// ��ȡ�������б��������ͼƬ
		if (adapter != null) {
			adapter.setBeans(bmobFiles);
			adapter.notifyDataSetChanged();
			if (bmobFiles != null) {
				Log.v("fpff", "-" + position + "-fileFile:"
						+ bmobFiles.get(0).getFileUrl());
			}

		}
		//��������ID��ȡ������Ϣ
		getCommentList(beans.get(position),adapter2);
		return v;
	}
    /**
     * ��ȡ������Ϣ
     * @param post
     */
	private void getCommentList(Post post,final CommentInfoAdapter adapter) {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		//��������ID����ѯ����
		Post post2 = new Post();
		post2.setObjectId(post.getObjectId());
		query.addWhereEqualTo("post", new BmobPointer(post2));
		query.include("user,post.author");//���������û���Ҳ���в�ѯ
		//query.include("post");//�����������ӱ��ѯ����
		query.order("-createdAt");//��ʱ��ݼ�
		query.findObjects(new FindListener<Comment>() {
			
			@Override
			public void done(List<Comment> comments, BmobException arg1) {
				// TODO Auto-generated method stub
				if(comments!=null){
					Log.v("fpfff", comments.toString());
					adapter.setBeans(comments);
					adapter.notifyDataSetChanged();
				}
				
			}
		});
		
	}

	class ViewHolder {
		ImageView ivPic;// �����û�ͷ��
		TextView tv_username, tv_date, tv_content;// �û���������ʱ�䡢��������
		MyGridView gv_img_list;// ������ʾ����ȦͼƬ
		MyListView lv_comment_list;// ��ʾ������Ϣ
		Button btn_pinglun, btn_dianzan;// ���ۣ�����
	}

	/**
	 * ��ť�ĵ���¼�
	 * 
	 * @author Administrator
	 * 
	 */
	class MyOnClick implements OnClickListener {
		private ViewHolder holder;
		private int mPosition = 0;

		public MyOnClick(ViewHolder holder, int position) {
			this.holder = holder;
			mPosition = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_pinglun:// ����
				Toast.makeText(mContext, "����", Toast.LENGTH_SHORT).show();
				// ��ת�����۽���
				Intent intent = new Intent(mContext, CommentActivity.class);
				// �õ���ǰ���ӣ��û��Ѿ�������
				Post post = beans.get(mPosition);
				intent.putExtra("post", post);
				mContext.startActivity(intent);
				break;
			case R.id.btn_dianzan:// ����
				Toast.makeText(mContext, "����", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	}

}
