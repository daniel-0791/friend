package com.example.friendcircle;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.example.friendcircle.adapter.AddPostImageAdapter;
import com.example.friendcircle.bean.AddPostImageBean;
import com.example.friendcircle.bean.Post;
import com.example.friendcircle.bean.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 发布帖子界面
 * @author Administrator
 *
 */
public class AddPostActivity extends Activity {
    private GridView mGridView;
    private List<AddPostImageBean> beans = new ArrayList<AddPostImageBean>();
    private AddPostImageAdapter mAdapter;
    private EditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_post);
		mGridView = (GridView) findViewById(R.id.gv_img_list);
		editText = (EditText) findViewById(R.id.et_content);
		mAdapter = new AddPostImageAdapter(this);
		mGridView.setAdapter(mAdapter);
		initData();
		
	}
	//添加默认数据
    private void initData() {
    	//"+"添加
		AddPostImageBean bean = new AddPostImageBean();
		bean.setState(0);
        beans.add(bean);
        mAdapter.setBeans(beans);
		mAdapter.notifyDataSetChanged();
	}
    /**
     * 保存帖子到服务器
     * @param v
     */
    public void save(View v){
    	if(beans!=null && beans.size()>1){
    		upLoadPost();
    	}else{
    		Toast.makeText(this, "请先添加图片", Toast.LENGTH_LONG).show();
    	}
    }
    /**
     * 上传帖子
     */
    private void upLoadPost() {
    	// TODO Auto-generated method stub
    	String content = editText.getText().toString().trim();
    	if(TextUtils.isEmpty(content)){
    		Toast.makeText(this, "请输入你要发布的帖子内容", Toast.LENGTH_LONG).show();
    		return;
    	}
    	final Post post = new Post();
    	//获取当前登录的用户
    	User user = BmobUser.getCurrentUser(User.class);
    	post.setAuthor(user);//用户
    	post.setContent(content);   	
    	if(beans!=null &&beans.size()>1){
    		String filePaths[] = new String[beans.size()-1];
    		for (int i = 0; i < beans.size()-1; i++) {
				filePaths[i] = beans.get(i).getImgPic();
				Log.v("fpf", "vvvvvvvvvvvvvvvvvvP:"+filePaths[i]);
			}
    		post.setFilePaths(filePaths);
    	}
    	//保存数据到服务器
    	post.save(new SaveListener<String>() {
			
			@Override
			public void done(final String objectId, BmobException e) {
				if(e==null){
					Toast.makeText(AddPostActivity.this, "帖子内容发布成功", Toast.LENGTH_LONG).show();
					//上传图片
					
					BmobFile.uploadBatch(post.getFilePaths(), new UploadBatchListener() {
						
						@Override
						public void onSuccess(List<BmobFile> files,List<String> urls) {
							 //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
					        //2、urls-上传文件的完整url地址
					        if(urls.size()==post.getFilePaths().length){//如果数量相等，则代表文件全部上传完成
					        	/**
					        	 * 修改表数据，将图片保存进服务器
					        	 */
					        	post.setBmobFiles(files);//要修改的数据
					        	post.update(objectId, new UpdateListener() {
									
									@Override
									public void done(BmobException e) {
										 if(e==null){
									            Log.i("bmob","更新成功");
									            Toast.makeText(AddPostActivity.this, "图片数据更新成功", Toast.LENGTH_LONG).show();
									            //Toast.makeText(AddPostActivity.this, "帖子图片上传成功", Toast.LENGTH_LONG).show();
									        	AddPostActivity.this.finish();
									        }else{
									            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
									            Toast.makeText(AddPostActivity.this, "图片数据更新失败："+e.getMessage()+","+e.getErrorCode(), Toast.LENGTH_LONG).show();
									        }
									}
								});//根据帖子表的主键ID修改
					        	
					        }
						}
						
						@Override
						public void onProgress(int arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onError(int statuscode, String errormsg) {
							Toast.makeText(AddPostActivity.this, "错误码"+statuscode +",错误描述："+errormsg, Toast.LENGTH_LONG).show();
		
						}
					});
		        }else{
		        	Toast.makeText(AddPostActivity.this, "失败："+e.getMessage()+","+e.getErrorCode(), Toast.LENGTH_LONG).show();
		            
		        }
			}
		});
    	
		
	}
	/**
     * 接收相册回传的地址
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode==0 && resultCode==RESULT_OK){
			//拿到显示图片的地址
    		beans.clear();
    		//返回的图片地址
    		List<String> SelectImagePathList = (List<String>) data.getSerializableExtra("ImagePathList");
    		if(SelectImagePathList!=null&&SelectImagePathList.size()>0){
    			//图片
    			for (int i = 0; i < SelectImagePathList.size(); i++) {
    				AddPostImageBean bean = new AddPostImageBean();
        			bean.setImgPic(SelectImagePathList.get(i));
        			bean.setState(1);//0：“+”,1：图片
        			beans.add(bean);
				}
    			//"+"
    			AddPostImageBean bean = new AddPostImageBean();
    			bean.setState(0);//0：“+”,1：图片
    			beans.add(bean);
    			mAdapter.setBeans(beans);
    			mAdapter.notifyDataSetChanged();
    		}   		
			
		}	
    	super.onActivityResult(requestCode, resultCode, data);
    	
    }
	@Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	if(beans!=null){
    		beans.clear();
    		beans=null;
    	}
    }
	


}
