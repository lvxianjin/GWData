package com.iscas.data.service.impl;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.NodeInfoService;
import com.iscas.data.tool.FileClient;
import com.iscas.data.tool.RedisClient;
import net.sf.json.JSONObject;
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
     *
     * @param: time 时间
     * @return: 符合格式的节点信息
     * @auther: lvxianjin
     * @date: 2019/10/22 19:20
     */
    @Override
    public List<Map<String, String>> getNodeInfo(String time) {
        List<Map<String, String>> info = nodeInfoDao.getNodeInfo(time);
        List<Map<String, String>> nodeInfo = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            Map<String, String> map = info.get(i);
            map.put("lng", map.get("location").split(",")[0]);
            map.put("lat", map.get("location").split(",")[1]);
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
            List<Map<String, String>> location = new ArrayList<>();
            while ((str = br.readLine()) != null) {
                String[] strs = str.split(",");
                if (strs.length != 5) {
                    info.add(location);
                    location = new ArrayList<>();
                    count = count + 1;
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("lng", strs[4]);
                    map.put("lat", strs[3]);
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
        List<Map<String, String>> info = new ArrayList<>();
        List<Map<String, String>> list = nodeInfoDao.getLineByLevel(level);
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<>();
            String from_location = nodeInfoDao.getLocationById(list.get(i).get("from"));
            String to_location = nodeInfoDao.getLocationById(list.get(i).get("to"));
            map.put("Flng", from_location.split(",")[0]);
            map.put("Flat", from_location.split(",")[1]);
            map.put("Tlng", to_location.split(",")[0]);
            map.put("Tlat", to_location.split(",")[1]);
            info.add(map);
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getErrorLine() {
        List<Map<String, String>> info = new ArrayList<>();
        List<Map<String, String>> errorLine = nodeInfoDao.getErrorLine();
        for (int i = 0; i < errorLine.size(); i++) {
            Map<String, String> map = errorLine.get(i);
            String from = nodeInfoDao.getLocationById(map.get("from"));
            String to = nodeInfoDao.getLocationById(map.get("to"));
            map.put("Flng", from.split(",")[0]);
            map.put("Flat", from.split(",")[1]);
            map.put("Tlng", to.split(",")[0]);
            map.put("Tlat", to.split(",")[1]);
            info.add(map);
        }
        return info;
    }

    @Override
    public Map<String, List> getNodeInfo() {
        Map<String, List> data = new HashMap<>();
        List<Map<String, String>> data1 = getNodeInfo("0");
        List<Map<String, String>> data2 = getRouteInfo("0");
        data.put("data1", data1);
        data.put("data2", data2);
        return data;
    }

    @Override
    public List<Map<String, String>> getRouteInfo(String time) {
        RedisClient redisClient = new RedisClient();
        String level = redisClient.getValue("id") + "kv";
        List<Map<String, String>> info = nodeInfoDao.getRouteInfo(time);
        List<Map<String, String>> route_info = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            Map<String, String> map = info.get(i);
            if (map.get("level").equals(level)) {
                map.put("color", String.valueOf((int) (Math.random() * (3 - 1) + 1)));
            } else {
                map.put("color", String.valueOf("0"));
            }
            String from = map.get("from");
            String to = map.get("to");
            map.put("Flng", nodeInfoDao.getLocationById(from).split(",")[0]);
            map.put("Flat", nodeInfoDao.getLocationById(from).split(",")[1]);
            map.put("Tlng", nodeInfoDao.getLocationById(to).split(",")[0]);
            map.put("Tlat", nodeInfoDao.getLocationById(to).split(",")[1]);
            if (map.get("head_active").contains("-")) {
                map.put("Flng", nodeInfoDao.getLocationById(to).split(",")[0]);
                map.put("Flat", nodeInfoDao.getLocationById(to).split(",")[1]);
                map.put("Tlng", nodeInfoDao.getLocationById(from).split(",")[0]);
                map.put("Tlat", nodeInfoDao.getLocationById(from).split(",")[1]);
                map.put("from", to);
                map.put("to", from);
            }
            route_info.add(map);
        }
        return route_info;
    }

    @Override
    public Map<String, String> getBasicInfo(String id) {
        Map<String, String> map = nodeInfoDao.getBasicInfo(id);
        map.put("v", map.get("v") + "kv");
        map.put("p", map.get("p") + "MW");
        map.put("q", map.get("q") + "MVar");
        return map;
    }

    @Override
    public List<Map<String, String>> getClusterById(String id) {
        RedisClient client = new RedisClient();
        String time = client.getValue("time");
        List<Map<String, String>> info = new ArrayList<>();
        List<Map<String, String>> list = nodeInfoDao.getCluster(id, time);
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            map.put("lng", map.get("location").split(",")[0]);
            map.put("lat", map.get("location").split(",")[1]);
            info.add(map);
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getRate() {
        List<Map<String, String>> info = new ArrayList<>();
        for (int i = 1; i < 40; i++) {
            Map<String, String> map = new HashMap<>();
            String location = nodeInfoDao.getLocationById(String.valueOf(i));
            map.put("lat", location.split(",")[0]);
            map.put("lng", location.split(",")[1]);
            map.put("count", String.valueOf((int) (Math.random() * (100 - 1) + 1)));
            info.add(map);
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getdcline() {
        List<Map<String, String>> list = nodeInfoDao.getdcline();
        List<Map<String, String>> info = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("Flng", nodeInfoDao.getLocationByName(list.get(i).get("from")).get("lng"));
            map.put("Flat", nodeInfoDao.getLocationByName(list.get(i).get("from")).get("lat"));
            map.put("from",list.get(i).get("from"));
            map.put("to",list.get(i).get("to"));
            map.put("Tlng", nodeInfoDao.getLocationByName(list.get(i).get("to")).get("lng"));
            map.put("Tlat", nodeInfoDao.getLocationByName(list.get(i).get("to")).get("lat"));
            info.add(map);
        }
        return info;
    }

    @Override
    public List<Map<String, List>> getAreaBorder(String area) {
        String rootPath = "/jar/data/";
        //String rootPath = "C:\\Users\\lvxianjin\\Desktop\\data\\";
        FileClient fileClient = new FileClient();
        List<Map<String, List>> info = new ArrayList<>();
        List<Map<String, String>> province = new ArrayList<>();
        if (area.equals("全国")) {
            province = nodeInfoDao.getProvince();
        } else {
            province = nodeInfoDao.getProvinceByArea(area);
        }
        for (int i = 0; i < province.size(); i++) {
            Map<String, List> map = new HashMap<>();
            List<String> contents = new ArrayList<>();
            List<String> names = new ArrayList<>();
            List<String> areas = new ArrayList<>();
            names.add(province.get(i).get("province"));
            map.put("name", names);
            List<List> data_list = getdata(rootPath + province.get(i).get("id") + ".txt");
            map.put("data", data_list);
            List<Map<String, String>> center_list = new ArrayList<>();
            Map<String, String> center = new HashMap<>();
            center.put("lng", province.get(i).get("center").split(",")[0]);
            center.put("lat", province.get(i).get("center").split(",")[1]);
            center_list.add(center);
            areas.add(nodeInfoDao.getAreaByName(province.get(i).get("province")));
            map.put("center", center_list);
            map.put("area",areas);
            JSONObject object = JSONObject.fromObject(map);
            contents.add(object.toString());
            //fileClient.writeResult(contents,rootPath+province.get(i).get("province")+".json");
            info.add(map);
        }
        return info;
    }

    @Override
    public List<List> getCityBorder(String cityName) {
        String rootPath = "/jar/data3/";
        FileClient fileClient = new FileClient();
        List<Map<String, String>> province = new ArrayList<>();
        List<List> data_list = getdata(rootPath + "1.txt");
        return data_list;
    }

    @Override
    public void updateType() {
        FileClient fileClient = new FileClient();
        List<String> st_names = fileClient.getContent("C:\\Users\\lvxianjin\\Desktop\\获取基础数据程序\\Scada_4\\Scada_data\\station\\name.txt");
        List<String> bv_ids = fileClient.getContent("C:\\Users\\lvxianjin\\Desktop\\获取基础数据程序\\Scada_4\\Scada_data\\station\\bv_id");
        for (int i = 0; i <st_names.size() ; i++) {
            nodeInfoDao.updateType(st_names.get(i).trim(),bv_ids.get(i).trim());
        }
    }

    @Override
    public List<List<Map<String,String>>> getBorderLine() {
        String rootPath = "/jar/data1/";
        FileClient fileClient = new FileClient();
        List<List<Map<String,String>>> info = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            List<String> content = fileClient.getContent(rootPath+i+".txt");
            List<Map<String,String>> location = new ArrayList<>();
            for (int j = 0; j < content.size(); j++) {
                Map<String,String> map = new HashMap<>();
                map.put("lng",content.get(j).split(",")[0]);
                map.put("lat",content.get(j).split(",")[1]);
                location.add(map);
            }
            info.add(location);
        }
        return info;
    }

    @Override
    public List<Map<String, String>> getCenterByArea(String area) {
        List<Map<String,String>> list = new ArrayList<>();
        List<Map<String,String>> center = nodeInfoDao.getCenterByArea(area);
        for (int i = 0; i < center.size(); i++) {
            Map<String,String> map = new HashMap<>();
            map.put("lng",center.get(i).get("center").split(",")[0]);
            map.put("lat",center.get(i).get("center").split(",")[1]);
            map.put("name",center.get(i).get("province"));
            map.put("e_name",center.get(i).get("english"));
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map> getCityByName(String name) {
        String rootPath = "/jar/data2/";
        //String rootPath = "C:\\Users\\lvxianjin\\Desktop\\黑龙江哈尔滨市\\";
        FileClient fileClient = new FileClient();
        List<Map> info = new ArrayList<>();
        List<Map<String,String>> cityInfo = nodeInfoDao.getCountyInfo(name);
        for (int i = 0; i < cityInfo.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            Map<String,String> center = new HashMap<>();
            center.put("lng",cityInfo.get(i).get("lng"));
            center.put("lat",cityInfo.get(i).get("lat"));
            System.out.println(rootPath + cityInfo.get(i).get("county_id") + ".txt");
            List<List> data_list = getdata(rootPath + cityInfo.get(i).get("county_id") + ".txt");
            map.put("name",cityInfo.get(i).get("county"));
            map.put("center",center);
            map.put("data", data_list);
            info.add(map);
        }
        return info;
    }

    List<List> getdata(String filePath) {
        List<List> info = new ArrayList<>();
        BufferedReader br = null;
        File file = new File(filePath);
        int count = 0;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
            String str;
            List<Map<String, String>> location = new ArrayList<>();
            while ((str = br.readLine()) != null) {
                String[] strs = str.split(",");
                if (strs.length != 5) {
                    info.add(location);
                    location = new ArrayList<>();
                    count = count + 1;
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("lng", strs[4]);
                    map.put("lat", strs[3]);
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
}
