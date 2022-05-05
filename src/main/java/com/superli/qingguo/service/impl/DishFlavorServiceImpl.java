package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.entity.DishFlavor;
import com.superli.qingguo.mapper.CategoryMapper;
import com.superli.qingguo.mapper.DishFlavorMapper;
import com.superli.qingguo.service.CategoryService;
import com.superli.qingguo.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/1 23:08
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
