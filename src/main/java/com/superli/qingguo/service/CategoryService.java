package com.superli.qingguo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.superli.qingguo.entity.Category;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/30 21:15
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
