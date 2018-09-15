package com.example.friendcircle.adapter;

import java.util.List;

import com.example.friendcircle.R;
import com.example.friendcircle.bean.AddPostImageBean;
import com.sanlingyi.android.photo.lib.PhotoSelectFilePathActivity;

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
 * 发布帖子，图片管理适配器
 * @author Administrator
 *
 */
public class AddPostImageAdapter extends BaseAdapter{
    private List<AddPostImageBean> beans;
    private LayoutInflater inflater;
    private Activity mContext;
	public void setBeans(List<AddPostImageBean> beans) {
		this.beans = beans;
	}
    public AddPostImageAdapter(Activity context){
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
			v = inflater.inflate(R.layout.activity_add_post_item, null);
			holder.btnImageAdd = (Button) v.findViewById(R.id.iv_add_img);
			holder.ivImageShow = (ImageView) v.findViewById(R.id.iv_show_img);
			v.setTag(holder);
		}else{
			holder = (ViewHolder) v.getTag();
		}
		//处理ui的展示
		if(beans.get(position).getState()==0){//添加图片
			holder.btnImageAdd.setVisibility(View.VISIBLE);
			holder.ivImageShow.setVisibility(View.GONE);
			holder.btnImageAdd.setOnClickListener(new MyOnClick(holder));
		}else{//显示图片
			holder.btnImageAdd.setVisibility(View.GONE);
			holder.ivImageShow.setVisibility(View.VISIBLE);
			//使用原生的方法加载本地图片
			Bitmap bitmap = BitmapFactory.decodeFile(beans.get(position).getImgPic());
			holder.ivImageShow.setImageBitmap(bitmap);
		}
		return v;
	}
	class ViewHolder{
		Button btnImageAdd;//可点击的按钮
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
