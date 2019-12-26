package com.iscas.data.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lvxianjin
 * @Date: 2019/10/22 15:34
 * @Description:
 */
@Repository
@Mapper
public interface NodeInfoDao {
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
     *
     * @param: time 时间
     * @return:
     * @auther: lvxianjinnode.xml
     * @date: 2019/10/22 17:03
     */
    List<Map<String,String>> getRouteInfo(String time);
    /**
     *
     * 功能描述: 根据node_id获取经纬度
     *
     * @param: node_id 节点id
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/23 14:43
     */
    String getLocationById(String node_id);
    /**
     *
     * 功能描述: 根据id获取名称
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/24 19:54
     */
    /**
     *
     * 功能描述: 根据node_id获取节点名称
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/23 16:23
     */
    String getNameById(String node_id);
    /**
     *
     * 功能描述: 根据电压等级获取连线
     *
     * @param level 电压等级
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/26 12:13
     */
    List<Map<String,String>> getLineByLevel(String level);
    List<Map<String,String>> getErrorLine();
    List<Map<String,String>> getCluster(String node,String time);
    Map<String,String> getBasicInfo(String node_id);
    String getIdByName(String name);
    int updateLvel(int id,String level);
    List<Map<String,String>> getSt_name();
    List<Map<String,String>> getLine();
    Map<String,String> getLocationByName(String st_name);
    List<Map<String,String>> getdcline();
    List<Map<String,String>> getProvince();
    List<Map<String,String>> getProvinceByArea(String area);
    int updateType(String st_name,String type);
    String getAreaByName(String name);
    List<Map<String,String>> getCenterByArea(String area);
    List<Map<String,String>> getCountyInfo(String city);
}
