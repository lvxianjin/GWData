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
     * 功能描述: 获取发电机调参信息表
     *
     * @param:
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/23 15:58
     */
    List<Map<String,String>> getPolicyInfo();
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
}
