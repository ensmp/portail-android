package com.ensmp.portail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ensmp.portail.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendrierVue extends LinearLayout implements OnClickListener, OnItemClickListener {

	private GridView mGrid;
	private View mConvertView;
	private GregorianCalendar mCalendar;
	private Date[] mWeek;
	private Context mContext;
	private TextView mMonthText;
	private SimpleDateFormat mFormatMonth;
	private SimpleDateFormat mFormatDay;
	private SimpleDateFormat mFormatYear;
	private OnDispatchDateSelectListener mListenerDateSelect;
	private Button mArrowRight;
	private Button mArrowLeft;
	private CalendrierAdapter mAdapter;
	
	public interface OnDispatchDateSelectListener {
		public void onDispatchDateSelect(Date date);
	}
	
	public CalendrierVue(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		mFormatMonth = new SimpleDateFormat("MMMM");
		mFormatDay = new SimpleDateFormat("d");
		mFormatYear = new SimpleDateFormat("yyyy");
		
		mConvertView = LayoutInflater.from(context).inflate(R.layout.calendrier, this);
		mGrid = (GridView)mConvertView.findViewById(R.id.calendrier_jours);	
		
		mGrid.setOnItemClickListener(this);
		
		mMonthText = (TextView)mConvertView.findViewById(R.id.calendrier_mois);
		mArrowLeft = (Button)findViewById(R.id.calendrier_fleche_gauche);
		mArrowLeft.setOnClickListener(this);
		
		mArrowRight = (Button)findViewById(R.id.calendrier_fleche_droite);
		mArrowRight.setOnClickListener(this);
		
		mCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
		mCalendar.setTime(new Date());
		while(mCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			mCalendar.add(Calendar.DAY_OF_YEAR, -1);
		}		

		mWeek = new Date[7];
		for (int i=0; i<7; i++) {
			mWeek[i] = mCalendar.getTime();			
			mCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		changerTexteMois();
		
		mAdapter = new CalendrierAdapter(mContext, mWeek);
		mGrid.setAdapter(mAdapter);		
		
	}
	
	private void changerTexteMois() {
		if (mWeek[0].getMonth() == mWeek[6].getMonth()) {
			mMonthText.setText(mFormatMonth.format(mWeek[0]));
		}
		else {
			mMonthText.setText(mFormatMonth.format(mWeek[0]) + "/" + mFormatMonth.format(mWeek[6]));
		}
		
	}

	public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
		clearBackground();
		v.setBackgroundColor(Color.parseColor("#3A9CE9"));
		OnDispatchDateSelectListener listener = (OnDispatchDateSelectListener) mContext;
		listener.onDispatchDateSelect(mWeek[arg2]);
		
	}
	
	private void clearBackground() {		
		for (int i=0; i<6; i++) {	
			mGrid.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
		}
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.calendrier_fleche_gauche:
			semainePrecedente();
		break;
		case R.id.calendrier_fleche_droite:
			semaineSuivante();
		break;
		}
		
	}
	
	private void semaineSuivante() {
		
		for (int i=0; i<7; i++) {
			mWeek[i] = mCalendar.getTime();
			mCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		changerTexteMois();
		
		mAdapter.notifyDataSetChanged();
		
		clearBackground();
	}
	
	private void semainePrecedente() {
		mCalendar.add(Calendar.DAY_OF_YEAR, -14);
		for (int i=0; i<7; i++) {
			mWeek[i] = mCalendar.getTime();	
			mCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		changerTexteMois();
		mAdapter.notifyDataSetChanged();
		
		clearBackground();
	}
	
	public void setOnDispatchDateSelectListener(OnDispatchDateSelectListener v) {
		mListenerDateSelect = v;
	}

}
