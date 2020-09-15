package com.wllt.filecrawling.entity;

import java.io.Serializable;

/**
 * @program: file-crawling
 * @description:  对应获取接收的数据
 * @author: wllt
 * @create: 2020-09-15 18:01
 **/

public class User implements Serializable {

    private Long id;
    private String name;
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
