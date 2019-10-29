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
     * @param msg 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String msg) {
        System.out.println(new Date()+msg);
        Map<String,List> data_map = jcInfoService.getJCInfo();
        sendMessage(JSONObject.fromObject(data_map).toString());
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
