package com.wang.blog_system.service;

import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.data.AuthorGetComment;
import com.wang.blog_system.model.pojo.data.StatisticBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SiteService {
//    最新收到评论
    public List<AuthorGetComment> recentComments(int count, String author);
//    最新发表的文章
    public List<Article> recentArticles(int count, String author);
//    获取后台统计数据
    public StatisticBo getStatistics(String author);
//    更新文章统计数据
    public void updateStatistics(Article article);
}
