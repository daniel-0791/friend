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
 * �������ӽ���
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
	//���Ĭ������
    private void initData() {
    	//"+"���
		AddPostImageBean bean = new AddPostImageBean();
		bean.setState(0);
        beans.add(bean);
        mAdapter.setBeans(beans);
		mAdapter.notifyDataSetChanged();
	}
    /**
     * �������ӵ�������
     * @param v
     */
    public void save(View v){
    	if(beans!=null && beans.size()>1){
    		upLoadPost();
    	}else{
    		Toast.makeText(this, "�������ͼƬ", Toast.LENGTH_LONG).show();
    	}
    }
    /**
     * �ϴ�����
     */
    private void upLoadPost() {
    	// TODO Auto-generated method stub
    	String content = editText.getText().toString().trim();
    	if(TextUtils.isEmpty(content)){
    		Toast.makeText(this, "��������Ҫ��������������", Toast.LENGTH_LONG).show();
    		return;
    	}
    	final Post post = new Post();
    	//��ȡ��ǰ��¼���û�
    	User user = BmobUser.getCurrentUser(User.class);
    	post.setAuthor(user);//�û�
    	post.setContent(content);   	
    	if(beans!=null &&beans.size()>1){
    		String filePaths[] = new String[beans.size()-1];
    		for (int i = 0; i < beans.size()-1; i++) {
				filePaths[i] = beans.get(i).getImgPic();
				Log.v("fpf", "vvvvvvvvvvvvvvvvvvP:"+filePaths[i]);
			}
    		post.setFilePaths(filePaths);
    	}
    	//�������ݵ�������
    	post.save(new SaveListener<String>() {
			
			@Override
			public void done(final String objectId, BmobException e) {
				if(e==null){
					Toast.makeText(AddPostActivity.this, "�������ݷ����ɹ�", Toast.LENGTH_LONG).show();
					//�ϴ�ͼƬ
					
					BmobFile.uploadBatch(post.getFilePaths(), new UploadBatchListener() {
						
						@Override
						public void onSuccess(List<BmobFile> files,List<String> urls) {
							 //1��files-�ϴ���ɺ��BmobFile���ϣ���Ϊ�˷����Ҷ����ϴ�������ݽ��в�������������Խ����ļ����浽����
					        //2��urls-�ϴ��ļ�������url��ַ
					        if(urls.size()==post.getFilePaths().length){//���������ȣ�������ļ�ȫ���ϴ����
					        	/**
					        	 * �޸ı����ݣ���ͼƬ�����������
					        	 */
					        	post.setBmobFiles(files);//Ҫ�޸ĵ�����
					        	post.update(objectId, new UpdateListener() {
									
									@Override
									public void done(BmobException e) {
										 if(e==null){
									            Log.i("bmob","���³ɹ�");
									            Toast.makeText(AddPostActivity.this, "ͼƬ���ݸ��³ɹ�", Toast.LENGTH_LONG).show();
									            //Toast.makeText(AddPostActivity.this, "����ͼƬ�ϴ��ɹ�", Toast.LENGTH_LONG).show();
									        	AddPostActivity.this.finish();
									        }else{
									            Log.i("bmob","����ʧ�ܣ�"+e.getMessage()+","+e.getErrorCode());
									            Toast.makeText(AddPostActivity.this, "ͼƬ���ݸ���ʧ�ܣ�"+e.getMessage()+","+e.getErrorCode(), Toast.LENGTH_LONG).show();
									        }
									}
								});//�������ӱ������ID�޸�
					        	
					        }
						}
						
						@Override
						public void onProgress(int arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onError(int statuscode, String errormsg) {
							Toast.makeText(AddPostActivity.this, "������"+statuscode +",����������"+errormsg, Toast.LENGTH_LONG).show();
		
						}
					});
		        }else{
		        	Toast.makeText(AddPostActivity.this, "ʧ�ܣ�"+e.getMessage()+","+e.getErrorCode(), Toast.LENGTH_LONG).show();
		            
		        }
			}
		});
    	
		
	}
	/**
     * �������ش��ĵ�ַ
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode==0 && resultCode==RESULT_OK){
			//�õ���ʾͼƬ�ĵ�ַ
    		beans.clear();
    		//���ص�ͼƬ��ַ
    		List<String> SelectImagePathList = (List<String>) data.getSerializableExtra("ImagePathList");
    		if(SelectImagePathList!=null&&SelectImagePathList.size()>0){
    			//ͼƬ
    			for (int i = 0; i < SelectImagePathList.size(); i++) {
    				AddPostImageBean bean = new AddPostImageBean();
        			bean.setImgPic(SelectImagePathList.get(i));
        			bean.setState(1);//0����+��,1��ͼƬ
        			beans.add(bean);
				}
    			//"+"
    			AddPostImageBean bean = new AddPostImageBean();
    			bean.setState(0);//0����+��,1��ͼƬ
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
