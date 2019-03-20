package com.guli.edu.vo;

import lombok.Data;

import java.io.Serializable;

//二级分类
@Data
public class SubJectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;
}
