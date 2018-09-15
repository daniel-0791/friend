package com.sanlingyi.android.photo.lib.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanlingyi.android.photo.lib.R;
import com.sanlingyi.android.photo.lib.base.PhotoFilePathBase;
import com.sanlingyi.android.photo.lib.util.GetBitmapPoolUtil;
import com.sanlingyi.android.photo.lib.util.GetBitmapPoolUtil.Type;
/**
 * 文件夹名称及第一张图片展示
 * @author g
 *范晓锋
 */
public class PhotoSelectFilePathAdapter extends BaseAdapter {
	private Context mContext;
	private List<PhotoFilePathBase> mList;
	private List<String> SelectPhotoFile;
	private LayoutInflater inflater;
	private GetBitmapPoolUtil imageloader;
	
	public PhotoSelectFilePathAdapter(Context mContext,
			List<PhotoFilePathBase> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		init();
	}
	
	public void setPhotoFilePathList( List<PhotoFilePathBase> photoFileList){
		this.mList = photoFileList;
		notifyDataSetChanged();
	}
	
	public void setSelectImageFile( List<String> selectPhotoFile){
		this.SelectPhotoFile = selectPhotoFile;
		notifyDataSetChanged();
	}
	
	private void init(){
		inflater=LayoutInflater.from(mContext);
		imageloader=GetBitmapPoolUtil.getInstance(Type.LIFO);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		PhotoFilePathBase  base=mList.get(position);
		if(convertView==null){
			mHolder=new Holder();
			convertView=inflater.inflate(R.layout.photo_select_filepath_item, null);
			convertView.setTag(mHolder);
		}else {
			mHolder=(Holder) convertView.getTag();
		}
		mHolder.mImageView=(ImageView) convertView.findViewById(R.id.photo_image);
		mHolder.mTextView=(TextView) convertView.findViewById(R.id.photo_name_content);
		mHolder.mSelectFile=(ImageView) convertView.findViewById(R.id.photo_select_iamgeFile);
		
		String fileName = new File(base.getImagePath()).getParentFile().getName();
		mHolder.mTextView.setText(fileName+"("+base.getImageCount()+")");
		
		String fileNameString= new File(base.getImagePath()).getParentFile().toString();
		if(SelectPhotoFile!=null&&SelectPhotoFile.size()>0){
			if(SelectPhotoFile.contains(fileNameString)){
				mHolder.mSelectFile.setVisibility(View.VISIBLE);
			}else {
				mHolder.mSelectFile.setVisibility(View.GONE);
			}
		}else {
			mHolder.mSelectFile.setVisibility(View.GONE);
		}
		imageloader.imageLoader(base.getImagePath(), mHolder.mImageView);
		
		return convertView;
	}

	
	private class Holder{
		ImageView mImageView;
		TextView  mTextView;
		ImageView mSelectFile;
	}
	
}
