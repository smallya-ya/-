package com.ruoyi.common.battle.domain;

import java.time.LocalDateTime;

/**
 * @author hongjiasen
 */
public class SendDataMissionModel {

    private int priority;
    private byte[] data;
    private boolean isWait;
    private int vestNum;
    private LocalDateTime dateTime;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isWait() {
        return isWait;
    }

    public void setWait(boolean wait) {
        isWait = wait;
    }

    public int getVestNum() {
        return vestNum;
    }

    public void setVestNum(int vestNum) {
        this.vestNum = vestNum;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public static final class SendDataMissionModelBuilder {
        private int priority;
        private byte[] data;
        private boolean isWait;
        private int vestNum;
        private LocalDateTime dateTime;

        private SendDataMissionModelBuilder() {
        }

        public static SendDataMissionModelBuilder aSendDataMissionModel() {
            return new SendDataMissionModelBuilder();
        }

        public SendDataMissionModelBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public SendDataMissionModelBuilder data(byte[] data) {
            this.data = data;
            return this;
        }

        public SendDataMissionModelBuilder isWait(boolean isWait) {
            this.isWait = isWait;
            return this;
        }

        public SendDataMissionModelBuilder vestNum(int vestNum) {
            this.vestNum = vestNum;
            return this;
        }

        public SendDataMissionModelBuilder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public SendDataMissionModel build() {
            SendDataMissionModel sendDataMissionModel = new SendDataMissionModel();
            sendDataMissionModel.setData(data);
            sendDataMissionModel.setVestNum(vestNum);
            sendDataMissionModel.setDateTime(dateTime);
            sendDataMissionModel.isWait = this.isWait;
            sendDataMissionModel.setPriority(priority);
            return sendDataMissionModel;
        }
    }
}
