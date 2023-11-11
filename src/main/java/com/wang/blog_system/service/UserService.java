package com.wang.blog_system.service;

import com.github.pagehelper.PageInfo;
import com.wang.blog_system.model.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    //    1.注册
    public void register(User user);
    //    2.查看用户名，通过用户名查信息
    public User selectByUsername(String username);
    public void registerAuth(int id, int authority);
    public User selectAllByUsername(String username);
    public List<User> selectAllUser();
    public List<User> selectAllAdmin();
    public Integer userCount();
    public Integer adminCount();
    public void deleteUser(Integer id, String username);
}
