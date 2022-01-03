package com.violetluo.flashsale.db.mappers;

import java.util.List;

import com.violetluo.flashsale.db.po.FlashsaleActivity;

public interface FlashsaleActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FlashsaleActivity record);

    int insertSelective(FlashsaleActivity record);

    FlashsaleActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FlashsaleActivity record);

    int updateByPrimaryKey(FlashsaleActivity record);

    List<FlashsaleActivity> queryFlashsaleActivitysByStatus(int activityStatus);

    int lockStock(Long id);

    int deductStock(Long id);

    void revertStock(Long flashsaleActivityId);
}