package com.iscas.data.service.impl;
import com.iscas.data.model.Message;
import com.iscas.data.service.ModelService;
import org.springframework.stereotype.Service;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 13:34
 * @Description:
 */
@Service
public class ModelServiceImpl implements ModelService {
    @Override
    public Message getInfoByType(String type,int page,int limit) {
        List<Map<String,String>> info = new ArrayList<>();
        String name = "";
        if("1".equals(type)){
            name = "风电机组";
        }else if("2".equals(type)){
            name = "火电机组";
        }else if("3".equals(type)){
            name = "水电机组";
        }else if("4".equals(type)){
            name = "核电机组";
        }else if("5".equals(type)){
            name = "直流线路";
        }else if("6".equals(type)){
            name = "交流线路";
        }else if("7".equals(type)){
            name = "静态负荷模型";
        }else if("8".equals(type)){
            name = "感应电动机模型";
        }else if("9".equals(type)){
            name = "配网支路模型";
        }
        for (int i = 0; i <1000 ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("name",name+String.valueOf(i+1));
            int u = (int) (Math.random() * (20000 - 500) + 500);
            double p = (int) (Math.random() * (800 - 200) + 200)*0.1;
            double q = (int) (Math.random() * (200 - 10) + 10)*0.01;
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            if(i%2==0){
                map.put("U",String.valueOf(295070-u));
                map.put("P",Nformat.format(366.101-p));
                map.put("Q","-"+Nformat.format(5.86096-q));
            }else {
                map.put("U",String.valueOf(295070+u));
                map.put("P",Nformat.format(366.101+p));
                map.put("Q","-"+Nformat.format(5.86096+q));
            }
            info.add(map);
        }
        int start = (page-1)*limit;
        Message msg = new Message(0,"",1000,info.subList(start,start+limit-1));
        return msg;
    }
}
