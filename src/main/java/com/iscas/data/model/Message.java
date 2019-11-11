package com.iscas.data.model;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 16:43
 * @Description:
 */
public class Message {
    private int total;
    private List<Map<String,String>> data;
    private int limit;
    private int page;

    public Message(int total, List<Map<String, String>> data, int limit, int page) {
        this.total = total;
        this.data = data;
        this.limit = limit;
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
