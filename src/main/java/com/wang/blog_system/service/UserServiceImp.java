package com.wang.blog_system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wang.blog_system.mapper.ArticleMapper;
import com.wang.blog_system.mapper.CommentMapper;
import com.wang.blog_system.mapper.StatisticMapper;
import com.wang.blog_system.mapper.UserMapper;
import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.Statistic;
import com.wang.blog_system.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void register(User user) {
        try {
            userMapper.register(user);
        } catch (Exception e) {
            System.out.println("注册失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    public User selectByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        return user;
    }

    @Override
    public void registerAuth(int id, int authority) {
        userMapper.registerAuth(id, authority);
    }

    @Override
    public User selectAllByUsername(String username) {
        return userMapper.selectAllByUsername(username);
    }

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public List<User> selectAllAdmin() {
        return userMapper.selectAllAdmin();
    }


    @Override
    public Integer userCount() {
        return userMapper.userCount();
    }

    @Override
    public Integer adminCount() {
        return userMapper.adminCount();
    }

    @Override
    public void deleteUser(Integer id, String username) {
        List<Comment> comments = commentMapper.selectByAuthor(username);
        for (Comment comment : comments) {
            Statistic statistic = statisticMapper.selectStatisticByArticleId(comment.getArticleId());
            if(statistic.getCommentsNum()>0){
                statistic.setCommentsNum(statistic.getCommentsNum()-1);
            }
            statisticMapper.updateArticleCommentsById(statistic);
            commentMapper.deleteComment(comment.getId());
        }

        List<Article> articleList = articleMapper.selectByAuthor(username);
        articleMapper.deleteByUsername(username);
        for (Article article : articleList) {
            redisTemplate.delete("article_"+ article.getId());
            statisticMapper.deleteStatisticById(article.getId());
            commentMapper.deleteCommentById(article.getId());
        }

        userMapper.delete(id);
    }
}
