package com.wang.blog_system.web.clientController;

import com.github.pagehelper.PageInfo;
import com.wang.blog_system.model.pojo.Article;
import com.wang.blog_system.model.pojo.Comment;
import com.wang.blog_system.model.pojo.User;
import com.wang.blog_system.service.ArticleService;

import com.wang.blog_system.service.CommentService;
import com.wang.blog_system.service.SiteService;
import com.wang.blog_system.service.UserService;
import com.wang.blog_system.utils.MailUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private ArticleService articleServiceImp;
    @Autowired
    private CommentService commentServiceImp;
    @Autowired
    private SiteService siteServiceImp;
    @Autowired
    private UserService userServiceImp;
    @Autowired
    private MailUtils mailUtils;

    @GetMapping("/")
    private String index(Model model) {
        return this.index(model, 1, 5, "");
    }
    @RequestMapping("/select")
    private String select(Model model,String title){
        return this.index(model,1,5, title);
    }
    @GetMapping("/page/{page}")
    public String index(Model model, @PathVariable("page") int page, @RequestParam(value = "count", defaultValue = "5") int count, String title) {
        PageInfo<Article> articles = articleServiceImp.selectArticleByPage(page, count);
        Map<String, String> map = new HashMap<>();
        map.put("title","");
        if(title!=null && !title.equals("")){
            map.put("title",title);
            articles = articleServiceImp.selectArticleByTitle(page, count, title);
        }
        // 获取文章热度统计信息
        List<Article> articleList = articleServiceImp.getHeatArticles();
        model.addAttribute("articles", articles);
        model.addAttribute("articleList", articleList);
        model.addAttribute("title",map);
        logger.info("--------------------------------------------");
        logger.info("分页获取文章信息: 页码 "+page+",条数 "+count);
        return "client/index";
    }
    @GetMapping("/article/{id}")
    public String getArticleById(@PathVariable("id") Integer id, HttpServletRequest request,Model model){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if(user==null){
            return "error/403";
        }
        model.addAttribute("username",user.getUsername());
        Article article = articleServiceImp.selectArticleById(id);
        if(article!=null){
            getArticleComments(request, article);
            siteServiceImp.updateStatistics(article);
            request.setAttribute("article",article);
            return "client/articleDetails";
        } else {
            logger.warn("查询文章详情为空，文章ID："+id);
            return "error/404";
        }
    }

    private void getArticleComments (HttpServletRequest request, Article article){
        if(article.getAllowComment()){
            String cp = request.getParameter("cp");
            cp = StringUtils.isBlank(cp) ? "1" : cp;
            request.setAttribute("cp",cp);
            PageInfo<Comment> comments = commentServiceImp.getComments(article.getId(), Integer.parseInt(cp), 3);
            request.setAttribute("cp",cp);
            request.setAttribute("comments", comments);
        }
    }

    @GetMapping("/login")
    public String toLogin(){
        return "comm/login";
    }
    @RequestMapping("/loginInfo")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model){
//        当前用户
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return this.index(model);
        } catch (AuthenticationException e) {
            System.out.println("用户名不存在");
            model.addAttribute("msg","用户名或密码错误");
            return "comm/login";
        }

    }
    @RequestMapping("/logout")
    public String logout(){
        return "redirect:client/index";
    }

    @RequestMapping("/toRegister")
    public String go(HttpServletRequest request){
        String role = request.getParameter("role");
        request.setAttribute("role",role);
        return "comm/register";
    }
    @RequestMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, String role, String verificationCode, Model model, HttpSession session){
        String code = (String) session.getAttribute("code");
        if(!Objects.equals(code,verificationCode)){
            model.addAttribute("msg","验证码错误");
            return "comm/register";
        }
        String algorithmName = "MD5";
        int hashIterations = 1024;
        String salt = RandomStringUtils.random(5,true,false);
        SimpleHash simpleHash = new SimpleHash(algorithmName, ByteSource.Util.bytes(password), ByteSource.Util.bytes(salt), hashIterations);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreated(new Date());
        user.setValid(true);
        user.setSalt(salt);
        user.setPassword(String.valueOf(simpleHash));
        logger.info("user:"+user);
        if(userServiceImp.selectByUsername(username)==null){
            userServiceImp.register(user);
            User user1 = userServiceImp.selectByUsername(username);
            logger.info("u1:"+user1);
            logger.info("u1.getId:"+user1.getId());
            //注册权限
            if(role!=null && role.equals("admin")){
                userServiceImp.registerAuth(user1.getId(), 1);
            }else {
                userServiceImp.registerAuth(user1.getId(), 2);
            }

            return this.index(model);
        }else {
            model.addAttribute("msg","用户名已存在");
            return "comm/register";
        }
    }

    @RequestMapping("/toError404")
    public String toError404(){
        return "error/404";
    }
    @RequestMapping("/toError403")
    public String toError430(){
        return "error/403";
    }

    @RequestMapping("/sentEmail")
    public String sentEmail(String email, HttpSession session){
//        验证码
        String code = RandomStringUtils.random(5,true,true);
        session.setAttribute("code",code);
//        发送邮箱
        mailUtils.sentVerify(email,"博客园用户注册","您的验证码为: "+code+",如果您没有请求此代码，切勿泄露给他人");
        return code;
    }
}
