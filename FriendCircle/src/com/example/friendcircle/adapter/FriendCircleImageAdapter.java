package com.example.friendcircle.adapter;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

import com.example.friendcircle.R;
import com.example.friendcircle.bean.AddPostImageBean;
import com.sanlingyi.android.photo.lib.PhotoSelectFilePathActivity;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
/**
 * 朋友圈图片展示适配器
 * @author Administrator
 *
 */
public class FriendCircleImageAdapter extends BaseAdapter{
    private List<BmobFile> beans;
    private LayoutInflater inflater;
    private Activity mContext;
	public void setBeans(List<BmobFile> beans) {
		this.beans = beans;
	}
    public FriendCircleImageAdapter(Activity context){
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
			v = inflater.inflate(R.layout.friend_circle_img_item, null);
			holder.ivImageShow = (ImageView) v.findViewById(R.id.iv_show_img);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		//获取文件地址
		String imgpic = beans.get(position).getFileUrl();
		holder.ivImageShow.setTag(imgpic);//先缓存
		if(holder.ivImageShow.getTag().equals(imgpic)){//防止图片显示错乱
			//利用picasso框架访问网络图片
			Picasso.with(mContext)
					.load(imgpic).into(holder.ivImageShow);
		}
		
		return v;
	}
	class ViewHolder{
		ImageView ivImageShow;//可显示的图片
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
