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

public class SetActivity extends Activity {
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
        webView.loadUrl("file:///android_asset/set.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
    }

    public final class JSInterface {
        @JavascriptInterface
        public void back() {
//            startActivity(new Intent(SetActivity.this, UserInfoActivity.class));
//            // 去除滑动动画效果
//            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public void about() {
            startActivity(new Intent(SetActivity.this, AboutActivity.class));
            // 去除滑动动画效果
            overridePendingTransition(0, 0);
        }

        @JavascriptInterface
        public void exit() {
            startActivity(new Intent(SetActivity.this, LoginActivity.class));
            // 去除滑动动画效果
            overridePendingTransition(0, 0);
            SharedPreUtil.removeParam(SetActivity.this, SharedPreUtil.IS_LOGIN);
            SharedPreUtil.removeParam(SetActivity.this, SharedPreUtil.LOGIN_DATA);
            finish();
            MeActivity.finishMe();
        }


        @JavascriptInterface
        public void setUser(String userStr) {
            SharedPreUtil.setParam(SetActivity.this, SharedPreUtil.LOGIN_DATA, userStr);
            Toast.makeText(SetActivity.this, "用户信息更新完成", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SetActivity.this, UserInfoActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }

    }
}
