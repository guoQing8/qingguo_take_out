package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.dto.DishDto;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.entity.Dish;
import com.superli.qingguo.entity.DishFlavor;
import com.superli.qingguo.mapper.CategoryMapper;
import com.superli.qingguo.mapper.DishMapper;
import com.superli.qingguo.service.CategoryService;
import com.superli.qingguo.service.DishFlavorService;
import com.superli.qingguo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/30 22:42
 */
@Service
@Slf4j
public class DishServiceIImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     *  新增菜品,同时保存口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveDishWithFlavor(DishDto dishDto) {
        super.save(dishDto);
        Long dishId = dishDto.getId();//菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();//菜品口味
        //给List里面加id
       flavors.stream().map((item)->{
           item.setDishId(dishId);
           return  item;
       }).collect(Collectors.toList());
       dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateDishWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        //删除原有的口味
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);
        //添加新口味

        List<DishFlavor> flavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();//菜品id
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return  item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);


    }

    @Override
    public DishDto byIdQueryDishWithFlavor(Long id) {
        //查寻菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查寻口味
        LambdaQueryWrapper<DishFlavor> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(QueryWrapper);
       dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void deleteDishWithFlavor(Long id) {
        this.removeById(id);
        //删除原有的口味
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(lambdaQueryWrapper);
    }

}
