package com.wang.blog_system.model.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Article implements Serializable {
    private int id;
    private String title;
    private String content;
    private Date created;
    private Date modified;
    private String categories;
    private String tags;
    private Boolean allowComment;
    private String thumbnail;
    private int hits;
    private int commentsNum;
    private String author;
}
