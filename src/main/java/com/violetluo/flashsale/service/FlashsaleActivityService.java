package com.violetluo.flashsale.service;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.violetluo.flashsale.db.dao.OrderDao;
import com.violetluo.flashsale.db.dao.FlashsaleActivityDao;
import com.violetluo.flashsale.db.dao.FlashsaleItemDao;
import com.violetluo.flashsale.db.po.Order;
import com.violetluo.flashsale.db.po.FlashsaleActivity;
import com.violetluo.flashsale.db.po.FlashsaleItem;
import com.violetluo.flashsale.mq.RocketMQService;
import com.violetluo.flashsale.util.RedisService;
import com.violetluo.flashsale.util.SnowFlake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class FlashsaleActivityService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private FlashsaleActivityDao flashsaleActivityDao;

    @Autowired
    private RocketMQService rocketMQService;

    @Autowired
    FlashsaleItemDao flashsaleItemDao;

    @Autowired
    OrderDao orderDao;

    /**
     * datacenterId;
     * machineId;
     */
    private SnowFlake snowFlake = new SnowFlake(1, 1);

    /**
     * Create order
     *
     * @param flashsaleActivityId
     * @param userId
     * @return
     * @throws Exception
     */
    public Order createOrder(long flashsaleActivityId, long userId) throws Exception {
        /*
         * 1. create order ID based on Snowflake algorithm
         */
        FlashsaleActivity flashsaleActivity = flashsaleActivityDao.queryFlashsaleActivityById(flashsaleActivityId);
        Order order = new Order();
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setFlashsaleActivityId(flashsaleActivity.getId());
        order.setUserId(userId);
        order.setOrderAmount(flashsaleActivity.getFlashsalePrice().longValue());
        /*
         * 2. send order creation message
         */
        rocketMQService.sendMessage("flashsale_order", JSON.toJSONString(order));

        /*
         * 3. send order payment status message
         */
        rocketMQService.sendDelayMessage("pay_check", JSON.toJSONString(order), 3);

        return order;
    }

    /**
     * Check if an item is in stock
     *
     * @param activityId
     * @return
     */
    public boolean flashsaleStockValidator(long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }


    /**
     * Import flash sale info into Redis
     *
     * @param flashsaleActivityId
     */
    public void pushFlashsaleInfoToRedis(long flashsaleActivityId) {
        FlashsaleActivity flashsaleActivity = flashsaleActivityDao.queryFlashsaleActivityById(flashsaleActivityId);
        redisService.setValue("flashsaleActivity:" + flashsaleActivityId, JSON.toJSONString(flashsaleActivity));

        FlashsaleItem flashsaleItem = flashsaleItemDao.queryFlashsaleItemById(flashsaleActivity.getItemId());
        redisService.setValue("flashsaleItem:" + flashsaleActivity.getItemId(), JSON.toJSONString(flashsaleItem));
    }

    /**
     * Process payment complete orders
     *
     * @param orderNo
     */
    public void payOrderProcess(String orderNo) throws Exception {
        log.info("Order payment complete. Order No: " + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        /*
         * 1. check if order exists
         * 2. check if order is pending payment
         */
        if (order == null) {
            log.error("Order does not exist: " + orderNo);
            return;
        } else if(order.getOrderStatus() != 1 ) {
            log.error("Order status invalid: " + orderNo);
            return;
        }
        /*
         * 2. order payment complete
         */
        order.setPayTime(new Date());
        // order status: 0: invalid order b/c no inventory; 1: order created pending payment; 2: payment complete
        order.setOrderStatus(2);
        orderDao.updateOrder(order);
        /*
         * 3. Send message of order payment complete
         */
        rocketMQService.sendMessage("pay_done", JSON.toJSONString(order));
    }
}
