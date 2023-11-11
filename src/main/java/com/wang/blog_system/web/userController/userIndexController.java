package com.wang.blog_system.web.userController;

import com.github.pagehelper.PageInfo;
import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.User;
import com.wang.blog_system.model.pojo.data.AuthorGetComment;
import com.wang.blog_system.model.pojo.data.StatisticBo;
import com.wang.blog_system.service.ArticleService;
import com.wang.blog_system.service.SiteService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class userIndexController {
    @Autowired
    private SiteService siteServiceImp;
    @Autowired
    private ArticleService articleServiceImp;

    @RequestMapping("/user/toIndex")
    public String toIndex(Model model) {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user==null){
            return "error/403";
        }
        model.addAttribute("username", user.getUsername());
        List<Article> articles = siteServiceImp.recentArticles(5, user.getUsername());
        List<AuthorGetComment> comments = siteServiceImp.recentComments(5, user.getUsername());
        StatisticBo statistics = siteServiceImp.getStatistics(user.getUsername());
        model.addAttribute("articles", articles);
        model.addAttribute("comments", comments);
        model.addAttribute("statistics", statistics);

        return "user/index";
    }

    @RequestMapping("/user/toArticleM")
    public String toArticleManagement(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "count", defaultValue = "10") int count, Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        PageInfo<Article> articles = articleServiceImp.selectAuthorArticleByPage(page, count, user.getUsername());
        model.addAttribute("articles", articles);
        return "user/articleManagement";
    }

    @RequestMapping("/user/toArticleReady")
    public String toArticleReady() {
        return "user/articleReady";
    }

    @RequestMapping("/user/publish")
    public ResponseEntity articlePublish(String title, String tags, String categories, String allowComment, String content) {
        Article article = new Article();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        article.setAuthor(user.getUsername());
        article.setTitle(title);
        article.setTags(tags);
        article.setCategories(categories);
        article.setAllowComment(Boolean.valueOf(allowComment));
        article.setContent(content);
        try {
            articleServiceImp.publishArticle(article);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("{\"success\": false, \"message\": \"删除失败\"}");
        }


    }

    @RequestMapping("/user/toUpdate/{id}")
    public String toUpdate(@PathVariable("id") String id, Model model) {
        Article article = articleServiceImp.selectArticleById(Integer.parseInt(id));
        model.addAttribute("contents", article);
        return "user/articleReady";
    }

    @RequestMapping("/user/update")
    public ResponseEntity articleUpdate(String id, String title, String tags, String categories, String allowComment, String content) {
        Article article = new Article();
        article.setId(Integer.parseInt(id));
        article.setTitle(title);
        article.setTags(tags);
        article.setCategories(categories);
        article.setAllowComment(Boolean.valueOf(allowComment));
        article.setContent(content);
        try {
            articleServiceImp.updateArticle(article);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("{\"success\": false, \"message\": \"删除失败\"}");
        }
    }

    @RequestMapping("/user/delete")
    public ResponseEntity<Object> delete(@RequestParam String id) {
        int Did = Integer.parseInt(id);
        try {
            articleServiceImp.deleteArticle(Did);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("{\"success\": false, \"message\": \"删除失败\"}");
        }
    }
}
