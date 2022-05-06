package com.superli.qingguo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.superli.qingguo.common.BaseContext;
import com.superli.qingguo.common.R;
import com.superli.qingguo.entity.ShoppingCart;
import com.superli.qingguo.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/6 9:15
 */
@Slf4j
@RequestMapping("/shoppingCart")
@RestController
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        //设定当前用户id,指定购物车的东西是哪个用户的
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询添加的菜品在没在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if(dishId!=null){
            //添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }
        else{

            //添加是是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if(one==null){

             shoppingCart.setNumber(1);
             shoppingCart.setCreateTime(LocalDateTime.now());
            System.out.println(shoppingCart);
             shoppingCartService.save(shoppingCart);
             return R.success(shoppingCart);
        }
        else{
            Integer number = one.getNumber();
            one.setNumber(number+1);
            one.setCreateTime(LocalDateTime.now());
            shoppingCartService.updateById(one);
            return R.success(one);
        }



    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> shppingCartShow(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return  R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clear(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(queryWrapper);
        return  R.success("清空购物车成功");
    }
}
