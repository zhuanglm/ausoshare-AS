package com.auroratechdevelopment.ausoshare.ui.ext;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

//Created by Raymond Zhuang May 10 2016

public class MySearchView extends SearchView implements OnQueryTextListener{

	public MySearchView(Context context) {
		super(context);
		init(context);
	}

	

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /*public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/
	
	private void init(Context context){
		 
			//m_Search.setVisibility(View.VISIBLE);
	    	//m_Search.setBackgroundColor(getResources().getColor(android.R.color.white));
	    	//m_Search.setBackgroundResource(R.drawable.textfield_searchview_holo_light);
	    	
	    	//int id = m_Search.getContext().getResources().getIdentifier("android:id/search_button", null, null);
	    	
	    	//ImageView icon = (ImageView) m_Search.findViewById(id);
	    	
	    	//icon.setImageResource(R.drawable.ic_search);
	    	//id = m_Search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
	    	//TextView textView = (TextView) m_Search.findViewById(id);
	    	//textView.setTextColor(getResources().getColor(android.R.color.white));
			
			// 设置该SearchView默认是否自动缩小为图标
		this.setIconifiedByDefault(true);
		
			// 设置该SearchView显示搜索按钮
		this.setSubmitButtonEnabled(true);
			// 设置该SearchView内默认显示的提示文本
			//m_Search.setQueryHint("查找");
			
		/*this.setOnSearchClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				setBackgroundColor(getResources().getColor(android.R.color.white));
					
			}
				
		});*/
			
		/*this.setOnCloseListener(new OnCloseListener() {
				 
			@Override
			public boolean onClose() {
				setBackgroundColor(Color.argb(0x00, 0xff, 0x00, 0x00));
				//m_Search.setIconified(true);
				return true;
			}
		});*/
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		//Toast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
		setBackgroundColor(Color.argb(0x00, 0xff, 0x00, 0x00));
		
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		//Toast.makeText(this, "changed", Toast.LENGTH_SHORT).show();
		return false;
	}
	
	@Override
    public void onActionViewCollapsed() {
        super.onActionViewCollapsed();
        setBackgroundColor(Color.argb(0x00, 0xff, 0x00, 0x00));
    }

   

}
