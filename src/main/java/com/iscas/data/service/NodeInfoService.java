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
    /**
     *
     * 功能描述: 根据电压等级获取线路信息
     *
     * @param level 电压等级
     * @return: 线路信息
     * @auther: lvxianjin
     * @date: 2019/10/26 10:39
     */
    List<Map<String,String>> getLineByLevel(String level);
    /**
     *
     * 功能描述: 获取辐射范围
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/26 17:33
     */
    List<Map<String,String>> getErrorLine();
    /**
     *
     * 功能描述:
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/26 18:57
     */
    Map<String,List> getNodeInfo();
    /**
     *
     * 功能描述: 获取基础信息
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/28 9:33
     */
    Map<String,String> getBasicInfo(String id);
    /**
     *
     * 功能描述: 根据id获取聚类信息
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/28 9:49
     */
    List<Map<String,String>> getClusterById(String id);
    /**
     *
     * 功能描述: 全国热力图
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/11/11 16:28
     */
    public List<Map<String,String>> getRate();
}
