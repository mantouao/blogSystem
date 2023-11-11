package com.wang.blog_system.model.pojo.data;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorGetComment {
    private int id;
    private String author;
    private Date created;
    private String title;
    private String content;
}
