package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.common.CustomException;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.entity.Dish;
import com.superli.qingguo.entity.Employee;
import com.superli.qingguo.entity.Setmeal;
import com.superli.qingguo.mapper.CategoryMapper;
import com.superli.qingguo.mapper.EmployeeMapper;
import com.superli.qingguo.service.CategoryService;
import com.superli.qingguo.service.DishService;
import com.superli.qingguo.service.EmployeeService;
import com.superli.qingguo.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/30 21:16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类,删除之前判断
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        //查询当前分类是否关联了菜品
        LambdaQueryWrapper<Dish> DishQueryWrapper = new LambdaQueryWrapper<>();
        DishQueryWrapper.eq(Dish::getCategoryId,ids);
        int count = dishService.count(DishQueryWrapper);
        if(count>0){
            //抛出异常
            throw new CustomException("当前分类关联了菜品,不能删除");
        }
        //查询当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> SeteanlQueryWrapper = new LambdaQueryWrapper<>();
        SeteanlQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count1 = setmealService.count(SeteanlQueryWrapper);
        if(count1>0){
            //抛出异常
            throw new CustomException("当前分类关联了套餐,不能删除");
        }
        //正常删除
        super.removeById(ids);

    }
}
