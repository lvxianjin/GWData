package com.iscas.data.service.impl;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.NodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
/**
 * @author: lvxianjin
 * @Date: 2019/10/22 19:14
 * @Description:
 */
@Service
public class NodeInfoServiceImpl implements NodeInfoService {
    @Autowired
    private NodeInfoDao nodeInfoDao;
    /**
     * 功能描述: 通过时间获取节点信息
     * @param: time 时间
     * @return: 符合格式的节点信息
     * @auther: lvxianjin
     * @date: 2019/10/22 19:20
     */
    @Override
    public List<Map<String, String>> getNodeInfo(String time) {
        List<Map<String,String>> info = nodeInfoDao.getNodeInfo(time);
        List<Map<String,String>> nodeInfo = new ArrayList<>();
        for (int i = 0; i <info.size() ; i++) {
            Map<String,String> map = info.get(i);
            map.put("lng",map.get("location").split(",")[0]);
            map.put("lat",map.get("location").split(",")[1]);
            map.remove("location");
            nodeInfo.add(map);
        }
        return nodeInfo;
    }

    @Override
    public List<List> getBorder() {
        List<List> info = new ArrayList<>();
        BufferedReader br = null;
        File file = new File("/jar/json.txt");
        int count = 0;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String str;
            List<Map<String,String>> location = new ArrayList<>();
            while ((str = br.readLine()) != null) {
                String[] strs = str.split(",");
                if(strs.length != 5){
                    info.add(location);
                    location = new ArrayList<>();
                    count = count+1;
                }else {
                    Map<String,String> map = new HashMap<>();
                    map.put("lng",strs[4]);
                    map.put("lat",strs[3]);
                    location.add(map);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getRouteInfo(String time) {
        List<Map<String,String>> info = nodeInfoDao.getRouteInfo(time);
        List<Map<String,String>> route_info = new ArrayList<>();
        for (int i = 0; i <info.size() ; i++) {
            Map<String,String> map = info.get(i);
            String from = map.get("from");
            String to = map.get("to");
            map.put("Flng",nodeInfoDao.getLocationById(from).split(",")[0]);
            map.put("Flat",nodeInfoDao.getLocationById(from).split(",")[1]);
            map.put("Tlng",nodeInfoDao.getLocationById(to).split(",")[0]);
            map.put("Tlat",nodeInfoDao.getLocationById(to).split(",")[1]);
            if (map.get("head_active").contains("-")) {
                map.put("Flng",nodeInfoDao.getLocationById(to).split(",")[0]);
                map.put("Flat",nodeInfoDao.getLocationById(to).split(",")[1]);
                map.put("Tlng",nodeInfoDao.getLocationById(from).split(",")[0]);
                map.put("Tlat",nodeInfoDao.getLocationById(from).split(",")[1]);
                map.put("from", to);
                map.put("to", from);
            }
            route_info.add(map);
        }
        return route_info;
    }
}
