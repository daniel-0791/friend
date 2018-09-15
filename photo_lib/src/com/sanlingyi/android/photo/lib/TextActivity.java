package com.sanlingyi.android.photo.lib;
import java.util.List;
import com.sanlingyi.android.photo.lib.PhotoSelectFilePathActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
/**
 * 调用示例
 * @author Administrator
 *
 */
public class TextActivity extends Activity {
	/**
	 * 存放选中的图片
	 */
	private List<String> SelectImagePathList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
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
			
			
		}	
		super.onActivityResult(requestCode, resultCode, data);
	}

}
