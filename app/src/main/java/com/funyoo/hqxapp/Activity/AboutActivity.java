package com.funyoo.hqxapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.funyoo.hqxapp.service.UpdateManager;
import com.funyoo.hqxapp.util.AppUtil;
import com.funyoo.hqxapp.util.SharedPreUtil;

public class AboutActivity extends Activity {
    private WebView webView = null;

    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        webView = findViewById(R.id.webview);
        // 启用javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setBlockNetworkImage(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }

//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return motionEvent.getAction() == MotionEvent.ACTION_MOVE;
//            }
//        });
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.loadUrl( "javascript:window.location.reload( true )" );
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/about.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用

        //Android 6.0以上版本需要临时获取权限
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1&&
                PackageManager.PERMISSION_GRANTED!=checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            requestPermissions(perms,PERMS_REQUEST_CODE);
        }
    }

    public final class JSInterface {
        @JavascriptInterface
        public void back() {
//            startActivity(new Intent(SetActivity.this, UserInfoActivity.class));
//            // 去除滑动动画效果
            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public void check() {
            new UpdateManager(AboutActivity.this).checkUpdate(1);
        }

        @JavascriptInterface
        public int getVersonCode() {
            return AppUtil.getVersionCode(AboutActivity.this);
        }

        @JavascriptInterface
        public String getVersonName() {
            return AppUtil.getVersionName(AboutActivity.this);
        }
    }
}
