package com.funyoo.hqxapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.funyoo.hqxapp.model.Article;
import com.funyoo.hqxapp.model.Version;
import com.funyoo.hqxapp.service.UpdateManager;
import com.funyoo.hqxapp.util.MyDatabaseHelper;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class MainActivity extends Activity {
    private static WebView webView = null;
    private static boolean isChecked = false;
    protected static MyDatabaseHelper databaseHelper = null;

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

        // 设置缓存机制 2019/12/8
        // 根据cache-control决定是否从网络上取数据。
        // https://www.jianshu.com/p/f1efb0928ebc
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

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
        webView.loadUrl("file:///android_asset/main.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用

        databaseHelper = new MyDatabaseHelper(MainActivity.this, "articls", null, 1);

        if (!isChecked) {
            //Android 6.0以上版本需要临时获取权限
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1&&
                    PackageManager.PERMISSION_GRANTED!=checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                requestPermissions(perms,PERMS_REQUEST_CODE);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    new UpdateManager(MainActivity.this).checkUpdate(0);
                    System.out.println("检查版本");
                    isChecked = true;
                    Looper.loop();
                }
            }).start();
        }

    }

    public final class JSInterface {

        @JavascriptInterface
        public void toArticle(String htmlUrl, int id) {
            ArticleActivity.setHtmlUrl(htmlUrl);
            ArticleActivity.setArtId(id);
            startActivity(new Intent(MainActivity.this, ArticleActivity.class));
        }

        @JavascriptInterface
        public void goPart(String partName) {
            if (partName.equals("me")) {
                startActivity(new Intent(MainActivity.this, MeActivity.class));
            } else if (partName.equals("school")) {
                startActivity(new Intent(MainActivity.this, SchoolListActivity.class));
            }
            overridePendingTransition(0, 0);
            finish();
        }

        @JavascriptInterface
        public void saveArticle(String part, String articleJson) {
            Article article = JSON.parseObject(articleJson, Article.class);

            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", article.getId());
            values.put("title", article.getTitle());
            values.put("type", article.getType());
            values.put("htmlUrl", article.getHtmlUrl());
            values.put("picUrl", article.getPicUrl());
            values.put("count", article.getCount());
            values.put("recommend", article.getRecommend());
            values.put("date", article.getDate());
            database.insert(part, null, values);

//            database.execSQL("INSERT INTO " + part + " " +
//                    "(id, title, htmlUrl, count, recommend, date) " +
//                    "VALUES" +
//                    "(" + id + "," + title + "," + htmlUrl + "," + count + "," + recommend + "," + date +")");
        }

        @JavascriptInterface
        public void cleanArticles(String part) {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            database.execSQL("DELETE * from " + part );
        }

        @JavascriptInterface
        public String getTopArticle(String part) {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * from " + part + " WHERE recommend = 2 ORDER BY id DESC LIMIT 1", null);

            if (cursor == null) {
                return null;
            }
            Article article = buildArticle(cursor);
            return JSON.toJSONString(article);
        }

        @JavascriptInterface
        public String getArticles(String part) {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            List<Article> articles = null;
            Cursor cursor = null;
            cursor = database.rawQuery("SELECT * from " + part +
                    " ORDER BY id DESC ", null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Article article = buildArticle(cursor);
                    articles.add(article);
                }
                return JSON.toJSONString(articles);
            }
            return null;
        }

        private Article buildArticle(Cursor cursor) {
            Article article = new Article();
            article.setId(cursor.getInt(cursor.getColumnIndex("id")));
            article.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            article.setType(cursor.getString(cursor.getColumnIndex("type")));
            article.setHtmlUrl(cursor.getString(cursor.getColumnIndex("htmlUrl")));
            article.setPicUrl(cursor.getString(cursor.getColumnIndex("picUrl")));
            article.setCount(cursor.getInt(cursor.getColumnIndex("count")));
            article.setRecommend(cursor.getInt(cursor.getColumnIndex("recommed")));
            article.setDate(cursor.getString(cursor.getColumnIndex("date")));
            return article;
        }

    }
}
