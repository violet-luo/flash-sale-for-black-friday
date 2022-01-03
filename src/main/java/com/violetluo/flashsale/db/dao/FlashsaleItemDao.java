package com.violetluo.flashsale.db.dao;

import com.violetluo.flashsale.db.po.FlashsaleItem;

public interface FlashsaleItemDao {

    public FlashsaleItem queryFlashsaleItemById(long itemId);
}
