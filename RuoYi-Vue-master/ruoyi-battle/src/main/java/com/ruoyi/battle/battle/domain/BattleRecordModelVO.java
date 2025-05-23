package com.ruoyi.battle.battle.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.battle.map.domain.MapEntity;

import java.util.List;

/**
 * @author hongjiasen
 */
public class BattleRecordModelVO {

    private List<BattleLogEntity> logList;
    @JsonProperty("battleRecordEntity")
    private BattleRecordVO battleRecordVO;
    private MapEntity mapEntity;
    private Long totIndex;
    private Integer nowIndex;
    private String mapBase64;

    public List<BattleLogEntity> getLogList() {
        return logList;
    }

    public void setLogList(List<BattleLogEntity> logList) {
        this.logList = logList;
    }

    public BattleRecordVO getBattleRecordVO() {
        return battleRecordVO;
    }

    public void setBattleRecordVO(BattleRecordVO battleRecordVO) {
        this.battleRecordVO = battleRecordVO;
    }

    public MapEntity getMapEntity() {
        return mapEntity;
    }

    public void setMapEntity(MapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }

    public Long getTotIndex() {
        return totIndex;
    }

    public void setTotIndex(Long totIndex) {
        this.totIndex = totIndex;
    }

    public Integer getNowIndex() {
        return nowIndex;
    }

    public void setNowIndex(Integer nowIndex) {
        this.nowIndex = nowIndex;
    }

    public String getMapBase64() {
        return mapBase64;
    }

    public void setMapBase64(String mapBase64) {
        this.mapBase64 = mapBase64;
    }

    public static final class BattleRecordModelVOBuilder {
        private List<BattleLogEntity> logList;
        private BattleRecordVO battleRecordVO;
        private MapEntity mapEntity;
        private Long totIndex;
        private Integer nowIndex;
        private String mapBase64;

        private BattleRecordModelVOBuilder() {
        }

        public static BattleRecordModelVOBuilder aBattleRecordModelVO() {
            return new BattleRecordModelVOBuilder();
        }

        public BattleRecordModelVOBuilder logList(List<BattleLogEntity> logList) {
            this.logList = logList;
            return this;
        }

        public BattleRecordModelVOBuilder battleRecordVO(BattleRecordVO battleRecordVO) {
            this.battleRecordVO = battleRecordVO;
            return this;
        }

        public BattleRecordModelVOBuilder mapEntity(MapEntity mapEntity) {
            this.mapEntity = mapEntity;
            return this;
        }

        public BattleRecordModelVOBuilder totIndex(Long totIndex) {
            this.totIndex = totIndex;
            return this;
        }

        public BattleRecordModelVOBuilder nowIndex(Integer nowIndex) {
            this.nowIndex = nowIndex;
            return this;
        }

        public BattleRecordModelVOBuilder mapBase64(String mapBase64) {
            this.mapBase64 = mapBase64;
            return this;
        }

        public BattleRecordModelVO build() {
            BattleRecordModelVO battleRecordModelVO = new BattleRecordModelVO();
            battleRecordModelVO.setLogList(logList);
            battleRecordModelVO.setBattleRecordVO(battleRecordVO);
            battleRecordModelVO.setMapEntity(mapEntity);
            battleRecordModelVO.setTotIndex(totIndex);
            battleRecordModelVO.setNowIndex(nowIndex);
            battleRecordModelVO.setMapBase64(mapBase64);
            return battleRecordModelVO;
        }
    }
}
