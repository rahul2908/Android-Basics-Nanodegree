package com.example.dell.newsapp;

/**
 * Created by dell on 3/7/2017.
 */

public class News {

    private String sectionName;
    private String webTitle;
    private String webPublicationDate;
    private String webUrl;

    public News(String sc_name, String title, String date, String url){
        this.sectionName =   sc_name;
        this.webTitle = title;
        this.webPublicationDate = date;
        this.webUrl = url;
    }

    public String getSectionName(){
        return sectionName;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
