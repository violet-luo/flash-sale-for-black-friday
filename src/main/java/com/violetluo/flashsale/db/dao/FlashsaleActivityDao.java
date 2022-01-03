package com.violetluo.flashsale.db.dao;

import java.util.List;

import com.violetluo.flashsale.db.po.FlashsaleActivity;

public interface FlashsaleActivityDao {

    public List<FlashsaleActivity> queryFlashsaleActivitysByStatus(int activityStatus);

    public void inertFlashsaleActivity(FlashsaleActivity flashsaleActivity);

    public FlashsaleActivity queryFlashsaleActivityById(long activityId);

    public void updateFlashsaleActivity(FlashsaleActivity flashsaleActivity);

    boolean lockStock(Long flashsaleActivityId);

    boolean deductStock(Long flashsaleActivityId);

    void revertStock(Long flashsaleActivityId);
}
