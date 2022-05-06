package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.entity.Setmeal;
import com.superli.qingguo.entity.ShoppingCart;
import com.superli.qingguo.mapper.SetmealMapper;
import com.superli.qingguo.mapper.ShoppingCartMapper;
import com.superli.qingguo.service.SetmealService;
import com.superli.qingguo.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/6 9:11
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
