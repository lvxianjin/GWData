package com.iscas.data.service.impl;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.NodeInfoService;
import com.iscas.data.tool.RedisClient;
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
    public List<Map<String, String>> getLineByLevel(String level) {
        List<Map<String,String>> info = new ArrayList<>();
        List<Map<String,String>> list = nodeInfoDao.getLineByLevel(level);
        for (int i = 0; i <list.size() ; i++) {
            Map<String,String> map = new HashMap<>();
            String from_location = nodeInfoDao.getLocationById(list.get(i).get("from"));
            String to_location = nodeInfoDao.getLocationById(list.get(i).get("to"));
            map.put("Flng",from_location.split(",")[0]);
            map.put("Flat",from_location.split(",")[1]);
            map.put("Tlng",to_location.split(",")[0]);
            map.put("Tlat",to_location.split(",")[1]);
            info.add(map);
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getErrorLine() {
        List<Map<String,String>> info = new ArrayList<>();
        List<Map<String,String>> errorLine = nodeInfoDao.getErrorLine();
        for (int i = 0; i <errorLine.size() ; i++) {
            Map<String,String> map = errorLine.get(i);
            String from = nodeInfoDao.getLocationById(map.get("from"));
            String to = nodeInfoDao.getLocationById(map.get("to"));
            map.put("Flng",from.split(",")[0]);
            map.put("Flat",from.split(",")[1]);
            map.put("Tlng",to.split(",")[0]);
            map.put("Tlat",to.split(",")[1]);
            info.add(map);
        }
        return info;
    }

    @Override
    public Map<String, List> getNodeInfo() {
        Map<String,List> data = new HashMap<>();
        List<Map<String,String>> data1 = getNodeInfo("0");
        List<Map<String,String>> data2 = getRouteInfo("0");
        data.put("data1",data1);
        data.put("data2",data2);
        return data;
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

    @Override
    public Map<String, String> getBasicInfo(String id) {
        Map<String,String> map =nodeInfoDao.getBasicInfo(id);
        map.put("v",map.get("v")+"kv");
        map.put("p",map.get("p")+"MW");
        map.put("q",map.get("q")+"MVar");
        return map;
    }

    @Override
    public List<Map<String, String>> getClusterById(String id) {
        RedisClient client = new RedisClient();
        String time = client.getValue("time");
        List<Map<String, String>> info = new ArrayList<>();
        List<Map<String, String>> list = nodeInfoDao.getCluster(id,time);
        for (int i = 0; i <list.size() ; i++) {
            Map<String,String> map = list.get(i);
            map.put("lng",map.get("location").split(",")[0]);
            map.put("lat",map.get("location").split(",")[1]);
            info.add(map);
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getRate() {
        List<Map<String,String>> info = new ArrayList<>();
        for (int i = 1; i <40 ; i++) {
            Map<String,String> map = new HashMap<>();
            String location = nodeInfoDao.getLocationById(String.valueOf(i));
            map.put("lat",location.split(",")[0]);
            map.put("lng",location.split(",")[1]);
            map.put("count",String.valueOf((int) (Math.random() * (100 - 1) + 1)));
            info.add(map);
        }
        return info;
    }
}
