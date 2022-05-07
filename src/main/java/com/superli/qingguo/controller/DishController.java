package com.superli.qingguo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.superli.qingguo.common.R;
import com.superli.qingguo.dto.DishDto;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.entity.Dish;
import com.superli.qingguo.entity.DishFlavor;
import com.superli.qingguo.entity.Employee;
import com.superli.qingguo.service.CategoryService;
import com.superli.qingguo.service.DishFlavorService;
import com.superli.qingguo.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/1 23:10
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired

    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveDishWithFlavor(dishDto);
        log.info(dishDto.toString());
        String key="dish_"+dishDto.getCategoryId()+"_1";

        redisTemplate.delete(key);
    return R.success("新增菜品成功");
    }

    /**
     * 菜品信息展示
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page();
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
         dishService.page(pageInfo, lambdaQueryWrapper);
         //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //给disDto拷贝Dish属性
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            //disDto添加菜品分类名
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);

    }

    /**
     * 根据id查寻菜品信息,口味数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.byIdQueryDishWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        //清理所有redis菜品缓存
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        dishService.updateDishWithFlavor(dishDto);
        return R.success("更新成功");



    }
    /**
     * 删除菜品_口味信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){

        dishService.deleteDishWithFlavor(ids);
        return R.success("信息删除成功");
    }

    /**
     * 更改状态信息
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> open(@PathVariable int status, Long ids){

        Dish dish = dishService.getById(ids);

        dish.setStatus(status);    System.out.println(dish.toString());
        dishService.updateById(dish);
        return R.success("成功");



    }

    /**
     *
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> byCategoryId(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//       queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getCreateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> byCategoryId(Dish dish){
        List<DishDto> dishDtolist1=null;
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //先从redis中获取缓存数据,
         dishDtolist1 =  (List<DishDto>)redisTemplate.opsForValue().get(key);
        // 如果数据存在,直接返回
        if(dishDtolist1!=null){
            return R.success(dishDtolist1);
        }
        //如果数据不存在,数据库查询
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getCreateTime);
        List<Dish> list = dishService.list(queryWrapper);
         dishDtolist1 = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //给disDto拷贝Dish属性
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            //disDto添加菜品分类名
            dishDto.setCategoryName(categoryName);
            //菜品id
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());
        //将数据库查询的存入redis
        redisTemplate.opsForValue().set(key,dishDtolist1,60, TimeUnit.MINUTES);
        return R.success(dishDtolist1);
    }

}

