package com.wang.blog_system.model.pojo.data;

import lombok.Data;

import java.util.Date;
@Data
public class Comments {
    private int id;
    private int articleId;
    private Date created;
    private String ip;
    private String content;
    private String author;
    private String title;
}
