package com.wang.blog_system.model.pojo;

import lombok.Data;

@Data
public class Statistic {
    private int id;
    private int articleId;
    private int hits;
    private int commentsNum;
}
