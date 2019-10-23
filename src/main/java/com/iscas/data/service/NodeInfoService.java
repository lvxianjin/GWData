package com.iscas.data.service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lvxianjin
 * @Date: 2019/10/22 19:13
 * @Description:
 */
public interface NodeInfoService {
    /**
     *
     * 功能描述: 获取暂态节点幅值
     *
     * @param: time 时间
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/22 17:03
     */
    List<Map<String,String>> getNodeInfo(String time);
    /**
     * 功能描述: 获取支路信息
     * @param: time 时间
     * @return:
     * @auther: lvxianjinnode.xml
     * @date: 2019/10/22 17:03
     */
    List<Map<String,String>> getRouteInfo(String time);
    /**
     *
     * 功能描述: 获取边界经纬度
     *
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/23 12:47
     */
    List<List> getBorder();
}
