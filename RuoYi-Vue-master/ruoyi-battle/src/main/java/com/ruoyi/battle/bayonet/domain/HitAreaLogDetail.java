package com.ruoyi.battle.bayonet.domain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongjiasen
 */
public class HitAreaLogDetail {

    private String hitPart;

    private AtomicInteger hitTimes;

    private AtomicInteger hitScore;

    public HitAreaLogDetail() {
    }

    public HitAreaLogDetail(String hitPart, AtomicInteger hitTimes, AtomicInteger hitScore) {
        this.hitPart = hitPart;
        this.hitTimes = hitTimes;
        this.hitScore = hitScore;
    }

    public String getHitPart() {
        return hitPart;
    }

    public void setHitPart(String hitPart) {
        this.hitPart = hitPart;
    }

    public Integer getHitTimes() {
        return hitTimes.get();
    }

    public void hitTimesGetAndIncrement() {
        this.hitTimes.getAndIncrement();
    }

    public Integer getHitScore() {
        return hitScore.get();
    }

    public void hitScoreGetAndIncrement(int score) {
        this.hitScore.addAndGet(score);
    }
}
