package com.wang.blog_system.mapper;

import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.Statistic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface StatisticMapper {
//    1.插入文章的统计信息
    @Insert("insert into t_statistic(article_id,hits,comments_num) values(#{id},0,0)")
    public void addStatistic(Article article);

//    2.根据文章id查询统计信息
    @Select("select * from t_statistic where article_id = #{articleId}")
    public Statistic selectStatisticByArticleId(Integer articleId);

//    通过文章id更新点击量
    @Update("update t_statistic set hits = #{hits} where article_id = #{articleId}")
    public void updateArticleHitById(Statistic statistic);

//    通过文章id更新评论数量
    @Update("update t_statistic set comments_num = #{commentsNum} where article_id = #{articleId}")
    public void updateArticleCommentsById(Statistic statistic);

//    根据文章id删除统计信息
    @Delete("delete from t_statistic where article_id = #{articleId}")
    public void deleteStatisticById(int articleId);

//    查询热度不为0 的统计信息
    @Select("select * from t_statistic where hits !='0' order by hits desc, comments_num desc")
    public List<Statistic> getStatistic();

//    总热度
    @Select("select sum(hits) from t_statistic")
    public long getTotalHit();

//    总评论数
    @Select("select sum(comments_num) from t_statistic")
    public long getTotalComment();

}
