package com.iscas.data.service.impl;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.JCInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:59
 * @Description:
 */
@Service
public class JCInfoServiceImpl implements JCInfoService {
    //安全性指标
    private int index_1 = 0;
    //经济型指标
    private int index_2 = 0;
    //综合指标
    private int index_3 = 0;
    //安全分析指标
    private int index_4 = 0;
    //暂态安全分析指标
    private int index_5 = 0;
    @Autowired
    private NodeInfoDao nodeInfoDao;
    @Override
    public List<String> getErrorInfo() {
        List<String> info = new ArrayList<>();
        for (int i = 0; i <6 ; i++) {
            int node_id = (int)(Math.random() * (39 - 1) + 1);
            String name = nodeInfoDao.getNameById(String.valueOf(node_id));
            info.add(name);
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getPolicyInfo() {
        return null;
    }

    @Override
    public Map<String, String> getIndex() {
        Map<String,String> info = new HashMap<>();
        index_1 = (int) (Math.random() * (85 - 30) + 30);
        index_2 = (int) (Math.random() * (85 - 30) + 30);
        index_3 = (int) (Math.random() * (85 - 30) + 30);
        index_4 = (int) (Math.random() * (85 - 30) + 30);
        index_5 = (int) (Math.random() * (85 - 30) + 30);
        info.put("index_1",String.valueOf(index_1));
        info.put("index_2",String.valueOf(index_2));
        info.put("index_3",String.valueOf(index_2));
        info.put("index_4",String.valueOf(index_2));
        info.put("index_5",String.valueOf(index_2));
        return info;
    }
}
