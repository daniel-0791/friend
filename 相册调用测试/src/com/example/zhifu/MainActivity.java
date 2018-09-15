package com.example.zhifu;

import java.io.Serializable;
import java.util.List;

import com.sanlingyi.android.photo.lib.PhotoSelectFilePathActivity;
import com.sanlingyi.android.photo.lib.PhotoSelectImagePathActivity;
import com.sanlingyi.android.photo.lib.util.ToastUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private LinearLayout layout;
	/**
	 * 存放选中的图片
	 */
	private List<String> SelectImagePathList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(R.layout.activity_main);
		layout = (LinearLayout) findViewById(R.id.ll_images);
	}

	public void select(View v) {
		Intent intent = new Intent(this, PhotoSelectFilePathActivity.class);
		intent.putExtra("imageCount", 10);//选择的图片数量
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0 && resultCode==RESULT_OK){
			//拿到显示图片的地址
			SelectImagePathList = (List<String>) data.getSerializableExtra("ImagePathList");
			if(SelectImagePathList!=null&&SelectImagePathList.size()>0){
				for (int i = 0; i < SelectImagePathList.size(); i++) {
					Bitmap bitmap = BitmapFactory.decodeFile(SelectImagePathList.get(i));
					ImageView imageView = new ImageView(this);
					imageView.setLayoutParams(new LayoutParams(200, 100));
					imageView.setPadding(5, 5, 5, 5);
					imageView.setImageBitmap(bitmap);
					layout.addView(imageView);
				}
			}
			
		}	
		super.onActivityResult(requestCode, resultCode, data);
	}

}
