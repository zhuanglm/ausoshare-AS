package com.auroratechdevelopment.ausoshare.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.auroratechdevelopment.ausoshare.CustomApplication;
import com.auroratechdevelopment.ausoshare.R;
import com.auroratechdevelopment.ausoshare.Util;
import com.auroratechdevelopment.ausoshare.ui.ActivityBase;
import com.auroratechdevelopment.ausoshare.ui.contact.ContactURLActivity;
import com.auroratechdevelopment.ausoshare.ui.ext.CustomAlertDialog;
import com.auroratechdevelopment.ausoshare.ui.login.LoginActivity;
import com.auroratechdevelopment.ausoshare.util.Constants;
import com.auroratechdevelopment.common.ViewUtils;
import com.auroratechdevelopment.common.util.ImageService;
import com.auroratechdevelopment.common.util.LoadNetPicture;
import com.auroratechdevelopment.common.webservice.WebServiceHelper;
import com.auroratechdevelopment.common.webservice.models.AdDataItem;
import com.auroratechdevelopment.common.webservice.models.OnGoingAdItem;
import com.auroratechdevelopment.common.webservice.response.ResponseBase;
import com.auroratechdevelopment.common.webservice.response.UpdateUserProfileResponse;
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * Created by happy pan on 2015/11/16.
 */
public class PrepareShareAdActivity extends ActivityBase {

    private OnGoingAdItem adItem;
    private WebView wv;
    private String url;
    private Button shareConfirmBtn;
    
    private IWXAPI api;
    
    private Button backButton;
    
    ImageService imageService;
    private AdDataItem adDataItem;
    
    private Bitmap thumb, bitmap;

    private ImageView img;
    private Bitmap bit;
    private String myJpgPath = Environment.getExternalStorageDirectory()+"/ausoshare1/" + "2.png"; 
    private static final int THUMB_SIZE = 150;
    
    private ImageView imageView, viewImage_ad_share;
    private FrameLayout layoutWebView;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setView() {
    	
        Bundle b = getIntent().getExtras();
        adDataItem = b.getParcelable(Constants.BUNDLE_AD_ITEM);

        setContentView(R.layout.activity_prepare_share_ad);
        shareConfirmBtn = (Button)findViewById(R.id.share_confirm_btn);   
        viewImage_ad_share = (ImageView)findViewById(R.id.view_image_ad_share);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
		
		backButton = (Button)findViewById(R.id.backButton);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(backButtonOnClickedListener);
		
		Log.e("Edward", "adDataItem.reviewURL: " + adDataItem.reviewURL + ", adDataItem.shareURL: " + adDataItem.shareURL);
		
		
        url = adDataItem.reviewURL; 
        
        //define the webview
        wv = new WebView(getApplicationContext());
        WebSettings settings = wv.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        layoutWebView =  (FrameLayout)findViewById(R.id.layoutWebView);
        layoutWebView.addView(wv);
        
//        wv = (WebView)findViewById(R.id.reviewurl_webview);
        
        getNetImage(adDataItem.thumb);
        
        openURLWeb(url);
    }
    
    View.OnClickListener backButtonOnClickedListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.backButton:
				Log.e("Edward","at prepareShareAD page: OnClickListener, try to return back");
				hideSoftBoard();
				final Bundle bundle = new Bundle(); 
		        bundle.putString(Constants.LAST_PAGE, Constants.HOME_PAGE);
		        
				ViewUtils.startPage(bundle, PrepareShareAdActivity.this, HomeActivity.class);
				break;
			default:
				hideSoftBoard();
				final Bundle bundle1 = new Bundle(); 
		        bundle1.putString(Constants.LAST_PAGE, Constants.HOME_PAGE);
				ViewUtils.startPage(bundle1, PrepareShareAdActivity.this, HomeActivity.class);
				break;
			}
			
		}
	};
	
    private void hideSoftBoard(){
    	if(getCurrentFocus()!=null)  
        {  
    		Log.e("Edward","in hideSoftBoard");
    		
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))  
            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   
        }  
    }

    @Override
    public void onDestroy(){

    	if(wv != null){
    		
    		wv.clearCache(true);
    		wv.clearFormData();
    		wv.clearHistory();
    		wv.removeAllViews();
    		layoutWebView.removeView(wv);
    		wv.destroy();
    		
    		
    	}
    	
    	super.onDestroy();
    }
    
