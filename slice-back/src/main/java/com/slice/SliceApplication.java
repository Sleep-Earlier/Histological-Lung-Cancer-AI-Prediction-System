package com.slice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("com.slice.mapper")
@SpringBootApplication
public class SliceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SliceApplication.class, args);
    }

}
