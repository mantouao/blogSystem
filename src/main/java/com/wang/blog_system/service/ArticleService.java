package com.wang.blog_system.service;

import com.github.pagehelper.PageInfo;
import com.wang.blog_system.model.pojo.Article;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ArticleService {
    public PageInfo<Article> selectArticleByPage(Integer page, Integer count);
    public List<Article> getHeatArticles();

//  根据文章id查询文章详情
    public Article selectArticleById(Integer id);
    public PageInfo<Article> selectAuthorArticleByPage(Integer page, Integer count, String author);
    public void publishArticle(Article article);
    public void updateArticle(Article article);
    public void deleteArticle(int id);
    public PageInfo<Article> selectArticleByTitle(Integer page, Integer count, String title);

}
