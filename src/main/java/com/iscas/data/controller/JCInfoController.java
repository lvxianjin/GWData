package com.iscas.data.controller;

import com.iscas.data.service.JCInfoService;
import com.iscas.data.tool.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
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
        //报警个数
        List<Integer> error_number = new ArrayList<>();
        //策略表
        List<Map<String,String>> data2_list = new ArrayList<>();
        //五大指标
        List<Map<String,String>> data3_list = new ArrayList<>();
        int high = 0;
        int middle = 0;
        int low = 0 ;
        List<String> names = jcInfoService.getErrorInfo();
        RedisClient client = new RedisClient();
        String time = client.getValue("time");
        if(Double.parseDouble(time)>=1.01&&Double.parseDouble(time)<=1.22){
            String message = df.format(new Date())+"  三相短路  "+"高风险";
            data1_list.add(message);
            high = 1;
        }
        for (int i = 0; i <names.size() ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("station_name",names.get(i));
            double police = ((int) (Math.random() * (8 - 2) + 2)) * 0.01;;
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            if(i ==0){
                map.put("FDJ_name","Ⅲ段母线");
                map.put("Police",Nformat.format(police));
            }else if(i ==1){
                map.put("FDJ_name","2#发电机");
                map.put("Police",Nformat.format(police));
            }else if(i ==2){
                map.put("FDJ_name","Ⅴ段母线");
                map.put("Police","-"+Nformat.format(police));
            }else if(i ==3){
                map.put("FDJ_name","Ⅵ段母线");
                map.put("Police",Nformat.format(police));
            }else if(i ==4){
                map.put("FDJ_name","1#发电机");
                map.put("Police","-"+Nformat.format(police));
            }else {
                map.put("FDJ_name","3#发电机");
                map.put("Police","-"+Nformat.format(police));
            }
            double before = ((int) (Math.random() * (30 - 10) + 10))*0.01;
            double after = ((int) (Math.random() * (70 - 50) + 50))*0.01;
            map.put("before",Nformat.format(before));
            map.put("after",Nformat.format(after));
            String error = "";
            if(before<=0.2){
                error = df.format(new Date())+"  "+ names.get(i)+"  "+"一般风险";
                middle = middle+1;
            }else {
                error = df.format(new Date())+"  "+ names.get(i)+"  "+"低风险";
                low = low+1;
            }
            data1_list.add(error);
            data2_list.add(map);
        }
        data3_list.add(jcInfoService.getIndex());
        error_number.add(high);
        error_number.add(middle);
        error_number.add(low);
        data_map.put("data1",data1_list);
        data_map.put("data2",data2_list);
        data_map.put("data3",data3_list);
        data_map.put("data4",error_number);
        return data_map;
    }
    @RequestMapping("getHZInfo.json")
    public List<Map<String,String>> getHZInfo(){
        return jcInfoService.getHZInfo();
    }
}
