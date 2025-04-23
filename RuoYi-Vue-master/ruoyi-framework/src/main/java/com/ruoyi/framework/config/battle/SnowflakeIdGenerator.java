package com.ruoyi.framework.config.battle;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

/**
 * @author hongjiasen
 */
@Component
public class SnowflakeIdGenerator implements IdentifierGenerator {

    private SnowflakeConfig snowFlake;

    public SnowflakeIdGenerator(SnowflakeConfig snowFlake) {
        this.snowFlake = snowFlake;
    }

    @Override
    public Long nextId(Object entity) {
        return snowFlake.snowflakeId();
    }
}
