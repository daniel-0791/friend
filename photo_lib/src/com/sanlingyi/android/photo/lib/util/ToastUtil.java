package com.sanlingyi.android.photo.lib.util;

import com.sanlingyi.android.photo.lib.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ToastUtil {
	private static Toast mToast ;
	/**
	 * 弹出Toast
	 * @param context
	 * @param resId 
	 * @param text  提示内容
	 * @param duration
	 */
    public static void showNewToast(Context context, CharSequence text) {  
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        View layout = inflater.inflate(R.layout.photo_custom_toast, null);  
        ImageView imageView = (ImageView) layout.findViewById(R.id.photo_iv_icon);  
        TextView textView = (TextView) layout.findViewById(R.id.photo_tv_content);
        	imageView.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(text)){
         	textView.setText(text);
        }else{
         	textView.setText("");
        }
        if(null == mToast){
           mToast = new Toast(context); 
        }
        mToast.setView(layout);  
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();  
    }  
	
}
	
	
	

