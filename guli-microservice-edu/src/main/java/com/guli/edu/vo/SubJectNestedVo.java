package com.guli.edu.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//一级分类
@Data
public class SubJectNestedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private List<SubJectVo> children = new ArrayList<>();
}
