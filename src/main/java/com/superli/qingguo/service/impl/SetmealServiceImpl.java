package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.common.CustomException;
import com.superli.qingguo.dto.SetmealDto;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.entity.Setmeal;
import com.superli.qingguo.entity.SetmealDish;
import com.superli.qingguo.mapper.CategoryMapper;
import com.superli.qingguo.mapper.SetmealMapper;
import com.superli.qingguo.service.CategoryService;
import com.superli.qingguo.service.SetmealDishService;
import com.superli.qingguo.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/30 22:43
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    /**
     * 新增套餐,同时保存套餐和菜品关联关系
     * @param setmealDto
     */
    @Autowired

    private SetmealDishService setmealDishService;
    @Override
    @Transactional//事务注解
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        //保存关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //添加setmealId
        for(SetmealDish item:setmealDishes){
            item.setSetmealId(setmealDto.getId());
            setmealDishService.save(item);
        }


    }

    /**
     * 删除套餐及其关联菜品
     * @param ids
     */
    @Transactional
    @Override
    public void deleteWithDish(List<Long> ids) {
        //查询套餐状态,只有停售才能删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);


        //不能删除,抛出异常
        if(count>0){
            throw new CustomException("套餐处于在售状态,无法删除");
        }
        //如果可以删除.先删除套餐表中数据
        this.removeByIds(ids);
        //删除关联表中数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }
}
