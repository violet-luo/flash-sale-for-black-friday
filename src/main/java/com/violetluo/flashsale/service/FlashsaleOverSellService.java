package com.violetluo.flashsale.service;

import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.po.FlashsaleActivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlashsaleOverSellService {
    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    public String  processFlashsale(long activityId) {
        FlashsaleActivity flashsaleActivity = flashsaleActivityDao.queryFlashsaleActivityById(activityId);
        long availableStock = flashsaleActivity.getAvailableStock();
        String result;
        if (availableStock > 0) {
            result = "Congrats!";
            System.out.println(result);
            availableStock = availableStock - 1;
            flashsaleActivity.setAvailableStock(new Integer("" + availableStock));
            flashsaleActivityDao.updateFlashsaleActivity(flashsaleActivity);
        } else {
            result = "Sorry, this item is out of stock.";
            System.out.println(result);
        }
        return result;
    }
}
