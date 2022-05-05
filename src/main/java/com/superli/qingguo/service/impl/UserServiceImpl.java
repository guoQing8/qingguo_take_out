package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.entity.User;
import com.superli.qingguo.mapper.UserMapper;
import com.superli.qingguo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/4 22:19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
