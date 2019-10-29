package com.iscas.data.service.impl;

import com.iscas.data.dao.NodeInfoDao;
import com.iscas.data.service.JCInfoService;
import com.iscas.data.service.NodeInfoService;
import com.iscas.data.service.VoiceService;
import com.iscas.data.socket.VoiceSocket;
import com.iscas.data.tool.RedisClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
    private NodeInfoService nodeInfoService;
    @Autowired
    private NodeInfoDao nodeInfoDao;
    @Autowired
    private VoiceSocket voiceSocket;

    @Override
    public String queryCommand(String commandType, String area) {
        String message = "查询成功";
        Map<String, String> voice_map = new HashMap<>();
        String type = "0";
        String msg = "";
        String url = "http://192.168.101.80:8100/tts?access_token=speech&language=zh&domain=1&voice_name=静静&text=";
        if (commandType.equals("01")) {
            String index = jcInfoService.getIndexByFlag("1");
            msg = url + "当前电网安全性指标是" + index;
        } else if (commandType.equals("02")) {
            String index = jcInfoService.getIndexByFlag("2");
            msg = url + "当前电网经济性指标是" + index;
        } else if (commandType.equals("03")) {
            String index = jcInfoService.getIndexByFlag("3");
            msg = url + "当前电网综合评估指标是" + index;
        } else if (commandType.equals("04")) {
            String index = jcInfoService.getIndexByFlag("4");
            msg = url + "当前电网静态安全指标是" + index;
        } else if (commandType.equals("05")) {
            String index = jcInfoService.getIndexByFlag("5");
            msg = url + "当前电网暂态安全指标是" + index;
        } else if (commandType.equals("06")) {
            String police = jcInfoService.getPoliceInfo();
            msg = url + "当前电网的调控策略有：" + police;
        } else if (commandType.equals("07")) {
            String hz = jcInfoService.getHZByArea(area);
            if (hz.equals("")) {
                message = "查询失败";
                msg = url + "没有查到相关信息";
            } else {
                msg = url + area + "地区的振荡频率是" + hz;
            }
        } else if (commandType.equals("08")) {
            String percent = jcInfoService.getZNByArea(area);
            if (percent.equals("")) {
                message = "查询失败";
                msg = url + "没有查到相关信息";
            } else {
                msg = url + area + "地区的阻尼比是" + percent;
                message = "查询成功";
            }
        }
        voice_map.put("type", type);
        voice_map.put("message", msg);
        voiceSocket.sendMessage(JSONObject.fromObject(voice_map).toString());
        return message;
    }

    @Override
    public String operate(String commandType, String parameter) {
        String url = "http://192.168.101.80:8100/tts?access_token=speech&language=zh&domain=1&voice_name=静静&text=";
        String message = "";
        Map<String, String> voice_map = new HashMap<>();
            if (commandType.equals("00")) {
                if (parameter.equals("110kv")) {
                    voice_map.put("type", "1");
                } else if (parameter.equals("220kv")) {
                    voice_map.put("type", "2");
                } else if (parameter.equals("500kv")) {
                    voice_map.put("type", "3");
                } else if (parameter.equals("1000kv")) {
                    voice_map.put("type", "4");
                }
                voice_map.put("message", url + "已为您关闭" + parameter + "线路");
                message = "操作成功";
            } else if (commandType.equals("01")) {
                if (parameter.equals("110kv")) {
                    voice_map.put("type", "1");
                } else if (parameter.equals("220kv")) {
                    voice_map.put("type", "2");
                } else if (parameter.equals("500kv")) {
                    voice_map.put("type", "3");
                } else if (parameter.equals("1000kv")) {
                    voice_map.put("type", "4");
                }
                voice_map.put("message", url + "已为您展示" + parameter + "线路");
                message = "操作成功";
            } else if (commandType.equals("02")) {
                String id = nodeInfoDao.getIdByName(parameter);
                if (id.equals("")) {
                    message = "未查到相关信息";
                } else {
                    voice_map.put("type", "5");
                    message = "查询成功";
                    String msg = url + parameter + "电压：" + nodeInfoService.getBasicInfo(id).get("v") + "；有功功率：" + nodeInfoService.getBasicInfo(id).get("p")+"；无功功率：" + nodeInfoService.getBasicInfo(id).get("q");
                    voice_map.put("message", msg);
                    voice_map.put("id", id);
                    voice_map.put("lng", nodeInfoDao.getLocationById(id).split(",")[0]);
                    voice_map.put("lat", nodeInfoDao.getLocationById(id).split(",")[1]);
                }
            } else if (commandType.equals("03")) {
                voice_map.put("type", "6");
                voice_map.put("message", url + "已为您切换到聚类信息界面");
            } else if (commandType.equals("04")) {
                String id = nodeInfoDao.getIdByName(parameter);
                if (id.equals("")) {
                    message = "未查到相关信息";
                } else {
                    voice_map.put("type", "7");
                    String msg = url+"与" + parameter + "相关节点有：";
                    List<Map<String, String>> info = nodeInfoService.getClusterById(id);
                    for (int i = 0; i < info.size(); i++) {
                        msg = msg + nodeInfoDao.getNameById(info.get(i).get("node")) + ",";
                    }
                    voice_map.put("id",id);
                    voice_map.put("lng", nodeInfoDao.getLocationById(id).split(",")[0]);
                    voice_map.put("lat", nodeInfoDao.getLocationById(id).split(",")[1]);
                    voice_map.put("message", msg);
                }
            } else if (commandType.equals("05")) {
                String id = nodeInfoDao.getIdByName(parameter);
                if (id.equals("")) {
                    message = "未查到相关信息";
                } else {
                    voice_map.put("type", "8");
                    Map<String, String> map = jcInfoService.getSecure();
                    String msg = "在预想故障下" + parameter + "的安全概率是:" + map.get("Secure_Rate") + ",不安全概率是：" + map.get("UNSecure_Rate");
                    voice_map.put("message", msg);
                    voice_map.put("id",id);
                    voice_map.put("lng", nodeInfoDao.getLocationById(id).split(",")[0]);
                    voice_map.put("lat", nodeInfoDao.getLocationById(id).split(",")[1]);
                }
                message = parameter;
            } else if (commandType.equals("06")) {
                voice_map.put("type", "9");
                voice_map.put("message", url + "已为您切换到薄弱节点信息界面");
            } else if (commandType.equals("07")) {
                String id = nodeInfoDao.getIdByName(parameter);
                if (id.equals("")) {
                    message = "未查到相关信息";
                } else {
                    voice_map.put("type", "10");
                    Map<String, String> map = jcInfoService.getInfoById(id);
                    voice_map.put("id",id);
                    voice_map.put("lng", nodeInfoDao.getLocationById(id).split(",")[0]);
                    voice_map.put("lat", nodeInfoDao.getLocationById(id).split(",")[1]);
                    voice_map.put("message", url + parameter + "调控前裕度是：" + map.get("before") + ",调控后裕度是：" + map.get("after"));
                }
            } else if (commandType.equals("08")) {
                voice_map.put("type", "11");
                voice_map.put("message", url + "已为您切换到电压等级界面");
            } else if (commandType.equals("09")) {
                voice_map.put("type", "12");
                voice_map.put("message", url + "已为您关闭信息窗口");
            } else if (commandType.equals("0A")) {
                voice_map.put("type", "13");
                voice_map.put("message", url + "切换到模型测辨界面");
            } else if (commandType.equals("0B")) {
                voice_map.put("type", "14");
                voice_map.put("message", url + "切换到大数据平台监控界面");
            } else if (commandType.equals("0C")) {
                voice_map.put("type", "14");
                voice_map.put("message", url + "切换到综合分析界面");
            }
            voiceSocket.sendMessage(JSONObject.fromObject(voice_map).toString());
            return message;
        }
    }
