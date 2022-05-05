package com.superli.qingguo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.superli.qingguo.common.R;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.entity.Employee;
import com.superli.qingguo.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/30 21:18
 */
@RestController
@Slf4j
@RequestMapping("/category")

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     *查询展示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //log.info("page={},pageSize={}",page,pageSize);
        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);


    }

    /**
     * 新增分类
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){


        boolean save = categoryService.save(category);
        if(save){
            return R.success("新增分类成功");
        }
        return  R.error("添加分类失败");
    }
    /**
     * 分类信息更新
     * @param category

     * @return
     */
    @PutMapping
    public R<String> open(@RequestBody Category category){



        boolean b = categoryService.updateById(category);
        if(b){
            return R.success("更新成功");
        }
        return R.error("更新失败");


    }

    /**
     * 删除分类信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("信息删除成功");
    }

    /**
     * 根据条件查询分类
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category ){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);


    }
}
