package com.patrick.disruptoromssettlement.util;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbUtil {
    private static DbUtil dbUtil = null;

    private DbUtil(){}

    //如何在静态工具类中注入spring管理的对象？
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    //如何在静态工具类中注入spring管理的对象？
    @PostConstruct
    private void init(){
        dbUtil = new DbUtil();
        dbUtil.setSqlSessionTemplate(this.sqlSessionTemplate);
    }

    public static long getId(){
        SqlSessionTemplate sqlSessionTemplate1 = dbUtil.getSqlSessionTemplate();
        Long res = sqlSessionTemplate1.selectOne("testMapper.queryBalance");
        if (res == null) {
            return -1;
        }
        return res;
    }


    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
