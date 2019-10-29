package com.iscas.data.cron;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.NodeInfoService;
import com.iscas.data.socket.NodeSocket;
import com.iscas.data.tool.RedisClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 节点的定时任务类
 *
 * @author : lvxianjin
 * @Date: 2019/10/22 19:45
 * @Description:
 */
@Component
@EnableScheduling
public class NodeSchedule implements SchedulingConfigurer {
    @Autowired
    private NodeInfoService nodeInfoService;
    @Autowired
    private NodeSocket nodeSocket;
    @Autowired
    private NodeInfoDao nodeInfoDao;
    private static final String DEFAULT_CRON = "0/2 * * * * ?";
    private String cron = DEFAULT_CRON;
    private double time = 0.95;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                RedisClient client = new RedisClient();
                setCron("0/2 * * * * ?");
                Map<String,List> data_map = new HashMap<>();
                Map<String,String> error_map = new HashMap<>();
                Map<String,String> error_line = new HashMap<>();
                List<Map<String,String>> data4 = new ArrayList<>();
                List<Map<String,String>> data5 = new ArrayList<>();
                String cluster_id = client.getValue("id");
                if(time>=1.01&&time<=1.22){
                    setCron("0/1 * * * * ?");
                    error_map.put("Flng", "119.035082");
                    error_map.put("Flat", "25.725132");
                    error_map.put("Tlng", "113.515893");
                    error_map.put("Tlat", "23.77633");
                    error_map.put("percent","0.3");
                    data4 = nodeInfoService.getErrorLine();
                }else {
                    data4.add(error_line);
                }
                client.setValue("time",String.valueOf(time));
                List<Map<String,String>> data1 = nodeInfoService.getNodeInfo(String.valueOf(time));
                List<Map<String,String>> cluster_info = nodeInfoDao.getCluster(cluster_id,String.valueOf(time));
                if(!cluster_id.equals("40")){
                    for (int i = 0; i <cluster_info.size() ; i++) {
                        Map<String,String> map = cluster_info.get(i);
                        map.put("lng",map.get("location").split(",")[0]);
                        map.put("lat",map.get("location").split(",")[1]);
                        map.remove("location");
                        data5.add(map);
                    }
                }
                List<Map<String,String>> data2 = new ArrayList<>();
                List<Map<String,String>> data3 = nodeInfoService.getRouteInfo(String.valueOf(time));
                data2.add(error_map);
                data_map.put("data1",data1);
                data_map.put("data2",data2);
                data_map.put("data3",data3);
                data_map.put("data4",data4);
                data_map.put("data5",data5);
                JSONObject data = JSONObject.fromObject(data_map);
                nodeSocket.sendMessage(data.toString());
                time = add(time,0.01);
                if(time == 1.3){
                    time = 0.95;
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }
    public void setCron(String cron) {
        this.cron = cron;
    }
    public static double add(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(String.valueOf(m1));
        BigDecimal p2 = new BigDecimal(String.valueOf(m2));
        return p1.add(p2).doubleValue();
    }
}
