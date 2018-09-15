package com.example.friendcircle.adapter;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

import com.example.friendcircle.R;
import com.example.friendcircle.bean.AddPostImageBean;
import com.example.friendcircle.bean.Comment;
import com.sanlingyi.android.photo.lib.PhotoSelectFilePathActivity;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 评论信息适配器
 * @author Administrator
 *
 */
public class CommentInfoAdapter extends BaseAdapter{
    private List<Comment> beans;
    private LayoutInflater inflater;
    private Activity mContext;
	public void setBeans(List<Comment> beans) {
		this.beans = beans;
	}
    public CommentInfoAdapter(Activity context){
    	mContext = context;
    	inflater = LayoutInflater.from(context);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans!=null?beans.size():0;
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
		if(v==null){
			holder =new ViewHolder();
			v = inflater.inflate(R.layout.comment_info_item, null);
			holder.tv_comment = (TextView) v.findViewById(R.id.tv_comment);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		try {
			holder.tv_comment.setText(
					Html.fromHtml("<font color='red'>"+beans.get(position).getUser().getUsername()+"</font>"+
							"评论"+
							"<font color='blue'>"+beans.get(position).getPost().getAuthor().getUsername()+"</font>"
							+"："+beans.get(position).getContent()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return v;
	}
	class ViewHolder{
		TextView tv_comment;
	}
	/**
	 * 按钮的点击事件
	 * @author Administrator
	 *
	 */
    class MyOnClick implements OnClickListener{
    	private ViewHolder holder;
        public MyOnClick(ViewHolder holder){
        	this.holder = holder;
        }
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_add_img://访问相册
				Intent intent = new Intent(mContext, PhotoSelectFilePathActivity.class);
				intent.putExtra("imageCount", 9);//选择的图片数量
				mContext.startActivityForResult(intent, 0);
				break;

			default:
				break;
			}
		}
    	
    }
    
}
