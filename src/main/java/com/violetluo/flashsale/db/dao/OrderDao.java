package com.violetluo.flashsale.db.dao;

import com.violetluo.flashsale.db.po.Order;

public interface OrderDao {

    void insertOrder(Order order);

    Order queryOrder(String orderNo);

    void updateOrder(Order order);
}
