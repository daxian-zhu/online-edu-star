package com.clark.daxian.auth.server;

import com.clark.daxian.auth.server.token.JwtAccessToken;
import com.clark.daxian.auth.server.properties.AuthServerProperties;
import com.clark.daxian.auth.server.service.web.UsernameUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 认证服务配置
 * @author 大仙
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TokenStore authTokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsernameUserDetailService userDetailService;


    @Autowired
    private DataSource dataSource;

    @Bean("jdbcClientDetailsService")
    public ClientDetailsService clientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 使用JdbcClientDetailsService客户端详情服务
        clients.withClientDetails(clientDetailsService());
    }

    @Bean("authTokenStore")
    //指定filter用服务端的
    @Primary
    public TokenStore authTokenStore() {
        return new JwtTokenStore(authJwtAccessTokenConverter());
    }
    /**
     * 配置授权服务器端点，如令牌存储，令牌自定义，用户批准和授权类型，不包括端点安全配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)  {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailService)
                .tokenServices(defaultTokenServices());
    }

    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
        TokenEnhancerChain tokenEnhancerChain=new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(authTokenStore);
        //是否可以重用刷新令牌
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        return defaultTokenServices;
    }

    /**
     * 配置授权服务器端点的安全
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)  {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }


    @Bean
    public AuthServerProperties authServerProperties(){
        return new AuthServerProperties();
    }

    /**
     * key
     * @return
     */
    @Bean
    public KeyPair keyPair(){
        KeyPair keyPair = new KeyStoreKeyFactory(
                authServerProperties().getKeyPath(),
                authServerProperties().getSecret().toCharArray()).getKeyPair(authServerProperties().getAlias());
        return keyPair;
    }

    /**
     * jwt构造
     * @return
     */
    @Bean("jwtAccessTokenConverter")
    public JwtAccessTokenConverter authJwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessToken();
        converter.setKeyPair(keyPair());
        return converter;
    }


}



