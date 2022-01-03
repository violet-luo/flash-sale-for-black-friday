package com.violetluo.flashsale.db.dao;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import com.violetluo.flashsale.db.mappers.FlashsaleItemMapper;
import com.violetluo.flashsale.db.po.FlashsaleItem;

@Repository
public class FlashsaleItemDaoImpl implements FlashsaleItemDao {

    @Resource
    private FlashsaleItemMapper flashsaleItemMapper;

    @Override
    public FlashsaleItem queryFlashsaleItemById(long itemId) {
        return flashsaleItemMapper.selectByPrimaryKey(itemId);
    }
}
