package com.funyoo.hqxapp.Activity;

import android.app.Activity;
import android.content.ContentValues;
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

import com.alibaba.fastjson.JSON;
import com.funyoo.hqxapp.model.Article;
import com.funyoo.hqxapp.util.MyDatabaseHelper;

import java.sql.Date;
import java.util.List;

public class SchoolListActivity extends Activity {
    private WebView webView = null;
    protected static MyDatabaseHelper databaseHelper = null;

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
        webView.loadUrl("file:///android_asset/schoolActivity.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用

        databaseHelper = new MyDatabaseHelper(SchoolListActivity.this, "articls", null, 1);
    }

    public final class JSInterface {

        @JavascriptInterface
        public void goSchoolActivity(String title, String initiator, String detail, String when,
                                     String where, String pic1, String pic2, String pic3, int id) {
            SchoolDetailActivity.id = id;
            SchoolDetailActivity.title = title;
            SchoolDetailActivity.initiator = initiator;
            SchoolDetailActivity.detail = detail;
            SchoolDetailActivity.when = when;
            SchoolDetailActivity.where = where;
            SchoolDetailActivity.pic1 = pic1;
            SchoolDetailActivity.pic2 = pic2;
            SchoolDetailActivity.pic3 = pic3;
            startActivity(new Intent(SchoolListActivity.this, SchoolDetailActivity.class));
        }

        @JavascriptInterface
        public void goPart(String partName) {
            if (partName.equals("me")) {
                startActivity(new Intent(SchoolListActivity.this, MeActivity.class));
            } else if (partName.equals("main")) {
                startActivity(new Intent(SchoolListActivity.this, MainActivity.class));
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
            values.put("date", JSON.toJSONString(article.getDate()));
            database.insert(part, null, values);

//            database.execSQL("INSERT INTO " + part + " " +
//                    "(id, title, htmlUrl, count, recommend, date) " +
//                    "VALUES" +
//                    "(" + id + "," + title + "," + htmlUrl + "," + count + "," + recommend + "," + date +")");
        }


    }
}
