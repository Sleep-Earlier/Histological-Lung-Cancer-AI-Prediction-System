package com.slice.config;

import com.slice.common.Commons;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilePathConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/slices/**") //虚拟url路径
                .addResourceLocations("file:"+ new Commons().local); //真实本地路径
    }
}
