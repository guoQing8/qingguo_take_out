package com.superli.qingguo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superli.qingguo.entity.Orders;
import com.superli.qingguo.entity.SetmealDish;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/6 11:04
 */
public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
