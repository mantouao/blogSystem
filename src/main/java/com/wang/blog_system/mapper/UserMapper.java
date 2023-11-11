package com.wang.blog_system.mapper;

import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    //    1.注册
    @Insert("insert into t_user(salt,username,password,email,created,valid)"+
            "values(#{salt},#{username},#{password},#{email},#{created},#{valid})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void register(User user);
//    2.查看用户名，通过用户名查信息
    @Select("select * from t_user where username = #{username}")
    public User selectByUsername(String username);
//    3.注册权限 默认为普通用户
    @Insert("insert into t_user_authority(user_id, authority_id)"+
        "values(#{id},#{authority})")
    public void registerAuth(int id, int authority);
//    4.查询全部
    @Select("select t_user.id, t_user.username, t_user.password,t_user.email, t_user.created, t_user.valid, t_user.salt, t_authority.authority from t_user, t_user_authority, t_authority where t_user.id = t_user_authority.user_id and t_authority.id = t_user_authority.authority_id and username = #{username}")
    public User selectAllByUsername(String username);
//    查询全部用户
    @Select("select t_user.id, t_user.username, t_user.password,t_user.email, t_user.created, t_user.valid, t_user.salt, t_authority.authority from t_user, t_user_authority, t_authority where t_user.id = t_user_authority.user_id and t_authority.id = t_user_authority.authority_id and t_user_authority.authority_id = 2")
    public List<User> selectAllUser();
//    查询全部管理员
    @Select("select t_user.id, t_user.username, t_user.password,t_user.email, t_user.created, t_user.valid, t_user.salt, t_authority.authority from t_user, t_user_authority, t_authority where t_user.id = t_user_authority.user_id and t_authority.id = t_user_authority.authority_id and t_user_authority.authority_id = 1")
    public List<User> selectAllAdmin();
    @Select("select count(1) from t_user, t_user_authority, t_authority where t_user.id = t_user_authority.user_id and t_authority.id = t_user_authority.authority_id and t_user_authority.authority_id = 2")
    public Integer userCount();
    @Select("select count(1) from t_user, t_user_authority, t_authority where t_user.id = t_user_authority.user_id and t_authority.id = t_user_authority.authority_id and t_user_authority.authority_id = 1")
    public Integer adminCount();
//    删除用户
    @Delete("delete from t_user where id = #{id}")
    public void delete(Integer id);
}
