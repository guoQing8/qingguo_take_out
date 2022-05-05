package com.superli.qingguo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.superli.qingguo.common.R;
import com.superli.qingguo.entity.Employee;
import com.superli.qingguo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Author superli
 * @Description
 * @Date 2022/4/27 16:17
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的代码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据提交的username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到,返回登录失败
        if(emp==null){
            return R.error("用户名不存在");
        }
        //4.进行密码对比,如果不一致返回密码错误
        if(!emp.getPassword().equals(password)){
            return  R.error("密码错误");
        }
        //5.查看员工状态,如果禁用,返回禁用结果
        if(emp.getStatus()!=1){
            return  R.error("你已经离职了");
        }
        //6.登录成功,将员工id存入session,返回成功的结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出登录
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存当前员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    /**
     * 新增员工
     */
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
       // employee.setUpdateTime(LocalDateTime.now());
       // Long empId = (Long) request.getSession().getAttribute("employee");
       // employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        boolean save = employeeService.save(employee);
        if(save){
            return R.success("新增员工成功");
        }
        return  R.error("添加员工失败");
    }
    @GetMapping("/page")
    public R<Page> selectAllEmployee(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lambdaQueryWrapper.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);


    }

    /**
     * 状态更新
     * @param employee
     * @param request
     * @return
     */
    @PutMapping
    public R<String> open(@RequestBody Employee employee,
                          HttpServletRequest request){

        log.info(employee.toString());
        Long empId = (Long)request.getSession().getAttribute("employee");
        //employee.setUpdateUser(empId);
        //employee.setUpdateTime(LocalDateTime.now());
        boolean b = employeeService.updateById(employee);
        if(b){
            return R.success("状态更新成功");
        }
        return R.error("状态更新失败");


    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
       return  R.error("没有查询到");

    }
}
