package com.clark.daxian.auth.server.service.webflux;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * 仿造 RequestContextHolder 切面获取request
 */
public class ReactiveRequestContextHolder {

	static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

	/**
	 * 通过上下文获取request
	 * @return
	 */
	public static Mono<ServerHttpRequest> getRequest() {
		return Mono.subscriberContext()
			.map(ctx -> ctx.get(CONTEXT_KEY));
	}

}