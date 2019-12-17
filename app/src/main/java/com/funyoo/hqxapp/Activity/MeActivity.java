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

import com.alibaba.fastjson.JSONObject;
import com.funyoo.hqxapp.model.User;
import com.funyoo.hqxapp.util.SharedPreUtil;


public class MeActivity extends Activity {
    private WebView webView = null;
    private User user;

    private static MeActivity me;

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
        webView.loadUrl("file:///android_asset/me.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用

        String userInfo = (String) SharedPreUtil.getParam(MeActivity.this, SharedPreUtil.LOGIN_DATA, "");
        //Toast.makeText(MeActivity.this,"===" +  userInfo, Toast.LENGTH_SHORT).show();
        user = JSONObject.parseObject(userInfo, User.class);
        //webView.loadUrl("javascript:setUserInfo(" + user + ")");
        me = this;
    }

    public static void finishMe() {
        if (me != null) {
            me.finish();
        }
    }

    public final class JSInterface {
        @JavascriptInterface
        public void goPart(String partName) {
            if (partName.equals("main")) {
                startActivity(new Intent(MeActivity.this, MainActivity.class));
            } else if (partName.equals("school")) {
                startActivity(new Intent(MeActivity.this, SchoolListActivity.class));
            }
            // 去除滑动动画效果
            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public void goCollection() {
            startActivity(new Intent(MeActivity.this, CollectionActivity.class));
        }

        @JavascriptInterface
        public void goSet() {
            startActivity(new Intent(MeActivity.this, SetActivity.class));
        }

        @JavascriptInterface
        public void goUserData() {
            startActivity(new Intent(MeActivity.this, UserInfoActivity.class));
            finish();
        }

        @JavascriptInterface
        public String getUser() {
            return  (String) SharedPreUtil.getParam(MeActivity.this, SharedPreUtil.LOGIN_DATA, "");
        }

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(MeActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
