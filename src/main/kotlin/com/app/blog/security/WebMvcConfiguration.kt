package com.app.blog.security

import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.servlet.config.annotation.*

@EnableWebMvc
@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/login").setViewName("login")
        registry.addViewController("/signup").setViewName("signup")
        registry.addViewController("/").setViewName("index")
        registry.addViewController("/post/{id}").setViewName("post")
        registry.addViewController("/community").setViewName("communities")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/login")
                .allowedOrigins("http://localhost:3000").allowCredentials(true)
        registry.addMapping("/")
                .allowedOrigins("http://localhost:3000")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
    }
}