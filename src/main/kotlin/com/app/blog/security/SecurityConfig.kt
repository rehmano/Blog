package com.app.blog.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(val dataSource: DataSource, val bCryptPasswordEncoder: BCryptPasswordEncoder) : WebSecurityConfigurerAdapter() {

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?")
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder)

    }



    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/resources/static/css/*").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/post").authenticated()
                    .antMatchers("/communities").authenticated()
                    .antMatchers("/").authenticated()
                    .antMatchers("/admin").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/login")
                .and()
                .logout()
                .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
    }
}