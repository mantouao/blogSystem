package com.wang.blog_system.web.clientController;

import com.vdurmont.emoji.EmojiParser;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.User;
import com.wang.blog_system.model.pojo.data.ArticleResponseData;
import com.wang.blog_system.service.CommentService;
import com.wang.blog_system.utils.MyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private CommentService commentServiceImp;
    @PostMapping("/publish")
    @ResponseBody
    public ArticleResponseData publishComment(@RequestParam Integer aid, @RequestParam String text, HttpServletRequest request){
        text = MyUtils.cleanXSS(text);
        text = EmojiParser.parseToAliases(text);
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        logger.info("user:"+user);
        logger.info("principal:"+subject.getPrincipal());
        Comment comments = new Comment();
        comments.setArticleId(aid);
        comments.setIp(request.getRemoteAddr());
        comments.setCreated(new Date());
        comments.setAuthor(user.getUsername());
        comments.setContent(text);
        try {
            commentServiceImp.pushComment(comments);
            logger.info("成功 文章id="+aid);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.info("失败，文章id="+aid+"错误:"+e.getMessage());
            return ArticleResponseData.fail();
        }

    }
}
