package com.violetluo.flashsale.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.po.FlashsaleActivity;
import com.violetluo.flashsale.util.RedisService;

@Component
public class RedisPreheatRunner implements ApplicationRunner {

    @Autowired
    RedisService redisService;

    @Autowired
    FlashsaleActivityDao flashsaleActivityDao;

    /**
     * sync inventory to Redis
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<FlashsaleActivity> flashsaleActivities = flashsaleActivityDao.queryFlashsaleActivitysByStatus(1);
        for (FlashsaleActivity flashsaleActivity : flashsaleActivities) {
            redisService.setValue("stock:" + flashsaleActivity.getId(),
                    (long) flashsaleActivity.getAvailableStock());
        }
    }

}
