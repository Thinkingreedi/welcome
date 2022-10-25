package com.welcome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welcome.dao.Address_bookDao;
import com.welcome.entity.Address_book;
import com.welcome.service.IAddress_bookService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author bugger
 * @since 2022-10-10
 */
@Service
public class Address_bookServiceImpl extends ServiceImpl<Address_bookDao, Address_book> implements IAddress_bookService {

}
