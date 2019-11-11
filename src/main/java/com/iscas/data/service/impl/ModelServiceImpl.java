package com.iscas.data.service.impl;
import com.iscas.data.model.Message;
import com.iscas.data.service.ModelService;
import org.springframework.stereotype.Service;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 13:34
 * @Description:
 */
@Service
public class ModelServiceImpl implements ModelService {
    private List<Map<String,String>> model_data = new ArrayList<>();
    @Override
    public Message getInfoByType(String type,int page,int limit) {
        model_data = new ArrayList<>();
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
            model_data.add(map);
        }
        int start = (page-1)*limit;
        Message msg = new Message(1000,info.subList(start,start+limit-1),limit,page);
        return msg;
    }

    @Override
    public List<Map<String,String>> getInfoByName(String name) {
        List<Map<String,String>> info = new ArrayList<>();
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数。
        Nformat.setMaximumFractionDigits(2);
        System.out.println(model_data);
        for (int i = 0; i <model_data.size() ; i++) {
            Map<String,String> map = new HashMap<>();
            int u = (int) (Math.random() * (20000 - 500) + 500);
            double p = (int) (Math.random() * (800 - 200) + 200)*0.1;
            double q = (int) (Math.random() * (200 - 10) + 10)*0.01;
            Calendar calendar = Calendar.getInstance();
            if(name.equals(model_data.get(i).get("name"))){
                map.put("BU",String.valueOf(Double.parseDouble(model_data.get(i).get("U"))-u));
                map.put("BP",Nformat.format(Double.parseDouble(model_data.get(i).get("P"))-p));
                map.put("BQ",Nformat.format(Double.parseDouble(model_data.get(i).get("Q"))-q));
                map.put("AU",model_data.get(i).get("U"));
                map.put("AP",model_data.get(i).get("P"));
                map.put("AQ",model_data.get(i).get("Q"));
                map.put("time",String.valueOf(calendar.get(Calendar.SECOND)));
                info.add(map);
            }
        }
        System.out.println(info);
        return info;
    }
}
