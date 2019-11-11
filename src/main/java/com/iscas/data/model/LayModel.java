package com.iscas.data.model;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/11/4 09:47
 * @Description:
 */
public class LayModel {
    private int code;
    private String msg;
    private int count;
    private List<Map<String,String>> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
