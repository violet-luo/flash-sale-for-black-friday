package com.violetluo.flashsale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.mappers.FlashsaleActivityMapper;
import com.violetluo.flashsale.db.po.FlashsaleActivity;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class DaoTest {

    @Autowired
    private FlashsaleActivityMapper flashsaleActivityMapper;

    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    @Test
    void FlashsaleActivityTest() {
        FlashsaleActivity flashsaleActivity = new FlashsaleActivity();
        flashsaleActivity.setName("test");
        flashsaleActivity.setItemId(999L);
        flashsaleActivity.setTotalStock(100L);
        flashsaleActivity.setFlashsalePrice(new BigDecimal(99));
        flashsaleActivity.setActivityStatus(16);
        flashsaleActivity.setOldPrice(new BigDecimal(99));
        flashsaleActivity.setAvailableStock(100);
        flashsaleActivity.setLockStock(0L);
        flashsaleActivityMapper.insert(flashsaleActivity);
        System.out.println("====>>>>" + flashsaleActivityMapper.selectByPrimaryKey(1L));
    }

    @Test
    void setFlashsaleActivityQuery(){
        List<FlashsaleActivity> flashsaleActivitys = flashsaleActivityDao.queryFlashsaleActivitysByStatus(0);
        System.out.println(flashsaleActivitys.size());
        flashsaleActivitys.stream().forEach(flashsaleActivity -> System.out.println(flashsaleActivity.toString()));
    }

}
