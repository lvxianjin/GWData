package com.iscas.data.service;

import com.iscas.data.model.Message;

import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 13:31
 * @Description:
 */
public interface ModelService {
    /**
     *
     * 功能描述: 根据type获取数据
     *
     * @param: type 查询类型
     * @return:
     * @auther: lvxianjin
     * @date: 2019/10/26 13:33
     */
   Message getInfoByType(String type,int page,int limit);
}
