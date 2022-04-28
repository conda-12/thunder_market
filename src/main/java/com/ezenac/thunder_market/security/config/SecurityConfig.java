package com.ezenac.thunder_market.security.config;


import com.ezenac.thunder_market.security.auth.PrincipalDetailsService;
import com.ezenac.thunder_market.security.factory.UrlResourcesMapFactoryBean;
import com.ezenac.thunder_market.security.filter.PermitAllFilter;
import com.ezenac.thunder_market.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import com.ezenac.thunder_market.security.oauth.PrincipalOauth2MemberService;
import com.ezenac.thunder_market.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PrincipalOauth2MemberService principalOauth2MemberService;
    private final PrincipalDetailsService principalDetailsService;
    private final SecurityResourceService securityResourceService;
    private final String[] permitAll = {"/", "/member/auth/**"};
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/member/auth/signin")
                .usernameParameter("memberId")
                .loginProcessingUrl("/member/auth/signin")
                .defaultSuccessUrl("/")
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
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
                .oauth2Login()
                .loginPage("/member/auth/signin")
                .userInfoEndpoint()
                .userService(principalOauth2MemberService);
    }


    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAll);
        permitAllFilter.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
        permitAllFilter.setAccessDecisionManager(affirmativeBased());
        permitAllFilter.setAuthenticationManager(authenticationManagerBean());

        return permitAllFilter;
    }

    private AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {

        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(roleVoter());

        return accessDecisionVoters;
    }

    private AccessDecisionVoter<?> roleVoter() {

        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new RoleHierarchyImpl();
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        return new UrlResourcesMapFactoryBean(securityResourceService);
    }
}