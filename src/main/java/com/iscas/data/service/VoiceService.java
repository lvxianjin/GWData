package com.iscas.data.service;

import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/24 09:02
 * @Description: 语音业务逻辑处理
 */
public interface VoiceService {
    /**
     *
     * 功能描述: 语音获取实时信息
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/27 21:39
     */
    String queryCommand(String commandType,String area);
    /**
     *
     * 功能描述: 语音操作大屏
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/28 8:39
     */
    String operate(String commandType,String parameter);
    /**
     *
     * 功能描述: 获取沙盘操作
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/11/18 19:29
     */
    void getOrder(String node_id,String operation);
}
