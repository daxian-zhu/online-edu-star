package com.clark.daxian.auth.resource.content;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * ReactiveRequestContextHolder
 *
 * @author L.cm
 */
public class ReactiveRequestContextHolder {

	private static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

	/**
	 * Gets the {@code Mono<ServerWebExchange>} from Reactor {@link Context}
	 *
	 * @return the {@code Mono<ServerWebExchange>}
	 */
	public static Mono<ServerWebExchange> getExchange() {
		/**
		 * mica中是这么写的，但是我这样写一直会报错 content is null;
		 */
//		return Mono.subscriberContext()
//				.map(ctx -> ctx.get(CONTEXT_KEY));
		/**
		 * 下面是我仿照Security中的改写的。
		 */
		return Mono.subscriberContext()
				.filter(c -> c.hasKey(CONTEXT_KEY))
				.flatMap(c -> Mono.just(c.get(CONTEXT_KEY)));
	}

	/**
	 * Gets the {@code Mono<ServerHttpRequest>} from Reactor {@link Context}
	 *
	 * @return the {@code Mono<ServerHttpRequest>}
	 */
	public static Mono<ServerHttpRequest> getRequest() {
		return ReactiveRequestContextHolder.getExchange()
			.map(ServerWebExchange::getRequest);
	}

	/**
	 * Put the {@code ServerWebExchange} to Reactor {@link Context}
	 *
	 * @param context  Context
	 * @param exchange ServerWebExchange
	 * @return the Reactor {@link Context}
	 */
	public static Context put(Context context, ServerWebExchange exchange) {
		return context.put(CONTEXT_KEY, exchange);
	}
}