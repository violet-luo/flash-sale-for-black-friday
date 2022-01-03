package com.violetluo.flashsale.db.mappers;

import com.violetluo.flashsale.db.po.FlashsaleItem;

public interface FlashsaleItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FlashsaleItem record);

    int insertSelective(FlashsaleItem record);

    FlashsaleItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FlashsaleItem record);

    int updateByPrimaryKey(FlashsaleItem record);
}