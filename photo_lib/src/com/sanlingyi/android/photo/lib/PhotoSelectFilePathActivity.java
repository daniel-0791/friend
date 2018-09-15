package com.sanlingyi.android.photo.lib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanlingyi.android.photo.lib.adapter.PhotoSelectFilePathAdapter;
import com.sanlingyi.android.photo.lib.base.PhotoFilePathBase;
import com.sanlingyi.android.photo.lib.global.PhotoGlobalVariable;
import com.sanlingyi.android.photo.lib.util.GetPhotoFilePathUtil;
import com.sanlingyi.android.photo.lib.util.ToastUtil;

/**
 * 选择图片文件夹
 * @author g
 *
 */
public class PhotoSelectFilePathActivity  extends Activity{
	private Button mConfirmButton;
	private ListView mListView;
	private List<PhotoFilePathBase> mList;
	private Handler mHandler;
	private PhotoSelectFilePathAdapter adapter;
	
	private final int REQUEST_CODE=0;
	private int imageCount = 0;
	/**
	 * 存放选中的图片
	 */
	private List<String> SelectImagePathList;
	/**
	 * 存放有选中图片的文件夹
	 */
	private List<String> SelectFilePathList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_select_filepath_activity);
		Intent bundles=this.getIntent();
		if(bundles!=null){
			imageCount=bundles.getIntExtra("imageCount", 0);
		}
		if(imageCount < 0) {
			imageCount = 0;
		}
		
		if(mList!=null){
			mList.clear();
		}
		
		mHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					mList=(List<PhotoFilePathBase>) msg.obj;
					if(mList!=null&&mList.size()>0){
						adapter.setPhotoFilePathList(mList);
					}
					break;
				default:
					break;
				}
			}
		};
		
		init();
	}

	public void init(){
		mListView=(ListView) findViewById(R.id.photofile_select_listview);
		LinearLayout titlelayout=(LinearLayout) findViewById(R.id.photo_select_filepath_title);
		RelativeLayout back=(RelativeLayout) titlelayout.findViewById(R.id.layout_return);
		TextView titleName=(TextView) titlelayout.findViewById(R.id.tv_title_txt);
		titleName.setText(R.string.photo_album);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoSelectFilePathActivity.this.finish();
			}
		});
		mConfirmButton = (Button) titlelayout.findViewById(R.id.btn_right1);
		mConfirmButton.setVisibility(View.VISIBLE);
		mConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBaceSelectData();
			}
		});
		
		mList = new ArrayList<PhotoFilePathBase>();
		adapter=new PhotoSelectFilePathAdapter(PhotoSelectFilePathActivity.this, mList);
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(new mOnItemClickListener());
		new Thread(){
			@Override
			public void run() {
				mList=(List<PhotoFilePathBase>) new GetPhotoFilePathUtil(PhotoSelectFilePathActivity.this).getFilePath();
				Message msg=Message.obtain();
				msg.what=0;
				msg.obj=mList;
				mHandler.sendMessage(msg);
			}
		}.start();
	}
	
	public class mOnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent=new Intent(PhotoSelectFilePathActivity.this, PhotoSelectImagePathActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("seclite_image_File", mList.get(position).getImageFilePath());
			bundle.putInt("imageCount", imageCount);
			intent.putExtras(bundle);
			startActivityForResult(intent, REQUEST_CODE);
		}
		
	}
	
	
	/**
	 * 返回选择的图片路径到调用界面
	 */
	public void onBaceSelectData(){
		if(SelectImagePathList!=null&&SelectImagePathList.size()>0){
			Intent intent=new Intent();
			Bundle bundle=new Bundle();
			bundle.putSerializable("ImagePathList", (Serializable) SelectImagePathList);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			
			PhotoSelectFilePathActivity.this.finish();
		}else {
			ToastUtil.showNewToast(PhotoSelectFilePathActivity.this,"你还没有选择图片" );
		}
	}
	
	
	/**
	 * 清除选择的数据
	 */
	private void gotoClearSelectImage(){
		PhotoGlobalVariable.mList.clear();
		PhotoGlobalVariable.mSelectState.clear();
		PhotoGlobalVariable.selectFile.clear();
		PhotoGlobalVariable.SELECT_IMAGE_NUMBER=0;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		gotoClearSelectImage();
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE:
			if(resultCode!=RESULT_OK){
				return;
			}
			if(data == null){
				return;
			}
			SelectFilePathList=(List<String>) data.getSerializableExtra("SelectFilePath");
			SelectImagePathList=(List<String>) data.getSerializableExtra("SelectImagePath");
			if(SelectFilePathList!=null&&SelectFilePathList.size()>0){
				adapter.setSelectImageFile(SelectFilePathList);
			}else {
				adapter.setSelectImageFile(null);
			}
			
			if(SelectImagePathList!=null){
				if(SelectImagePathList.size()==0) {
					mConfirmButton.setText("确定");
				}else {
					mConfirmButton.setText("确定("+SelectImagePathList.size()+"/"+imageCount+")");
				}
			}
			break;
		default:
			break;
		}
	}

}
