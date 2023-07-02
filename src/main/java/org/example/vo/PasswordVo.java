package org.example.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PasswordVo {
    private Long id;
    private long userId;
    private String pass;
    private Date date;
}
