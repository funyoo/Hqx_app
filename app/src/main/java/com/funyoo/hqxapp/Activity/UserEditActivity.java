package com.funyoo.hqxapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.funyoo.hqxapp.util.SharedPreUtil;

public class UserEditActivity extends Activity {
    private WebView webView = null;

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
        webView.loadUrl("file:///android_asset/edit.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
    }

    public final class JSInterface {
        @JavascriptInterface
        public void back() {
            startActivity(new Intent(UserEditActivity.this, UserInfoActivity.class));
            // 去除滑动动画效果
            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public String getUser() {
            return  (String) SharedPreUtil.getParam(UserEditActivity.this, SharedPreUtil.LOGIN_DATA, "");
        }



        @JavascriptInterface
        public void setUser(String userStr) {
            SharedPreUtil.setParam(UserEditActivity.this, SharedPreUtil.LOGIN_DATA, userStr);
            Toast.makeText(UserEditActivity.this, "用户信息更新完成", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserEditActivity.this, UserInfoActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }

    }
}