//    @Override
//    public void onStop(){
//    	super.onStop();
//    	
//    	if(wv != null){
//    		wv.removeAllViews();
//    		wv.destroy();
//    		wv.clearCache(true);
//    		wv.clearFormData();
//    		wv.clearHistory();
//    	}
//    }
    
    @Override
    public void onBackPressed() {
    	Log.e("Edward","??at prepareShareAD page: onBackPressed, try to return back");
    	hideSoftBoard();
    	
    	final Bundle bundle = new Bundle(); 
        bundle.putString(Constants.LAST_PAGE, Constants.HOME_PAGE);
        
		ViewUtils.startPageWithClearStack(bundle, PrepareShareAdActivity.this, HomeActivity.class);
        super.onBackPressed();
    }
    
    private void openURLWeb(String url){
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        
        showWaiting();
        
        wv.loadUrl(url);
        
        wv.setWebViewClient(new WebViewClient() {
        	
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        
        wv.setWebChromeClient(new WebChromeClient(){
        	@Override
        	public void onProgressChanged(WebView view, int progress){
        		if(progress > 85){
        			dismissWaiting();
        		}
        		super.onProgressChanged(view, progress);
        	}
        });
    }
    
    private void requestUserLogin(){
		Log.e("Edward", "Did not loginnnn");
		showCenterScreenOkCancelAlert(this,
                getResources().getString(R.string.login_prompt_text),
                getResources().getString(R.string.alert_share_ad_login_prompt_text),
                getString(R.string.button_ok), getString(R.string.button_cancel), null, true
				);
    }
    
    public CustomAlertDialog customAlertDialog;
    
    public void showCenterScreenOkCancelAlert(final Context context,
            final CharSequence alertTitle, final String msg,
            final String okButtonName, final String cancelButtonName,
            final CustomAlertDialog.AlertCallback callback, final boolean isCancelable) {

    	Log.e("Edward", "showCenterScreenAlert");
    	
        this.runOnUiThread(new Runnable() {
            public void run() {
                dismissCustomAlertDialog();
                ((PrepareShareAdActivity) context).customAlertDialog = new CustomAlertDialog(
                        context, R.style.alertStyle);
                customAlertDialog.setContentView(R.layout.alert_center_screen);
                customAlertDialog.setMessage(msg, R.id.alert_message);
                customAlertDialog.setTitle(alertTitle, R.id.alert_title);
                customAlertDialog.setOnCancelListener(alertCancelClicked(callback));
                customAlertDialog.setOnDismissListener(alertDismissClicked());
                if (okButtonName != null && okButtonName.length() > 0) {
                    customAlertDialog.setButton(okButtonName,
                            alertButtonOkayClicked(callback));
                    
                }

                if (cancelButtonName != null && cancelButtonName.length() > 0) {
                    customAlertDialog.setButton2(cancelButtonName,
                            alertButtonCancelClicked(callback));
                }

                customAlertDialog.setCancelable(isCancelable);
                customAlertDialog.show();
            }
        });
    }
    
    protected DialogInterface.OnClickListener alertButtonOkayClicked(
            final CustomAlertDialog.AlertCallback callback) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    callback.GetAlertButton(which);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                
                final Bundle bundle = new Bundle(); 
                bundle.putString(Constants.SHARED_AD_ID, adDataItem.adID);
                bundle.putString(Constants.LAST_PAGE, Constants.LOGIN_PAGE_FROM_AD_PREPARE_SHARE);
            	ViewUtils.startPage(bundle, PrepareShareAdActivity.this, LoginActivity.class);

            }
        };
    }
    
    protected DialogInterface.OnClickListener alertButtonCancelClicked(
            final CustomAlertDialog.AlertCallback callback) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    callback.GetAlertButton(which);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                
                return;
            }
        };
    }

    protected DialogInterface.OnCancelListener alertCancelClicked(final CustomAlertDialog.AlertCallback callback) {
        return new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (callback != null)
                    callback.AlertCancelled();
            }
        };
    }
    
    private void readyToShare(String whereToShare){
    	if(CustomApplication.getInstance().getEmail().equalsIgnoreCase("")){
			requestUserLogin();
		}
		else{
			viewImage_ad_share.setImageBitmap(bitmap);
			
			ImageService imageService = new ImageService();
			
			WXWebpageObject webpage = new WXWebpageObject();
			
			webpage.webpageUrl = adDataItem.shareURL;
			
			WXMediaMessage msg = new WXMediaMessage(webpage);
			msg.title = adDataItem.description;
			msg.description = adDataItem.description_long;
	
			Bitmap shareBmp = BitmapFactory.decodeFile(myJpgPath);
			Bitmap shareThumbBmp = Bitmap.createScaledBitmap(shareBmp, THUMB_SIZE, THUMB_SIZE, true);
		    
		    msg.thumbData = Util.bmpToByteArray(shareThumbBmp, true);  //set thumb image
		    
//		    whereToShare(whereToShare, msg);
		    
		    SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("webpage");

			req.message = msg;
//			req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
			
			if(whereToShare.equals(Constants.SHARE_TO_FRIENDS)){
				req.scene = SendMessageToWX.Req.WXSceneSession;
			}else{
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			}
			
			api.sendReq(req);
			//recycle image
			BitmapRecycle(shareBmp);
			BitmapRecycle(shareThumbBmp);
			
			updatedSharedTimes();
		}
    }
    
    
    public void BitmapRecycle(Bitmap recycleBitmap){
    	//recycle image
		if(recycleBitmap != null && !recycleBitmap.isRecycled()){
			recycleBitmap.recycle();
			recycleBitmap = null;
		}
		System.gc();
    }
  
    
    private void whereToShare(String whereToShare, WXMediaMessage msg){
    	SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");

		req.message = msg;
//		req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		
		if(whereToShare.equals(Constants.SHARE_TO_FRIENDS)){
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}else{
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		
		api.sendReq(req);
		updatedSharedTimes();
    }
    
	public void PrepareToShareOnClicked(View view){
		readyToShare(Constants.SHARE_TO_MOMENTS);
	}
	
	public void PrepareToShareToFriendOnClicked(View view){
		readyToShare(Constants.SHARE_TO_FRIENDS);
	}
	
//	public static Bitmap getLoacalBitmap(String url) {
//        try {
//             FileInputStream fis = new FileInputStream(url);
//             return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        
//
//          } catch (FileNotFoundException e) {
//             e.printStackTrace();
//             return null;
//        }
//   }
	
	
	private void updatedSharedTimes(){
		int shared_ad_times = 0;
		
		//send to backend
		WebServiceHelper.getInstance().updateSharedTime(
                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID),
                CustomApplication.getInstance().getEmail(),adDataItem.adID
        );
		
		shared_ad_times = CustomApplication.getInstance().getSharedADTime();
		if (shared_ad_times < 3){
			CustomApplication.getInstance().setSharedADTime(shared_ad_times + 1);
		}
	}
	
	@Override
    public void ResponseFailedCallBack(int tag, final ResponseBase response) {
        //dismissWaiting();
        super.ResponseFailedCallBack(tag, response);
    }
	
	@Override
    public void ResponseSuccessCallBack(int tag, ResponseBase response) {
        //dismissWaiting();

        //final UpdateUserProfileResponse updateUserProfileResponse;

        //if (response instanceof UpdateUserProfileResponse) {}
    }
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
    

    @Override
    protected void viewInitialized() {

    }

    private void getNetImage( final String url){
    	 new Thread(new Runnable() {
				
				@Override
				public void run() {
		             Bitmap tmpBitmap = null;  
		             try { 
			             InputStream is = new java.net.URL(url).openStream(); 
			             tmpBitmap = BitmapFactory.decodeStream(is); 
			             saveMyBitmap(tmpBitmap);
	
			             //bitmap recycle
			             BitmapRecycle(tmpBitmap);
			             
	//		             bit = getBitmapByPath("myBitmap");
			             is.close();
			             handler.sendEmptyMessage(1);
			             } catch (Exception e) { 
			             e.printStackTrace(); 
		             } 
					
				}
			}).start();
    }
    
//    public Bitmap getBitmapByPath(String fileName) {   
//        BitmapFactory.Options options = new BitmapFactory.Options();  
////      options.inSampleSize = 12;  
//        Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);  
//        return bm;  
//    }  
    
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
			}
		}
	};


    public void saveMyBitmap(Bitmap mBitmap)  
            throws IOException {  
        File tmp = new File("/sdcard/ausoshare1/");  
        if (!tmp.exists()) {  
            tmp.mkdir();  
        }  
        File f = new File(myJpgPath);  
        f.createNewFile();  
        FileOutputStream fOut = null;  
        try {  
            fOut = new FileOutputStream(f);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
        mBitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);  
//        mBitmap.recycle();
        try {  
            fOut.flush();  
            fOut.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        BitmapRecycle(mBitmap);
    }  

}
