package com.mapletestone.revitPlugin.service;

import com.alibaba.fastjson.JSONObject;
import com.mapletestone.revitPlugin.utils.ReJeeParseUtils;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: iot-project
 * @BelongsPackage: com.mapletestone.iot.iotproject.service
 * @Author: zhanpc
 * @CreateTime: 2024-03-13  14:42
 * @Description: TODO
 */
@Service
public class TemAndHumService {

    public void dealMessage(String hexString) {
        JSONObject jsonObject = new JSONObject(ReJeeParseUtils.parseGatewayPacket(hexString));
        System.out.println(jsonObject);
    }
}
