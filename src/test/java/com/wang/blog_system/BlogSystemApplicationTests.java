package com.wang.blog_system;

import com.github.pagehelper.PageInfo;
import com.wang.blog_system.mapper.ArticleMapper;
import com.wang.blog_system.mapper.CommentMapper;
import com.wang.blog_system.mapper.UserMapper;
import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.User;
import com.wang.blog_system.model.pojo.data.AuthorGetComment;
import com.wang.blog_system.model.pojo.data.Comments;
import com.wang.blog_system.model.pojo.data.StatisticBo;
import com.wang.blog_system.service.ArticleService;
import com.wang.blog_system.service.SiteService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BlogSystemApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SiteService siteServiceImp;
    @Test
    void contextLoads() {
        String password = "zhendeok";
        String algorithmName = "MD5";
        int hashIterations = 1024;
//        String salt = RandomStringUtils.random(5,true,false);
        String salt = "sss";
        SimpleHash simpleHash = new SimpleHash(algorithmName, ByteSource.Util.bytes(password), ByteSource.Util.bytes(salt), hashIterations);
        System.out.println(simpleHash);
        String s = String.valueOf(simpleHash);
        System.out.println(String.valueOf(simpleHash));
        System.out.println(s);
//        User user = new User();
//        user.setPassword(password);
//        System.out.println(user.getPassword());
    }
    @Test
    void selectAll(){
//        List<Article> articles = siteServiceImp.recentArticles(5,"qqq");
        List<AuthorGetComment> comments = siteServiceImp.recentComments(5, "qqq");
//        StatisticBo statistics = siteServiceImp.getStatistics("qqq");
        for (AuthorGetComment comment : comments) {
            System.out.println(comment.getAuthor());
            System.out.println(comment.getCreated());
            System.out.println(comment.getTitle());
//            System.out.println(comment.getContent());
        }
    }
    @Test
    void select(){
        for (Comments comments : commentMapper.selectNewComment()) {
            System.out.println(comments);
        }


    }

}
