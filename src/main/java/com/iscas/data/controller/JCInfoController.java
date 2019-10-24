package com.iscas.data.controller;

import com.iscas.data.service.JCInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : lvxianjin
 * @Date: 2019/10/24 12:14
 * @Description: 决策信息
 */
@RestController
@CrossOrigin("*")
public class JCInfoController {
    @Autowired
    private JCInfoService jcInfoService;
    @RequestMapping("getJC.json")
    public Map<String, List> getJCInfo(){
        Map<String,List> data_map = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        //报警信息
        List<String> data1_list = new ArrayList<>();
        //策略表
        List<Map<String,String>> data2_list = new ArrayList<>();
        //五大指标
        List<Map<String,String>> data3_list = new ArrayList<>();
        List<String> names = jcInfoService.getErrorInfo();
        for (int i = 0; i <names.size() ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("station_name",names.get(i));
            if(i ==0){
                map.put("FDJ_name","Ⅲ段母线");
            }else if(i ==1){
                map.put("FDJ_name","2#发电机");
            }else if(i ==2){
                map.put("FDJ_name","Ⅴ段母线");
            }else if(i ==3){
                map.put("FDJ_name","Ⅵ段母线");
            }else if(i ==4){
                map.put("FDJ_name","1#发电机");
            }else {
                map.put("FDJ_name","3#发电机");
            }
            map.put("Police","-0.5");
            map.put("before","0.2");
            map.put("after","0.6");
            String error = df.format(new Date())+" "+ names.get(i)+" "+"不稳定";
            data1_list.add(error);
            data2_list.add(map);
        }
        data3_list.add(jcInfoService.getIndex());
        data_map.put("data1",data1_list);
        data_map.put("data2",data2_list);
        data_map.put("data3",data3_list);
        return data_map;
    }
    @RequestMapping("getHZInfo.json")
    public List<Map<String,String>> getHZInfo(){
        return jcInfoService.getHZInfo();
    }
}
