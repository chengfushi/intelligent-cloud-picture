package com.chengfu.yunpictureapi.model.vo.tags;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureTagCategory implements Serializable {
    private List<String> tagList;
    private List<String> categoryList;
    private static final long serialVersionUID = 1L;
}

