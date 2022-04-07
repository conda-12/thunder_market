package com.ezenac.thunder_market.config;

import com.ezenac.thunder_market.config.oauth.PrincipalOauth2MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2MemberService principalOauth2MemberService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/member/my-page").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/member/auth/signin")
                .usernameParameter("memberId")
                .loginProcessingUrl("/member/auth/signin")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutUrl("/member/auth/signout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .oauth2Login()
                .loginPage("/member/auth/signin")
                .userInfoEndpoint()
                .userService(principalOauth2MemberService);;
    }
}
