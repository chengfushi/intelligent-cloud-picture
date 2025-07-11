package com.chengfu.yunpictureapi;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author chengfu
 */
@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})
@MapperScan("com.chengfu.yunpictureapi.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class YunPictureApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(YunPictureApiApplication.class, args);
    }

}
