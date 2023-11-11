package com.wang.blog_system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wang.blog_system.mapper.ArticleMapper;
import com.wang.blog_system.mapper.CommentMapper;
import com.wang.blog_system.mapper.StatisticMapper;
import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImp implements ArticleService{
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CommentMapper commentMapper;


    @Override
    public PageInfo<Article> selectArticleByPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticleByPage();
        for (int i=0;i<articleList.size();i++){
            Article article = articleList.get(i);
            Statistic statistic = statisticMapper.selectStatisticByArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    @Override
    public List<Article> getHeatArticles() {
        List<Statistic> list = statisticMapper.getStatistic();
        List<Article> articleList =new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Article article = articleMapper.selectArticleById(list.get(i).getArticleId());
            article.setHits(list.get(i).getHits());
            article.setCommentsNum(list.get(i).getCommentsNum());
            articleList.add(article);
            if(i>=9){
                break;
            }
        }
        return articleList;
    }

    @Override
    public Article selectArticleById(Integer id) {
        Article article = null;
        Object o = redisTemplate.opsForValue().get("article_" + id);
        if(o!=null){
            article = (Article) o;
        }else {
            article = articleMapper.selectArticleById(id);
            if(article!=null){
                redisTemplate.opsForValue().set("article_"+id,article);
            }
        }
        return article;

    }

    @Override
    public PageInfo<Article> selectAuthorArticleByPage(Integer page, Integer count, String author) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectByAuthor(author);
        for (int i=0;i<articleList.size();i++){
            Article article = articleList.get(i);
            Statistic statistic = statisticMapper.selectStatisticByArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    @Override
    public void publishArticle(Article article) {
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setCreated(new Date());
        article.setHits(0);
        article.setCommentsNum(0);
        articleMapper.insertArticle(article);
        statisticMapper.addStatistic(article);
    }

    @Override
    public void updateArticle(Article article) {
        article.setModified(new Date());
        articleMapper.updateArticleById(article);
        redisTemplate.delete("article_" + article.getId());
    }

    @Override
    public void deleteArticle(int id) {
        articleMapper.deleteArticleById(id);
        redisTemplate.delete("article_"+ id);
        statisticMapper.deleteStatisticById(id);
        commentMapper.deleteCommentById(id);
    }

    @Override
    public PageInfo<Article> selectArticleByTitle(Integer page, Integer count, String title) {
        PageHelper.startPage(page, count);
        title = "%"+title+"%";
        List<Article> articleList = articleMapper.selectArticleByTitle(title);
        for (int i=0;i<articleList.size();i++){
            Article article = articleList.get(i);
            Statistic statistic = statisticMapper.selectStatisticByArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }
}
