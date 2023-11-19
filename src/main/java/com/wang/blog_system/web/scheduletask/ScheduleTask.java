package com.wang.blog_system.web.scheduletask;

import com.wang.blog_system.mapper.StatisticMapper;
import com.wang.blog_system.mapper.UserMapper;
import com.wang.blog_system.model.pojo.User;
import com.wang.blog_system.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {
    @Autowired
    private MailUtils mailUtils;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private UserMapper userMapper;

    @Scheduled(cron = "0 0 18 1 * ?")
    public void sendEmail(){
        for (User user : userMapper.selectAll()) {
            long totalHit = statisticMapper.getTotalHitByAuthor(user.getUsername());
            long totalComment = statisticMapper.getTotalCommentByAuthor(user.getUsername());
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("博客的总访问量为:"+totalHit).append("\n");
            stringBuffer.append("博客的总评论量为:"+totalComment).append("\n");
            mailUtils.sendSimpleEmail(user.getEmail(), "博客流量统计情况", stringBuffer.toString());
        }
    }
}
