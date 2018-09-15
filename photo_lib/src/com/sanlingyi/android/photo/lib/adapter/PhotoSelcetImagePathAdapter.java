package com.sanlingyi.android.photo.lib.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.sanlingyi.android.photo.lib.R;
import com.sanlingyi.android.photo.lib.global.PhotoGlobalVariable;
import com.sanlingyi.android.photo.lib.util.GetBitmapPoolUtil;
import com.sanlingyi.android.photo.lib.util.ToastUtil;
import com.sanlingyi.android.photo.lib.util.GetBitmapPoolUtil.Type;
/**
 * 选择照片adapter
 * @author g
 *范晓锋
 */
public class PhotoSelcetImagePathAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mList;
	private String mFilePath;
	private LayoutInflater inflater;
	private int moxImageCount;
	private GetBitmapPoolUtil imageLoader;
	/**
	 * 已选择图片数量
	 */
	int selectSize = PhotoGlobalVariable.SELECT_IMAGE_NUMBER;
	/**
	 * 判断图片是否被选中
	 */
	private SparseBooleanArray checkboxSelectArray;
	/**
	 * 存储有照片的文件夹
	 */
	private List<String> mSelectIcon;
	/**
	 * 判断是否有图片的文件夹
	 */
	private List<String> selectFileNameList=PhotoGlobalVariable.selectFile;
	/**
	 * 存放选中的图片路径
	 */
	private List<String> selectImagePathList=PhotoGlobalVariable.mList;
	/**
	 * checkbox是否选中及选中的位置
	 */
	private Map<String, SparseBooleanArray> selectimageMap=PhotoGlobalVariable.mSelectState;
	
	public PhotoSelcetImagePathAdapter(Context mContext, List<String> mlList,
			String filePath,int imageCount) {
		super();
		this.mContext = mContext;
		this.mList = mlList;
		this.mFilePath = filePath;
		this.moxImageCount=imageCount;
		init();
	}
	public void init(){
		inflater=LayoutInflater.from(mContext);
		mSelectIcon=new ArrayList<String>();
		checkboxSelectArray=new SparseBooleanArray();
		imageLoader=GetBitmapPoolUtil.getInstance(Type.LIFO);
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
		Hodler mHodler;
		String imagepath=mList.get(position);
		if(convertView==null){
			mHodler=new Hodler();
			convertView=inflater.inflate(R.layout.photo_select_imagepath_item, null);
			convertView.setTag(mHodler);
		}else {
			mHodler=(Hodler) convertView.getTag();
		}
		
		mHodler.mImageView=(ImageView) convertView.findViewById(R.id.photo_griditem_image);
		mHodler.mCheckBox=(CheckBox) convertView.findViewById(R.id.photo_griditem_checkbox);
		mHodler.mCheckBox.setTag(position);
		
		mHodler.mImageView.setImageResource(R.drawable.photo_add_picture);
		mHodler.mCheckBox.setOnCheckedChangeListener(null);
		mHodler.mCheckBox.setChecked(checkboxSelectArray.indexOfKey(position)>=0?checkboxSelectArray.get(position):false);
		if(selectimageMap.containsKey(mFilePath)){
			checkboxSelectArray=selectimageMap.get(mFilePath);
			if(checkboxSelectArray.indexOfKey(position)>=0&&checkboxSelectArray.get(position)==true){
				mHodler.mImageView.setColorFilter(mContext.getResources().getColor(R.color.photo_image_checked_bg));
			}else {
				mHodler.mImageView.setColorFilter(null);
			}
		}
		if(mHodler.mCheckBox.isChecked()){
			if(!mSelectIcon.contains(imagepath)){
				mSelectIcon.add(imagepath);
			}
		}
		imageLoader.imageLoader(imagepath, mHodler.mImageView);
		
		mHodler.mCheckBox.setOnCheckedChangeListener(new mOnCheckedChangeListener(mHodler, position));
		return convertView;
	}

	public class Hodler{
		ImageView mImageView;
		CheckBox  mCheckBox;
	}
	
	
	public class mOnCheckedChangeListener implements OnCheckedChangeListener{
		Hodler hodler;
		int position;
		public mOnCheckedChangeListener(Hodler hodler, int position) {
			super();
			this.hodler = hodler;
			this.position = position;
		}
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Integer position = (Integer) buttonView.getTag();
			String itemimage=mList.get(position);
			if(selectImagePathList!=null){
				if(selectImagePathList.contains(itemimage)){
					selectImagePathList.remove(itemimage);
				}else {
					selectImagePathList.add(itemimage);
				}
			}
			
			if (isChecked) {
				selectSize ++;
			} else {
				selectSize --;
			}
			
			if(selectSize<=moxImageCount){
				selectSize = selectSize < 0 ? 0 : selectSize;
				if(isChecked){
					if(!mSelectIcon.contains(itemimage)){
						mSelectIcon.add(itemimage);
					}
					hodler.mImageView.setColorFilter(mContext.getResources().getColor(R.color.photo_image_checked_bg));
				}else {
					selectImagePathList.remove(itemimage);
					mSelectIcon.remove(itemimage);
					hodler.mImageView.setColorFilter(null);
				}
				checkboxSelectArray.put(position, isChecked);
			}else {
				selectSize=selectSize>moxImageCount?moxImageCount:selectSize;
				selectImagePathList.remove(itemimage);
				mSelectIcon.remove(itemimage);
				hodler.mCheckBox.setChecked(false);
				hodler.mImageView.setColorFilter(null);
				ToastUtil.showNewToast(mContext, "只能选择"+moxImageCount+"张图片");
			}
			PhotoGlobalVariable.SELECT_IMAGE_NUMBER=selectSize;
			selectimageMap.put(mFilePath, checkboxSelectArray);
		}
		
	}
	/**
	 * 获取存放选中的图片
	 * @return
	 */
	public List<String> getSelectImagePathList(){
		return selectImagePathList;
	}
	/**
	 * 获取存放的文件夹名称
	 * @return
	 */
	public List<String> getSelectFilePathList(){
		if(mSelectIcon!=null&&mSelectIcon.size()>0){
			selectFileNameList.add(mFilePath);
		}else {
			selectFileNameList.remove(mFilePath);
		}
		return selectFileNameList;
	}
	
}
