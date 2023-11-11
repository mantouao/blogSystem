package com.wang.blog_system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wang.blog_system.mapper.CommentMapper;
import com.wang.blog_system.mapper.StatisticMapper;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.Statistic;
import com.wang.blog_system.model.pojo.data.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImp implements CommentService{
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page,count);
        List<Comment> comments = commentMapper.selectCommentByAId(aid);
        PageInfo<Comment> commentPageInfo = new PageInfo<>(comments);
        return commentPageInfo;
    }

    @Override
    public void pushComment(Comment comment) {
        commentMapper.pushComment(comment);
        Statistic statistic = statisticMapper.selectStatisticByArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleCommentsById(statistic);
    }

    @Override
    public PageInfo<Comments> selectNewComment(int page, int count) {
        PageHelper.startPage(page, count);
        List<Comments> comments = commentMapper.selectNewComment();
        PageInfo<Comments> commentPageInfo = new PageInfo<>(comments);
        return commentPageInfo;
    }

    @Override
    public void deleteComment(Integer id, Integer aid) {
        Statistic statistic = statisticMapper.selectStatisticByArticleId(aid);
        if(statistic.getCommentsNum()>0){
            statistic.setCommentsNum(statistic.getCommentsNum()-1);
        }
        statisticMapper.updateArticleCommentsById(statistic);
        commentMapper.deleteComment(id);
    }
}
