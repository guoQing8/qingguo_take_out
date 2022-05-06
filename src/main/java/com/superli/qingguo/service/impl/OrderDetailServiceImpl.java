package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.entity.OrderDetail;
import com.superli.qingguo.entity.Orders;
import com.superli.qingguo.mapper.OrderDetailMapper;
import com.superli.qingguo.mapper.OrdersMapper;
import com.superli.qingguo.service.OrderDetailService;
import com.superli.qingguo.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/6 11:06
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
