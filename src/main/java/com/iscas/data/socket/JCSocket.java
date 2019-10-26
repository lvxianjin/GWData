package com.iscas.data.socket;

import com.iscas.data.service.JCInfoService;
import com.iscas.data.tool.RedisClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:45
 * @Description:
 */
@ServerEndpoint("/JCSocket")
@Controller
public class JCSocket {
    private static JCInfoService jcInfoService;
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<JCSocket> webSocketSet = new CopyOnWriteArraySet();
    @Autowired
    public void setJCInfoService(JCInfoService jcInfoService) {
        JCSocket.jcInfoService = jcInfoService;
    }
    /**
     * 功能描述: websocket 连接建立成功后进行调用
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/22 19:29
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("决策Socket连接成功");
        while (true){
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
            sendMessage(JSONObject.fromObject(data_map).toString());
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("决策Socket连接关闭");
    }

    /**
     * 收到节点信息
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        for (JCSocket item : webSocketSet) {
            item.sendMessage(message);
        }
    }

    /**
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        for (JCSocket socketServer : webSocketSet) {
            try {
                //synchronized (session) {
                socketServer.session.getBasicRemote().sendText(message);
                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
