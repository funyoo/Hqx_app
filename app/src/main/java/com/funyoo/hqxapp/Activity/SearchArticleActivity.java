package com.funyoo.hqxapp.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.funyoo.hqxapp.model.User;
import com.funyoo.hqxapp.util.MyDatabaseHelper;
import com.funyoo.hqxapp.util.SharedPreUtil;

public class SearchArticleActivity extends Activity {
    private WebView webView = null;
    protected static MyDatabaseHelper databaseHelper = null;
    private Dialog dialog;

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
        webView.loadUrl("file:///android_asset/search.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用

        //databaseHelper = new MyDatabaseHelper(CollectionActivity.this, "articls", null, 1);
    }


    public final class JSInterface {

        @JavascriptInterface
        public void toReturn() {
            SearchArticleActivity.this.finish();
        }

        @JavascriptInterface
        public void toArticle(String htmlUrl, int aid) {
            ArticleActivity.setHtmlUrl(htmlUrl);
            ArticleActivity.setArtId(aid);
            startActivity(new Intent(SearchArticleActivity.this, ArticleActivity.class));
        }

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(SearchArticleActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
