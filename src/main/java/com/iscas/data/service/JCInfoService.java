package com.iscas.data.service;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/23 15:55
 * @Description:
 */
public interface JCInfoService {
   /**
    *
    * 功能描述: 获取报警信息
    *
    * @param:
    * @return:
    * @auther: lvxianjin
    * @date: 2019/10/23 15:57
    */
    List<String> getErrorInfo();
    /**
     *
     * 功能描述: 获取五大指标
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/23 17:08
     */
    Map<String,String> getIndex();
    /**
     *
     * 功能描述: 获取频率和阻尼比信息
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/24 11:05
     */
    List<Map<String,String>> getHZInfo();
    /**
     *
     * 功能描述: 根据站点名称获取指标信息
     *
     * @param station_name 站点名称
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/24 20:39
     */
    Map<String,String> getDataByName(String station_name);
    /**
     *
     * 功能描述: 根据Id获取指标信息
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/24 20:54
     */
    Map<String,String> getDataById(String id);
     /**
      *
      * 功能描述: 获取模式模态信息
      *
      * @param:
      * @return:
      * @auther: lvxianjin
      * @date: 2019/10/25 13:57
      */
     Map<String, List> getSysMode(String filePath,int count);
}
