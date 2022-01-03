
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

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "pay_check_group")
public class PayStatusCheckListener implements RocketMQListener<MessageExt> {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    @Resource
    private RedisService redisService;


    @Override
    public void onMessage(MessageExt messageExt) {
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("Order payment status check message received: " + message);
        Order order = JSON.parseObject(message, Order.class);
        //1. get order info
        Order orderInfo = orderDao.queryOrder(order.getOrderNo());
        //2. see if order completes payment
        if (orderInfo.getOrderStatus() != 2) {
            //3. order closes due to incomplete payment
            log.info("Order closes due to incomplete payment. Order No: " + orderInfo.getOrderNo());
            orderInfo.setOrderStatus(99);
            orderDao.updateOrder(orderInfo);
            //4. restore database inventory
            flashsaleActivityDao.revertStock(order.getFlashsaleActivityId());
            // restore Redis inventory
            redisService.revertStock("stock:" + order.getFlashsaleActivityId());
            //5. remove user from purchased list
            redisService.removeLimitMember(order.getFlashsaleActivityId(), order.getUserId());
        }
    }
}

