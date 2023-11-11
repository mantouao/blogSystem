package com.wang.blog_system.service;

import com.github.pagehelper.PageHelper;
import com.wang.blog_system.mapper.ArticleMapper;
import com.wang.blog_system.mapper.CommentMapper;
import com.wang.blog_system.mapper.StatisticMapper;
import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.Statistic;
import com.wang.blog_system.model.pojo.data.AuthorGetComment;
import com.wang.blog_system.model.pojo.data.StatisticBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SiteServiceImp implements SiteService{
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public List<AuthorGetComment> recentComments(int count, String author) {
        PageHelper.startPage(1,count>10 || count < 1 ? 10 : count);
        List<AuthorGetComment> authorGetComments = commentMapper.authorGetComments(author);
        return authorGetComments;
    }

    @Override
    public List<Article> recentArticles(int count, String author) {
        PageHelper.startPage(1, count>10 || count<1 ? 10 :count);
        List<Article> articleList = articleMapper.selectByAuthor(author);
        for (int i = 0 ; i < articleList.size() ;i++){
            Article article = articleList.get(i);
            Statistic statistic = statisticMapper.selectStatisticByArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return articleList;
    }

    @Override
    public StatisticBo getStatistics(String author) {
        StatisticBo statisticBo = new StatisticBo();
        Integer countArticle = articleMapper.countArticle(author);
        Integer countComment = commentMapper.countComment(author);
        statisticBo.setArticles(countArticle);
        statisticBo.setComments(countComment);
        return statisticBo;
    }

    @Override
    public void updateStatistics(Article article) {
        Statistic statistic = statisticMapper.selectStatisticByArticleId(article.getId());
        statistic.setHits(statistic.getHits()+1);
        statisticMapper.updateArticleHitById(statistic);
    }
}
