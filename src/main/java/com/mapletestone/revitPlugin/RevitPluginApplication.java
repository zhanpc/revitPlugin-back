package com.mapletestone.revitPlugin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mapletestone.revitPlugin.dao.mapper")
public class RevitPluginApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RevitPluginApplication.class, args);
    }

}
