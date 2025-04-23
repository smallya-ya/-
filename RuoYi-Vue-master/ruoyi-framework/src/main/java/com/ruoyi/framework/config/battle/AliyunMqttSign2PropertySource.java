package com.ruoyi.framework.config.battle;

import com.ruoyi.common.utils.battle.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 用于计算阿里云微消息队列MQTT版username、password
 * @author hongjiasen
 */
public class AliyunMqttSign2PropertySource extends PropertySource {

    private static Logger logger = LoggerFactory.getLogger(AliyunMqttSignPropertySource.class);

    public static final String ALIYUN_MQTT_PROPERTY_SOURCE_NAME = "aliyun-mqtt2";
    private static final String PREFIX = "aliyun-mqtt2.";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_PASSWORD = "password";
    private static final int PARAMETER_LENGTH = 4;

    public AliyunMqttSign2PropertySource() {
        this(ALIYUN_MQTT_PROPERTY_SOURCE_NAME);
    }

    public AliyunMqttSign2PropertySource(String name) {
        super(name);
    }

    /**
     * 解析参数并返回所需要属性,property格式如下,四个参数位置不可更改
     * @param property: aliyun-mqtt2.username[instanceId,accessKey,secretKey,clientId]
     * @param property: aliyun-mqtt2.password[instanceId,accessKey,secretKey,clientId]
     * @return
     */
    @Override
    public Object getProperty(String property) {
        if (!property.startsWith(PREFIX)) {
            return null;
        }
        String type0 = property.substring(PREFIX.length());
        String type = getType(type0, PROPERTY_USERNAME);
        if (type != null) {
            return getUsername(type);
        }
        type = getType(type0, PROPERTY_PASSWORD);
        if (type != null) {
            return getPassword(type);
        }
        return null;
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
            return "Signature|" + tokens[1] + "|" + tokens[0];
        }
        return null;
    }

    private String getPassword(String token) {
        String[] tokens = StringUtils.commaDelimitedListToStringArray(token);
        if (tokens.length == PARAMETER_LENGTH) {
            try {
                return CryptoUtil.macSignature(tokens[3], tokens[2]);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
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
            logger.trace("AliyunMqttSign2PropertySource already present");
            return;
        }
        AliyunMqttSign2PropertySource aliyunMqttSign2PropertySource
                = new AliyunMqttSign2PropertySource(ALIYUN_MQTT_PROPERTY_SOURCE_NAME);
        if (sources.get(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME) != null) {
            sources.addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, aliyunMqttSign2PropertySource);
        } else {
            sources.addLast(aliyunMqttSign2PropertySource);
        }
        logger.trace("AliyunMqttSign2PropertySource add to Environment");
    }
}
