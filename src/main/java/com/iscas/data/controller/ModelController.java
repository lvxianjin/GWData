package com.iscas.data.controller;

import com.iscas.data.model.Message;
import com.iscas.data.service.ModelService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/26 08:56
 * @Description:
 */
@RestController
@CrossOrigin("*")
public class ModelController {
    @Autowired
    private ModelService modelService;
    @RequestMapping("getInfo.json")
    public String getInfoByType(@RequestParam String type,@RequestParam int page,@RequestParam int limit){
        Message msg = modelService.getInfoByType(type,page,limit);
        JSONObject object = JSONObject.fromObject(msg);
        return object.toString();
    }
    @RequestMapping("getCompare.json")
    public List<Map<String, String>> getCompare(@RequestParam String name){
        return modelService.getInfoByName(name);
    }
}
