package com.wang.blog_system.model.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Comment implements Serializable {
    private int id;
    private int articleId;
    private Date created;
    private String ip;
    private String content;
    private String status;
    private String author;
}
