package com.superli.qingguo.controller;

import com.superli.qingguo.common.R;
import com.superli.qingguo.entity.Orders;
import com.superli.qingguo.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/6 11:08
 */
@Slf4j
@RequestMapping("/order")
@RestController
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("结算 成功");
    }

}
