package com.iscas.data.controller;

import com.iscas.data.service.JCInfoService;
import com.iscas.data.service.NodeInfoService;
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
    @RequestMapping("/getBorder.json")
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
}
