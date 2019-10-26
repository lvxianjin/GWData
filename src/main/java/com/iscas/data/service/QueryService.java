package com.iscas.data.service;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 15:14
 * @Description:
 */
public interface QueryService {
    /**
     *
     * 功能描述: 获取节点的稳定次数
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/26 15:17
     */
    public List<Map<String, String>> getSecureRate(String station_name);
    }
