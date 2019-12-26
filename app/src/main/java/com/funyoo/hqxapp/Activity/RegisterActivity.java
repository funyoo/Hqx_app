package com.funyoo.hqxapp.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.funyoo.hqxapp.util.MyDatabaseHelper;

public class RegisterActivity extends Activity {

    private MyDatabaseHelper dbHelper;

    private WebView webView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

        setContentView(R.layout.activity_welcome);
        webView = (WebView) findViewById(R.id.webview);
        // 启用javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setBlockNetworkImage(false);
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
        webView.loadUrl( "javascript:window.location.reload( true )" );
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/register.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用
    }

    public final class JSInterface {
        @JavascriptInterface
        public void register() {

//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put("id", id);
//            values.put("mail", mail);
//
//            db.insert("User", null, values);

            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            finish();
        }

        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
