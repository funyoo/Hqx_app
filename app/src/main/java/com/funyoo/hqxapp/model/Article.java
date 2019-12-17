package com.funyoo.hqxapp.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 文章实例
 */
public class Article {

    private Integer id;

    private String title;

    private String type;

    private String htmlUrl;

    private Integer count;

    private Integer recommend;

    private String date;

    private Timestamp createdTime;

    // 首页时所需要的图片URL
    private String picUrl;

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public int getRecommend() {
        return recommend;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", count=" + count +
                ", recommend=" + recommend +
                ", date=" + date +
                ", createdTime=" + createdTime +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}

