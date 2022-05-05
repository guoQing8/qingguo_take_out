package com.superli.qingguo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superli.qingguo.entity.Employee;
import com.superli.qingguo.mapper.EmployeeMapper;
import com.superli.qingguo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/27 16:15
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
