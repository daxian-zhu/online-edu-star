package com.clark.daxian.auth.server;

import com.clark.daxian.auth.server.handler.webflux.WebfluxLoginAuthSuccessHandler;
import com.clark.daxian.auth.server.handler.webflux.WebfluxLoginFailureHandler;
import com.clark.daxian.auth.server.handler.webflux.WebfluxLogoutHandler;
import com.clark.daxian.auth.server.handler.webflux.WebfluxLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

/**
 * 配置spring security
 * ResourceServerConfig 是比SecurityConfig 的优先级低的
 * @author 大仙
 *
 */
//@Configuration
//@EnableWebFluxSecurity
//@Order(1)
public class WebfluxSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.cors().and()
				.authorizeExchange().pathMatchers(HttpMethod.OPTIONS).permitAll() //option 请求默认放行
				.anyExchange().authenticated()
				.and()
				.httpBasic()
				.and()
				.formLogin().loginPage("/auth/login")
				.authenticationSuccessHandler(getLoginSuccessAuth()) //认证成功
				.authenticationFailureHandler(getLoginFailure()) //登陆验证失败
				.and().logout().logoutHandler(getLogoutHandler()).logoutSuccessHandler(getLogoutSuccessHandler())
				.and() .csrf().disable();//必须支持跨域

		return http.build();

	}

	@Bean
	public WebfluxLoginAuthSuccessHandler getLoginSuccessAuth(){
		WebfluxLoginAuthSuccessHandler myLoginAuthSuccessHandler = new WebfluxLoginAuthSuccessHandler();
		return myLoginAuthSuccessHandler;
	}

	@Bean
	public WebfluxLoginFailureHandler getLoginFailure(){
		WebfluxLoginFailureHandler myLoginFailureHandler = new WebfluxLoginFailureHandler();
		return myLoginFailureHandler;
	}

	@Bean
	public ServerLogoutHandler getLogoutHandler(){
		WebfluxLogoutHandler myLogoutHandler = new WebfluxLogoutHandler();
		return myLogoutHandler;
	}

	@Bean
	public ServerLogoutSuccessHandler getLogoutSuccessHandler(){
		WebfluxLogoutSuccessHandler logoutSuccessHandler = new WebfluxLogoutSuccessHandler();
		return logoutSuccessHandler;
	}
}
