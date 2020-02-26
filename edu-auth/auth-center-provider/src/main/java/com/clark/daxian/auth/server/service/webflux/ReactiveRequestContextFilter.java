package com.clark.daxian.auth.server.service.webflux;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 新建过滤，捕获过滤器
 * @author 大仙
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestContextFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		return chain.filter(exchange)
			.subscriberContext(ctx -> ctx.put(ReactiveRequestContextHolder.CONTEXT_KEY, request));
	}
}