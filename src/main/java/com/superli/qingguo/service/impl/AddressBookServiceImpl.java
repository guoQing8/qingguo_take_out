package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.entity.AddressBook;
import com.superli.qingguo.entity.Category;
import com.superli.qingguo.mapper.AddressBookMapper;
import com.superli.qingguo.service.AddressBookService;
import com.superli.qingguo.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/5 10:38
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
