package com.superli.qingguo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.superli.qingguo.common.R;
import com.superli.qingguo.dto.SetmealDto;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.entity.Setmeal;
import com.superli.qingguo.service.CategoryService;
import com.superli.qingguo.service.SetmealDishService;
import com.superli.qingguo.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author superli
 * @Description 套餐管理
 * @Date 2022/5/4 15:33
 */
@Slf4j
@RequestMapping("/setmeal")
@RestController
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /**
     *  保存套餐,菜品信息
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 套餐分页查询
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> pageInfo1 = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo,pageInfo1,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category categoryServiceById = categoryService.getById(categoryId);
            if(categoryServiceById!=null){
                String name1 = categoryServiceById.getName();
                setmealDto.setCategoryName(name1);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        pageInfo1.setRecords(list);
        return R.success(pageInfo1);

    }

    /**
     *  删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.deleteWithDish(ids);
        return R.success("套餐删除成功");

    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Long categoryId){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId);
        queryWrapper.eq(Setmeal::getStatus,1);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);

    }

}
