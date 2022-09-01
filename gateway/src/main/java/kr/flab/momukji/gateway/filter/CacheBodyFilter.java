package kr.flab.momukji.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;


@Component
public class CacheBodyFilter extends AbstractGatewayFilterFactory<CacheBodyFilter.Config> {
    
    public CacheBodyFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> { // Request Body를 Cache에 저장
            return ServerWebExchangeUtils.cacheRequestBody(exchange, (serverHttpRequest) ->
                    chain.filter(exchange.mutate().request(serverHttpRequest).build())
                );
        };
    }

    public static class Config {

    }
}