package com.iscas.data.controller;

import com.iscas.data.service.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : lvxianjin
 * @Date: 2019/10/28 08:34
 * @Description:
 */
@RestController
@CrossOrigin("*")
public class VoiceController {
    @Autowired
    private VoiceService voiceService;
    @RequestMapping(value = "querycommand",method = RequestMethod.POST)
    public String queryCommand(@RequestParam String commandtype,@RequestParam(defaultValue = "null") String area){
        String message = voiceService.queryCommand(commandtype,area);
        return message;
    }
    @RequestMapping(value = "operate",method = RequestMethod.POST)
    public String operate(@RequestParam String operationtype,@RequestParam(defaultValue = "null") String parameter){
        String message = voiceService.operate(operationtype,parameter);
        return message;
    }
    @RequestMapping(value = "getOrder.json")
    public void getOrder(@RequestParam String node_id,@RequestParam String operation){
        voiceService.getOrder(node_id,operation);
    }
}
