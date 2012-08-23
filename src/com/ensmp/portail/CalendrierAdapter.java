package com.ensmp.portail;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ensmp.portail.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendrierAdapter extends BaseAdapter{
	
	private Context mContext;
	private Date[] mWeek;
	private SimpleDateFormat mFormatNumber;
	private SimpleDateFormat mFormatDay;
	private LayoutInflater mInflater;
	
	public CalendrierAdapter(Context _context, Date[] _week) {
		mContext = _context;
		mWeek = _week;
		mFormatNumber = new SimpleDateFormat("d");
		mFormatDay = new SimpleDateFormat("EEE");
		mInflater = LayoutInflater.from(mContext);
		
	}
	
	private static class ViewHolder {
		public TextView tvDay;
		public TextView tvNumber;
	}

	public int getCount() {
		return mWeek.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.calendrier_jour, null);
			holder = new ViewHolder();
			holder.tvDay = (TextView) convertView.findViewById(R.id.calendrier_tv_jour);
			holder.tvNumber = (TextView) convertView.findViewById(R.id.calendrier_tv_nombre);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvDay.setText(mFormatDay.format(mWeek[position]));
		holder.tvNumber.setText(mFormatNumber.format(mWeek[position]));
		Date today = new Date();
		if (today.getMonth() == mWeek[position].getMonth() && today.getDate() == mWeek[position].getDate()) {
			convertView.setBackgroundColor(Color.GREEN);
		}		
		
		return convertView;
	}

}
