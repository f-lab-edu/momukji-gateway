package kr.flab.momukji.gateway.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import kr.flab.momukji.gateway.dto.request.TokenDto;
import kr.flab.momukji.gateway.dto.response.CommonResponse;
import kr.flab.momukji.gateway.dto.response.ResultCode;
import kr.flab.momukji.gateway.service.AuthServiceClient;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {

    private final AuthServiceClient authServiceClient;

    public CustomAuthFilter(final AuthServiceClient authServiceClient) {
        super(Config.class);
        this.authServiceClient = authServiceClient;
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

            if (!validateToken(token)) {
                return handleUnauthorized(exchange);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean validateToken(String token) {

        TokenDto tokenDto = new TokenDto(token);
        CommonResponse response = authServiceClient.validate(tokenDto).getBody();

        return response.resultCode == ResultCode.VALID_TOKEN;
    }

    public static class Config {

    }
}
