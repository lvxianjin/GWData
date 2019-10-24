package com.iscas.data.controller;

import com.iscas.data.service.JCInfoService;
import com.iscas.data.service.NodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 坐标信息
 * @author : lvxianjin
 * @Date: 2019/10/23 13:13
 * @Description:
 */
@RestController
@CrossOrigin("*")
public class LocationController {
    @Autowired
    private NodeInfoService nodeInfoService;
    @RequestMapping("/getBorder.json")
    public List<List> getBorder(){
        return nodeInfoService.getBorder();
    }
}
