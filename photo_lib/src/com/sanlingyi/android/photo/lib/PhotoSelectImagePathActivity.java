package com.sanlingyi.android.photo.lib;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanlingyi.android.photo.lib.adapter.PhotoSelcetImagePathAdapter;
import com.sanlingyi.android.photo.lib.global.PhotoGlobalVariable;
import com.sanlingyi.android.photo.lib.util.GetFileImagePathUtil;
import com.sanlingyi.android.photo.lib.util.ToastUtil;

/**
 * 选择图片
 * @author g
 *
 */
public class PhotoSelectImagePathActivity extends Activity{
	private GridView mGridView;
	private String  mFilePath;
	private List<String> mList;
	private PhotoSelcetImagePathAdapter adapter;
	
	private List<String> SelectImagePathList;
	private List<String> upSelectFilePathList;
	
	private int  selectImageCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_select_imagepath_activity);
		Bundle bundles=this.getIntent().getExtras();
		if(bundles!=null){
			mFilePath=bundles.getString("seclite_image_File");
			selectImageCount=bundles.getInt("imageCount");
		}
		
		init();
	}

	private void init(){
		mList=new ArrayList<String>();
		LinearLayout titlelayout=(LinearLayout) findViewById(R.id.photo_select_imagepath_title);
		RelativeLayout back=(RelativeLayout) titlelayout.findViewById(R.id.layout_return);
		TextView titleName=(TextView) titlelayout.findViewById(R.id.tv_title_txt);
		titleName.setText(R.string.photo_image);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getSelectImagePaht();
				PhotoSelectImagePathActivity.this.finish();
			}
		});
		
		mGridView=(GridView) this.findViewById(R.id.photo_select_imagepath_grid);
		if(mFilePath!=null){
			mList=new GetFileImagePathUtil().getImagePath(mFilePath);
			if(mList!=null&&mList.size()>0){
				adapter=new PhotoSelcetImagePathAdapter(this,mList,mFilePath,selectImageCount);
				mGridView.setAdapter(adapter);
			}
		}
	}
	
	
	/**
	 * 返回到相册文件夹选择activity
	 */
	private void getSelectImagePaht(){
		if(adapter!=null){
			upSelectFilePathList=adapter.getSelectFilePathList();
			SelectImagePathList=adapter.getSelectImagePathList();
		}
		Intent intent= getIntent();
		Bundle  bundle=new Bundle();
		bundle.putSerializable("SelectFilePath", (Serializable) upSelectFilePathList);
		bundle.putSerializable("SelectImagePath", (Serializable) SelectImagePathList);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		PhotoSelectImagePathActivity.this.finish();
	}
	
	/**
	 * 确定后清除选择数据(备用)
	 */
	private void gotoClearSelectImage(){
		if(adapter!=null){
			SelectImagePathList=adapter.getSelectImagePathList();
		}
		if(SelectImagePathList.size()>0){
			//图片返回到需要的activity
			
			PhotoGlobalVariable.mList.clear();
			PhotoGlobalVariable.mSelectState.clear();
			PhotoGlobalVariable.selectFile.clear();
			PhotoGlobalVariable.SELECT_IMAGE_NUMBER=0;
			PhotoSelectImagePathActivity.this.finish();
		}else {
			ToastUtil.showNewToast(this, "你还没选择图片");
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			getSelectImagePaht();
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
