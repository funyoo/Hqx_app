package com.funyoo.hqxapp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.funyoo.hqxapp.model.Article;
import com.funyoo.hqxapp.model.User;
import com.funyoo.hqxapp.util.MyDatabaseHelper;
import com.funyoo.hqxapp.util.SharedPreUtil;

import java.util.List;

public class CollectionActivity extends Activity {
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
        webView.loadUrl("file:///android_asset/collection.html");
        webView.addJavascriptInterface(new CollectionActivity.JSInterface(), "android");//开放接口给js调用

        //databaseHelper = new MyDatabaseHelper(CollectionActivity.this, "articls", null, 1);
    }


    public final class JSInterface {

        @JavascriptInterface
        public void toReturn() {
            CollectionActivity.this.finish();
        }

        @JavascriptInterface
        public void toArticle(String htmlUrl, int aid) {
            ArticleActivity.setHtmlUrl(htmlUrl);
            ArticleActivity.setArtId(aid);
            startActivity(new Intent(CollectionActivity.this, ArticleActivity.class));
        }

        @JavascriptInterface
        public int getUid() {
            String userStr = (String) SharedPreUtil.getParam(CollectionActivity.this, SharedPreUtil.LOGIN_DATA, "");
            if (!userStr.equals("")) {
                User user = JSON.parseObject(userStr, User.class);
                if (user != null) {
                    return user.getId();
                }
            }
            return -1;
        }

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(CollectionActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
