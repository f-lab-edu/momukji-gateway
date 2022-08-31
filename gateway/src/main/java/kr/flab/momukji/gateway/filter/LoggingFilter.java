package kr.flab.momukji.gateway.filter;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.stream.Collectors;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import kr.flab.momukji.gateway.filter.log.Log;
import kr.flab.momukji.gateway.filter.log.LogService;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    @Autowired
    private LogService logService;

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        GatewayFilter filter = new OrderedGatewayFilter((exchange,chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            
            String method = request.getMethodValue();
            String uri = request.getURI().getPath();
            DBObject requestParams = BasicDBObject.parse(convertQueryParamsToJsonStr(request.getQueryParams()));
            DBObject requestBody = BasicDBObject.parse(getBodyContent(exchange)); // 캐시에 저장해둔 RequestBody 꺼내서 몽고디비의 DBObject 형태로 만듬
            String ip = request.getRemoteAddress().getHostString();
            Instant requestedTimestamp = Instant.now();
            
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Instant responsedTimestamp = Instant.now();
                String status = String.valueOf(response.getStatusCode().value());
                
                Log log = Log.builder()
                    .method(method)
                    .uri(uri)
                    .requestParams(requestParams)
                    .requestBody(requestBody)
                    .ip(ip)
                    .requestedTimestamp(requestedTimestamp)
                    .responsedTimestamp(responsedTimestamp)
                    .status(status)
                    .build();
                
                logService.save(log);
            }));
        }, Ordered.LOWEST_PRECEDENCE);
        
        return filter;
    }

    public String getBodyContent(ServerWebExchange exchange) {
    
        Object attribute = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        if (attribute == null || !(attribute instanceof DataBuffer)) {
            return null;
        }

        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        if (contentType != null && contentType.toString().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            return null;
        }

        DataBuffer nettyDataBuffer = (DataBuffer) attribute;
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(nettyDataBuffer.asByteBuffer());
        return charBuffer.toString();
    }

    public String convertQueryParamsToJsonStr(MultiValueMap<String, String> map) {
        return map.entrySet().stream()
            .map((param) ->
                "\"" + param.getKey() + "\": " +
                param.getValue().stream()
                    .map((item) -> "\"" + item + "\"")
                    .collect(Collectors.joining(", ", "[", "]"))
            )
            .collect(Collectors.joining(", ", "{ ", " }"));
    }

    public static class Config {

    }
}