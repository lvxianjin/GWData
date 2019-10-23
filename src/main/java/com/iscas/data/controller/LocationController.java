package com.iscas.data.controller;

import com.iscas.data.service.JCInfoService;
import com.iscas.data.service.NodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
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
    @Autowired
    private NodeInfoService nodeInfoService;
    @Autowired
    private JCInfoService jcInfoService;
    @RequestMapping("/getBorder.json")
    public List<List> getBorder(){
        return nodeInfoService.getBorder();
    }
    @RequestMapping("getJC.json")
    public Map<String,List> getJCInfo(){
        Map<String,List> data_map = new HashMap<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        List<String> data1_list = new ArrayList<>();
        List<Map<String,String>> data2_list = new ArrayList<>();
        List<String> names = jcInfoService.getErrorInfo();
        for (int i = 0; i <names.size() ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("station_name",names.get(i));
            map.put("FDJ_name","");
            map.put("Police","-0.5");
            map.put("before","0.2");
            map.put("after","0.6");
            String error = df.format(new Date())+" "+ names.get(i)+" "+"不稳定";
            data1_list.add(error);
            data2_list.add(map);
        }
        data_map.put("data1",data1_list);
        data_map.put("data2",data2_list);
        return data_map;
    }
}
