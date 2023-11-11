package com.wang.blog_system.mapper;

import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.data.AuthorGetComment;
import com.wang.blog_system.model.pojo.data.Comments;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {
//    1.展示文章的评论
    @Select("select * from t_comment where article_id = #{aid} order by id desc")
    public List<Comment> selectCommentByAId(Integer aid);
//    2.查询最新评论
    @Select("select * from t_comment , t_article where t_comment.article_id = t_article.id order by t_comment.created desc")
    public List<Comments> selectNewComment();
//    3.发表评论
    @Insert("insert into t_comment (article_id,created,author,ip,content) values (#{articleId}, #{created}, #{author}, #{ip}, #{content})")
    public void pushComment(Comment comment);

//    4.统计评论数量
    @Select("select count(1) from t_article,t_comment where t_article.id = t_comment.article_id and t_article.author = #{author}")
    public Integer countComment(String author);

//    5.根据文章id删除评论
    @Delete("delete from t_comment where article_id = #{aid}")
    public void deleteCommentById(Integer aid);
//    6.查询作者收到的评论
    @Select("select t_article.id,t_comment.author,t_comment.created,t_article.title,t_comment.content from t_article,t_comment where t_article.id = t_comment.article_id and t_article.author = #{author} order by created desc")
    public List<AuthorGetComment> authorGetComments(String author);

    @Delete("delete from t_comment where id = #{id}")
    public void deleteComment(Integer id);

    @Delete("delete from t_comment where author = #{author}")
    public void deleteCommentByAuthor(String author);

    @Select("select * from t_comment where author = #{author}")
    public List<Comment> selectByAuthor(String author);
}
