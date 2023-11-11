package com.wang.blog_system.web.adminController;

import com.github.pagehelper.PageInfo;
import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.User;
import com.wang.blog_system.model.pojo.data.Comments;
import com.wang.blog_system.service.ArticleService;
import com.wang.blog_system.service.CommentService;
import com.wang.blog_system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class adminIndexController {
    @Autowired
    private UserService userServiceImp;

    @Autowired
    private ArticleService articleServiceImp;
    @Autowired
    private CommentService commentServiceImp;

    @RequestMapping("/admin/toUserM")
    public String toUserManagement(Model model){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user==null){
            return "error/403";
        }
        model.addAttribute("username", user.getUsername());
        List<User> userList = userServiceImp.selectAllUser();
        List<User> adminList = userServiceImp.selectAllAdmin();
        Integer userCount = userServiceImp.userCount();
        Integer adminCount = userServiceImp.adminCount();
        model.addAttribute("userList", userList);
        model.addAttribute("adminList", adminList);
        model.addAttribute("userCount", userCount);
        model.addAttribute("adminCount", adminCount);
        return "back/userManagement";
    }
    @RequestMapping("/admin/toArticleM")
    public String toArticleManagement(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "count", defaultValue = "10") int count, Model model){
        PageInfo<Article> articles = articleServiceImp.selectArticleByPage(page, count);
        model.addAttribute("articles", articles);
        return "back/articleManagement";
    }

    @RequestMapping("/admin/delete")
    public ResponseEntity<Object> delete(@RequestParam String id, String username) {
        int Did = Integer.parseInt(id);
        try {
            userServiceImp.deleteUser(Did, username);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("{\"success\": false, \"message\": \"删除失败\"}");
        }
    }
    @RequestMapping("/admin/toCommentsM")
    public String toCommentsManagement(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "count", defaultValue = "10") int count, Model model){
        PageInfo<Comments> comments = commentServiceImp.selectNewComment(page, count);
        model.addAttribute("comments", comments);
        return "back/commentsManagement";
    }
    @RequestMapping("/admin/deleteComment")
    public ResponseEntity<Object> deleteComment(@RequestParam String id, String aid) {
        int Did = Integer.parseInt(id);
        int Aid = Integer.parseInt(aid);
        try {
            commentServiceImp.deleteComment(Did,Aid);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("{\"success\": false, \"message\": \"删除失败\"}");
        }
    }
}
