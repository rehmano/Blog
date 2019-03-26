package com.app.blog.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(val dataSource: DataSource) : WebSecurityConfigurerAdapter() {

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(BCryptPasswordEncoder())
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username=?")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/validate").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
    }
}