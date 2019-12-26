package com.funyoo.hqxapp.Activity;

import android.app.Activity;
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
import com.alibaba.fastjson.JSONObject;
import com.funyoo.hqxapp.model.User;
import com.funyoo.hqxapp.util.MyDatabaseHelper;
import com.funyoo.hqxapp.util.SharedPreUtil;
import com.funyoo.hqxapp.util.TimeCount;

public class LoginActivity extends Activity {

    private WebView webView = null;

    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new MyDatabaseHelper(this, "UserDB.db", null, 1);

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

//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return motionEvent.getAction() == MotionEvent.ACTION_MOVE;
//            }
//        });
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl( "javascript:window.location.reload( true )" );
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/login.html");
        webView.addJavascriptInterface(new JSInterface(), "android");//开放接口给js调用


        // 检查用户是否登录 是：直接首页  否：登录页
//        Object isLogin = SharedPreUtil.getParam
//                (LoginActivity.this, SharedPreUtil.IS_LOGIN, Boolean.class);
//        if (isLogin != null) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    public final class JSInterface {
        @JavascriptInterface
        public void showMessage(String message) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void loginSuccess(String userinfo) {

            // 初始化用户信息
            User user = JSONObject.parseObject(userinfo, User.class);
            if (user.getNick() == null || user.getNick().equals("")) {
                user.setNick("小红旗-" + user.getId());
            }
            if (user.getShowing() == null || user.getShowing().equals("")) {
                user.setShowing("此人很懒，暂无个性签名");
            }
            userinfo = JSONObject.toJSONString(user);
            // 缓存用户信息(json字符串)
            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.IS_LOGIN, true);
            SharedPreUtil.setParam(LoginActivity.this, SharedPreUtil.LOGIN_DATA, userinfo);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            TimeCount.getInstance().setTime(System.currentTimeMillis());
            startActivity(intent);
            finish();
        }


        @JavascriptInterface
        public void toRegister() {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void login(String mail) {

            // 登录成功后保存用户身份
//            SQLiteDatabase db = databaseHelper.getReadableDatabase();
//            Cursor cursor = db.rawQuery("select * from User where name=?", new String[]{mail});
//
//            if (cursor.moveToFirst()) {
//                String dbPass = cursor.getString(cursor.getColumnIndex("password"));
//
//            }
        }
    }
}
