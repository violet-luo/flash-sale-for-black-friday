package com.violetluo.flashsale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import com.violetluo.flashsale.messagequeue.RocketMQService;
import com.violetluo.flashsale.service.FlashsaleActivityService;

@SpringBootTest
public class MQTest {

    @Autowired
    RocketMQService rocketMQService;

    @Autowired
    FlashsaleActivityService flashsaleActivityService;

    @Test
    public void sendMQTest() throws Exception {
        rocketMQService.sendMessage("test-violetluo", "Hello World!" + new Date().toString());
    }


}
