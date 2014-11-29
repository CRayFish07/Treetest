package com.geniuseoe.spiner.demo.widget;

import java.util.ArrayList;
import java.util.List;

import com.gao.tree.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class AbstractSpinerAdapter<T> extends BaseAdapter {

	public static interface IOnItemSelectListener{
		public void onItemClick(int pos);
	};
	
	 private Context mContext;   
	 private List<T> mObjects = new ArrayList<T>();
	 private int mSelectItem = 0;	    
	 private LayoutInflater mInflater;
	 int iWidth,iHeight;
	 public  AbstractSpinerAdapter(Context context,int iVal,int iVal1)
	 {
		 iWidth=iVal;
		 iHeight=iVal1;
		 init(context);
	 }
	 
	 public void refreshData(List<T> objects, int selIndex){
		 mObjects = objects;
		 if (selIndex < 0){
			 selIndex = 0;
		 }
		 if (selIndex >= mObjects.size()){
			 selIndex = mObjects.size() - 1;
		 }
		 
		 mSelectItem = selIndex;
	 }
	 
	 private void init(Context context) {
	        mContext = context;
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	    
	    
	@Override
	public int getCount() {

		return mObjects.size();
	}

	@Override
	public Object getItem(int pos) {
		return mObjects.get(pos).toString();
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) 
	{
		 ViewHolder viewHolder;    	 
	     if (convertView == null) 
	     {	    	 
	    	 if(iWidth==1920 && iHeight ==1128)
	    		 convertView = mInflater.inflate(R.layout.spiner_item_layoutbig, null);
	 		else if(iWidth==1024 && iHeight ==720)
	 			 convertView = mInflater.inflate(R.layout.spiner_item_layoutmid, null);
	 		else
	 			 convertView = mInflater.inflate(R.layout.spiner_item_layoutsmall, null);
	    	
	         viewHolder = new ViewHolder();
	         viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
	         convertView.setTag(viewHolder);
	     }
	     else
	     {
	         viewHolder = (ViewHolder) convertView.getTag();
	     }

	     
	     Object item =  getItem(pos);
		 viewHolder.mTextView.setText(item.toString());

	     return convertView;
	}

	public static class ViewHolder
	{
	    public TextView mTextView;
    }


}