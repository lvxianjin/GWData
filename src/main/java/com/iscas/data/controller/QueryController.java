package com.iscas.data.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 16:00
 * @Description:
 */
@RestController
@CrossOrigin("*")
public class QueryController {
    @RequestMapping(value = "")
    public List<Map<String,String>> getSecureRate(@RequestParam String start_time,@RequestParam String end_time){
        List<Map<String,String>> info = new ArrayList<>();
        return info;
    }
}
