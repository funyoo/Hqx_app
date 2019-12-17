package com.funyoo.hqxapp.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funyoo.hqxapp.Activity.R;
import com.funyoo.hqxapp.model.Version;
import com.funyoo.hqxapp.util.AppUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author funyoo
 * @date 2019/12/03 17:58
 *
 * 软件更新管理器
 */
public class UpdateManager {

    private static final int UPDATE_TOKEN = 0x29; // handler 标识
    private static final int INSTALL_TOKEN = 0x31; // handler 标识

    private Context context;          // 上下文，即在哪个activity上
    private Dialog dialog;            // 弹窗对象
    private ProgressBar progressBar;  // 进度条对象
    private volatile int curProcess;  // 当前进度标识
    private boolean isCancle;         // 是否取消标志

    private Version version;          // 版本对象

    // handler对象，可以理解为通知线程
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TOKEN:
                    System.out.println(curProcess);
                    progressBar.setProgress(curProcess);
                    break;

                case INSTALL_TOKEN:
                    installApp();
                    break;
            }
        }
    };

    public UpdateManager(Context context) {
        this.context = context;
    }

    /**
     * 检查是否有最新版本
     * @param flag 1:没有新版本时显示当前版本信息
     */
    public void checkUpdate(int flag) {
        try {
            version = getkUpdate();
        } catch (IOException e) {
            Toast.makeText(context, "小红旗出错啦~ 请稍后再试~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isNewVersion(version)) {
            dialog = new AlertDialog.Builder(context)
                    .setTitle("软件版本更新")
                    .setMessage(version.getVersionDes())
                    .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                            showDownloadDiglog();
                        }
                    }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        } else if (flag == 1) {
            dialog = new AlertDialog.Builder(context)
                    .setTitle("当前版本已是最新版本！")
                    .setMessage(version.getVersionDes())
                    .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    /**
     * 显示下载弹窗
     */
    private void showDownloadDiglog() {
        View view = LayoutInflater.from(context).inflate(R.layout.processbar, null);
        progressBar = view.findViewById(R.id.processBar);
        progressBar.setMax(100);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("版本更新");
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                isCancle = true;
            }
        });
        dialog = builder.create();
        dialog.show();
        downloadApp();
    }

    /**
     * 下载
     */
    private void downloadApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                HttpURLConnection connection = null;
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    url = new URL(version.getDownLoadUrl());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    long fileLength = connection.getContentLength();
                    is = connection.getInputStream();
                    File fileDir = new File(AppUtil.getApkDir());
                    if (!fileDir.exists()) {
                        fileDir.mkdir();
                    }
                    fos = new FileOutputStream(AppUtil.getSaveFilePath(version.getDownLoadUrl()));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLen = 0;
                    while ((len = is.read(buffer)) != -1) {
                        if (isCancle) {
                            break;
                        }
                        fos.write(buffer, 0, len);
                        readedLen += len;
                        curProcess = (int) ((float)readedLen / fileLength * 100);
                        handler.sendEmptyMessage(UPDATE_TOKEN);
                        if (readedLen >= fileLength) {
                            dialog.dismiss();
                            handler.sendEmptyMessage(INSTALL_TOKEN);
                            break;
                        }
                    }
                    fos.flush();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File file = new File(AppUtil.getSaveFilePath(version.getDownLoadUrl()));
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName()+ ".fileprovider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 查询服务器最新app版本
     * @return 版本实例
     */
    private Version getkUpdate() throws IOException {
        URL url = new URL("http://114.116.118.232:8081/version/checkUpdate");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String res = "";
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            res += line;
        }
        JSONObject object = JSONObject.parseObject(res);
        if (object == null || !object.getString("code").equals("0")) {
            return null;
        }
        res = object.getString("data");
        Version version = JSONObject.parseObject(res, Version.class);
        String[] des = version.getVersionDes().split("&");
        String value = "";
        for (String str : des) {
            value += str + '\n';
        }
        version.setVersionDes(value);
        return version;
    }

    /**
     * 判断版本是否为新版本
     * @param version  版本实例
     * @return
     */
    private boolean isNewVersion(Version version) {
        int old = AppUtil.getVersionCode(context);
        if (old < version.getVersionCode()) {
            return true;
        }
        return false;
    }
}
