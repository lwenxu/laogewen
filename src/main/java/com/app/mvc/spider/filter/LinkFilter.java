package com.app.mvc.spider.filter;

/**
 * Created by wenheng on 2017/7/16.
 */
public interface LinkFilter {
    public boolean accept(String url,String... filter);
}
