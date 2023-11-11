package com.wang.blog_system.model.pojo;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.Date;
import java.util.Random;

@Data
public class User {
    private int id;
    private String salt;
    private String username;
    private String password;
    private String email;
    private Date created;
    private Boolean valid;
    private String authority;

}
