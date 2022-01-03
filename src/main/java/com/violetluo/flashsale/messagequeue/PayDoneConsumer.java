package com.violetluo.flashsale.messagequeue;

import com.alibaba.fastjson.JSON;
import com.violetluo.flashsale.db.dao.OrderDao;
import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.po.Order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_done", consumerGroup = "pay_done_group")
public class PayDoneConsumer implements RocketMQListener<MessageExt> {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    @Override
    public void onMessage(MessageExt messageExt) {
        //1. analyze request to create order
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("Receive request to create order: " + message);
        Order order = JSON.parseObject(message, Order.class);
        //2. deduct inventory
        flashsaleActivityDao.deductStock(order.getFlashsaleActivityId());
    }
}
