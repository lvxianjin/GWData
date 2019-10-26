package com.iscas.data.socket;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
 * @Date: 2019/10/26 18:37
 * @Description:
 */
@ServerEndpoint("/AreaSocket")
@Controller
public class AreaSocket {
    /**
     * session 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * webSocketSet concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<AreaSocket> webSocketSet = new CopyOnWriteArraySet();

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
        System.out.println("区域Socket连接成功");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        while (true){
            List<Map<String, String>> info = new ArrayList<>();
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
                info.add(map);
            }
            sendMessage(JSONArray.fromObject(info).toString());
            try {
                Thread.sleep(60000);
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
        System.out.println("区域Socket连接关闭");
    }

    /**
     * 收到节点信息
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        for (AreaSocket item : webSocketSet) {
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
        for (AreaSocket socketServer : webSocketSet) {
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
