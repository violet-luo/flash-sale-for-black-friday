package com.violetluo.flashsale.messagequeue;

import com.alibaba.fastjson.JSON;
import com.violetluo.flashsale.db.dao.OrderDao;
import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.po.Order;
import com.violetluo.flashsale.util.RedisService;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = "flashsale_order", consumerGroup = "flashsale_order_group")
public class OrderConsumer implements RocketMQListener<MessageExt> {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public void onMessage (MessageExt messageExt) {
        //1. analyze request to create order
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("Receive request to create order: " + message);
        Order order = JSON.parseObject(message, Order.class);
        order.setCreateTime(new Date());
        //2. deduct inventory
        boolean lockStockResult = flashsaleActivityDao.lockStock(order.getFlashsaleActivityId());
        if (lockStockResult) {
            // order status: 0: invalid order b/c no inventory; 1: order created pending payment
            order.setOrderStatus(1);
            // add user to restricted users
            redisService.addLimitMember(order.getFlashsaleActivityId(), order.getUserId());
        } else {
            order.setOrderStatus(0);
        }
        //3. insert order
        orderDao.insertOrder(order);
    }
}
