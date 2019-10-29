package com.iscas.data.controller;

import com.iscas.data.model.Message;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.*;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 16:00
 * @Description:
 */
@RestController
@CrossOrigin("*")
public class QueryController {
    @RequestMapping(value = "/queryPMUData", method = RequestMethod.GET)
    @ResponseBody
    public String queryPMUData(@RequestParam String station,
                                                 @RequestParam String type,
                                                 @RequestParam String start_date,
                                                 @RequestParam String end_date,
                                                 @RequestParam int page,
                                                 @RequestParam int limit){
        List<Map<String, String>> data = new ArrayList<>();
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数。
        Nformat.setMaximumFractionDigits(2);
        double value = 0;
        for (int i = 0; i<50 ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("date",start_date);
            map.put("station_name",station);
            if(type.equals("电压")){
                value = ((int) (Math.random() * (700000 - 300000) + 300000)) * 0.001;
                map.put("des",start_date+"UAV");
                map.put("value",Nformat.format(value)+"kv");
            }else if(type.equals("电流")){
                value = ((int) (Math.random() * (70 - 30) + 30)) * 0.001;
                map.put("des",start_date+"IAA");
                map.put("value",Nformat.format(value)+"A");
            }else if(type.equals("有功功率")){
                value = ((int) (Math.random() * (700 - 300) + 300)) * 0.0001;
                map.put("des",start_date+"00P");
                if(i%2 == 0){
                    map.put("value",Nformat.format(value)+"MW");
                }else {
                    map.put("value","-"+Nformat.format(value)+"MW");
                }

            }else {
                value = ((int) (Math.random() * (700 - 300) + 300)) * 0.0001;
                map.put("des",start_date+"00Q");
                map.put("value","-"+Nformat.format(value)+"MVar");
            }
            data.add(map);
        }
        int start = (page-1)*limit;
        Message message = new Message(0,"",1000,data.subList(start,start+limit-1));
        System.out.println(data.size());
        return JSONObject.fromObject(message).toString();
    }
}
