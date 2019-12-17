package com.funyoo.hqxapp.model;

public class Version {
    private String downLoadUrl;
    private Integer versionCode;
    private String versionName;
    private String versionDes;

    public Version() {
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDes() {
        return versionDes;
    }

    public void setVersionDes(String versionDes) {
        this.versionDes = versionDes;
    }

    @Override
    public String toString() {
        return "Version{" +
                "downLoadUrl='" + downLoadUrl + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", versionDes='" + versionDes + '\'' +
                '}';
    }
}
