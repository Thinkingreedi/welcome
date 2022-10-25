package com.welcome;

import com.welcome.dao.DishDao;
import com.welcome.dao.UserDao;
import com.welcome.entity.Dish;
import com.welcome.service.IDishService;
import com.welcome.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class WelcomeApplicationTests {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DishDao dishDao;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDishService dishService;

    @Test
    void contextLoads() {
    }

    @Test
    void testDaoAndService(){
        List<Dish> users = dishDao.selectList(null);
        for (Dish user : users) {
            System.out.println(user);
        }

//        List<Dish> users1 =  dishService.list();
//        for (Dish user : users1) {
//            System.out.println();
//        }
    }
}
