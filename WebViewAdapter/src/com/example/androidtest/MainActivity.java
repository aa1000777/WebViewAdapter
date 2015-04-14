package com.example.androidtest;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ZoomButtonsController;
/**
 * 
 *****************************************************
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>Coffee</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2015-4-14 上午9:18:07</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>WebView</dd>
 * @author aa1000777 - Email:aa1000777@qq.com
 *****************************************************
 */
public class MainActivity extends Activity {
	private final String URL = "file:///android_asset/Live Q&A.html";
	private WebView wv;
	private JSKit js;
	private Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8521:
				System.out.println("JS抛出比例：" + wv.getScale());
//				Toast.makeText(MainActivity.this, "JS抛出比例："+wv.getScale(), Toast.LENGTH_LONG).show();
				// wv.zoomIn();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 实例化js对象
		js = new JSKit(this);
		initViews();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initViews() {
		// TODO Auto-generated method stub
		wv = (WebView) findViewById(R.id.mWV_web);
		EditText tv=(EditText) findViewById(R.id.mTV_text);
		tv.requestFocus();
		// 把js绑定到全局的myjs上，myjs的作用域是全局的，初始化后可随处使用
//		 wv.addJavascriptInterface(js, "myjs");
		// wv.requestFocusFromTouch();
//		wv.setInitialScale(100);
		// 自适应屏幕
		// wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		// wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(URL);
		wv.setWebViewClient(new wvc());
//		wv.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				System.out.println("-------OnFocusChangeListener1------------");
//				if (hasFocus) {
//					System.out.println("-------OnFocusChangeListener2------------");
////					setDefaultScale();
//					System.out.println("聚焦后比例：" + wv.getScale());
////					Toast.makeText(MainActivity.this, "聚焦后比例："+wv.getScale(), Toast.LENGTH_LONG).show();
//					showInvalidQRCodeMessage("聚焦后比例：", ""+wv.getScale());
//				}
//			}
//		});
		WebSettings settings = wv.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		int sysVersion = VERSION.SDK_INT;
		if (sysVersion >= 11) {
			settings.setDisplayZoomControls(false);
		} else {
			ZoomButtonsController zbc = new ZoomButtonsController(wv);
			zbc.getZoomControls().setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 ***************************************************** 
	 * 方法简介: 设置缩放比例 参考http://www.cppblog.com/guojingjia2006/archive/2012/12/18/196429.html
	 ***************************************************** 
	 */
	private void setDefaultScale() {
		try {
			Field defaultScale = WebView.class.getDeclaredField("mDefaultScale");
			defaultScale.setAccessible(true);
			// Object zoomValue = defaultScale.get(wv);
			// float sv = defaultScale.getFloat(zoomValue);
			defaultScale.setFloat(MainActivity.this, 2.0f);// 0.7f根据手机分辨率自行计算设置
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			System.out.println("1、mDefaultScale");
			try {
				Field zoomManager;
				zoomManager = WebView.class.getDeclaredField("mZoomManager");
				zoomManager.setAccessible(true);
				Object zoomValue = zoomManager.get(wv);
				Field defaultScale = zoomManager.getType().getDeclaredField("mDefaultScale");
				defaultScale.setAccessible(true);
				float sv = defaultScale.getFloat(zoomValue);
				defaultScale.setFloat(zoomValue, 2.0f);
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
				System.out.println("2、mZoomManager");
				try {
					Field mProviderField = WebView.class.getDeclaredField("mProvider");
					mProviderField.setAccessible(true);
					// mProviderField.getClass()
					Object webviewclassic = mProviderField.get(wv);
					Field zoomManager = webviewclassic.getClass().getDeclaredField("mZoomManager");
					zoomManager.setAccessible(true);
					Object zoomValue = zoomManager.get(webviewclassic);
					Field defaultScale = zoomManager.getType().getDeclaredField("mDefaultScale");
					defaultScale.setAccessible(true);
					float sv = defaultScale.getFloat(zoomValue);
					defaultScale.setFloat(zoomValue, 2.0f);
				} catch (Exception e2) {
					System.out.println("3、mProvider");
					e2.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		wv.removeAllViews();
		wv.destroy();
	}

	private class wvc extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			System.out.println("初始化比例：" + wv.getScale());
//			Toast.makeText(MainActivity.this, "初始化比例："+wv.getScale(), Toast.LENGTH_LONG).show();
			showInvalidQRCodeMessage("初始化比例：", ""+wv.getScale());
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		wv.setVisibility(View.VISIBLE);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		wv.setVisibility(View.GONE);
	}

	public class JSKit {
		private MainActivity ma;

		public JSKit(MainActivity context) {
			this.ma = context;
		}

		/**
		 * 
		 ***************************************************** 
		 * 方法简介: 4.2之前向webview注入的对象所暴露的接口toString没有注释语句@JavascriptInterface，而4.2
		 * 及以后的则多了注释语句@JavascriptInterface
		 * 
		 * 经过查官方文档所知，因为这个接口允许JavaScript
		 * 控制宿主应用程序，这是个很强大的特性，但同时，在4.2的版本前存在重大安全隐患，因为JavaScript
		 * 可以使用反射访问注入webview的java对象的public
		 * fields，在一个包含不信任内容的WebView中使用这个方法，会允许攻击者去篡改宿主应用程序
		 * ，使用宿主应用程序的权限执行java代码。因此4.2以后，任何为JS暴露的接口，都需要加@JavascriptInterface
		 * 
		 * @param msg
		 ***************************************************** 
		 */
		@JavascriptInterface
		public void showMsg() {
//			Toast.makeText(ma, "这是一个测试方法", Toast.LENGTH_LONG).show();
			h.sendEmptyMessage(8521);
		}
	}
	
	/**
	 * 
	 ***************************************************** 
	 * 方法简介: 不是正确的QRCode
	 ***************************************************** 
	 */
	private void showInvalidQRCodeMessage(String title,String value) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(value).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// continue with delete
				dialog.cancel();
			}
		}).show();
	}
}
