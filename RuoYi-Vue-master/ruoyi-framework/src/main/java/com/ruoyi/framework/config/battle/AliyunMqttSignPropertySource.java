package com.ruoyi.framework.config.battle;

import com.ruoyi.common.utils.battle.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringUtils;

/**
 * 用于计算设备接入阿里云物联网平台的MQTT连接参数username、password和clientid
 * @author hongjiasen
 */
public class AliyunMqttSignPropertySource extends PropertySource {

    private static Logger logger = LoggerFactory.getLogger(AliyunMqttSignPropertySource.class);

    public static final String ALIYUN_MQTT_PROPERTY_SOURCE_NAME = "aliyun-mqtt";
    private static final String PREFIX = "aliyun-mqtt.";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_PASSWORD = "password";
    private static final String PROPERTY_CLIENT_ID = "clientid";
    private static final int PARAMETER_LENGTH = 3;

    public AliyunMqttSignPropertySource() {
        this(ALIYUN_MQTT_PROPERTY_SOURCE_NAME);
    }

    public AliyunMqttSignPropertySource(String name) {
        super(name);
    }

    /**
     * 解析参数并返回所需要属性,name格式如下,三个参数位置不可更改
     * @param name: aliyun-mqtt.username[productKey,deviceName,deviceSecret]
     * @param name: aliyun-mqtt.password[productKey,deviceName,deviceSecret]
     * @param name: aliyun-mqtt.clientid[productKey,deviceName,deviceSecret]
     * @return MQTT的连接参数
     */
    @Override
    public Object getProperty(String name) {
        if (!name.startsWith(PREFIX)) {
            return null;
        }
        String type0 = name.substring(PREFIX.length());
        String type = getType(type0, PROPERTY_USERNAME);
        if (type != null) {
            return getUsername(type);
        }
        String timestamp = Long.toString(System.currentTimeMillis());
        type = getType(type0, PROPERTY_PASSWORD);
        if (type != null) {
            return getPassword(type, timestamp);
        }
        type = getType(type0, PROPERTY_CLIENT_ID);
        if (type != null) {
            return getClientid(type, timestamp);
        }
        return name;
    }

    private String getType(String type, String prefix) {
        if (type.startsWith(prefix)) {
            int startIndex = prefix.length() + 1;
            if (type.length() > startIndex) {
                return type.substring(startIndex, type.length() - 1);
            }
        }
        return null;
    }

    private String getUsername(String token) {
        String[] tokens = StringUtils.commaDelimitedListToStringArray(token);
        if (tokens.length == PARAMETER_LENGTH) {
            return tokens[1] + "&" + tokens[0];
        }
        return null;
    }

    private String getPassword(String token, String timestamp) {
        String[] tokens = StringUtils.commaDelimitedListToStringArray(token);
        if (tokens.length == PARAMETER_LENGTH) {
            String plainPasswd = "clientId" + tokens[0] + "." + tokens[1] + "deviceName" +
                    tokens[1] + "productKey" + tokens[0] + "timestamp" + timestamp;
            try {
                return CryptoUtil.hmacSha256(plainPasswd, tokens[2]);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private String getClientid(String token, String timestamp) {
        String[] tokens = StringUtils.commaDelimitedListToStringArray(token);
        if (tokens.length == PARAMETER_LENGTH) {
            return tokens[0] + "." + tokens[1] + "|" + "timestamp=" + timestamp +
                    ",_v=paho-java-1.2.5,securemode=1,signmethod=hmacsha256|";
        }
        return null;
    }

    public static void addToEnvironment(ConfigurableEnvironment environment) {
        addToEnvironment(environment, logger);
    }

    public static void addToEnvironment(ConfigurableEnvironment environment, Logger logger) {
        MutablePropertySources sources = environment.getPropertySources();
        PropertySource<?> existing = sources.get(ALIYUN_MQTT_PROPERTY_SOURCE_NAME);
        if (existing != null) {
            logger.trace("AliyunMqttSignPropertySource already present");
            return;
        }
        AliyunMqttSignPropertySource aliyunMqttSignPropertySource
                = new AliyunMqttSignPropertySource(ALIYUN_MQTT_PROPERTY_SOURCE_NAME);
        if (sources.get(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME) != null) {
            sources.addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, aliyunMqttSignPropertySource);
        } else {
            sources.addLast(aliyunMqttSignPropertySource);
        }
        logger.trace("AliyunMqttSignPropertySource add to Environment");
    }
}
