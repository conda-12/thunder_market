package com.ezenac.thunder_market.config;


import com.ezenac.thunder_market.config.auth.PrincipalDetailsService;
import com.ezenac.thunder_market.config.oauth.PrincipalOauth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2MemberService principalOauth2MemberService;
    private final PrincipalDetailsService principalDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/member/auth/signin", "/member/auth/register").permitAll()
                .antMatchers("/member/**").access("hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/products/new").access("hasRole('ROLE_MEMBER') or hasRole('ROLE_MANAGER')")
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/member/auth/signin")
                .usernameParameter("memberId")
                .loginProcessingUrl("/member/auth/signin").defaultSuccessUrl("/")
                .and()
                .rememberMe()
                .key("@thunder_market_remember-me_cookie")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(86400 * 14)
                .userDetailsService(principalDetailsService)
                .and()
                .logout()
                .logoutUrl("/member/auth/signout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID")
                .and()
                .oauth2Login()
                .loginPage("/member/auth/signin")
                .userInfoEndpoint()
                .userService(principalOauth2MemberService);
    }
}
