package com.welcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welcome.dao.EmployeeDao;
import com.welcome.entity.Employee;
import com.welcome.service.IEmployeeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements IEmployeeService {

}
