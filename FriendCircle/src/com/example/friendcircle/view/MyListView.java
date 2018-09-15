package com.example.friendcircle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;
/**
 * 自定义ListView 
 * @author Administrator
 *
 */
public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 这是view的方法，用来计算当前视图的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 //此处是代码的关键 ，重新自己计算视图的高度
        //MeasureSpec.AT_MOST的意思就是wrap_content  
        //Integer.MAX_VALUE >> 2 是使用最大值的意思,也就表示的无边界模式  
        //Integer.MAX_VALUE >> 2 此处表示是父布局能够给他提供的大小  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec); 
	}
	

}
