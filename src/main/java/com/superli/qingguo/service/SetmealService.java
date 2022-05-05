package com.superli.qingguo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superli.qingguo.dto.DishDto;
import com.superli.qingguo.dto.SetmealDto;
import com.superli.qingguo.entity.Setmeal;

import java.util.List;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/30 22:42
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void deleteWithDish(List<Long> ids);
}
