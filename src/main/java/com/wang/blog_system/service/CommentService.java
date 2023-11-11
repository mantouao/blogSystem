package com.wang.blog_system.service;

import com.github.pagehelper.PageInfo;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.data.Comments;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
//    获取文章下的评论
    public PageInfo<Comment> getComments(Integer aid, int page, int count);
//    发表评论
    public void pushComment(Comment comment);
//    全部评论
    public PageInfo<Comments> selectNewComment(int page, int count);
    public void deleteComment(Integer id, Integer aid);
}
