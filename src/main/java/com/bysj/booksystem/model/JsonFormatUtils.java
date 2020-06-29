package com.bysj.booksystem.model;

import lombok.Data;

import java.util.List;

/**
 * @Author:         jjq
 * @CreateDate:     2020/4/2 14:44
 * @Version:        1.0
 * @Description:    对数据进行封装（这是返回给layui数据表格的数据格式）
 */
@Data
public class JsonFormatUtils <T> {

    private int code;
    private String msg;
    private int count;
    private List<T> data;
}
