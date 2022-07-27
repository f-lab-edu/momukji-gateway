package kr.flab.momukji.gateway.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import kr.flab.momukji.gateway.dto.request.TokenDto;
import kr.flab.momukji.gateway.dto.response.CommonResponse;
import kr.flab.momukji.gateway.dto.response.ResultCode;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {
    
    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public CustomAuthFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey("Token")) {
                return handleUnauthorized(exchange);
            }

            List<String> tokens = request.getHeaders().get("Token");
            String token = Objects.requireNonNull(tokens).get(0);

            TokenDto tokenDto = new TokenDto(token);
            WebClient authClient = WebClient.builder()
                .filter(this.lbFunction)    
                .baseUrl("http://auth")
                .build();
            
            // 인증 성공 시 그대로 라우팅, 실패 시 401 응답
            return authClient.post()
                .uri("/api/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tokenDto)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .flatMap(res -> {
                    if (res.resultCode == ResultCode.VALID_TOKEN) {
                        return chain.filter(exchange);
                    }
                    return handleUnauthorized(exchange);
                });
        };
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {

    }
}
