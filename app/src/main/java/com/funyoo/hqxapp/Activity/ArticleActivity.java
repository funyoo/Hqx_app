package com.funyoo.hqxapp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.funyoo.hqxapp.model.User;
import com.funyoo.hqxapp.util.SharedPreUtil;

public class ArticleActivity extends Activity {
    private WebView webView = null;
    private static String htmlUrl;
    private static Integer artId;

    public static void setHtmlUrl(String html) {
        htmlUrl = html;
    }

    public static void setArtId(Integer id) {
        ArticleActivity.artId = id;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 从assets目录下面的加载html
        webView.loadUrl(htmlUrl);
        webView.addJavascriptInterface(new JSInterface(),"android");
    }

    /**
     * 重写方法: 为什么这个Activity需要重写，而其他则大都不需要？
     * 原因：其他的webView加载的html是静态的模板，其url不会变化
     *      而这个Activity webView的url是不断变化的
     *      每个从网络加载url app会下载缓存再进行加载
     *      如果不清理，会导致app占据消耗的空间越来越大
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }
    public void destroyWebView() {
        webView.removeAllViews();
        if(webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.freeMemory();
            webView = null;
        }

    }

    public final class JSInterface {
        @JavascriptInterface
        public void toReturn() {
            ArticleActivity.this.finish();
        }

        // TODO 判断是否用户已经收藏 通过用户id查用户收藏表的文章（part与id）
        // TODO 点击收藏后保存提供文章属性 至 某方法  本地保存 并 异步要求服务器保存提交insert
        // TODO 分享功能

        @JavascriptInterface
        public int getUserId() {
            String userStr = (String) SharedPreUtil.getParam(ArticleActivity.this, SharedPreUtil.LOGIN_DATA, "");
            if (!userStr.equals("")) {
                User user = JSON.parseObject(userStr, User.class);
                if (user != null) {
                    return user.getId();
                }
            }
            return -1;
        }

        @JavascriptInterface
        public int getArtId() {
            return ArticleActivity.artId;
        }
    }
}
