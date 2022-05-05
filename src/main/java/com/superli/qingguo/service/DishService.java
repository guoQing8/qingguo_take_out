package com.superli.qingguo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superli.qingguo.dto.DishDto;
import com.superli.qingguo.entity.Dish;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/30 22:41
 */
public interface DishService extends IService<Dish> {
    //新增菜品,同时插入口味dish表和dish_flavor
    public void saveDishWithFlavor(DishDto dishDto);
    public void updateDishWithFlavor(DishDto dishDto);
    public DishDto byIdQueryDishWithFlavor(Long id );
    public void deleteDishWithFlavor(Long id);
}
