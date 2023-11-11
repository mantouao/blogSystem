package com.wang.blog_system.mapper;

import com.wang.blog_system.model.pojo.Article;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper {
//    1.通过id查询文章
    @Select("select * from t_article where id = #{id}")
    public Article selectArticleById(Integer id);

//    2.插入文章，自动生成主键id
    @Insert("insert into t_article(title,created,modified,tags,categories,allow_comment,thumbnail,content,author)"+
            "values(#{title},#{created},#{modified},#{tags},#{categories},#{allowComment},#{thumbnail},#{content},#{author})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public Integer insertArticle(Article article);

//    3.文章分页查询
    @Select("select * from t_article order by id desc")
    public List<Article> selectArticleByPage();

//    4.根据id删除文章
    @Delete("delete from t_article where id = #{id}")
    public void deleteArticleById(Integer id);

//    5.统计某个作者文章数量
    @Select("select count(1) from t_article where author = #{author}")
    public Integer countArticle(String author);

//    6.根据id更新文章
    public Integer updateArticleById(Article article);
//    7.根据作者查文章
    @Select("select * from t_article where author = #{author} order by id desc")
    public List<Article> selectByAuthor(String author);
//    8查询文章
    @Select("select * from t_article where title like #{title} order by id desc")
    public List<Article> selectArticleByTitle(String title);
//    9.根据作者名删除文章
    @Delete("delete from t_article where author = #{author}")
    public void deleteByUsername(String author);
}
