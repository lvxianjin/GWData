package com.iscas.data.service.impl;

import com.iscas.data.service.JCInfoService;
import com.iscas.data.service.VoiceService;
import com.iscas.data.socket.VoiceSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : lvxianjin
 * @Date: 2019/10/24 09:12
 * @Description: 语音部分
 */
@Service
public class VoiceServiceImpl implements VoiceService {
    @Autowired
    private JCInfoService jcInfoService;
    @Autowired
    private VoiceSocket voiceSocket;
    @Override
    public void getIndexInfo(int flag) {
        Map<String,String> map = jcInfoService.getIndex();
        String message = "";
        if(flag == 1){
            message = "当前电网安全性指标是"+map.get(String.valueOf(flag))+"%";
        }else if(flag == 2){
            message = "当前电网安全性指标是"+map.get(String.valueOf(flag))+"%";
        }else if(flag == 3){
            message = "当前电网综合评估指标是"+map.get(String.valueOf(flag))+"%";
        }else if(flag == 4){
            message = "当前电网静态安全指标是"+map.get(String.valueOf(flag))+"%";
        }else {
            message = "当前电网暂态安全指标是"+map.get(String.valueOf(flag))+"%";
        }
        voiceSocket.sendMessage(message);
    }
}
