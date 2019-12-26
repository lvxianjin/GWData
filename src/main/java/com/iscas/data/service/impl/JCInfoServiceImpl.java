package com.iscas.data.service.impl;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.JCInfoService;
import com.iscas.data.tool.FileClient;
import com.iscas.data.tool.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:59
 * @Description: 控件信息
 */
@Service
public class JCInfoServiceImpl implements JCInfoService {
    /*
     * 安全性指标
     * */
    private int index_1 = 0;
    /*
     * 经济型指标
     * */
    private int index_2 = 0;
    /*
     * 综合指标
     * */
    private int index_3 = 0;
    /*
     * 静态安全分析指标
     * */
    private int index_4 = 0;
    /*
     * 暂态安全分析指标
     * */
    private int index_5 = 0;
    /*
    * 薄弱节点id
    * */
    List<Integer> ids = new ArrayList<>();
    /*
    * 策略表信息
    * */
    List<Map<String, String>> data2_list = new ArrayList<>();
    /*
    * 六大地区振荡频率和阻尼比信息
     */
    List<Map<String, String>> area_info = new ArrayList<>();
    /*
    * 安全概率
    * */
    Map<String, String> Secure_map = new HashMap<>();
    @Autowired
    private NodeInfoDao nodeInfoDao;

    @Override
    public List<String> getErrorInfo() {
        List<String> info = new ArrayList<>();
        ids = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int node_id = (int) (Math.random() * (39 - 1) + 1);
            ids.add(node_id);
            String name = nodeInfoDao.getNameById(String.valueOf(node_id));
            info.add(name);
        }
        return info;
    }

    @Override
    public Map<String, String> getIndex() {
        Map<String, String> info = new HashMap<>();
        index_1 = (int) (Math.random() * (85 - 30) + 30);
        index_2 = (int) (Math.random() * (85 - 30) + 30);
        index_3 = (int) (Math.random() * (85 - 30) + 30);
        index_4 = (int) (Math.random() * (85 - 30) + 30);
        index_5 = (int) (Math.random() * (85 - 30) + 30);
        info.put("index_1", String.valueOf(index_1));
        info.put("index_2", String.valueOf(index_2));
        info.put("index_3", String.valueOf(index_3));
        info.put("index_4", String.valueOf(index_4));
        info.put("index_5", String.valueOf(index_5));
        return info;
    }

    @Override
    public Map<String, String> getDataByName(String station_name) {
        Secure_map = new HashMap<>();
        String Secure_Rate = "0.00" + String.valueOf((int) (Math.random() * (6 - 2) + 2));
        String UNSecure_Rate = String.valueOf(1 - Double.parseDouble(Secure_Rate));
        double d = ((int) (Math.random() * (90 - 10) + 10)) * 0.01;
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数
        Nformat.setMaximumFractionDigits(2);
        // 执行格式化转换
        String cct = Nformat.format(d);
        Secure_map.put("Secure_Rate", Secure_Rate);
        Secure_map.put("UNSecure_Rate", UNSecure_Rate);
        Secure_map.put("cct", cct);
        return Secure_map;
    }

    @Override
    public Map<String, String> getDataById(String id) {
        String name = nodeInfoDao.getNameById(id);
        Map<String,String> map = getDataByName(name);
        map.put("name",name);
        return map;
    }

    @Override
    public Map<String, List> getSysMode(String filePath, int count) {
        Map<String,String> map = new HashMap<>();
        Map<String,List> data = new HashMap<>();
        List<Map<String,String>> data1 = new ArrayList<>();
        List<Map<String,String>> data2 = new ArrayList<>();
        FileClient client = new FileClient();
        List<String> info = client.getContent(filePath);
        String[] name = info.get(count).split(",");
        String[] value = info.get(count+1).split(",");
        map.put("address","华中地区");
        map.put("damping",value[0]);
        map.put("frequency",value[1]);
        data1.add(map);
        for (int i = 2; i < 12; i++) {
            Map<String,String> map2 = new HashMap<>();
            map2.put("name",name[i]);
            map2.put("i",value[i].split(" ")[0]);
            map2.put("j",value[i].split(" ")[1].substring(0,value[i].split(" ")[1].indexOf("j")));
            data2.add(map2);
        }
        data.put("data1",data1);
        data.put("data2",data2);
        return data;
    }

    @Override
    public Map<String, List> getJCInfo() {
        data2_list = new ArrayList<>();
        Map<String, List> data_map = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        //报警信息
        List<Map<String,String>> data1_list = new ArrayList<>();
        //报警个数
        List<Integer> error_number = new ArrayList<>();
        //策略表
        //五大指标
        List<Map<String, String>> data3_list = new ArrayList<>();
        List<Map<String,String>> data5 = new ArrayList<>();
        int high = 0;
        int middle = 0;
        int low = 0;
        String message = "";
        String type = "";
        List<String> names = getErrorInfo();
        RedisClient client = new RedisClient();
        String time = client.getValue("time");
        if (Double.parseDouble(time) >= 1.01 && Double.parseDouble(time) <= 1.22) {
            Map<String,String> map = new HashMap<>();
            message = df.format(new Date()) + "  三相短路";
            type = "高风险";
            map.put("message",message);
            map.put("type",type);
            data1_list.add(map);
            high = 1;
        }
        for (int i = 0; i < names.size(); i++) {
            Map<String, String> map = new HashMap<>();
            Map<String,String> short_map = new HashMap<>();
            String location = nodeInfoDao.getLocationById(String.valueOf(ids.get(i)));
            short_map.put("lng",location.split(",")[0]);
            short_map.put("lat",location.split(",")[1]);
            short_map.put("id",String.valueOf(ids.get(i)));
            data5.add(short_map);
            map.put("station_name", names.get(i));
            double police = ((int) (Math.random() * (8 - 2) + 2)) * 0.01;
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            if (i == 0) {
                map.put("FDJ_name", "Ⅲ段母线");
                map.put("Police", Nformat.format(police));
            } else if (i == 1) {
                map.put("FDJ_name", "2号发电机");
                map.put("Police", Nformat.format(police));
            } else if (i == 2) {
                map.put("FDJ_name", "Ⅴ段母线");
                map.put("Police", "-" + Nformat.format(police));
            } else if (i == 3) {
                map.put("FDJ_name", "Ⅵ段母线");
                map.put("Police", Nformat.format(police));
            } else if (i == 4) {
                map.put("FDJ_name", "1号发电机");
                map.put("Police", "-" + Nformat.format(police));
            } else {
                map.put("FDJ_name", "3号发电机");
                map.put("Police", "-" + Nformat.format(police));
            }
            double before = ((int) (Math.random() * (30 - 10) + 10)) * 0.01;
            double after = ((int) (Math.random() * (70 - 50) + 50)) * 0.01;
            map.put("before", Nformat.format(before));
            map.put("after", Nformat.format(after));
            String error = "";
            Map<String,String> error_map = new HashMap<>();
            if (before <= 0.2) {
                error = df.format(new Date()) + "  " + names.get(i);
                error_map.put("message",error);
                error_map.put("type","一般风险");
                middle = middle + 1;
            } else {
                error = df.format(new Date()) + "  " + names.get(i);
                error_map.put("message",error);
                error_map.put("type","低风险");
                low = low + 1;
            }
            data1_list.add(error_map);
            data2_list.add(map);
        }
        data3_list.add(getIndex());
        error_number.add(high);
        error_number.add(middle);
        error_number.add(low);
        List<Map<String,String>> data6 = getImIndex();
        data_map.put("data1", data1_list);
        data_map.put("data2", data2_list);
        data_map.put("data3", data3_list);
        data_map.put("data4", error_number);
        data_map.put("data5",data5);
        data_map.put("data6",data6);
        return data_map;
    }

    @Override
    public Map<String, String> getInfoById(String id) {
        Map<String,String> info = new HashMap<>();
        String name = nodeInfoDao.getNameById(id);
        for (int i = 0; i <data2_list.size() ; i++) {
            Map<String,String> map = data2_list.get(i);
            if(map.get("station_name").equals(name)){
                info.put("name",name);
                info.put("before",map.get("before"));
                info.put("after",map.get("after"));
            }
        }
        return info;
    }

    @Override
    public String getIndexByFlag(String flag) {
        String index = "";
        if(flag.equals("1")){
            index = "百分之"+String.valueOf(index_1);
        }else if(flag.equals("2")){
            index = "百分之"+String.valueOf(index_3);
        }else if(flag.equals("3")){
            index = "百分之"+String.valueOf(index_2);
        }else if(flag.equals("4")){
            index = "百分之"+String.valueOf(index_4);
        }else {
            index = "百分之"+String.valueOf(index_5);
        }
        return index;
    }

    @Override
    public List<Map<String, String>> getAreaInfo() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        area_info = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            double hz = ((int) (Math.random() * (60 - 40) + 40)) * 0.01;;
            NumberFormat Nformat = NumberFormat.getInstance();
            // 设置小数位数。
            Nformat.setMaximumFractionDigits(2);
            map.put("hz",Nformat.format(hz));
            if (i == 0) {
                map.put("name", "华北");
            } else if (i == 1) {
                map.put("name", "华东");
            } else if (i == 2) {
                map.put("name", "华中");
            } else if (i == 3) {
                map.put("name", "东北");
            } else if (i == 4) {
                map.put("name", "西北");
            } else if (i == 5) {
                map.put("name", "西南");
            }
            map.put("percent", String.valueOf((int) (Math.random() * (60 - 20) + 20)) + "%");
            map.put("time", df.format(new Date()));
            area_info.add(map);
        }
        return area_info;
    }

    @Override
    public String getHZByArea(String area) {
        String message = "";
        for (int i = 0; i <area_info.size() ; i++) {
            Map<String,String> map = area_info.get(i);
            System.out.println(area);
            if(map.get("name").equals(area)){
                message = map.get("hz");
            }
        }
        return message;
    }

    @Override
    public String getZNByArea(String area) {
        String message = "";
        for (int i = 0; i <area_info.size() ; i++) {
            Map<String,String> map = area_info.get(i);
            if(map.get("name").equals(area)){
                message = map.get("percent");
            }
        }
        return message;
    }

    @Override
    public String getPoliceInfo() {
        String police = "";
        for (int i = 0; i <data2_list.size() ; i++) {
            Map<String,String> map = data2_list.get(i);
            police = police+map.get("station_name")+map.get("FDJ_name")+"调控量"+map.get("Police")+";";
        }
        return police;
    }

    @Override
    public Map<String, String> getSecure() {
        return Secure_map;
    }

    @Override
    public List<Map<String, String>> getImIndex() {
        List<String> ids = new ArrayList<>();
        List<Map<String,String>> list = new ArrayList<>();
        ids.add("2");
        ids.add("20");
        ids.add("14");
        ids.add("16");
        ids.add("23");
        NumberFormat Nformat = NumberFormat.getInstance();
        // 设置小数位数
        Nformat.setMaximumFractionDigits(2);
        for (int i = 0; i <ids.size() ; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("name",nodeInfoDao.getNameById(ids.get(i)));
            double d = ((int) (Math.random() * (60 - 40) + 40)) * 0.01;
            map.put("index",Nformat.format(d));
            list.add(map);
        }
        return list;
    }
}
