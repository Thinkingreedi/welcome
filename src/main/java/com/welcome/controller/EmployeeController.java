package com.welcome.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welcome.dao.EmployeeDao;
import com.welcome.entity.Employee;
import com.welcome.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private IEmployeeService service;
    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 用户登录请求
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        // 密码加密处理
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        // 比对
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = service.getOne(queryWrapper);

        if(emp==null || !emp.getPassword().equals(password)){
            return Result.error("登录失败");
        }
        if(emp.getStatus()==0){
            return Result.error("该账号已禁用");
        }
        // 登录后把用户的id放session里备用
        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 用户分页查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<IPage<Employee>> listPage(@RequestParam("page") int pageNum, @RequestParam("pageSize") int pageSize,String name){
        IPage<Employee> page = new Page<>(pageNum,pageSize);
        // 添加查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Employee::getName,name)
                .orderByDesc(Employee::getUpdateTime);
        service.page(page,queryWrapper);
        return Result.success(page);
    }

    /**
     * 新增一个员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> addEmployee(@RequestBody Employee employee){
        log.info("新增员工,信息:" + employee);
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        employee.setStatus(1);
        service.save(employee);

        return Result.success("新增员工成功");
    }

    /**
     * 按id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> selectById(@PathVariable Long id){
        Employee emp = service.getById(id);
        return emp != null ? Result.success(emp) : Result.error("用户不存在");
    }

    /**
     * 根据id修改用户信息
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> updateById(@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        if(service.updateById(employee)) return Result.success("修改成功");
        else return Result.error("修改失败");
    }
}

