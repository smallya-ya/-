package com.ruoyi.battle.battle.handler;

import cn.hutool.core.util.HexUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.battle.battle.domain.BattleLogEntity;
import  com.ruoyi.battle.battle.mapper.BattleLogMapper;
import  com.ruoyi.battle.battle.service.BattleService;
import com.ruoyi.battle.battle.thread.SocketThread;
import com.ruoyi.battle.shootingvest.domian.ShootingVestModel;
import com.ruoyi.common.battle.Constant;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author hongjiasen
 */
public class HeartbeatHandler extends CommonDataHandler {

    private Pattern pattern = Pattern.compile("^(ff03)\\w{18}(affa)$");
    private BattleLogMapper battleLogMapper = SpringUtil.getBean(BattleLogMapper.class);
    //外带着启动socket线程
    private SocketThread socketThread =new SocketThread();
    public  static boolean updateFlage =true;
    @Override
    protected boolean dataTypeVerification(String data) {
        return pattern.matcher(data).matches();
    }

    @Override
    public void handle(byte[] data) {
        super.handle(data);

        String str = HexUtil.encodeHexStr(data);
        int vestNum = Integer.parseInt(str.substring(6, 10), 16);
        ShootingVestModel vestEntity = BattleService.context.getVestEntityMap().get(vestNum);
        int oldHp = vestEntity.getHp();
        int oldAmmo1 = vestEntity.getAmmo1();
        int oldAmmo2 = vestEntity.getAmmo2();

        int ammo1 = Integer.parseInt(str.substring(10, 14), 16);
        int ammo2 = Integer.parseInt(str.substring(16, 20), 16);
        int hp = Integer.parseInt(str.substring(14, 16), 16);
        updateFlage =true;
        if (vestEntity.getManualLiveFlag() != 1 || (vestEntity.getManualLiveFlag() == 1 && hp != 0)) {
            vestEntity.setStatus(Constant.LIVE_STATUS);
            vestEntity.setRealStatus(Constant.LIVE_STATUS);
            vestEntity.setAmmo1(ammo1);
            vestEntity.setAmmo2(ammo2);

            vestEntity.setHp(hp);
            vestEntity.setLastReportTime(LocalDateTime.now());
            if (hp == 0 && vestEntity.getManualLiveFlag() != 1 && oldHp != 0) {
                vestEntity.setStatus(Constant.DIE_STATUS);
                BattleLogEntity battleLogEntity = BattleLogEntity.BattleLogEntityBuilder
                        .aBattleLogEntity()
                        .battleId(BattleService.context.getBattleEntity().getId())
                        .time(LocalDateTime.now())
                        .type(Constant.DIE_LOG_TYPE)
                        .log(vestNum + "号阵亡")
                        .isShow(0)
                        .build();
                if (Objects.nonNull(vestEntity.getHitRecordModel())) {
                    log.info("[指令处理]：{}号阵亡 击杀者：{}号", vestNum, vestEntity.getHitRecordModel().getShotNum());
                } else {
                    log.info("[指令处理]：{}号阵亡 未收到或未处理[0x05]击中指令就收到{}号生命值为0的数据帧，击杀者未知", vestNum, vestNum);
                }
                battleLogMapper.insert(battleLogEntity);
            }
            if (!(hp ==0 && oldHp == 0)) {
                log.info("[指令处理]：收到{}号心跳上报 hp：{} 子弹1：{} 子弹2：{}", vestNum, hp, ammo1, ammo2);
                log.info("[线程操作]：唤醒串口发送线程，继续发送数据");
                if(hp == oldHp&& ammo1 == oldAmmo1&& ammo2 == oldAmmo2){
                    log.info("[指令处理]: 血量、武器数据不变，设置不发送数据到前端");
                    updateFlage =false;
                }
                BattleService.context.getSerialPortWriteDataThread().wakeUp();
            }
        } else {
            log.info("[指令处理]：判活后未收到[0x05]击中指令就收到{}号生命值为0的数据帧，放弃", vestNum);
        }
    }
}
