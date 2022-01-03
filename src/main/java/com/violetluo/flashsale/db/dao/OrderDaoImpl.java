package com.violetluo.flashsale.db.dao;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import com.violetluo.flashsale.db.mappers.OrderMapper;
import com.violetluo.flashsale.db.po.Order;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public void insertOrder(Order order) {
        orderMapper.insert(order);
    }

    @Override
    public Order queryOrder(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public void updateOrder(Order order) {
        orderMapper.updateByPrimaryKey(order);
    }

}
