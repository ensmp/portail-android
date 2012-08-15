package com.example.portail;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class VendomeDetailActivity extends MenuActivity {

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    WebView mWebView=new WebView(VendomeDetailActivity.this);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.getSettings().setPluginsEnabled(true);
	    Log.d("url", getString(R.string.domain) + getIntent().getExtras().getString("url"));
	    mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+getString(R.string.domain) + getIntent().getExtras().getString("url"));
	    setContentView(mWebView);
	  }
	}