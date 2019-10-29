package com.iscas.data.controller;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.JCInfoService;
import com.iscas.data.service.NodeInfoService;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * 坐标信息
 * @author : lvxianjin
 * @Date: 2019/10/23 13:13
 * @Description:
 */
@RestController
@CrossOrigin("*")
public class LocationController {
    private int count = 0;
    @Autowired
    private NodeInfoService nodeInfoService;
    @Autowired
    private JCInfoService jcInfoService;
    @RequestMapping("getBorder.json")
    public List<List> getBorder(){
        return nodeInfoService.getBorder();
    }
    @RequestMapping("getData.json")
    public Map<String,String> getDataById(@RequestParam String id){
        return jcInfoService.getDataById(id);
    }
    @RequestMapping(value = "getSystemMode")
    public Map<String,List> getSystemMode(){
        Map<String,List> info= jcInfoService.getSysMode("/jar/SystemMode.csv",count);
        count = count+2;
        if(count == 10){
            count = 0;
        }
        return info;
    }
    @RequestMapping(value = "getLineByLevel")
    public List<Map<String,String>> getLineByLevel(@RequestParam String level){
        return nodeInfoService.getLineByLevel(level+"kv");
    }
    @RequestMapping(value = "getNodeInfo.json")
    public Map<String,List> getNodeInfo(){
        return nodeInfoService.getNodeInfo();
    }
    @RequestMapping(value = "getJCInfo.json")
    public Map<String,String> getJCInfo(@RequestParam String id){
        return jcInfoService.getInfoById(id);
    }
    @RequestMapping(value = "getBasicInfo.json")
    public Map<String,String> getBasicInfo(@RequestParam String id){
        return nodeInfoService.getBasicInfo(id);
    }
    @RequestMapping(value = "getCluster.json")
    public List<Map<String,String>> getClusterByid(@RequestParam String id){
        return nodeInfoService.getClusterById(id);
    }
}
