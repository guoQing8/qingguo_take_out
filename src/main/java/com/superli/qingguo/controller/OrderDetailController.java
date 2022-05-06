package com.superli.qingguo.controller;

import com.superli.qingguo.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/6 11:09
 */
@Slf4j
@RequestMapping("/orderDetail")
@RestController
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
}
