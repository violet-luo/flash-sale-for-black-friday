package com.violetluo.flashsale.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import com.violetluo.flashsale.db.mappers.FlashsaleActivityMapper;
import com.violetluo.flashsale.db.po.FlashsaleActivity;

import java.util.List;

@Slf4j
@Repository
public class FlashsaleActivityDaoImpl implements FlashsaleActivityDao {

    @Resource
    private FlashsaleActivityMapper flashsaleActivityMapper;

    @Override
    public List<FlashsaleActivity> queryFlashsaleActivitysByStatus(int activityStatus) {
        return flashsaleActivityMapper.queryFlashsaleActivitysByStatus(activityStatus);
    }

    @Override
    public void inertFlashsaleActivity(FlashsaleActivity flashsaleActivity) {
        flashsaleActivityMapper.insert(flashsaleActivity);
    }

    @Override
    public FlashsaleActivity queryFlashsaleActivityById(long activityId) {
        return flashsaleActivityMapper.selectByPrimaryKey(activityId);
    }

    @Override
    public void updateFlashsaleActivity(FlashsaleActivity flashsaleActivity) {
        flashsaleActivityMapper.updateByPrimaryKey(flashsaleActivity);
    }

    @Override
    public boolean lockStock(Long flashsaleActivityId) {
        int result = flashsaleActivityMapper.lockStock( flashsaleActivityId );
        if (result < 1) {
            log.error("Failed to lock inventory.");
            return false;
        }
        return true;
    }

    @Override
    public boolean deductStock(Long flashsaleActivityId) {
        int result = flashsaleActivityMapper.deductStock(flashsaleActivityId);
        if (result < 1) {
            log.error("Failed to deduct inventory.");
            return false;
        }
        return true;
    }

    @Override
    public void revertStock(Long flashsaleActivityId) {
        flashsaleActivityMapper.revertStock(flashsaleActivityId);
    }
}
