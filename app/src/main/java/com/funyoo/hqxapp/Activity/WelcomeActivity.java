package com.funyoo.hqxapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.funyoo.hqxapp.util.SharedPreUtil;


public class WelcomeActivity extends Activity {

    private WebView webView = null;

    public WelcomeActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        // 定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        // 获得当前窗体对象
        Window window = WelcomeActivity.this.getWindow();
        // 设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        setContentView(R.layout.activity_welcome);
        webView = (WebView) findViewById(R.id.webview);
        // 启用javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setBlockNetworkImage(false);
        webSettings.setAllowFileAccess(true);
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);

        }
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/welcome.html");
//        contentWebView.addJavascriptInterface(WelcomeActivity.this,"android");

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    // 检查用户是否登录 是：直接首页  否：登录页
                    boolean isLogin = (boolean) SharedPreUtil.getParam
                            (WelcomeActivity.this, SharedPreUtil.IS_LOGIN, false);
                    if (isLogin) {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    overridePendingTransition(0, 0);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }



    //由于安全原因 需要加 @JavascriptInterface
//    @JavascriptInterface
//    public void startFunction(){
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(WelcomeActivity.this,"Toast",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    @JavascriptInterface
//    public void startFunction(final String text){
//        runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                new AlertDialog.Builder(WelcomeActivity.this).setMessage(text).show();
//
//            }
//        });
//
//
//    }
}
