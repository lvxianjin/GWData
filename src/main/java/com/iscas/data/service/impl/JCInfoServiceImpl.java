package com.iscas.data.service.impl;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.JCInfoService;
import com.iscas.data.tool.FileClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:59
 * @Description: 控件信息
 */
@Service
public class JCInfoServiceImpl implements JCInfoService {
    /*
     * 安全性指标
     * */
    private int index_1 = 0;
    /*
     * 经济型指标
     * */
    private int index_2 = 0;
    /*
     * 综合指标
     * */
    private int index_3 = 0;
    /*
     * 静态安全分析指标
     * */
    private int index_4 = 0;
    /*
     * 暂态安全分析指标
     * */
    private int index_5 = 0;
    @Autowired
    private NodeInfoDao nodeInfoDao;

    @Override
    public List<String> getErrorInfo() {
        List<String> info = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int node_id = (int) (Math.random() * (39 - 1) + 1);
            String name = nodeInfoDao.getNameById(String.valueOf(node_id));
            info.add(name);
        }
        return info;
    }

    @Override
    public Map<String, String> getIndex() {
        Map<String, String> info = new HashMap<>();
        index_1 = (int) (Math.random() * (85 - 30) + 30);
        index_2 = (int) (Math.random() * (85 - 30) + 30);
        index_3 = (int) (Math.random() * (85 - 30) + 30);
        index_4 = (int) (Math.random() * (85 - 30) + 30);
        index_5 = (int) (Math.random() * (85 - 30) + 30);
        info.put("index_1", String.valueOf(index_1));
        info.put("index_2", String.valueOf(index_2));
        info.put("index_3", String.valueOf(index_3));
        info.put("index_4", String.valueOf(index_4));
        info.put("index_5", String.valueOf(index_5));
        return info;
    }

    @Override
    public Map<String, String> getDataByName(String station_name) {
        Map<String, String> map = new HashMap<>();
        String Secure_Rate = "0.00" + String.valueOf((int) (Math.random() * (6 - 2) + 2));
        String UNSecure_Rate = String.valueOf(1 - Double.parseDouble(Secure_Rate));
        double d = ((int) (Math.random() * (90 - 10) + 10)) * 0.01;
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数。
        Nformat.setMaximumFractionDigits(2);
        // 执行格式化转换。
        String cct = Nformat.format(d);
        map.put("Secure_Rate", Secure_Rate);
        map.put("UNSecure_Rate", UNSecure_Rate);
        map.put("cct", cct);
        return map;
    }

    @Override
    public Map<String, String> getDataById(String id) {
        String name = nodeInfoDao.getNameById(id);
        Map<String,String> map = getDataByName(name);
        map.put("name",name);
        return map;
    }

    @Override
    public Map<String, List> getSysMode(String filePath, int count) {
        Map<String,String> map = new HashMap<>();
        Map<String,List> data = new HashMap<>();
        List<Map<String,String>> data1 = new ArrayList<>();
        List<Map<String,String>> data2 = new ArrayList<>();
        FileClient client = new FileClient();
        List<String> info = client.LoadFile(filePath);
        String[] name = info.get(count).split(",");
        String[] value = info.get(count+1).split(",");
        map.put("address","华中地区");
        map.put("damping",value[0]);
        map.put("frequency",value[1]);
        data1.add(map);
        for (int i = 2; i < 12; i++) {
            Map<String,String> map2 = new HashMap<>();
            map2.put("name",name[i]);
            map2.put("i",value[i].split(" ")[0]);
            map2.put("j",value[i].split(" ")[1].substring(0,value[i].split(" ")[1].indexOf("j")));
            data2.add(map2);
        }
        data.put("data1",data1);
        data.put("data2",data2);
        return data;
    }
}
